package ca.ulaval.tp2.glo3004.control;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ca.ulaval.tp2.glo3004.ExecutionParameters;
import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.car.CarFactory;
import ca.ulaval.tp2.glo3004.car.InvalidCarActionException;
import ca.ulaval.tp2.glo3004.control.runnable.sync.SynchroIntersection;
import ca.ulaval.tp2.glo3004.intersection.Intersection;
import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.light.LightColor;
import ca.ulaval.tp2.glo3004.road.Direction;
import ca.ulaval.tp2.glo3004.view.LightView;
import ca.ulaval.tp2.glo3004.view.StateView;

/**
 * Classe permettant le controle du traffic des voitures, pietons et lumieres
 * aux intersection en T et en croix
 */
public class SyncController {

	private static final boolean IN_SYNCHRO = true;
	protected LightController lightController;
	private Map<Direction, Boolean> timeOutMaps = new HashMap<Direction, Boolean>();
	private Map<Direction, Object> directionLocks = new HashMap<Direction, Object>();
	
	private BlockingQueue<Car> crossIncomingCars = new LinkedBlockingQueue<>(1);
	private BlockingQueue<Car> threeWayIncomingCars = new LinkedBlockingQueue<>(1);

	protected int numberOfCars;
	private int numberOfPedestrians;
	
	private Object lock = new Object();

	private LightView threeWayLightView;
	private LightView crossLightView;
	private int WAITING_TIME = 2000;
	private StateView stateView;

	private SynchroIntersection synchroIntersection;
	

	// Constructeur avec parametres pour le controle du traffic sur une intersection
	// en T ou en croix
	public SyncController(ExecutionParameters parameters, 
			StateView stateView,
			LightView threeWayLightView,
			LightView crossLightView, 
			LightController lightController,
			SynchroIntersection synchroIntersection) {
		
		this.lightController = lightController;
		this.synchroIntersection = synchroIntersection;
		this.numberOfCars = parameters.getNumberOfCars();
		this.numberOfPedestrians = parameters.getNumberOfPedestrians();
		this.stateView = stateView;
		this.threeWayLightView = threeWayLightView;
		this.crossLightView = crossLightView;

		initializeDirectionVariables();
	}

	// Methode qui initialise les lumieres des interesections
	private void initializeDirectionVariables() {

		for (Direction direction : synchroIntersection.getAllDirections()) {
			timeOutMaps.put(direction, false);
			directionLocks.put(direction, new Object());
		}
	}

	// Methode qui affiche le statut des lumieres en fonction des intersections
	private void printLightStates() {
		StringBuilder lightStates = new StringBuilder();
	
		threeWayLightView.setLights(lightController.getLights());
		crossLightView.setLights(lightController.getLights());
		lightStates.append(lightController.getLightStates());

		System.out.print(lightStates.toString());
	}

	// Methode qui empeche les lumieres de l'est d'etre vertes en meme temps que
	// celles du sud
	public void controlLight(Direction direction) throws Exception {

		synchronized (lock) {

			while (atLeastOneNeighBoorIsGreen(direction)) {

				lock.wait();
			}

			switchLight(direction, LightColor.GREEN);
		}

		Thread.sleep(2000);
		switchBackToRedAfterCarCirculation(direction);
	}

	private void switchLight(Direction direction, LightColor color) {

		synchronized (lock) {
			try {
				Thread.sleep(WAITING_TIME);
			} catch (InterruptedException e) {
				System.out.println("LIGHT THREAD STOP");
			}

			lightController.switchLight(direction, color);

			lock.notifyAll();
		}
	}

	private boolean atLeastOneLightIsGreen() {
		return this.lightController.atLeastOneLightIsGreen();
	}

	private boolean atLeastOneNeighBoorIsGreen(Direction direction) {
		List<Direction> neighboors = synchroIntersection.getNeighboors(direction);
		return this.lightController.atLeastOneNeighBoorIsGreen(neighboors, direction);
	}

	// Synchronisation du passage des pietons avec les lumieres
	public void pedestrianPass() throws InterruptedException {

		synchronized (lock) {

			while (atLeastOneLightIsGreen()) {
				lock.wait();
			}
			for (int i = 0; i < numberOfPedestrians; i++) {
				printLightStates();
				
				stateView.displayPedestrians();
			}
			lock.notifyAll();
		}
	}

	// Methode qui change la couleur des lumieres suivant les directions
	private void switchBackToRedAfterCarCirculation(Direction direction) throws InterruptedException {
		Object directionLock = directionLocks.get(direction);

		synchronized (directionLock) {
			while (!timeOutMaps.get(direction)) {
				directionLock.wait();
			}
		}

		switchLight(direction, LightColor.RED);

		synchronized (directionLock) {
			timeOutMaps.put(direction, false);
			directionLock.notifyAll();
		}
	}

	// Methode qui synchronise la circulation des voitures aux intersections
	public void carMove(Direction direction, IntersectionType intersectionType) throws Exception {

		synchronized (lock) {

			while (lightController.getLight(direction).isRed()) {
				lock.wait();
			}

		}
			if (isEastCross(direction, intersectionType) || isWestThreeWay(direction, intersectionType)) {
				getCarFromPreviousIntersection(direction, intersectionType);
			} else {

				sendCarsToNextIntersection(direction, intersectionType);
			}

		synchronized (directionLocks.get(direction)) {
			timeOutMaps.put(direction, true);
			directionLocks.get(direction).notifyAll();
		}
	}

	private boolean isEastCross(Direction direction, IntersectionType intersectionType) {
		return direction.equals(Direction.EAST) && intersectionType.equals(IntersectionType.CROSS);
	}

	private boolean isWestThreeWay(Direction direction, IntersectionType intersectionType) {
		return direction.equals(Direction.WEST) && intersectionType.equals(IntersectionType.THREE_WAY);
	}

	private void moveInCorrectDirection(Car car, Direction direction, IntersectionType intersectionType)
			throws Exception {
		
		Intersection intersection = this.synchroIntersection.getIntersection(intersectionType);
		Direction oppositeDirection = intersection.getOppositeDirection(direction);

		for (int i = 0; i < numberOfCars; i++) {
			printLightStates();

			synchronized (lock) {
				if (oppositeDirection == null || lightController.getLight(oppositeDirection).isRed()) {
					car.randomMoveWithPriority();
				} else {
					car.randomMoveWithOppositeSideOn();
				}
				stateView.displayCarState(car, IN_SYNCHRO);
			}
		}
	}

	/*
	 * PRODUCTEURS=> 3-WAY: EST-CONTINUE, SUD:GAUCHE
	 * PRODUCTEURS => CROSS: NORD-GAUCHE, OUEST-CONTINUE, SUD:DROITE 
	 * */
	private void sendCarsToNextIntersection(Direction direction, IntersectionType intersectionType) throws Exception {
		Car car = synchroIntersection.getCar(direction, intersectionType);
		moveInCorrectDirection(car, direction, intersectionType);

		if (car.canMoveToNextIntersection()) {
			car.setPreviousAction();

			if (intersectionType.equals(IntersectionType.THREE_WAY)) {
				car.setNextIntersectionType(IntersectionType.CROSS);
				this.crossIncomingCars.put(car);

			} else if (intersectionType.equals(IntersectionType.CROSS)) {
				car.setNextIntersectionType(IntersectionType.THREE_WAY);
				this.threeWayIncomingCars.put(car);

			} else {
				throw new Exception("Only 2 intersections available...");
			}
		}

	}

	/*
	 * CONSOMMATEURS=> 3-WAY: OUEST
	 * CONSOMMATEURS => CROSS: EST 
	 * */
	private void getCarFromPreviousIntersection(Direction direction, IntersectionType intersectionType)
			throws Exception {
		Car car = null;
		if (intersectionType.equals(IntersectionType.THREE_WAY)) {

			car = this.crossIncomingCars.take();

		} else if (intersectionType.equals(IntersectionType.CROSS)) {

			car = this.threeWayIncomingCars.take();
		} else {
			throw new Exception("Only 2 intersections available...");
		}

		moveInCorrectDirection(car, direction, intersectionType);
	}
}
package ca.ulaval.tp2.glo3004.control;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.car.CarFactory;
import ca.ulaval.tp2.glo3004.car.InvalidCarActionException;
import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.light.LightColor;
import ca.ulaval.tp2.glo3004.light.LightController;
import ca.ulaval.tp2.glo3004.road.Direction;
import ca.ulaval.tp2.glo3004.view.LightView;
import ca.ulaval.tp2.glo3004.view.StateView;

/**
 * Classe permettant le controle du traffic des voitures, pietons et lumieres
 * aux intersection en T et en croix
 */
public class SyncController {

	protected LightController lightController;
	private Map<Direction, Boolean> timeOutMaps = new HashMap<Direction, Boolean>();
	private Map<Direction, Direction> oppositeDirectionMap = new HashMap<Direction, Direction>();
	private Map<Direction, Direction[]> adjacenceMap = new HashMap<Direction, Direction[]>();
	private Map<Direction, Object> directionLocks = new HashMap<Direction, Object>();

	private BlockingQueue<Car> crossIncomingCars = new LinkedBlockingQueue<>(1);
	private BlockingQueue<Car> threeWayIncomingCars = new LinkedBlockingQueue<>(1);

	protected int numberOfCars;
	private int numberOfPedestrians;
	private Direction[] directions;
	private Object lock = new Object();

	private CarFactory carFactory;
	private LightView threeWayLightView;
	private LightView crossLightView;
	private int WAITING_TIME = 2000;
	private StateView stateView;

	// Constructeur avec parametres pour le controle du traffic sur une intersection
	// en T ou en croix
	public SyncController(CarFactory carFactory, ExecutionParameters parameters, 
			StateView stateView,
			LightView threeWayLightView,
			LightView crossLightView, 
			LightController lightController) {
		// this.intersectionType = intersectionType;
		this.carFactory = carFactory;
		this.numberOfCars = parameters.getNumberOfCars();
		this.numberOfPedestrians = parameters.getNumberOfPedestrians();
		this.directions = getDirections();
		this.oppositeDirectionMap = getOppositeDirectionMap();
		this.adjacenceMap = getAdjacenceMap();
		this.stateView = stateView;
		this.threeWayLightView = threeWayLightView;
		this.crossLightView = crossLightView;
		this.lightController = lightController;

		initializeDirectionVariables();
	}

	public Map<Direction, Direction> getOppositeDirectionMap() {

		Map<Direction, Direction> oppositeDirectionMap = new HashMap<Direction, Direction>() {
			private static final long serialVersionUID = 1L;

			{
				put(Direction.EAST, Direction.WEST);
				put(Direction.WEST, Direction.EAST);
				put(Direction.SOUTH, Direction.NORTH);
				put(Direction.NORTH, Direction.SOUTH);
			}
		};

		return oppositeDirectionMap;
	}

	public Map<Direction, Direction[]> getAdjacenceMap() {

		Map<Direction, Direction[]> adjacenceMap = new HashMap<Direction, Direction[]>() {
			private static final long serialVersionUID = 1L;

			{
				put(Direction.EAST, new Direction[] { Direction.SOUTH, Direction.NORTH });
				put(Direction.WEST, new Direction[] { Direction.SOUTH, Direction.NORTH });
				put(Direction.SOUTH, new Direction[] { Direction.EAST, Direction.WEST });
				put(Direction.NORTH, new Direction[] { Direction.EAST, Direction.WEST });
			}

		};

		return adjacenceMap;
	}

	public Direction[] getDirections() {
		return new Direction[] { Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH };
	}

	// Methode qui initialise les lumieres des interesections
	private void initializeDirectionVariables() {

		for (Direction direction : directions) {
			timeOutMaps.put(direction, false);
			directionLocks.put(direction, new Object());
		}
	}

	// Methode qui affiche le statut des lumieres en fonction des intersections
	private void printLightStates(IntersectionType intersectionType) {
		StringBuilder lightStates = new StringBuilder();
		lightStates.append(String.format("🚥 %s:", intersectionType));

		threeWayLightView.setLights(lightController.getLights());
		crossLightView.setLights(lightController.getLights());
		lightStates.append(lightController.getLightStates());

		System.out.print(lightStates.toString());
	}

	// Methode qui empeche les lumieres de l'est d'�tre vertes en meme temps que
	// celles du sud
	public void controlLight(Direction direction) throws Exception {

		synchronized (lock) {

			while (atLeastOneNeighBoorIsGreen(direction)) {

				lock.wait();
			}

			switchLight(direction, LightColor.GREEN);
		}


		Thread.sleep(2000);
		System.out.println("****************** I AM UP........");
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

		List<Direction> neighboors = Arrays.asList(this.adjacenceMap.get(direction));
		return this.lightController.atLeastOneNeighBoorIsGreen(neighboors, direction);
	}

	// Synchronisation du passage des pi�tons avec les lumi�res
	public void pedestrianPass(IntersectionType intersectionType) throws InterruptedException {

		synchronized (lock) {

			while (atLeastOneLightIsGreen()) {
				lock.wait();
			}
			for (int i = 0; i < numberOfPedestrians; i++) {
				printLightStates(intersectionType);
				
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

		if (!direction.equals(Direction.NORTH) && intersectionType.equals(IntersectionType.THREE_WAY)) {
			if (isEastCross(direction, intersectionType) || isWestThreeWay(direction, intersectionType)) {
				getCarFromPreviousIntersection(direction, intersectionType);
			} else {

				sendCarsToNextIntersection(direction, intersectionType);
			}
		}
		
		synchronized (directionLocks.get(direction)) {
			timeOutMaps.put(direction, true);
			directionLocks.get(direction).notifyAll();
		}
	}

	public void carCrossMove(Direction direction, IntersectionType intersectionType) throws Exception {

		
			while (lightController.getLight(direction).isGreen()) {

				if (isEastCross(direction, intersectionType) || isWestThreeWay(direction, intersectionType)) {

					getCarFromPreviousIntersection(direction, intersectionType);
				} else {

					sendCarsToNextIntersection(direction, intersectionType);
				}
			}
		
		
	}

	private boolean isEastCross(Direction direction, IntersectionType intersectionType) {
		return direction.equals(Direction.EAST) && intersectionType.equals(IntersectionType.CROSS);
	}

	private boolean isWestThreeWay(Direction direction, IntersectionType intersectionType) {
		return direction.equals(Direction.WEST) && intersectionType.equals(IntersectionType.THREE_WAY);
	}

	private void moveInCorrectDirection(Car car, Direction direction, IntersectionType intersectionType)
			throws InvalidCarActionException {
		Direction oppositeDirection = oppositeDirectionMap.get(direction);

		for (int i = 0; i < numberOfCars; i++) {
			printLightStates(intersectionType);

			synchronized (lock) {
				if (oppositeDirection == null || lightController.getLight(oppositeDirection).isRed()) {
					car.randomMoveWithPriority();
				} else {
					car.randomMoveWithOppositeSideOn();
				}
			}
		}
	}

	/*
	 * PRODUCTEURS=> 3-WAY: EST-CONTINUE, SUD:GAUCHE
	 * PRODUCTEURS => CROSS: NORD-GAUCHE, OUEST-CONTINUE, SUD:DROITE 
	 * */
	private void sendCarsToNextIntersection(Direction direction, IntersectionType intersectionType) throws Exception {
		Car car = carFactory.createCar(direction, intersectionType);
		moveInCorrectDirection(car, direction, intersectionType);

		if (car.canMoveToNextIntersection()) {
			car.setPreviousAction();
			car.setTypeCar("PRODUCE");

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

		car.setTypeCar("CONSUME");
		moveInCorrectDirection(car, direction, intersectionType);
	}
}
package ca.ulaval.tp2.glo3004.control;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.LightView;
import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.car.CarFactory;
import ca.ulaval.tp2.glo3004.light.Light;
import ca.ulaval.tp2.glo3004.light.LightColor;
import ca.ulaval.tp2.glo3004.light.LightController;

/**
 * Classe permettant le controle du traffic des voitures, pietons et lumieres
 * aux intersection en T et en croix
 */
public abstract class TraficController {

	protected LightController lightController;
	private Map<Direction, Boolean> timeOutMaps = new HashMap<Direction, Boolean>();
	private Map<Direction, Direction> oppositeDirectionMap = new HashMap<Direction, Direction>();
	private Map<Direction, Direction[]> adjacenceMap = new HashMap<Direction, Direction[]>();
	private Map<Direction, Object> directionLocks = new HashMap<Direction, Object>();

	protected int numberOfCars;
	private int numberOfPedestrians;
	private Direction[] directions;
	private Object lock = new Object();

	private CarFactory carFactory;
	private IntersectionType intersectionType;
	private LightView lightView;
	private int WAITING_TIME = 2000;
	private boolean intersectionIsSync;

	// Constructeur avec parametres pour le controle du traffic sur une intersection
	// en T ou en croix
	public TraficController(CarFactory carFactory, IntersectionType intersectionType, ExecutionParameters parameters,
			LightView lightView, LightController lightController, boolean intersectionIsSync) {
		this.intersectionType = intersectionType;
		this.carFactory = carFactory;
		this.numberOfCars = parameters.getNumberOfCars();
		this.numberOfPedestrians = parameters.getNumberOfPedestrians();
		this.directions = getDirections();
		this.oppositeDirectionMap = getOppositeDirectionMap();
		this.adjacenceMap = getAdjacenceMap();
		this.lightView = lightView;
		this.lightController = lightController;
		this.intersectionIsSync = intersectionIsSync;
		initializeDirectionVariables();
	}

	public abstract Map<Direction, Direction> getOppositeDirectionMap();

	public abstract Map<Direction, Direction[]> getAdjacenceMap();

	public abstract Direction[] getDirections();

	// Methode qui initialise les lumieres des interesections
	private void initializeDirectionVariables() {

		for (Direction direction : directions) {
			timeOutMaps.put(direction, false);
			directionLocks.put(direction, new Object());
		}
	}

	// Methode qui affiche le statut des lumieres en fonction des intersections
	private void printLightStates() {
		StringBuilder lightStates = new StringBuilder();
		lightStates.append(String.format("🚥 %s:%s:", Thread.currentThread().getName(), intersectionType));
		lightView.setLights(lightController.getLights());

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
	public void pedestrianPass() throws InterruptedException {

		synchronized (lock) {
		
			while (atLeastOneLightIsGreen()) {
				lock.wait();
			}
			for (int i = 0; i < numberOfPedestrians; i++) {
				printLightStates();
				System.out.println("🚶 PEDESTRIANS::GO");
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
	public void carMove(Direction direction) throws Exception {
		
		synchronized (lock) {
			
			while (lightController.getLight(direction).isRed()) {
				lock.wait();
			}
		}

		Car car = carFactory.createCar(direction, intersectionType);
		Direction oppositeDirection = oppositeDirectionMap.get(direction);

		for (int i = 0; i < numberOfCars; i++) {
			printLightStates();

			synchronized (lock) {
				if (oppositeDirection == null || lightController.getLight(oppositeDirection).isRed()) {
					car.randomMoveWithPriority();
				} else {
					car.randomMoveWithOppositeSideOn();
				}
			}
		}

		synchronized (directionLocks.get(direction)) {
			timeOutMaps.put(direction, true);
			directionLocks.get(direction).notifyAll();
		}

	}

}

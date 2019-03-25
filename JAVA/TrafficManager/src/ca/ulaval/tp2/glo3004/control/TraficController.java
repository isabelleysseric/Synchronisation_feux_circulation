package ca.ulaval.tp2.glo3004.control;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.car.CarFactory;
import ca.ulaval.tp2.glo3004.light.LightColor;

/**
 * Classe permettant le controle du traffic des voitures, pietons et lumieres
 * aux intersection en T et en croix
 */
public abstract class TraficController {

	private Map<Direction, LightColor> lights = new HashMap<Direction, LightColor>();
	private Map<Direction, Boolean> timeOutMaps = new HashMap<Direction, Boolean>();
	private Map<Direction, Direction> oppositeDirectionMap = new HashMap<Direction, Direction>();
	private Map<Direction, Direction[]> adjacenceMap = new HashMap<Direction, Direction[]>();
	private Map<Direction, Object> directionLocks = new HashMap<Direction, Object>();

	private int numberOfCars;
	private int numberOfPedestrians;
	private Direction[] directions;
	private Object lock = new Object();

	private CarFactory carFactory;
	private IntersectionType intersectionType;

	// Constructeur avec parametres pour le controle du traffic sur une intersection
	// en T ou en croix
	public TraficController(CarFactory carFactory, IntersectionType intersectionType, ExecutionParameters parameters) {
		this.intersectionType = intersectionType;
		this.carFactory = carFactory;
		this.numberOfCars = parameters.getNumberOfCars();
		this.numberOfPedestrians = parameters.getNumberOfPedestrians();
		this.directions = getDirections();
		this.oppositeDirectionMap = getOppositeDirectionMap();
		this.adjacenceMap = getAdjacenceMap();

		initializeDirectionVariables();
	}

	public abstract Map<Direction, Direction> getOppositeDirectionMap();

	public abstract Map<Direction, Direction[]> getAdjacenceMap();

	public abstract Direction[] getDirections();

	// Methode qui initialise les lumieres des interesections
	private void initializeDirectionVariables() {

		for (Direction direction : directions) {
			lights.put(direction, LightColor.RED);
			timeOutMaps.put(direction, false);
			directionLocks.put(direction, new Object());
		}
	}

	// Methode qui affiche le statut des lumieres en fonction des intersections
	private void printLightStates() {

		synchronized (lock) {
			StringBuilder lightStates = new StringBuilder();
			lightStates.append("🚥");

			lights.forEach((direction, color) -> {
				lightStates.append(String.format(" %s:%s ", direction, color));
			});

			System.out.print(lightStates.toString());
		}
	}

	private void switchLight(Direction direction, LightColor color) {

		synchronized (lock) {
			lights.put(direction, color);
			lock.notifyAll();
		}
	}

	private boolean atLeastOneLightIsGreen() {
		return lights.values().contains(LightColor.GREEN);
	}

	private boolean atLeastOneNeighBoorIsGreen(Direction direction) {
		List<Direction> neighboors = Arrays.asList(this.adjacenceMap.get(direction));
		List<LightColor> neighboorsLights = neighboors.stream().map(neighboor -> this.lights.get(neighboor))
				.collect(Collectors.toList());

		return neighboorsLights.contains(LightColor.GREEN);
	}

	// Synchronisation du passage des pi�tons avec les lumi�res
	public void pedestrianPass() throws InterruptedException {

		synchronized (lock) {
			while (atLeastOneLightIsGreen()) {
				lock.wait();
			}
			for (int i = 0; i < numberOfPedestrians; i++) {
				printLightStates();
				System.out.println("🚶 PEDESTRIANS::CROSS");
			}
			lock.notifyAll();
		}
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
			while (lights.get(direction).isRed()) {
				lock.wait();
			}
		}

		Car car = carFactory.createCar(direction, intersectionType);
		Direction oppositeDirection = oppositeDirectionMap.get(direction);

		for (int i = 0; i < numberOfCars; i++) {
			printLightStates();

			synchronized (lock) {
				if (oppositeDirection == null || lights.get(oppositeDirection).isRed()) {
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

	// Methode qui empeche les lumieres de l'est d'�tre vertes en meme temps que
	// celles du sud
	/*
	 * public void controlEastLight() throws Exception { Direction direction =
	 * Direction.EAST;
	 * 
	 * synchronized (lock) {
	 * 
	 * while (atLeastOneNeighBoorIsGreen(direction)) { lock.wait(); }
	 * 
	 * switchLight(direction, LightColor.GREEN); }
	 * 
	 * switchBackToRedAfterCarCirculation(direction); }
	 * 
	 * 
	 * 
	 * // Methode qui empeche les lumieres de l'ouest d'�tre vertes en meme temps
	 * que // celles du sud public synchronized void controlWestLight() throws
	 * Exception { Direction direction = Direction.WEST;
	 * 
	 * synchronized (lock) { while (atLeastOneNeighBoorIsGreen(direction)) {
	 * lock.wait(); }
	 * 
	 * switchLight(direction, LightColor.GREEN); }
	 * 
	 * switchBackToRedAfterCarCirculation(direction); }
	 * 
	 * // Methode qui empeche les lumieres du sud d'�tre vertes en meme temps que //
	 * celles de l'est ou de l'ouest public void controlSouthLight() throws
	 * Exception { Direction direction = Direction.SOUTH;
	 * 
	 * synchronized (lock) { while (atLeastOneNeighBoorIsGreen(direction)) {
	 * lock.wait(); }
	 * 
	 * switchLight(direction, LightColor.GREEN); }
	 * 
	 * switchBackToRedAfterCarCirculation(direction); }
	 * 
	 * // Methode qui empeche les lumieres du nord d'�tre vertes en meme temps que
	 * // celles de l'est ou de l'ouest public void controlNorthLight() throws
	 * Exception { Direction direction = Direction.NORTH;
	 * 
	 * synchronized (lock) { while (atLeastOneNeighBoorIsGreen(direction)) {
	 * lock.wait(); }
	 * 
	 * switchLight(direction, LightColor.GREEN); }
	 * 
	 * switchBackToRedAfterCarCirculation(direction); }
	 */

}

package ca.ulaval.tp2.glo3004.control;

import java.util.HashMap;
import java.util.Map;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.car.CarFactory;
import ca.ulaval.tp2.glo3004.light.Light;
import ca.ulaval.tp2.glo3004.light.LightColor;

public abstract class TraficController {

	private Map<Direction, Light> lights = new HashMap<Direction, Light>();
	private Map<Direction, Boolean> timeOutMaps = new HashMap<Direction, Boolean>();
	private Map<Direction, Direction> oppositeDirectionMap = new HashMap<Direction, Direction>();

	private int numberOfCars;
	private int numberOfPedestrians;
	private Direction[] directions;
	
	private CarFactory carFactory;
	private IntersectionType intersectionType;
	
	public TraficController(CarFactory carFactory, IntersectionType intersectionType, ExecutionParameters parameters) {
		this.intersectionType = intersectionType;
		this.carFactory = carFactory;
		this.numberOfCars = parameters.getNumberOfCars();
		this.numberOfPedestrians = parameters.getNumberOfPedestrians();
		this.directions = getDirections();
		this.oppositeDirectionMap = getOppositeDirectionMap();
		
		initializeLightsAndTimeOuts();
	}

	private void initializeLightsAndTimeOuts() {
		for (Direction direction : directions) {
			Light light = new Light(direction);
			lights.put(direction, light);
			timeOutMaps.put(direction, false);
		}
	}

	public synchronized void pedestrianPass() throws InterruptedException {

		while (lights.get(Direction.EAST).isGreen() 
				|| lights.get(Direction.WEST).isGreen()
				|| lights.get(Direction.SOUTH).isGreen()) {
			wait();
		}

		for (int i = 0; i < numberOfPedestrians; i++) {
			printLightStates();
			System.out.println("🚶 PEDESTRIANS::CROSS");
		}

		notifyAll();
	}
	
	private void printLightStates() {
		LightColor eastColor = lights.get(Direction.EAST).getColor();
		LightColor westColor = lights.get(Direction.WEST).getColor();
		LightColor southColor = lights.get(Direction.SOUTH).getColor();
		String lightStates = String.format("🚥 EAST:%s WEST:%s SOUTH:%s ", eastColor, westColor, southColor);

		System.out.print(lightStates);
	}

	public synchronized void controlEastLight() throws Exception {
		while (lights.get(Direction.SOUTH).isGreen()) {
			wait();
		}
		
		Direction direction = Direction.EAST;
		handleLightState(direction);
	}

	private void handleLightState(Direction direction) throws InterruptedException {
		lights.get(direction).switchTo(LightColor.GREEN);
		notifyAll();

		while (!timeOutMaps.get(direction)) {
			wait();
		}
			
		lights.get(direction).switchTo(LightColor.RED);
		timeOutMaps.put(direction, false);
		notifyAll();
	}
	
	public synchronized void controlWestLight() throws Exception {
		while (lights.get(Direction.SOUTH).isGreen()) {
			wait();
		}
		
		Direction direction = Direction.WEST;
		handleLightState(direction);
	}

	public synchronized void controlSouthLight() throws Exception {
		while (lights.get(Direction.WEST).isGreen() || lights.get(Direction.EAST).isGreen()) {
			wait();
		}
		
		Direction direction = Direction.SOUTH;
		handleLightState(direction);
	}

	public synchronized void carMove(Direction direction) throws Exception {

		while (lights.get(direction).isRed()) {
			wait();
		}
		
		Car car = carFactory.createCar(direction, intersectionType);
		Direction oppositeDirection = oppositeDirectionMap.get(direction);

		for (int i = 0; i < numberOfCars; i++) {
			printLightStates();

			if (oppositeDirection == null || lights.get(oppositeDirection).isRed()) {
				car.randomMoveWithPriority();
			} else {
				car.randomMoveWithOppositeSideOn();
			}
		}

		timeOutMaps.put(direction, true);
		notifyAll();
	}
	
	public abstract Map<Direction, Direction> getOppositeDirectionMap();
	
	public abstract Direction[] getDirections();
	
}

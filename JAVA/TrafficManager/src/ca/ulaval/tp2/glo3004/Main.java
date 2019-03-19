package ca.ulaval.tp2.glo3004;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.tp2.glo3004.car.CarFactory;
import ca.ulaval.tp2.glo3004.car.InvalidCarActionException;
import ca.ulaval.tp2.glo3004.control.ExecutionParameters;
import ca.ulaval.tp2.glo3004.control.ThreeWayIntersectionController;
import ca.ulaval.tp2.glo3004.control.TraficController;
import ca.ulaval.tp2.glo3004.control.runnable.CarRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.LightRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.PedestrianRunnable;

public class Main {

	private static List<Thread> threads = new ArrayList<Thread>();
	
	public static void main(String[] args) throws InvalidCarActionException {
		
		System.out.println("TP2 setup ...");
		 
		Direction[] directions = new Direction[] { Direction.EAST, Direction.WEST};
		CarFactory carFactory = new CarFactory();
		
		int numberOfCars = 2;
		int numberOfPedestrians = 2;
		ExecutionParameters parameters = new ExecutionParameters(numberOfCars, numberOfPedestrians);
		
		TraficController traficController = new ThreeWayIntersectionController(carFactory, parameters);
		
		initializePedestriansThread(traficController);
		initializeAllDirectionThreads(directions, traficController);
		
		for(Thread thread: threads) {
			thread.start();
		}
		
	}
	
	private static void initializeAllDirectionThreads(Direction[] directions, TraficController traficController) {
		
		for(Direction direction: directions) {
			
			Runnable carRunnable = new CarRunnable(direction, traficController);
			addNewThread(carRunnable, direction, "CAR");
			
			Runnable greenRunnable = new LightRunnable(direction, traficController);
			addNewThread(greenRunnable, direction, "GREEN");
		}
	}
	
	private static void addNewThread(Runnable runnable, Direction direction, String type) {
		Thread thread = new Thread(runnable);
		String threadName = String.format("%s:%s::Thread", type, direction);
		thread.setName(threadName);
		threads.add(thread);
	}
	
	private static void initializePedestriansThread(TraficController traficController) {
		PedestrianRunnable pedestriansRunnable = new PedestrianRunnable(traficController);
		Thread pedestrianThread = new Thread(pedestriansRunnable);
		pedestrianThread.start();
	}
	
}
package ca.ulaval.tp2.glo3004;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.tp2.glo3004.car.InvalidCarActionException;
import ca.ulaval.tp2.glo3004.control.TraficController;
import ca.ulaval.tp2.glo3004.control.runnable.GreenRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.PedestrianRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.RedRunnable;
import ca.ulaval.tp2.glo3004.intersection.Intersection;
import ca.ulaval.tp2.glo3004.intersection.ThreeWayIntersection;

public class Main {

	private static List<Thread> threads = new ArrayList<Thread>();
	
	public static void main(String[] args) throws InvalidCarActionException {
		
		System.out.println("TP2 setup ...");
		 
		Intersection threeWayIntersection = new ThreeWayIntersection();
		Direction[] directions = threeWayIntersection.getAllowedDirections();
		
		TraficController traficController = new TraficController(threeWayIntersection);
		
		initializePedestriansThread(traficController);
		initializeRedThread(directions, traficController);
		
		for(Thread thread: threads) {
			thread.start();
		}
		
	}
	
	private static void initializeRedThread(Direction[] directions, TraficController traficController) {
		
		for(Direction direction: directions) {
			Runnable redRunnable = new RedRunnable(direction, traficController);
			addNewThread(redRunnable, direction, "RED");
			
			Runnable greenRunnable = new GreenRunnable(direction, traficController);
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

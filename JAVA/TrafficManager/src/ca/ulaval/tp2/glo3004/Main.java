package ca.ulaval.tp2.glo3004;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ca.ulaval.tp2.glo3004.car.CarFactory;
import ca.ulaval.tp2.glo3004.car.InvalidCarActionException;
import ca.ulaval.tp2.glo3004.control.CrossIntersectionController;
import ca.ulaval.tp2.glo3004.control.ExecutionParameters;
import ca.ulaval.tp2.glo3004.control.ThreeWayIntersectionController;
import ca.ulaval.tp2.glo3004.control.TraficController;
import ca.ulaval.tp2.glo3004.control.runnable.CarRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.LightRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.PedestrianRunnable;

public class Main {

	private static List<Thread> threads = new ArrayList<Thread>();
	
	public static void main(String[] args) throws InvalidCarActionException {
		
		System.out.println(""
				+ "**************************************** \n"
				+ "       BIENVENUE DANS LE PROGRAMME       \n"
				+ " Synchronisation de feux de circulation  \n"
				+ "**************************************** \n");
		
		BufferedReader myReader = new BufferedReader(new InputStreamReader(System.in)); 
        
        System.out.print(""
        		+ "Quelle intersection voulez-vous exécuter ? \n "
        		+ "1. Intersection en T \n "
        		+ "2. Intersection ordinaire \n "
        		+ "3. Les deux intersections \n"
        		+ "\n"
        		+ "Votre choix: " );
        
        String choice = "";
		try {
			choice = myReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		} 
  
		if (choice.equals("1")) {
			
			System.out.print("\n");
			
	       /* System.out.print(""
	        		+ "Quelle intersection voulez-vous exécuter ? \n "
	        		+ "1. Intersection en T \n "
	        		+ "2. Intersection ordinaire \n "
	        		+ "3. Les deux intersections"); */
			
			Direction[] directions = new Direction[] { Direction.EAST, Direction.WEST};
			CarFactory carFactory = new CarFactory();
			
			int numberOfCars = 2;
			int numberOfPedestrians = 2;
			ExecutionParameters parameters = new ExecutionParameters(numberOfCars, numberOfPedestrians);
			
			TraficController traficController = new ThreeWayIntersectionController(carFactory, parameters);
			
			initializePedestriansThread(traficController);
			initializeAllDirectionThreads(directions, traficController);
		} 
		else if (choice.equals("2")){ 
			
			System.out.print("\n");
			
		    /* System.out.print(""
    		+ "Quelle intersection voulez-vous exécuter ? \n "
    		+ "1. Intersection en T \n "
    		+ "2. Intersection ordinaire \n "
    		+ "3. Les deux intersections"); */
			
			Direction[] directions = new Direction[] { Direction.EAST, Direction.WEST,Direction.SOUTH, Direction.NORTH};
			CarFactory carFactory = new CarFactory();
			
			int numberOfCars = 2;
			int numberOfPedestrians = 2;
			ExecutionParameters parameters = new ExecutionParameters(numberOfCars, numberOfPedestrians);
			
			TraficController traficController = new CrossIntersectionController(carFactory, parameters);
			
			initializePedestriansThread(traficController);
			initializeAllDirectionThreads(directions, traficController);
		} 
		else {
			
			System.out.print("\n");
			
		    /* System.out.print(""
    		+ "Quelle intersection voulez-vous exécuter ? \n "
    		+ "1. Intersection en T \n "
    		+ "2. Intersection ordinaire \n "
    		+ "3. Les deux intersections"); */
			
			// A faire (cas 2 instersections)
		} 		
		
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
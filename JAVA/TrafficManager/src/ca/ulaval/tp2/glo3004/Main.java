package ca.ulaval.tp2.glo3004;

import java.util.Scanner;

import ca.ulaval.tp2.glo3004.car.InvalidCarActionException;
import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.view.MainView;

public class Main {

	
	private static MainView mainView;
	
	public static void main(String[] args) throws InvalidCarActionException {

		displayMenu();
		mainView.startExecution();
	
	}
	
	private static void displayMenu() {
		
		Scanner myReader = new Scanner(System.in);
		int typeIntersection = 0;
		int numberOfCars = 0;
		int numberOfPedestrians = 0;
		
		
		System.out.println("" + "**************************************** \n"
							  + "          WELCOME TO THE PROGRAM         \n" 
							  + "     Synchronization of traffic lights   \n"
							  + "**************************************** \n");

		System.out.print("\n" + "Which intersection do you want to run? \n " 
							  + "1. Intersection three way \n "
							  + "2. Intersection cross     \n " 
							  + "3. The two intersections  \n" 
							  + "Your choice: ");
		typeIntersection = myReader.nextInt();
		myReader.nextLine();

		System.out.print("\n" + "How many cars do you want ? \n" 
					          + "Your choice: ");
		numberOfCars = myReader.nextInt();
		myReader.nextLine();
	
		System.out.print("\n" + "How many pedestrians do you want ? \n"
					   		  + "Your choice: ");
		numberOfPedestrians = myReader.nextInt();
		myReader.nextLine();
		myReader.close();
		System.out.println("\n" + "**************************************** \n");

		ExecutionParameters parameters = new ExecutionParameters(numberOfCars, numberOfPedestrians);

		switch(typeIntersection) 
		{
		case 1:
			initializeApp(IntersectionType.THREE_WAY, parameters);
			break;
			
		case 2:
			initializeApp(IntersectionType.CROSS, parameters);
			break;
		case 3:
			initializeApp(IntersectionType.SYNCHRO, parameters);
			break;
			
		default:
			System.out.println("Sorry not implemented yet ...");
			break;
		}		
	}
	

	private static void initializeApp(IntersectionType intersectionType, ExecutionParameters parameters) {
		mainView = new MainView(intersectionType, parameters);
	}

}
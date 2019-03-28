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
							  + "       BIENVENUE DANS LE PROGRAMME       \n" 
							  + " Synchronisation de feux de circulation  \n"
							  + "**************************************** \n");

		System.out.print("\n" + "Quelle intersection voulez-vous executer ? \n " 
							  + "1. Intersection en T      \n "
							  + "2. Intersection en croix  \n " 
							  + "3. Les deux intersections \n" 
							  + "Votre choix: ");
		typeIntersection = myReader.nextInt();
		myReader.nextLine();

		System.out.print("\n" + "Combien de voitures voulez-vous ? \n" 
					          + "Votre choix: ");
		numberOfCars = myReader.nextInt();
		myReader.nextLine();
	
		System.out.print("\n" + "Combien de pietons voulez-vous ? \n"
					   		  + "Votre choix: ");
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
			
		default:
			System.out.println("Sorry not implemented yet ...");
			break;
		}		
	}
	

	private static void initializeApp(IntersectionType intersectionType, ExecutionParameters parameters) {
		mainView = new MainView(intersectionType, parameters);
	}

}
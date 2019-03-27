package ca.ulaval.tp2.glo3004.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ca.ulaval.tp2.glo3004.car.InvalidCarActionException;


import ca.ulaval.tp2.glo3004.control.ExecutionParameters;
import ca.ulaval.tp2.glo3004.control.IntersectionType;

public class Main {

	
	private static MainView mainView;
	
	public static void main(String[] args) throws InvalidCarActionException {

		displayMenu();
		//initializeApp(IntersectionType.THREE_WAY);
		mainView.startExecution();
	
	}
	
	private static void displayMenu() {
		System.out.println("" + "**************************************** \n"
				+ "       BIENVENUE DANS LE PROGRAMME       \n" + " Synchronisation de feux de circulation  \n"
				+ "**************************************** \n");

		BufferedReader myReader = new BufferedReader(new InputStreamReader(System.in));

		System.out.print("" + "Quelle intersection voulez-vous ex?cuter ? \n " + "1. Intersection en T \n "
				+ "2. Intersection en croix \n " + "3. Les deux intersections \n" + "\n" + "Votre choix: ");

		String choice = "";
		try {
			choice = myReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		switch(choice) {
		case "1":
			initializeApp(IntersectionType.THREE_WAY);
			break;
		case "2":
			initializeApp(IntersectionType.CROSS);
			break;
			
		case "3":
			initializeApp(IntersectionType.SYNCHRO);
			break;
			
		default:
			break;
		}
	
	}
	
	private static void initializeApp(IntersectionType intersectionType) {
		int numberOfCars = 2;
		int numberOfPedestrians = 1;

		ExecutionParameters parameters = new ExecutionParameters(numberOfCars, numberOfPedestrians);
		mainView = new MainView(intersectionType, parameters);
	}

}
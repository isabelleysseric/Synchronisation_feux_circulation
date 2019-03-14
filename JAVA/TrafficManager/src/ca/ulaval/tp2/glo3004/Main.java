package ca.ulaval.tp2.glo3004;

import ca.ulaval.tp2.glo3004.car.InvalidCarActionException;

public class Main {

	public static void main(String[] args) throws InvalidCarActionException {
		
		System.out.println("TP2 setup quick test...");
		
		TraficController traficController = new TraficController();
		traficController.testWithEastCirculation();
	}
	
}

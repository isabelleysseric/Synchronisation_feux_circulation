package ca.ulaval.tp2.glo3004;

import ca.ulaval.tp2.glo3004.car.InvalidCarActionException;
import ca.ulaval.tp2.glo3004.view.MainView;

public class Main {

	
	private static MainView mainView;
	
	public static void main(String[] args) throws InvalidCarActionException {
		mainView = new MainView();
		mainView.render();
	}

	
}
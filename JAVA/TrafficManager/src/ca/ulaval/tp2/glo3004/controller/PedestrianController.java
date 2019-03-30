package ca.ulaval.tp2.glo3004.controller;

import ca.ulaval.tp2.glo3004.view.StateView;

public class PedestrianController {

	private final StateView stateView;
	private final int numberOfPedestrians;

	public PedestrianController(StateView stateView, int numberOfPedestrians) {
		this.stateView = stateView;
		this.numberOfPedestrians = numberOfPedestrians;
	}
	
	public void canCrossStreet() {
			for(int i = 0; i< numberOfPedestrians; i++) {
				System.out.println("Pedestrian PASS");
				stateView.displayPedestrians();
			}
		
		
	}
}

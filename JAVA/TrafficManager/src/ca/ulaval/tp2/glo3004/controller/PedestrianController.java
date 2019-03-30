package ca.ulaval.tp2.glo3004.controller;

import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.view.StateView;

public class PedestrianController {

	private final StateView stateView;
	private final int numberOfPedestrians;
	private IntersectionType intersectionType;

	public PedestrianController(StateView stateView, int numberOfPedestrians, IntersectionType intersectionType) {
		this.stateView = stateView;
		this.numberOfPedestrians = numberOfPedestrians;
		this.intersectionType = intersectionType;
	}
	
	public void canCrossStreet() {
			for(int i = 0; i< numberOfPedestrians; i++) {
				System.out.println("Pedestrian PASS");
				stateView.displayPedestrianState(intersectionType);
			}
		
		
	}
}

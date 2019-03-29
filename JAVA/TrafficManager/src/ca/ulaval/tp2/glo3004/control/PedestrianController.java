package ca.ulaval.tp2.glo3004.control;

import ca.ulaval.tp2.glo3004.view.StateView;

public class PedestrianController {

	private StateView stateView;
	private int numberOfPedestrians;

	public PedestrianController(StateView stateView, int numberOfPedestrians) {
		this.stateView = stateView;
		this.numberOfPedestrians = numberOfPedestrians;
	}

	public void go() {

		for (int i = 0; i < numberOfPedestrians; i++) {
			stateView.displayPedestrians();
		}

	}

}

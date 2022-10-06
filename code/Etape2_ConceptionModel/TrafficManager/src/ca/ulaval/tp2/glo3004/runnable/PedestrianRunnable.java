package ca.ulaval.tp2.glo3004.runnable;

import ca.ulaval.tp2.glo3004.controller.PedestrianController;

public class PedestrianRunnable implements Runnable {

	private PedestrianController pedestrianController;

	public PedestrianRunnable(PedestrianController pedestrianController) {

		this.pedestrianController = pedestrianController;

	}

	public void run() {

		try {
			this.pedestrianController.canCrossStreet();
			Thread.sleep(2500);
		} catch (InterruptedException exception) {
			return;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

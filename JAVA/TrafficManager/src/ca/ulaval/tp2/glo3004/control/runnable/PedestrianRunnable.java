package ca.ulaval.tp2.glo3004.control.runnable;

import ca.ulaval.tp2.glo3004.control.PedestrianController;

public class PedestrianRunnable implements Runnable {

	private boolean btnValue;
	private boolean threadSuspended;
	private PedestrianController pedestrianController;

	public PedestrianRunnable(PedestrianController pedestrianController) {
		this.pedestrianController = pedestrianController;
	}

	public void setPaused(boolean pausedValue) {
		btnValue = pausedValue;
	}

	public void run() {

		try {
			synchronized (this) {
				while (threadSuspended)
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
						return;
					}
			}
			pedestrianController.go();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

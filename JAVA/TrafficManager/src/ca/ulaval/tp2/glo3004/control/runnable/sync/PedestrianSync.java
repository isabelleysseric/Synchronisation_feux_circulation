package ca.ulaval.tp2.glo3004.control.runnable.sync;

import ca.ulaval.tp2.glo3004.control.SyncController;

public class PedestrianSync implements Runnable {

	private boolean btnValue;
	private boolean threadSuspended;
	private SyncController syncController;

	public PedestrianSync(SyncController syncController) {
		this.syncController = syncController;
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
					}
			}
			syncController.pedestrianPass();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}


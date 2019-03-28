package ca.ulaval.tp2.glo3004.control.runnable;

import ca.ulaval.tp2.glo3004.control.IntersectionType;
import ca.ulaval.tp2.glo3004.control.SyncController;
import ca.ulaval.tp2.glo3004.control.TraficController;

public class PedestrianRunnable implements Runnable {

	private TraficController control;
	private SyncController syncController;

	private IntersectionType intersectionType;
	private boolean isSynchro;
	private boolean btnValue;
	private boolean threadSuspended;

	public PedestrianRunnable(IntersectionType intersectionType, TraficController control,
			SyncController syncController, boolean isSynchro) {
		this.control = control;
		this.syncController = syncController;
		this.intersectionType = intersectionType;
		this.isSynchro = isSynchro;
	}
	
	public void setPaused(boolean pausedValue) {
		btnValue = pausedValue;
	}

	public void run() {
		
		while (true) {

			try {
				if(isSynchro) syncController.pedestrianPass(intersectionType);
				else control.pedestrianPass();

				Thread.sleep(1500);
			} catch (InterruptedException exception) {
				return;

			} catch (Exception e) {
				e.printStackTrace();
			}
			
            synchronized(this) {
                while (threadSuspended)
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
            }

		}

	}

}

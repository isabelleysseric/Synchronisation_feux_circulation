package ca.ulaval.tp2.glo3004.control.runnable.cross;

import ca.ulaval.tp2.glo3004.control.IntersectionType;
import ca.ulaval.tp2.glo3004.control.SyncController;
import ca.ulaval.tp2.glo3004.control.TraficController;

public class PedestrianCrossRunnable implements Runnable {

	private TraficController controler;
	private SyncController syncController;

	private IntersectionType intersectionType;
	private boolean isSynchro;
	private boolean threadSuspended;

	public PedestrianCrossRunnable(IntersectionType intersectionType, TraficController control,
			SyncController syncController, boolean isSynchro) {
		this.controler = control;
		this.syncController = syncController;
		this.intersectionType = intersectionType;
		this.isSynchro = isSynchro;
	}

	public void run() {
		
		while (true) {

			try {
				if(isSynchro) syncController.pedestrianPass(intersectionType);
				else controler.pedestrianPass();

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

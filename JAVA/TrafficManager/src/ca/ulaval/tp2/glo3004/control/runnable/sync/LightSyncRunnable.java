package ca.ulaval.tp2.glo3004.control.runnable.sync;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.control.SyncController;

public class LightSyncRunnable implements Runnable {


	private Direction direction;
	private SyncController syncController;
	private boolean threadSuspended;

	public LightSyncRunnable(Direction direction, SyncController syncController) {
		this.direction = direction;
		this.syncController =  syncController;
	}

	public void run() {

		while (true) {

			try {
				syncController.controlLight(direction);
				
				Thread.sleep(1500);
			} catch (InterruptedException exception) {
				System.out.println("LIGHT RUNNABLE STOP");
			    return;
			} catch (Exception exception) {
				exception.printStackTrace();
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
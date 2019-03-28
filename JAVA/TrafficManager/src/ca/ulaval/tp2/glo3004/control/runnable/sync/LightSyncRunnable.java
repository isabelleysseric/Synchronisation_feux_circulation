package ca.ulaval.tp2.glo3004.control.runnable.sync;

import ca.ulaval.tp2.glo3004.control.SyncController;
import ca.ulaval.tp2.glo3004.road.Direction;

public class LightSyncRunnable implements Runnable {


	private Direction direction;
	private SyncController syncController;

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

		}
	
	}
	
}
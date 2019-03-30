package ca.ulaval.tp2.glo3004.runnable.sync;

import ca.ulaval.tp2.glo3004.controller.sync.LightSyncController;
import ca.ulaval.tp2.glo3004.road.Direction;

public class LightSyncRunnable implements Runnable {

	
	private Direction direction;
	private final LightSyncController controller;
	private int waitingTime;
	
	public LightSyncRunnable(Direction direction, LightSyncController controller, int waitingTime) {
		this.direction = direction;
		this.controller = controller;
		this.waitingTime = waitingTime;
	}

	public void run() {
		
		while (true) {
			
			try {
				controller.switchLightState(direction);
				
				Thread.sleep(waitingTime);
				
			} catch (InterruptedException exception) {
				return;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}


}
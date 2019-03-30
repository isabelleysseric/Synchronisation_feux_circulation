package ca.ulaval.tp2.glo3004.runnable;

import ca.ulaval.tp2.glo3004.controller.LightController;
import ca.ulaval.tp2.glo3004.road.Direction;

public class LightCarRunnable implements Runnable {

	
	private Direction direction;
	private LightController controller;
	private int waitingTime;
	
	public LightCarRunnable(Direction direction, LightController controller, int waitingTime) {
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
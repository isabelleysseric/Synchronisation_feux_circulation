package ca.ulaval.tp2.glo3004.control.runnable;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.control.TraficController;

public class RedRunnable implements Runnable{

	private TraficController controler;
	private Direction direction;

	public RedRunnable(Direction direction, TraficController controler) {
		this.controler = controler;
		this.direction = direction;
	}

	public void run() {

		while (true) {

			try {
				notifyControler();
				Thread.sleep(1500);
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			}

		}
	}

	private void notifyControler() throws InterruptedException {
		
		switch (direction) {
		
		case EAST:
			controler.eastSwitchToRed();
			break;
		case WEST:
			controler.westSwitchToRed();
			break;
		case SOUTH:
			controler.southSwitchToRed();
			break;
		case NORTH:
			break;
		}

	}
}

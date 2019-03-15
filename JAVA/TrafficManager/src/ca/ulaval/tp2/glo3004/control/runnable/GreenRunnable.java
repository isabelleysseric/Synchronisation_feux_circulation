package ca.ulaval.tp2.glo3004.control.runnable;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.control.TraficController;


public class GreenRunnable implements Runnable{

	private TraficController controler;
	private Direction direction;

	public GreenRunnable(Direction direction, TraficController controler) {
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
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void notifyControler() throws Exception {
		
		switch (direction) {
		
		case EAST:
			controler.eastSwitchToGreen();
			break;
		case WEST:
			controler.westSwitchToGreen();
			break;
		case SOUTH:
			controler.southSwitchToGreen();
			break;
		case NORTH:
			break;
		}

	}
}
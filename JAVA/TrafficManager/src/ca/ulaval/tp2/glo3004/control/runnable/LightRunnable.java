package ca.ulaval.tp2.glo3004.control.runnable;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.control.TraficController;

public class LightRunnable implements Runnable {

	private TraficController controler;
	private Direction direction;

	public LightRunnable(Direction direction, TraficController controler) {
		this.controler = controler;
		this.direction = direction;
	}

	public void run() {

		while (true) {

			try {
				//notifyControler();
				controler.controlLight(direction);
			
				Thread.sleep(1500);
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			} catch (Exception exception) {
				exception.printStackTrace();
			}

		}
	}

	private void notifyControler() throws Exception {

		/*switch (direction) {

		case EAST:
			controler.controlEastLight();
			break;
		case WEST:
			controler.controlWestLight();
			break;
		case SOUTH:
			controler.controlSouthLight();
			break;
		case NORTH:
			controler.controlNorthLight();
			break;
		}*/

	}
}
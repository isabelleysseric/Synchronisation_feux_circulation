package ca.ulaval.tp2.glo3004.control.runnable;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.control.TraficController;

public class LightRunnable implements Runnable {

	private TraficController controler;
	private Direction direction;
	private boolean threadSuspended;

	public LightRunnable(Direction direction, TraficController controler) {
		this.controler = controler;
		this.direction = direction;

	}

	public void run() {

		while (true) {

			try {
				if (!direction.equals(Direction.NORTH)) {
					controler.controlLight(direction);

				}
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
package ca.ulaval.tp2.glo3004.control.runnable;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.control.IntersectionType;
import ca.ulaval.tp2.glo3004.control.SyncController;
import ca.ulaval.tp2.glo3004.control.TraficController;

public class CarRunnable implements Runnable {

	private TraficController controler;
	private Direction direction;
	private SyncController syncController;
	private IntersectionType intersectionType;
	private boolean isSynchro;

	public CarRunnable(IntersectionType intersectionType, Direction direction, TraficController controler,
			SyncController syncController, boolean isSynchro) {
		this.controler = controler;
		this.direction = direction;
		this.syncController = syncController;
		this.intersectionType = intersectionType;
		this.isSynchro = isSynchro;

	}

	public void run() {

		if (this.isSynchro) {
			while (true) {

				try {
					
					 syncController.carMove(direction, intersectionType);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} else {
			normalThreeWayIntersection();
		}

	}

	private void normalThreeWayIntersection() {
		while (true) {

			try {
				if(!direction.equals(Direction.NORTH)){
					controler.carMove(direction);
				}

				Thread.sleep(1500);
			} catch (InterruptedException exception) {
				return;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
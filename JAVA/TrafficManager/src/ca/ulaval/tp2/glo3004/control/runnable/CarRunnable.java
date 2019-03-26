package ca.ulaval.tp2.glo3004.control.runnable;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.control.TraficController;

public class CarRunnable  implements Runnable{

	private TraficController controler;
	private Direction direction;
	
	public CarRunnable(Direction direction, TraficController controler) {
		this.controler = controler;
		this.direction = direction;
		
	}

	public void run() {

			
			while (true) {

				try {
					controler.carMove(direction);
					Thread.sleep(1500);
				} catch (InterruptedException exception) {
				    return;
				 
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			
		}
		
	
	
}
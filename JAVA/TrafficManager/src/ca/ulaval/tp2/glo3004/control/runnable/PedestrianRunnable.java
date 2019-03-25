package ca.ulaval.tp2.glo3004.control.runnable;

import ca.ulaval.tp2.glo3004.control.TraficController;

public class PedestrianRunnable implements Runnable {

	private TraficController control;
	
	public PedestrianRunnable(TraficController control) {
		this.control = control;
	}
	
	
	public void run() {
		try {
			while (true) {
				control.pedestrianPass();
				Thread.sleep(1000);
				
			}
		} catch (InterruptedException e) {
		}
	}
	
}

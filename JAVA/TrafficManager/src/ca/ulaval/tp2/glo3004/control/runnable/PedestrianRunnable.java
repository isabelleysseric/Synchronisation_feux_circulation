package ca.ulaval.tp2.glo3004.control.runnable;


public class PedestrianRunnable implements Runnable {

	private PedestrianController pedestrianController;
	
	public PedestrianRunnable(PedestrianController pedestrianController) {
		this.pedestrianController = pedestrianController;
	}
	

	public void run() {

			try {
				pedestrianController.go();
			} catch (Exception e) {
				e.printStackTrace();
		}
	}

}

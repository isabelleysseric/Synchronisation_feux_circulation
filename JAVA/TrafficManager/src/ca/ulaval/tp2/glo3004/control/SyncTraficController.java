package ca.ulaval.tp2.glo3004.control;

import java.util.concurrent.SynchronousQueue;

import ca.ulaval.tp2.glo3004.car.Car;

public class SyncTraficController {
	
	private CrossIntersectionController crossIntersection;
	private ThreeWayIntersectionController threeWayIntersection;
	private SynchronousQueue<Car> incomingCars = new SynchronousQueue<Car>();
	
	
	public SyncTraficController(CrossIntersectionController crossIntersection, ThreeWayIntersectionController threeWayIntersection, SynchronousQueue<Car> incomingCars) {
		this.crossIntersection = crossIntersection;
		this.threeWayIntersection = threeWayIntersection;
		this.incomingCars =incomingCars;
	}
	
	

}

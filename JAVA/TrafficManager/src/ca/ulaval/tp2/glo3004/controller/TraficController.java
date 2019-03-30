package ca.ulaval.tp2.glo3004.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import ca.ulaval.tp2.glo3004.ExecutionParameters;
import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.intersection.CrossIntersection;
import ca.ulaval.tp2.glo3004.intersection.Intersection;
import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.intersection.ThreeWayIntersection;
import ca.ulaval.tp2.glo3004.road.Direction;
import ca.ulaval.tp2.glo3004.runnable.CarRunnable;
import ca.ulaval.tp2.glo3004.runnable.LightCarRunnable;
import ca.ulaval.tp2.glo3004.runnable.PedestrianRunnable;
import ca.ulaval.tp2.glo3004.view.LightView;
import ca.ulaval.tp2.glo3004.view.StateView;

public class TraficController {

	private final LightView threeWayLightView;
	private final LightView crossLightView;
	private final StateView stateView;
	
	public TraficController(LightView threeWayLightView, LightView crossLightView,
			StateView stateView) {

		this.threeWayLightView = threeWayLightView;
		this.crossLightView = crossLightView;
		this.stateView = stateView;
	}

	public List<Thread> createIntersectionControllerThreads(IntersectionType intersectionType,
			ExecutionParameters parameters) {
		
		List<Thread> threads = new ArrayList<Thread>();

		switch (intersectionType) {
		
		case THREE_WAY:
			Intersection threeWayIntersection = new ThreeWayIntersection();
			threads = createIntersectionThreads(threeWayIntersection, parameters, threeWayLightView);
			break;
		case CROSS:
			Intersection crossIntersection = new CrossIntersection();
			threads = this.createIntersectionThreads(crossIntersection, parameters, crossLightView);
			break;
		case SYNCHRO:
			//initializeSyncIntersectionThreads(threads, parameters);

		default:
			break;
		}
		
		return threads;
	}
	
	private List<Thread> createIntersectionThreads(Intersection intersection, 
			ExecutionParameters parameters, LightView lightView) {
		
		List<Thread> threads = new ArrayList<Thread>();
		
		
		int numberOfCars = parameters.getNumberOfCars();
		int numberOfPedestrians = parameters.getNumberOfPedestrians();
		
		Direction[] directions = intersection.getAllDirections();
		int numberOfDirections = directions.length;
		
		final AllLightController allLightController = new AllLightController(intersection, lightView);
		
	
		CyclicBarrier pedestrianBarrier = createPedestrianBarrier(numberOfPedestrians, numberOfDirections);

		for(Direction direction: directions) {	
			CyclicBarrier carBarrier = this.createCarBarrier(intersection, direction, numberOfCars, allLightController);
			LightController lightController = new LightController(carBarrier, pedestrianBarrier, allLightController);
			
			int lightFrequency = parameters.getLightFrequency(direction);
			Runnable lightRunnable = new LightCarRunnable(direction, lightController, lightFrequency);
			Thread thread = new Thread(lightRunnable);
			
			threads.add(thread);
		}
	
		
		return threads;
	}
	
	private CyclicBarrier createPedestrianBarrier(int numberOfPedestrians, int numberOfDirections) {
		
		PedestrianController pedestrianController = new PedestrianController(stateView, numberOfPedestrians);
		Runnable pedestrianRunnable = new PedestrianRunnable(pedestrianController);
		CyclicBarrier pedestrianBarrier = new CyclicBarrier(numberOfDirections, pedestrianRunnable);
	
		return pedestrianBarrier;
	}
	
	private CyclicBarrier createCarBarrier(Intersection intersection, Direction direction, 
			int numberOfCars, AllLightController allLightController) {
		
		Car car = intersection.getCar(direction);
		Runnable carRunnable = new CarRunnable(car, numberOfCars, stateView, allLightController);
		CyclicBarrier carBarrier = new CyclicBarrier(1, carRunnable);
		
		return carBarrier;
	}
	
}


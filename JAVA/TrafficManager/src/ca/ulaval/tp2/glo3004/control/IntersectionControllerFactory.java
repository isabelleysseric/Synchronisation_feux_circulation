package ca.ulaval.tp2.glo3004.control;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import ca.ulaval.tp2.glo3004.control.runnable.CarRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.LightRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.PedestrianController;
import ca.ulaval.tp2.glo3004.control.runnable.PedestrianRunnable;
import ca.ulaval.tp2.glo3004.intersection.CrossIntersection;
import ca.ulaval.tp2.glo3004.intersection.Intersection;
import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.intersection.ThreeWayIntersection;
import ca.ulaval.tp2.glo3004.light.LightController;
import ca.ulaval.tp2.glo3004.road.Direction;
import ca.ulaval.tp2.glo3004.view.LightView;
import ca.ulaval.tp2.glo3004.view.StateView;

public class IntersectionControllerFactory {

	private static volatile LightController lightController;

	private LightView threeWayLightView;
	private LightView crossLightView;
	private StateView stateView;
	private SyncController syncController;
	
	
	public IntersectionControllerFactory(LightView threeWayLightView, LightView crossLightView,
			StateView stateView) {

		this.threeWayLightView = threeWayLightView;
		this.crossLightView = crossLightView;
		this.stateView = stateView;
	}

	public List<Thread> createIntersectionControllerThreads(IntersectionType intersectionType,
			ExecutionParameters parameters) {
		List<Thread> threads = new ArrayList<Thread>();

		Intersection intersection = null;
		LightView lightView = null;

		switch (intersectionType) {
		
		case THREE_WAY:
			intersection = new ThreeWayIntersection();
			lightView = threeWayLightView;

			break;
		case CROSS:
			intersection = new CrossIntersection();
			lightView = crossLightView;
			break;

		default:
			break;
		}

		lightController = new LightController(intersection, lightView);
		initializeThreads(threads, intersection, parameters, lightView);
		
		return threads;
	}

	private void initializeThreads(List<Thread> threads, Intersection intersection, 
			ExecutionParameters parameters, LightView lightView) {
		
			
		CyclicBarrier pedestrianBarrier = createPedestriansBarrier(intersection, parameters);
		TraficController traficController = new TraficController(intersection, parameters, stateView, lightController, pedestrianBarrier);

		initializeCarThreads(threads, intersection, traficController, false);
		initializeLightsThreads(threads, intersection, traficController, false);
	}

	private CyclicBarrier createPedestriansBarrier(Intersection intersection, ExecutionParameters parameters) {
		int numberOfIntersections = intersection.getAllDirections().length;
		int numberOfPedestrians = parameters.getNumberOfPedestrians();
		
		PedestrianController pedestrianController = new PedestrianController(stateView, numberOfPedestrians);
		PedestrianRunnable pedestriansRunnable = new PedestrianRunnable(pedestrianController);
		
		CyclicBarrier pedestrianBarrier = new CyclicBarrier(numberOfIntersections, pedestriansRunnable);
		
		return pedestrianBarrier;
	
	}
	
	/*private void initializePedestriansThread(List<Thread> threads, IntersectionType intersectionType,
			TraficController traficController, boolean isSynchro) {

	/*	PedestrianRunnable pedestriansRunnable = new PedestrianRunnable(intersectionType, traficController,
				syncController, isSynchro);
		PedestrianRunnable pedestriansRunnable = new PedestrianRunnable(new PedestrianController(lightController, stateView));
		Thread pedestrianThread = new Thread(pedestriansRunnable);
		threads.add(pedestrianThread);
	}*/
	
	private void initializeCarThreads(List<Thread> threads, Intersection intersection,
			TraficController traficController, boolean isSynchro) {

		for (Direction directionType : intersection.getAllDirections()) {

			Runnable carRunnable = new CarRunnable(intersection.getIntersectionType(), directionType, traficController,
					syncController, isSynchro);
			addNewThread(carRunnable, directionType, "CAR", threads);
		}
	}

	private void initializeLightsThreads(List<Thread> threads, Intersection intersection,
			TraficController traficController, boolean isSynchro) {

		for (Direction direction : intersection.getAllDirections()) {

			Runnable lightRunnable = new LightRunnable(direction, traficController);
			addNewThread(lightRunnable, direction, "LIGHT", threads);
		}
	}

	private void addNewThread(Runnable runnable, Direction direction, String type, List<Thread> threads) {
		Thread thread = new Thread(runnable);
		String threadName = String.format("%s:%s::Thread", type, direction);
		thread.setName(threadName);
		threads.add(thread);
	}

	
	
}

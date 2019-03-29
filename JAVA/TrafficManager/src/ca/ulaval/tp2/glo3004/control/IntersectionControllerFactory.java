package ca.ulaval.tp2.glo3004.control;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import ca.ulaval.tp2.glo3004.ExecutionParameters;
import ca.ulaval.tp2.glo3004.control.runnable.CarRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.LightRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.PedestrianRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.sync.LightSyncRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.sync.PedestrianSync;
import ca.ulaval.tp2.glo3004.control.runnable.sync.SynchroIntersection;
import ca.ulaval.tp2.glo3004.intersection.CrossIntersection;
import ca.ulaval.tp2.glo3004.intersection.Intersection;
import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.intersection.ThreeWayIntersection;
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

		switch (intersectionType) {
		
		case THREE_WAY:
			Intersection threeWayIntersection = new ThreeWayIntersection();
			initializeThreads(threads, threeWayIntersection, parameters, threeWayLightView);

			break;
		case CROSS:
			Intersection crossIntersection = new CrossIntersection();
			initializeThreads(threads, crossIntersection, parameters, crossLightView);

			break;
		case SYNCHRO:
			initializeSyncIntersectionThreads(threads, parameters);

		default:
			break;
		}
		
		return threads;
	}

	private void initializeThreads(List<Thread> threads, Intersection intersection, 
			ExecutionParameters parameters, LightView lightView) {
		
		lightController = new LightController(intersection, lightView);
		
			
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
	
	/*SYNCHRO INTERSECTION SETUP */
	private void initializeSyncIntersectionThreads(List<Thread> threads,
			ExecutionParameters parameters) {
		boolean inSynchro = true;
		
		Intersection threeWayIntersection = new ThreeWayIntersection();
		Intersection crossIntersection = new CrossIntersection();
		
		lightController = new LightController();
		
		SynchroIntersection syncIntersection = new SynchroIntersection(threeWayIntersection, crossIntersection);
		SyncController synchroController = new SyncController(parameters, stateView, threeWayLightView
				, crossLightView, 
				lightController, syncIntersection);
	
		this.syncController = synchroController;
		
		initializeCarThreads(threads, threeWayIntersection, null, inSynchro);
		initializeCarThreads(threads, crossIntersection, null, inSynchro);
		initializeSyncLights(threads, syncIntersection);
		initializePedestrianSync(threads);
	}

	private void initializeSyncLights(List<Thread> threads, SynchroIntersection syncIntersection) {
		for (Direction direction : syncIntersection.getAllDirections()) {

			Runnable lightSyncRunnable = new LightSyncRunnable(direction, syncController);
			addNewThread(lightSyncRunnable, direction, "LIGHT-SYNC", threads);
			
		}
	}
	
	private void initializePedestrianSync(List<Thread> threads) {
		Runnable pedestrianSyncRunnable = new PedestrianSync(syncController);
		addNewThread(pedestrianSyncRunnable, null, "PEDESTRIAN-SYNC", threads);
	}

	
}

package ca.ulaval.tp2.glo3004;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

import ca.ulaval.tp2.glo3004.car.CarFactory;
import ca.ulaval.tp2.glo3004.control.CrossIntersectionController;
import ca.ulaval.tp2.glo3004.control.ExecutionParameters;
import ca.ulaval.tp2.glo3004.control.IntersectionType;
import ca.ulaval.tp2.glo3004.control.ThreeWayIntersectionController;
import ca.ulaval.tp2.glo3004.control.TraficController;
import ca.ulaval.tp2.glo3004.control.runnable.CarRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.LightRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.PedestrianRunnable;
import ca.ulaval.tp2.glo3004.light.LightController;

public class IntersectionControllerFactory {

	private LightView threeWayLightComponent;
	private LightView crossLightComponent;

	private ThreeWayIntersectionController threeWayController;
	private CrossIntersectionController crossController;

	private Map<Direction, CyclicBarrier> barriers;
	private CarFactory carFactory = new CarFactory();
	
	private static volatile LightController lightSync = new LightController();
	
	
	public IntersectionControllerFactory(LightView threeWayLightComponent, LightView crossLightComponent
			) {

		initializeSyncIntersectionBarriers();
		this.threeWayLightComponent = threeWayLightComponent;
		this.crossLightComponent = crossLightComponent;
		
	}

	public List<Thread> createIntersectionControllerThreads(IntersectionType intersectionType,
			ExecutionParameters parameters) {
		List<Thread> threads = new ArrayList<Thread>();

		switch (intersectionType) {
		case THREE_WAY:
			initializeIntersectionControllers(false, parameters);
			initializeThreeWayIntersection(threads, parameters, threeWayController);
			break;
		case CROSS:
			initializeIntersectionControllers(false, parameters);
			initializeCrossIntersection(threads, parameters, crossController);
			break;
		case SYNCHRO:
			initializeIntersectionControllers(true, parameters);
			initializeSyncIntersectionWithoutConstraints(threads, parameters);
			break;
		}

		return threads;
	}

	private void initializeIntersectionControllers(boolean intersectionIsSync, ExecutionParameters parameters) {

		threeWayController = new ThreeWayIntersectionController(carFactory, parameters, threeWayLightComponent,
				lightSync, intersectionIsSync);

		crossController = new CrossIntersectionController(carFactory, parameters, crossLightComponent, lightSync,
				intersectionIsSync);

	}

	private void initializeThreeWayIntersection(List<Thread> threads, ExecutionParameters parameters,
			ThreeWayIntersectionController threeWayController) {
		Direction[] directions = new Direction[] { Direction.EAST, Direction.WEST, Direction.SOUTH };

		initializePedestriansThread(threads, threeWayController);
		initializeAllDirectionThreads(directions, threads, threeWayController);

	}

	private void initializeAllDirectionThreads(Direction[] directions, List<Thread> threads,
			TraficController traficController) {

		for (Direction direction : directions) {

			Runnable carRunnable = new CarRunnable(direction, traficController);
			addNewThread(carRunnable, direction, "CAR", threads);

			Runnable greenRunnable = new LightRunnable(direction, traficController);
			addNewThread(greenRunnable, direction, "GREEN", threads);
		}
	}

	private void initializePedestriansThread(List<Thread> threads, TraficController traficController) {
		PedestrianRunnable pedestriansRunnable = new PedestrianRunnable(traficController);
		Thread pedestrianThread = new Thread(pedestriansRunnable);
		threads.add(pedestrianThread);
	}

	private void initializeCrossIntersection(List<Thread> threads, ExecutionParameters parameters,
			CrossIntersectionController crossController) {
		Direction[] directions = new Direction[] { Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.NORTH };

		initializePedestriansThread(threads, crossController);
		initializeAllDirectionThreads(directions, threads, crossController);

	}

	
	private void addNewThread(Runnable runnable, Direction direction, String type, List<Thread> threads) {
		Thread thread = new Thread(runnable);
		String threadName = String.format("%s:%s::Thread", type, direction);
		thread.setName(threadName);
		threads.add(thread);
	}

	public void initializeSyncIntersectionWithoutConstraints(List<Thread> threads, ExecutionParameters parameters) {

		this.initializeThreeWayIntersection(threads, parameters, threeWayController);
		this.initializeCrossIntersection(threads, parameters, crossController);

	}

	private void initializeSyncIntersectionBarriers() {

		this.barriers = new HashMap<>();
		barriers.put(Direction.EAST, new CyclicBarrier(2));
		barriers.put(Direction.WEST, new CyclicBarrier(2));
		barriers.put(Direction.SOUTH, new CyclicBarrier(2));
		barriers.put(Direction.NORTH, new CyclicBarrier(1));

	}

}

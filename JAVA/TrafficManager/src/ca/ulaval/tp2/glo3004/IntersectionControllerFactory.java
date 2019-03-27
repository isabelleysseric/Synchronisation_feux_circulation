package ca.ulaval.tp2.glo3004;

import java.util.ArrayList;
import java.util.List;
import ca.ulaval.tp2.glo3004.car.CarFactory;
import ca.ulaval.tp2.glo3004.control.CrossIntersectionController;
import ca.ulaval.tp2.glo3004.control.ExecutionParameters;
import ca.ulaval.tp2.glo3004.control.IntersectionType;
import ca.ulaval.tp2.glo3004.control.SyncController;
import ca.ulaval.tp2.glo3004.control.ThreeWayIntersectionController;
import ca.ulaval.tp2.glo3004.control.TraficController;
import ca.ulaval.tp2.glo3004.control.runnable.CarRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.LightRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.PedestrianRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.cross.CarCrossRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.cross.LightCrossRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.cross.PedestrianCrossRunnable;
import ca.ulaval.tp2.glo3004.control.runnable.sync.LightSyncRunnable;
import ca.ulaval.tp2.glo3004.light.LightController;

public class IntersectionControllerFactory {

	private LightView threeWayLightComponent;
	private LightView crossLightComponent;

	private ThreeWayIntersectionController threeWayController;
	private CrossIntersectionController crossController;
	private SyncController syncController;

	private CarFactory carFactory = new CarFactory();

	private static volatile LightController lightSync = new LightController();
	private static Direction[] directions = new Direction[] { Direction.EAST, Direction.WEST, Direction.SOUTH,
			Direction.NORTH };

	public IntersectionControllerFactory(LightView threeWayLightComponent, LightView crossLightComponent) {

		this.threeWayLightComponent = threeWayLightComponent;
		this.crossLightComponent = crossLightComponent;

	}

	public List<Thread> createIntersectionControllerThreads(IntersectionType intersectionType,
			ExecutionParameters parameters) {
		List<Thread> threads = new ArrayList<Thread>();

		switch (intersectionType) {
		case THREE_WAY:
			initializeIntersectionControllers(false, parameters);
			initializeThreeWayIntersection(threads, parameters, threeWayController, false);
			break;
		case CROSS:
			initializeIntersectionControllers(false, parameters);
			initializeCrossIntersection(threads, parameters, crossController, false);
			break;
		case SYNCHRO_TEST:
			initializeIntersectionControllers(true, parameters);
			initializeSyncIntersectionWithoutConstraints(threads, parameters);
			break;
		case SYNCHRO:
			initializeIntersectionControllers(true, parameters);
			initializeSyncIntersection2(threads, parameters);
			break;
		}

		return threads;
	}

	private void initializeIntersectionControllers(boolean intersectionIsSync, ExecutionParameters parameters) {

		threeWayController = new ThreeWayIntersectionController(carFactory, parameters, threeWayLightComponent,
				lightSync);

		crossController = new CrossIntersectionController(carFactory, parameters, crossLightComponent, lightSync);

		syncController = new SyncController(carFactory, parameters, threeWayLightComponent, crossLightComponent,
				lightSync);

	}

	private void initializeThreeWayIntersection(List<Thread> threads, ExecutionParameters parameters,
			ThreeWayIntersectionController threeWayController, boolean isSynchro) {

		initializeThreeWayPedestriansThread(threads, threeWayController, isSynchro);
		initializeThreeWayCarThreads(threads, threeWayController, isSynchro);
		if (!isSynchro)
			initializeThreeWayLightsThreads(threads, threeWayController, isSynchro);

	}

	private void initializeThreeWayCarThreads(List<Thread> threads, TraficController traficController,
			boolean isSynchro) {

		for (Direction direction : directions) {

			Runnable carRunnable = new CarRunnable(IntersectionType.THREE_WAY, direction, traficController,
					syncController, isSynchro);
			addNewThread(carRunnable, direction, "CAR", threads);
		}
	}

	private void initializeThreeWayLightsThreads(List<Thread> threads, TraficController traficController,
			boolean isSynchro) {

		for (Direction direction : directions) {

			Runnable lightRunnable = new LightRunnable(direction, traficController);
			addNewThread(lightRunnable, direction, "LIGHT", threads);
		}
	}

	private void initializeThreeWayPedestriansThread(List<Thread> threads, TraficController traficController,
			boolean isSynchro) {

		PedestrianRunnable pedestriansRunnable = new PedestrianRunnable(IntersectionType.THREE_WAY, traficController,
				syncController, isSynchro);
		Thread pedestrianThread = new Thread(pedestriansRunnable);
		threads.add(pedestrianThread);
	}

	private void initializeCrossIntersection(List<Thread> threads, ExecutionParameters parameters,
			CrossIntersectionController crossController, boolean isSynchro) {

		// Direction[] directions = new Direction[] { Direction.EAST, Direction.WEST,
		// Direction.SOUTH, Direction.NORTH };

		initializeCrossPedestriansThread(threads, crossController, isSynchro);
		initializeCrossCarThreads(threads, crossController, isSynchro);
		if (!isSynchro)
			initializeCrossLightsThreads(threads, crossController, isSynchro);

	}

	private void initializeCrossPedestriansThread(List<Thread> threads, TraficController traficController,
			boolean isSynchro) {

		PedestrianCrossRunnable pedestriansRunnable = new PedestrianCrossRunnable(IntersectionType.CROSS,
				traficController, syncController, isSynchro);
		Thread pedestrianThread = new Thread(pedestriansRunnable);
		threads.add(pedestrianThread);
	}

	private void initializeCrossCarThreads(List<Thread> threads, TraficController traficController, boolean isSynchro) {

		for (Direction direction : directions) {

			Runnable carRunnable = new CarCrossRunnable(IntersectionType.CROSS, direction, traficController,
					syncController, isSynchro);
			addNewThread(carRunnable, direction, "CAR", threads);

		}
	}

	private void initializeCrossLightsThreads(List<Thread> threads, TraficController traficController,
			boolean isSynchro) {

		for (Direction direction : directions) {

			Runnable lightRunnable = new LightCrossRunnable(direction, traficController);
			addNewThread(lightRunnable, direction, "LIGHT", threads);
		}
	}

	private void addNewThread(Runnable runnable, Direction direction, String type, List<Thread> threads) {
		Thread thread = new Thread(runnable);
		String threadName = String.format("%s:%s::Thread", type, direction);
		thread.setName(threadName);
		threads.add(thread);
	}

	public void initializeSyncIntersectionWithoutConstraints(List<Thread> threads, ExecutionParameters parameters) {

		this.initializeThreeWayIntersection(threads, parameters, threeWayController, true);
		this.initializeCrossIntersection(threads, parameters, crossController, true);

	}

	public void initializeSyncIntersection2(List<Thread> threads, ExecutionParameters parameters) {

		this.initializeThreeWayIntersection(threads, parameters, threeWayController, true);
		this.initializeCrossIntersection(threads, parameters, crossController, true);
		initializeSyncLightsThreads(threads, threeWayController);
	}

	private void initializeSyncLightsThreads(List<Thread> threads, TraficController traficController) {

		Direction[] directions = new Direction[] { Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.NORTH };

		for (Direction direction : directions) {

			Runnable lightRunnable = new LightSyncRunnable(direction, syncController);
			addNewThread(lightRunnable, direction, "LIGHT", threads);
		}
	}

}

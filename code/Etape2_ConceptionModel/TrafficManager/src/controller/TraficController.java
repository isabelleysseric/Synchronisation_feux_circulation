package ca.ulaval.tp2.glo3004.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

import ca.ulaval.tp2.glo3004.ExecutionParameters;
import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.controller.sync.AllLightSyncController;
import ca.ulaval.tp2.glo3004.controller.sync.LightSyncController;
import ca.ulaval.tp2.glo3004.intersection.CrossIntersection;
import ca.ulaval.tp2.glo3004.intersection.Intersection;
import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.intersection.ThreeWayIntersection;
import ca.ulaval.tp2.glo3004.road.Direction;
import ca.ulaval.tp2.glo3004.runnable.CarRunnable;
import ca.ulaval.tp2.glo3004.runnable.LightRunnable;
import ca.ulaval.tp2.glo3004.runnable.PedestrianRunnable;
import ca.ulaval.tp2.glo3004.runnable.sync.LightSyncRunnable;
import ca.ulaval.tp2.glo3004.view.LightView;
import ca.ulaval.tp2.glo3004.view.StateView;

public class TraficController {

	private final LightView threeWayLightView;
	private final LightView crossLightView;
	private final StateView stateView;
	private final BlockingQueue<Car> crossIncomingCars = new LinkedBlockingQueue<>();
	private final BlockingQueue<Car> threeWayIncomingCars = new LinkedBlockingQueue<>();
	private final Intersection threeWayIntersection;
	private final Intersection crossIntersection;

	public TraficController(LightView threeWayLightView, LightView crossLightView, StateView stateView) {

		this.threeWayLightView = threeWayLightView;
		this.crossLightView = crossLightView;
		this.stateView = stateView;
		this.threeWayIntersection = new ThreeWayIntersection();
		this.crossIntersection = new CrossIntersection();
	}

	public List<Thread> createIntersectionControllerThreads(IntersectionType intersectionType,
			ExecutionParameters parameters) {

		List<Thread> threads = new ArrayList<Thread>();

		switch (intersectionType) {

		case THREE_WAY:
			threads = createIntersectionThreads(threeWayIntersection, parameters, threeWayLightView);
			break;
		case CROSS:
			threads = this.createIntersectionThreads(crossIntersection, parameters, crossLightView);
			break;
		case SYNCHRO:
			threads = this.createSyncIntersectionThreads(parameters);

		default:
			break;
		}

		return threads;
	}

	private List<Thread> createIntersectionThreads(Intersection intersection, ExecutionParameters parameters,
			LightView lightView) {

		List<Thread> threads = new ArrayList<Thread>();

		int numberOfCars = parameters.getNumberOfCars();
		int numberOfPedestrians = parameters.getNumberOfPedestrians();

		Direction[] directions = intersection.getAllDirections();
		int numberOfDirections = directions.length;

		final AllLightController allLightController = new AllLightController(intersection, lightView);
		IntersectionType intersectionType = intersection.getIntersectionType();

		CyclicBarrier pedestrianBarrier = createPedestrianBarrier(numberOfPedestrians, numberOfDirections,
				intersectionType);

		for (Direction direction : directions) {
			CyclicBarrier carBarrier = this.createCarBarrier(intersection, direction, numberOfCars, allLightController);
			LightController lightController = new LightController(carBarrier, pedestrianBarrier, allLightController);

			int lightFrequency = parameters.getLightFrequency(direction);
			Runnable lightRunnable = new LightRunnable(direction, lightController, lightFrequency);
			Thread thread = new Thread(lightRunnable);

			threads.add(thread);
		}

		return threads;
	}

	private CyclicBarrier createPedestrianBarrier(int numberOfPedestrians, int numberOfDirections,
			IntersectionType intersectionType) {

		PedestrianController pedestrianController = new PedestrianController(stateView, numberOfPedestrians,
				intersectionType);
		Runnable pedestrianRunnable = new PedestrianRunnable(pedestrianController);
		CyclicBarrier pedestrianBarrier = new CyclicBarrier(numberOfDirections, pedestrianRunnable);

		return pedestrianBarrier;
	}

	private CyclicBarrier createCarBarrier(Intersection intersection, Direction direction, int numberOfCars,
			AllLightController allLightController) {

		Car car = intersection.getCar(direction);
		Runnable carRunnable = new CarRunnable(car, numberOfCars, stateView, allLightController);
		CyclicBarrier carBarrier = new CyclicBarrier(1, carRunnable);

		return carBarrier;
	}

	private List<Thread> createSyncIntersectionThreads(ExecutionParameters parameters) {

		List<Thread> threads = new ArrayList<Thread>();

		int numberOfCars = parameters.getNumberOfCars();
		int numberOfPedestrians = parameters.getNumberOfPedestrians();

		final AllLightSyncController allLightSyncController = new AllLightSyncController(crossIntersection,
				threeWayLightView, crossLightView);

		int threeWayNumberOfDirection = threeWayIntersection.getNumberOfDirections();
		int crossNumberOfDirection = crossIntersection.getNumberOfDirections();
		IntersectionType threeWay = threeWayIntersection.getIntersectionType();
		IntersectionType cross = crossIntersection.getIntersectionType();

		CyclicBarrier threeWayPedestrianBarrier = createPedestrianBarrier(numberOfPedestrians,
				threeWayNumberOfDirection, threeWay);
		CyclicBarrier crossPedestrianBarrier = createPedestrianBarrier(numberOfPedestrians, crossNumberOfDirection,
				cross);

		final Map<Direction, CyclicBarrier> lightBarriers = this.getCyclicLightBarriers(allLightSyncController);

		/* CROSS SYNC */
		for (Direction direction : crossIntersection.getAllDirections()) {

			CyclicBarrier lightBarrier = lightBarriers.get(direction);
			LightSyncController lightSyncController = new LightSyncController(numberOfCars, lightBarrier,
					crossPedestrianBarrier, crossLightView, allLightSyncController, crossIntersection, stateView,
					crossIncomingCars, threeWayIncomingCars);

			int lightFrequency = parameters.getLightFrequency(direction);
			Runnable lightRunnable = new LightSyncRunnable(direction, lightSyncController, lightFrequency);
			Thread thread = new Thread(lightRunnable);

			threads.add(thread);

		}

		/* THREE-WAY SYNC */
		for (Direction direction : threeWayIntersection.getAllDirections()) {

			CyclicBarrier lightBarrier = lightBarriers.get(direction);
			LightSyncController lightSyncController = new LightSyncController(numberOfCars, lightBarrier,
					threeWayPedestrianBarrier, threeWayLightView, allLightSyncController, threeWayIntersection,
					stateView, crossIncomingCars, threeWayIncomingCars);

			int lightFrequency = parameters.getLightFrequency(direction);
			Runnable lightRunnable = new LightSyncRunnable(direction, lightSyncController, lightFrequency);
			Thread thread = new Thread(lightRunnable);

			threads.add(thread);

		}

		return threads;
	}

	
	private Map<Direction, CyclicBarrier> getCyclicLightBarriers(AllLightSyncController allLightSyncController) {
		Map<Direction, CyclicBarrier> barriers = new HashMap<Direction, CyclicBarrier>();

		barriers.put(Direction.EAST, new CyclicBarrier(2));
		barriers.put(Direction.WEST, new CyclicBarrier(2));
		barriers.put(Direction.SOUTH, new CyclicBarrier(2));
		barriers.put(Direction.NORTH, new CyclicBarrier(1));

		return barriers;
	}

}

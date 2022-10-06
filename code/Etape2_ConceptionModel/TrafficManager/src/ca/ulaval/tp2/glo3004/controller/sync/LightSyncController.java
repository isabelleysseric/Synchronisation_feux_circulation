package ca.ulaval.tp2.glo3004.controller.sync;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.car.CarFactory;
import ca.ulaval.tp2.glo3004.intersection.Intersection;
import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.light.LightColor;
import ca.ulaval.tp2.glo3004.road.Direction;
import ca.ulaval.tp2.glo3004.view.LightView;
import ca.ulaval.tp2.glo3004.view.StateView;

public class LightSyncController {

	
	private static boolean IS_SYNCHRO = true;
	
	private CyclicBarrier lightBarrier;
	private CyclicBarrier pedestrianBarrier;
	
	private final Intersection intersection;
	private final LightView lightView;
	private final StateView stateView;
	private final BlockingQueue<Car> crossIncomingCars;
	private final BlockingQueue<Car> threeWayIncomingCars;
	private final int numberOfCars;
	

	
	private final AllLightSyncController allLightSyncController;
	

	public LightSyncController(int numberOfCars,
			CyclicBarrier lightBarrier, 
			CyclicBarrier pedestrianBarrier,
			LightView lightView,
			AllLightSyncController allLightSyncController, 
			Intersection intersection, 
			StateView stateView, 
			BlockingQueue<Car> crossIncomingCars,
			BlockingQueue<Car> threeWayIncomingCars) {
		
		this.lightBarrier = lightBarrier;
		this.lightView = lightView;
		this.numberOfCars = numberOfCars;	
		this.pedestrianBarrier = pedestrianBarrier;
		this.stateView = stateView;
		this.threeWayIncomingCars = threeWayIncomingCars;
		this.crossIncomingCars = crossIncomingCars;
		
	this.allLightSyncController = allLightSyncController;
	this.intersection = intersection;
	}

	public synchronized void switchLightState(Direction direction) {
	
		boolean lightIsGreen = this.allLightSyncController.lightIsGreen(direction);
		
		if (lightIsGreen) {
			switchToRed(direction);
			
		} else {
			
			switchToGreen(direction);
		}
		
	}


	private void switchToRed(Direction direction) {
		
		try {
			lightBarrier.await();
			IntersectionType intersectionType = intersection.getIntersectionType();
			allLightSyncController.switchLight(intersectionType, direction, LightColor.RED, lightView);
			
			pedestrianBarrier.await();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}

	private void switchToGreen(Direction direction) {
	
		try {
			lightBarrier.await();
			IntersectionType intersectionType = intersection.getIntersectionType();
			
			allLightSyncController.switchLight(intersectionType, direction, LightColor.GREEN, lightView);
			
			carMovementWithSynchronization(direction);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		} catch (BrokenBarrierException e) {
		
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	

	public void carMovementWithSynchronization(Direction direction) throws Exception {		
	
		IntersectionType intersectionType = this.intersection.getIntersectionType();
		
		if (isEastCross(direction, intersectionType) || isWestThreeWay(direction, intersectionType)) {
			getCarFromPreviousIntersection(direction);
		} else {
			CarFactory carFactory = new CarFactory();
			Car car = carFactory.createCar(direction, intersectionType);
			sendCarsToNextIntersection(car);
		}

	}

	private boolean isEastCross(Direction direction, IntersectionType intersectionType) {
	
	
		return direction.equals(Direction.EAST) && intersectionType.equals(IntersectionType.CROSS);
	}

	private boolean isWestThreeWay(Direction direction, IntersectionType intersectionType) {
		return direction.equals(Direction.WEST) && intersectionType.equals(IntersectionType.THREE_WAY);
	}

	
	/*
	 * PRODUCTEURS=> 3-WAY: EST-CONTINUE, SUD:GAUCHE PRODUCTEURS => CROSS:
	 * NORD-GAUCHE, OUEST-CONTINUE, SUD:DROITE
	 * Ajoute des voitures à une voie entrante (EST, OUEST)
	 */
	private void sendCarsToNextIntersection(Car car) throws Exception {
		moveInCorrectDirection(car, car.getDirection());

		IntersectionType intersectionType = intersection.getIntersectionType();

		if (car.canMoveToNextIntersection()) {
			car.setPreviousAction();

			if (intersectionType.equals(IntersectionType.THREE_WAY)) {
				
				this.crossIncomingCars.put(car);
				car.setNextOrientation(IntersectionType.CROSS, Direction.EAST);

			} else if (intersectionType.equals(IntersectionType.CROSS)) {
				
				this.threeWayIncomingCars.put(car);
				car.setNextOrientation(IntersectionType.THREE_WAY, Direction.WEST);

			} 
		}
		
	}

	
	private void moveInCorrectDirection(Car car, Direction direction) throws Exception {

		for (int i = 0; i < numberOfCars; i++) {

			if (this.allLightSyncController.oppositeSideIsGreen(direction)) {
				car.randomMoveWithPriority();
				stateView.displayCarState(car, IS_SYNCHRO);
				
			} else {
				car.randomMoveWithOppositeSideOn();
				stateView.displayCarState(car, IS_SYNCHRO);
			}
			
		}

	}

	/*
	 * CONSOMMATEURS=> 3-WAY: OUEST CONSOMMATEURS => CROSS: EST
	 */
	private void getCarFromPreviousIntersection(Direction direction) throws Exception {

		IntersectionType intersectionType = intersection.getIntersectionType();

		Car car = null;
		if (intersectionType.equals(IntersectionType.THREE_WAY) && !this.crossIncomingCars.isEmpty()) {
			 car = this.crossIncomingCars.take();
			
			 moveInCorrectDirection(car, car.getNextDirection());

		} else if (intersectionType.equals(IntersectionType.CROSS) && !this.threeWayIncomingCars.isEmpty()) {
			 car = this.threeWayIncomingCars.take();
			
			 moveInCorrectDirection(car, car.getNextDirection());
		} 
	
	}
}

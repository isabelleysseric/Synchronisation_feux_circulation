package ca.ulaval.tp2.glo3004.runnable;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.controller.AllLightController;
import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.road.Direction;
import ca.ulaval.tp2.glo3004.view.StateView;

public class CarSyncRunnable implements Runnable {

	private final AllLightController allLightController;
	private final StateView stateView;
	private Car car;
	private final int numberOfCars;

	private BlockingQueue<Car> crossIncomingCars = new LinkedBlockingQueue<>(1);
	private BlockingQueue<Car> threeWayIncomingCars = new LinkedBlockingQueue<>(1);


	public CarSyncRunnable(Car car, int numberOfCars, StateView stateView, AllLightController allLightController) {

		this.car = car;
		this.numberOfCars = numberOfCars;
		this.allLightController = allLightController;
		this.stateView = stateView;
	
	}

	public void run() {

		try {
			carMovementWithSynchronization();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	// Methode qui synchronise la circulation des voitures aux intersections
	public void carMovementWithSynchronization() throws Exception {

		if (isEastCross() || isWestThreeWay()) {
			getCarFromPreviousIntersection();
		} else {
			sendCarsToNextIntersection();
		}

	}

	private boolean isEastCross() {
		Direction direction = car.getDirection();
		IntersectionType intersectionType = car.getIntersectionType();
		return direction.equals(Direction.EAST) && intersectionType.equals(IntersectionType.CROSS);
	}

	private boolean isWestThreeWay() {
		Direction direction = car.getDirection();
		IntersectionType intersectionType = car.getIntersectionType();
		return direction.equals(Direction.WEST) && intersectionType.equals(IntersectionType.THREE_WAY);
	}

	private void moveInCorrectDirection() throws Exception {

		Direction direction = this.car.getDirection();

		for (int i = 0; i < numberOfCars; i++) {

			if (this.allLightController.oppositeSideIsGreen(direction)) {
				car.randomMoveWithPriority();
			} else {
				car.randomMoveWithOppositeSideOn();
			}
			stateView.displayCarState(car, true);
		}

	}

	/*
	 * PRODUCTEURS=> 3-WAY: EST-CONTINUE, SUD:GAUCHE PRODUCTEURS => CROSS:
	 * NORD-GAUCHE, OUEST-CONTINUE, SUD:DROITE
	 */
	private void sendCarsToNextIntersection() throws Exception {
		moveInCorrectDirection();

		IntersectionType intersectionType = car.getIntersectionType();

		if (car.canMoveToNextIntersection()) {
			car.setPreviousAction();

			if (intersectionType.equals(IntersectionType.THREE_WAY)) {
				car.setNextIntersectionType(IntersectionType.CROSS);
				this.crossIncomingCars.put(car);

			} else if (intersectionType.equals(IntersectionType.CROSS)) {
				car.setNextIntersectionType(IntersectionType.THREE_WAY);
				this.threeWayIncomingCars.put(car);

			} else {
				throw new Exception("Only 2 intersections available...");
			}
		}

	}

	/*
	 * CONSOMMATEURS=> 3-WAY: OUEST CONSOMMATEURS => CROSS: EST
	 */
	private void getCarFromPreviousIntersection() throws Exception {

		IntersectionType intersectionType = car.getIntersectionType();

		if (intersectionType.equals(IntersectionType.THREE_WAY)) {

			car = this.crossIncomingCars.take();

		} else if (intersectionType.equals(IntersectionType.CROSS)) {

			car = this.threeWayIncomingCars.take();
		} else {
			throw new Exception("Only 2 intersections available...");
		}

		moveInCorrectDirection();
	}
}

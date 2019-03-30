package ca.ulaval.tp2.glo3004.runnable;

import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.car.InvalidCarActionException;
import ca.ulaval.tp2.glo3004.controller.AllLightController;
import ca.ulaval.tp2.glo3004.road.Direction;
import ca.ulaval.tp2.glo3004.view.StateView;

public class CarRunnable implements Runnable {

	private final AllLightController allLightController;
	private final StateView stateView;
	private final Car car;
	private final int numberOfCars;

	public CarRunnable(Car car, int numberOfCars, StateView stateView, AllLightController allLightController) {

		this.car = car;
		this.numberOfCars = numberOfCars;
		this.allLightController = allLightController;
		this.stateView = stateView;

	}

	public void run() {

		try {
			carMovementWithSingleIntersection();

		} catch (InterruptedException exception) {
			return;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void carMovementWithSingleIntersection() throws InvalidCarActionException, Exception {

		Direction direction = car.getDirection();
		for (int i = 0; i < numberOfCars; i++) {

			if (allLightController.oppositeSideIsGreen(direction)) {
				car.randomMoveWithOppositeSideOn();

			} else {
				car.randomMoveWithPriority();
			}

			stateView.displayCarState(car);
		}
	}

}

package ca.ulaval.tp2.glo3004;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.car.EastCar;
import ca.ulaval.tp2.glo3004.car.InvalidCarActionException;
import ca.ulaval.tp2.glo3004.light.Light;

public class Circulation {

	private Light light;
	private List<Car> cars;
	private Direction direction;

	public Circulation(Direction direction, Light light, int numberMaxOfCars, boolean isForCrossIntersection) {
		this.direction = direction;
		this.light = light;
		initializeCars(numberMaxOfCars, isForCrossIntersection);
	}

	private void initializeCars(int numberMaxOfCars, boolean isForCrossIntersection) {
		cars = new ArrayList<Car>();

		for (int i = 0; i < numberMaxOfCars; i++) {
			Car car = new EastCar(isForCrossIntersection);
			cars.add(car);
		}
	}

	public Direction getDirection() {
		return direction;
	}
	
	public void run() {

		if(this.light.isGreen()) {
			for (Car car : cars) {
				try {
					car.randomMove();
				} catch (InvalidCarActionException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}

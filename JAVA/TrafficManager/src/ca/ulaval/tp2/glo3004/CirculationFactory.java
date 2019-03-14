package ca.ulaval.tp2.glo3004;

import ca.ulaval.tp2.glo3004.light.Light;

public class CirculationFactory {

	private static final int EAST_NUMBER_OF_CARS = 3;
	private static final int WEST_NUMBER_OF_CARS = 4;
	private static final int SOUTH_NUMBER_OF_CARS = 2;

	public Circulation createCirculation(Direction direction, Light light, boolean isForCrossIntersection) {

		int numberMaxOfCars = getNumberMaxOfCars(direction);
		Circulation circulation = new Circulation(direction, light, numberMaxOfCars, isForCrossIntersection);

		return circulation;
	}

	private int getNumberMaxOfCars(Direction direction) {

		switch (direction) {
		case EAST:
			return EAST_NUMBER_OF_CARS;
		case WEST:
			return WEST_NUMBER_OF_CARS;
		case SOUTH:
			return SOUTH_NUMBER_OF_CARS;

		default:
			return 0;
		}
	}
}

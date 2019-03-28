package ca.ulaval.tp2.glo3004.car;

import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.road.Direction;

/**
 * Classe permettant la cr�ation de voitures
 */
public class CarFactory {

	
	public Car createCar(Direction direction, 
			IntersectionType intersectionType) {
		Car car = null;
		
		switch(direction) {
		case EAST:
			car = new EastCar(intersectionType);
			break;
		case WEST:
			car = new WestCar(intersectionType);
			break;
		case SOUTH:
			car = new SouthCar(intersectionType);
			break;
		case NORTH:
			car = new NorthCar(intersectionType);
			break;
		}
		
		return car;
	}
	
}

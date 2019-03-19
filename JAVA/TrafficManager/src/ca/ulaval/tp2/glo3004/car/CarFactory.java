package ca.ulaval.tp2.glo3004.car;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.car.implementation.EastCar;
import ca.ulaval.tp2.glo3004.car.implementation.NorthCar;
import ca.ulaval.tp2.glo3004.car.implementation.SouthCar;
import ca.ulaval.tp2.glo3004.car.implementation.WestCar;
import ca.ulaval.tp2.glo3004.control.IntersectionType;

public class CarFactory {

	public Car createCar(Direction direction, IntersectionType intersectionType) throws Exception {
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

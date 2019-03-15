package ca.ulaval.tp2.glo3004.car;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.car.implementation.EastCar;
import ca.ulaval.tp2.glo3004.car.implementation.NorthCar;
import ca.ulaval.tp2.glo3004.car.implementation.SouthCar;
import ca.ulaval.tp2.glo3004.car.implementation.WestCar;

public class CarFactory {

	public Car createCar(Direction direction, Action[] allowedActions) throws Exception {
		Car car = null;
		
		switch(direction) {
		case EAST:
			car = new EastCar(allowedActions);
			break;
		case WEST:
			car = new WestCar(allowedActions);
			break;
		case SOUTH:
			car = new SouthCar(allowedActions);
			break;
		case NORTH:
			car = new NorthCar(allowedActions);
			break;
		}
		
		return car;
	}
	

}

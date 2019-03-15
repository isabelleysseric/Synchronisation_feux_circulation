package ca.ulaval.tp2.glo3004.intersection;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.car.Action;
import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.car.CarFactory;
import ca.ulaval.tp2.glo3004.car.InvalidCarActionException;

public abstract class Intersection {

	private CarFactory carFactory;
	
	public Intersection(boolean isCrossIntersection) {
		this.carFactory = new CarFactory();
	}

	public void carMove(Direction direction) throws Exception {
		Action[] allowedActions = getAllowedActions(direction);
		Car car = carFactory.createCar(direction, allowedActions);
		
		try {
			car.randomMove();
		} catch (InvalidCarActionException e) {
			e.printStackTrace();
		}
		
	}
	
	public abstract Direction[] getAllowedDirections();
	
	public abstract Action[] getAllowedActions(Direction direction);
	
}

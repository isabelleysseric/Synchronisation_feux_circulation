package ca.ulaval.tp2.glo3004.car.implementation;


import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.car.Action;
import ca.ulaval.tp2.glo3004.car.Car;

public class EastCar extends Car {

	public EastCar(Action[] allowedActions){
		super(Direction.EAST, allowedActions);
	}

	
}

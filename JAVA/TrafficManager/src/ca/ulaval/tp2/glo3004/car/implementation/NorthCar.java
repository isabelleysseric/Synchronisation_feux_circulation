package ca.ulaval.tp2.glo3004.car.implementation;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.car.Action;
import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.car.Movement;
import ca.ulaval.tp2.glo3004.control.IntersectionType;

public class NorthCar extends Car{

	public NorthCar(IntersectionType intersectionType){
		super(Direction.NORTH, intersectionType);
	}

	public Movement getThreeWayIntersectionMovement() {
		Action[] actionsWhenPriority = new Action[]{};
		Action[] actionsWhenOppositeSideOn = new Action[] {};
		
		return new Movement(actionsWhenPriority, actionsWhenOppositeSideOn);
	}
	
	public Movement getCrossIntersectionMovement() {
		Action[]actionsWhenPriority = new Action[]{Action.TURN_LEFT, Action.TURN_RIGHT, Action.CONTINUE};
		Action[] actionsWhenOppositeSideOn = new Action[] {};
		
		return new Movement(actionsWhenPriority, actionsWhenOppositeSideOn);
	}
}

package ca.ulaval.tp2.glo3004.intersection;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.car.Action;

public class ThreeWayIntersection extends Intersection {

	public ThreeWayIntersection() {
		super(false);
	}

	@Override
	public Direction[] getAllowedDirections() {
		//return new Direction[] {Direction.WEST, Direction.EAST, Direction.SOUTH};
		return new Direction[] {Direction.EAST};
	}
	
	public Action[] getAllowedActions(Direction direction) {
		Action[] actions = new Action[] {};
		
		switch(direction) {
		case EAST:
			actions = new Action[]{Action.TURN_LEFT, Action.CONTINUE};
			break;
		case WEST:
			actions = new Action[]{Action.CONTINUE, Action.TURN_RIGHT};
			break;
		case SOUTH:
			actions = new Action[]{Action.TURN_LEFT, Action.TURN_RIGHT};
			break;
		default:
			break;
		}
		
		return actions;
	}
}

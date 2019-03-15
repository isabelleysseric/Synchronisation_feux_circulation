package ca.ulaval.tp2.glo3004.intersection;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.car.Action;

public class CrossIntersection extends Intersection{

	public CrossIntersection() {
		super(true);
	}

	@Override
	public Direction[] getAllowedDirections() {
		return new Direction[] {Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH};
	}

	public Action[] getAllowedActions(Direction direction) {
		Action[] actions = new Action[] {};
		
		switch(direction) {
		case EAST:
			actions =  new Action[]{Action.TURN_LEFT, Action.CONTINUE, Action.TURN_RIGHT};
			break;
		case WEST:
			actions = new Action[]{Action.CONTINUE, Action.TURN_RIGHT, Action.TURN_LEFT};
			break;
		case SOUTH:
			actions = new Action[]{Action.TURN_LEFT, Action.TURN_RIGHT, Action.CONTINUE};
			break;
		case NORTH:
			actions = new Action[]{Action.TURN_LEFT, Action.TURN_RIGHT, Action.CONTINUE};
			break;
		}
		
		return actions;
	}
}

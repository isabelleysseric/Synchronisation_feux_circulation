package ca.ulaval.tp2.glo3004.intersection;

import ca.ulaval.tp2.glo3004.road.Direction;

public class ThreeWayIntersection extends Intersection {

	private static Direction[] DIRECTION_TYPES = new Direction[] {
			Direction.EAST, Direction.WEST, 
			 Direction.SOUTH
	};

	
	public ThreeWayIntersection() {
		super(IntersectionType.THREE_WAY);
		
	}
	
	@Override
	public Direction[] getAllDirections() {
		return DIRECTION_TYPES;
	}
	
}

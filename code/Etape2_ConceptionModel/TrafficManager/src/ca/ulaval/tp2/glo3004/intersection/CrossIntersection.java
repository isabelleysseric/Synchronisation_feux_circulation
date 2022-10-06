package ca.ulaval.tp2.glo3004.intersection;

import ca.ulaval.tp2.glo3004.road.Direction;

public class CrossIntersection extends Intersection{

	
	private static Direction[] DIRECTION_TYPES = new Direction[] {
			Direction.EAST, Direction.WEST, 
			 Direction.SOUTH, Direction.NORTH
	};

	
	public CrossIntersection() {
		super(IntersectionType.CROSS);
		
	}
	
	@Override
	public Direction[] getAllDirections() {
		return DIRECTION_TYPES;
	}
	
	
	
	
}

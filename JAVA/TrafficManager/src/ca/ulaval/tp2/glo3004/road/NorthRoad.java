package ca.ulaval.tp2.glo3004.road;

import ca.ulaval.tp2.glo3004.intersection.IntersectionType;

public class NorthRoad extends Road{

	public NorthRoad(IntersectionType intersectionType) {
		super(intersectionType, Direction.NORTH);
	}
	 
	@Override
	public Direction[] getAllNeighboors() {
		switch(intersectionType) {
		
		case THREE_WAY:
			return new Direction[] {};
			
		case CROSS:
			return new Direction[] {Direction.EAST, Direction.WEST};
			
		default:
			return null;
		}
	}

	@Override
	public Direction getOppositeDirection() {
		return Direction.SOUTH;
	}

}

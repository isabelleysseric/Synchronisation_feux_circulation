package ca.ulaval.tp2.glo3004.road;

import ca.ulaval.tp2.glo3004.intersection.IntersectionType;

public class SouthRoad extends Road{

	public SouthRoad(IntersectionType intersectionType) {
		super(intersectionType, Direction.SOUTH);
	}
	 
	@Override
	public Direction[] getAllNeighboors() {
		return new Direction[]{Direction.EAST, Direction.WEST};
	}

	@Override
	public Direction getOppositeDirection() {
		if(intersectionType.equals(IntersectionType.CROSS)) {
			return Direction.SOUTH;
		}
		else {
			return null;
		}
		
	}

}

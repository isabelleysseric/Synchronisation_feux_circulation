 package ca.ulaval.tp2.glo3004.road;

import ca.ulaval.tp2.glo3004.car.CarFactory;
import ca.ulaval.tp2.glo3004.intersection.IntersectionType;

public class EastRoad extends Road{

	public EastRoad(IntersectionType intersectionType) {
		super(intersectionType, Direction.EAST);
	}
	 
	@Override
	public Direction[] getAllNeighboors() {
		switch(intersectionType) {
		
		case THREE_WAY:
			return new Direction[] {Direction.SOUTH};
			
		case CROSS:
			return new Direction[] {Direction.NORTH, Direction.SOUTH};
			
		default:
			return null;
		}
	}

	@Override
	public Direction getOppositeDirection() {
		return Direction.WEST;
	}

	
	
}

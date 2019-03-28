package ca.ulaval.tp2.glo3004.road;

import ca.ulaval.tp2.glo3004.intersection.IntersectionType;

public class RoadFactory {

	public Road createRoad(IntersectionType intersectionType, 
			Direction direction) {

		switch (direction) {
		case EAST:
			return new EastRoad(intersectionType);
		case WEST:
			return new WestRoad(intersectionType);
		case SOUTH:
			return new SouthRoad(intersectionType);
		case NORTH:
			return new NorthRoad(intersectionType);
		default:
			return null;
		}

	}
}

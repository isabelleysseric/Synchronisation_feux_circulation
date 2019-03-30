package ca.ulaval.tp2.glo3004.intersection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.road.Road;
import ca.ulaval.tp2.glo3004.road.RoadFactory;
import ca.ulaval.tp2.glo3004.road.Direction;

public abstract class Intersection {

	private RoadFactory directionFactory;
	private IntersectionType intersectionType;
	private Map<Direction, Road> roads = new HashMap<>();

	public Intersection(IntersectionType intersectionType) {
		
		this.directionFactory = new RoadFactory();;
		this.intersectionType = intersectionType;
		initializeDirections(intersectionType);

	}

	private void initializeDirections(IntersectionType intersectionType) {

		for (Direction direction : this.getAllDirections()) {
			Road road = this.directionFactory.createRoad(intersectionType, direction);
			this.roads.put(direction, road);
		}
		
	}

	public abstract Direction[] getAllDirections();

	public List<Direction> getAllNeighboors(Direction direction) {
		
		return Arrays.asList(this.roads.get(direction).getAllNeighboors());
		
	}
	
	public Road getRoad(Direction direction) {
		
		return this.roads.get(direction);
	}
	
	public IntersectionType getIntersectionType() {
		
		return this.intersectionType;
	}

	public Direction getOppositeDirection(Direction direction) {
	System.out.println("Direction: "+direction);
		return this.roads.get(direction).getOppositeDirection();
	}
	
	public Car getCar(Direction directionType) {
		
		return this.roads.get(directionType).getCar();
	}
	
	
}

package ca.ulaval.tp2.glo3004.intersection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.road.Direction;

public class SynchroIntersection {

	private Map<IntersectionType, Intersection> intersections = new HashMap<>();
	
	public SynchroIntersection(Intersection threeWayIntersection, Intersection crossIntersection) {
		this.intersections.put(IntersectionType.THREE_WAY, threeWayIntersection);
		this.intersections.put(IntersectionType.CROSS, crossIntersection);
		
	}
	
	public Intersection getIntersection(IntersectionType intersectionType) {
		return this.intersections.get(intersectionType);
	}
	
	public List<Direction> getNeighboors(Direction direction){
		Intersection crossIntersection = this.intersections.get(IntersectionType.CROSS);
		
		return crossIntersection.getAllNeighboors(direction);
	}
	
	public List<Direction> getAllDirections(){
		Intersection crossIntersection = this.intersections.get(IntersectionType.CROSS);
		
		return Arrays.asList(crossIntersection.getAllDirections());
	}

	public Car getCar(Direction direction, IntersectionType intersectionType) {
		Intersection intersection = this.intersections.get(intersectionType);

		return intersection.getCar(direction);
	}
}

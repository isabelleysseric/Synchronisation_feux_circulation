package ca.ulaval.tp2.glo3004.road;

import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.car.CarFactory;
import ca.ulaval.tp2.glo3004.intersection.IntersectionType;

public abstract class Road {

	protected IntersectionType intersectionType;
	protected int frequency;
	private Car car;
	
	public Road(IntersectionType intersectionType, Direction directionType) {
		CarFactory carFactory = new CarFactory();
		this.intersectionType = intersectionType;
		this.car = carFactory.createCar(directionType, intersectionType);
		//this.frequency = frequency;
	}
	
	public IntersectionType getIntersectionType() {
		return this.intersectionType;
	}
	
	public int getFrequency() {
		return this.frequency;
	};
	
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	public Car getCar() {
		return this.car;
	}
	
	public abstract Direction[] getAllNeighboors();
	
	public abstract Direction getOppositeDirection();
}

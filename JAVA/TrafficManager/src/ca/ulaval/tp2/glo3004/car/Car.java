package ca.ulaval.tp2.glo3004.car;

import java.util.Random;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.control.IntersectionType;

public abstract class Car {

	private Direction direction;
	private Movement movement;
	
	public Car(Direction direction, IntersectionType intersectionType) {
		this.direction = direction;
		initializeMovement(intersectionType);
	}
	
	private void initializeMovement(IntersectionType intersectionType) {
		
		switch(intersectionType) {
		case THREE_WAY:
			this.movement = this.getThreeWayIntersectionMovement();
			break;
		case CROSS:
			this.movement = this.getCrossIntersectionMovement();
			break;
		default:
			break;
		}
	}
	
	protected void printMovement(Action action) {
		String movement = String.format("🚙 CAR:%s::%s", direction, action);
		System.out.println(movement);
	}

	 public void randomMoveWithOppositeSideOn() throws InvalidCarActionException {
		 boolean oppositeSideOn = true;
		 this.randomMove(oppositeSideOn);
	   }
	 
	 public void randomMoveWithPriority() throws InvalidCarActionException {
		 boolean oppositeSideOn = false;
		 this.randomMove(oppositeSideOn);
	 }
	 
	 private void randomMove(boolean oppositeSideOn) throws InvalidCarActionException {
	        Random random = new Random();
	        Action[] allowedActions = (oppositeSideOn)? movement.getActionWhenOppositeSideOn(): movement.getActionWhenPriority();
			
	        Action randomAction = allowedActions[random.nextInt(allowedActions.length)];
	        
	        this.printMovement(randomAction);
	   }
	 
	 public abstract Movement getThreeWayIntersectionMovement();
	 public abstract Movement getCrossIntersectionMovement();
}

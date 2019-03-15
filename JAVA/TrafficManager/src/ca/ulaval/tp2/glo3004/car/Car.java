package ca.ulaval.tp2.glo3004.car;

import java.util.Arrays;
import java.util.Random;

import ca.ulaval.tp2.glo3004.Direction;

public abstract class Car {

	private Direction direction;
	private Action[] allowedActions;

	public Car(Direction direction, Action[] allowedActions) {
		this.direction = direction;
		this.allowedActions = allowedActions;
	}
	
	public void move(Action action) throws InvalidCarActionException {
		boolean actionIsAllowed = Arrays.asList(allowedActions).contains(action);
		
		if(!actionIsAllowed) {
			String errorMessage = String.format("%s car can't %s", direction, action.toString());
			
			throw new InvalidCarActionException(errorMessage);
		}
		
		this.printMovement(action);
	}
	
	protected void printMovement(Action action) {
		String movement = String.format("%s [CAR] %s", direction, action.toString().toLowerCase());
		System.out.println(movement);
	}
	
	 public void randomMove() throws InvalidCarActionException {
	        Random random = new Random();
	       
	        Action randomAction = allowedActions[random.nextInt(allowedActions.length)];
	      
	        this.move(randomAction); 
	   }
}

package ca.ulaval.tp2.glo3004.car;

import java.util.List;
import java.util.Random;

import ca.ulaval.tp2.glo3004.Direction;

public abstract class Car {

	private Direction direction;
	protected List<Action> allowedActions;

	public Car(Direction direction, boolean forCrossIntersection) {
		this.direction = direction;
		this.initializeAllowedActions(forCrossIntersection);
	}
	
	public void move(Action action) throws InvalidCarActionException {
		if(!this.allowedActions.contains(action)) {
			String errorMessage = String.format("%s car can't %s", direction, action.toString());
			
			throw new InvalidCarActionException(errorMessage);
		}
		else {
			this.printMovement(action);
		}
	}
	
	protected void printMovement(Action action) {
		String movement = String.format("%s [CAR] %s", direction, action.toString().toLowerCase());
		System.out.println(movement);
	}
	
	 public void randomMove() throws InvalidCarActionException {
	        Random random = new Random();
	        int numberOfActions = allowedActions.size();
	       
	        Action[] values = new Action[numberOfActions];
	        values = allowedActions.toArray(values);
	        
	        Action randomAction = values[random.nextInt(values.length)];
	      
	        this.move(randomAction); 
	   }
	
	 public abstract void initializeAllowedActions(boolean isForCrossIntersection);
}

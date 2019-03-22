package ca.ulaval.tp2.glo3004.car;

import java.util.Random;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.control.IntersectionType;

/**
 * Classe abstraite définissant les actions possibles des voitures
 */
public abstract class Car {

	// Attributs privés
	private Direction direction;
	private Movement movement;
	
	// Constructeur avec parametres
	public Car(Direction direction, IntersectionType intersectionType) {
		this.direction = direction;
		initializeMovement(intersectionType);
	}
	
	// Méthode initialisant les mouvements selon le type d'intersection
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
	
	// Méthode qui affiche les mouvements des voitures
	protected void printMovement(Action action) {
		String movement = String.format("ðŸš™ CAR:%s::%s", direction, action);
		System.out.println(movement);
	}

	//
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

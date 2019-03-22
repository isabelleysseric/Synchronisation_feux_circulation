package ca.ulaval.tp2.glo3004.car.implementation;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.car.Action;
import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.car.Movement;
import ca.ulaval.tp2.glo3004.control.IntersectionType;

/**
 * Classe permettant le passage des voitures venant du sud vers les différentes 
 * directions en toute sécurité.
 */
public class SouthCar extends Car{

	// Constructeur avec parametres d'une voiture venant du sud
	public SouthCar(IntersectionType intersectionType) {
		super(Direction.SOUTH, intersectionType);
	}
	
	// Methode permettant le mouvement en toute sécurité aux voitures 
	// venant du sud dans une intersection en T
	public Movement getThreeWayIntersectionMovement() {
		Action[] actionsWhenPriority = new Action[]{Action.TURN_LEFT, Action.TURN_RIGHT};
		Action[] actionsWhenOppositeSideOn = new Action[]{Action.TURN_LEFT, Action.TURN_RIGHT};
		
		return new Movement(actionsWhenPriority, actionsWhenOppositeSideOn);
	}
	
	// Methode permettant le mouvement en toute sécurité aux voitures  
	// venant du sud dans une intersection en croix
	public Movement getCrossIntersectionMovement() {
		Action[]actionsWhenPriority = new Action[]{Action.TURN_LEFT, Action.TURN_RIGHT, Action.CONTINUE};
		Action[] actionsWhenOppositeSideOn = new Action[] {Action.CONTINUE, Action.TURN_RIGHT};
		
		return new Movement(actionsWhenPriority, actionsWhenOppositeSideOn);
	}
}

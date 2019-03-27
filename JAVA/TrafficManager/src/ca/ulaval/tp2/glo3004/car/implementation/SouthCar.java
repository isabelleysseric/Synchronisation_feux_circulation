package ca.ulaval.tp2.glo3004.car.implementation;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.car.Action;
import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.car.Movement;
import ca.ulaval.tp2.glo3004.control.IntersectionType;
import ca.ulaval.tp2.glo3004.view.StateView;

/**
 * Classe permettant le passage des voitures venant du sud vers les diff�rentes 
 * directions en toute s�curit�.
 */
public class SouthCar extends Car{

	// Constructeur avec parametres d'une voiture venant du sud
	public SouthCar(IntersectionType intersectionType, StateView stateView) {
		super(Direction.SOUTH, intersectionType, stateView);
	}
	
	// Methode permettant le mouvement en toute s�curit� aux voitures 
	// venant du sud dans une intersection en T
	public Movement getThreeWayIntersectionMovement() {
		Action[] actionsWhenPriority = new Action[]{Action.TURN_LEFT, Action.TURN_RIGHT};
		Action[] actionsWhenOppositeSideOn = new Action[]{Action.TURN_LEFT, Action.TURN_RIGHT};
		
		return new Movement(actionsWhenPriority, actionsWhenOppositeSideOn);
	}
	
	// Methode permettant le mouvement en toute s�curit� aux voitures  
	// venant du sud dans une intersection en croix
	public Movement getCrossIntersectionMovement() {
		Action[]actionsWhenPriority = new Action[]{Action.TURN_LEFT, Action.TURN_RIGHT, Action.CONTINUE};
		Action[] actionsWhenOppositeSideOn = new Action[] {Action.CONTINUE, Action.TURN_RIGHT};
		
		return new Movement(actionsWhenPriority, actionsWhenOppositeSideOn);
	}
}

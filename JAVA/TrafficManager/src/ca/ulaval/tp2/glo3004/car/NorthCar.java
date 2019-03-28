package ca.ulaval.tp2.glo3004.car;

import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.road.Direction;

/**
 * Classe permettant le passage des voitures venant du nord vers les diff�rentes 
 * directions en toute s�curit�.
 */
public class NorthCar extends Car{

	// Constructeur avec parametres d'une voiture venant du nord
	public NorthCar(IntersectionType intersectionType){
		super(Direction.NORTH, intersectionType);
	}

	// Methode permettant le mouvement en toute s�curit� aux voitures 
	// venant du nord dans une intersection en T
	public ActionController getThreeWayIntersectionMovement() {
		Action[] actionsWhenPriority = new Action[]{};
		Action[] actionsWhenOppositeSideOn = new Action[] {};
		
		return new ActionController(actionsWhenPriority, actionsWhenOppositeSideOn);
	}
	
	// Methode permettant le mouvement en toute s�curit� aux voitures 
	// venant du nord dans une intersection en croix
	public ActionController getCrossIntersectionMovement() {
		Action[]actionsWhenPriority = new Action[]{Action.TURN_LEFT, Action.TURN_RIGHT, Action.CONTINUE};
		Action[] actionsWhenOppositeSideOn = new Action[] {Action.CONTINUE, Action.TURN_RIGHT};
		
		return new ActionController(actionsWhenPriority, actionsWhenOppositeSideOn);
	}
}

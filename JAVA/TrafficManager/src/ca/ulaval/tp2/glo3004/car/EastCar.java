package ca.ulaval.tp2.glo3004.car;


import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.road.Direction;


/**
 * Classe permettant le passage des voitures venant de l'est vers les diff�rentes 
 * directions en toute s�curit�.
 */
public class EastCar extends Car {

	public EastCar(IntersectionType intersectionType){
		super(Direction.EAST, intersectionType);
	}

	// Methode permettant le mouvement en toute s�curit� aux voitures 
	// venant de l'est dans une intersection en T
	public ActionController getThreeWayIntersectionMovement() {
		Action[] actionsWhenPriority = new Action[]{Action.TURN_LEFT, Action.CONTINUE};
		Action[] actionsWhenOppositeSideOn = new Action[] {Action.CONTINUE};
		
		return new ActionController(actionsWhenPriority, actionsWhenOppositeSideOn);
	}
	
	// Methode permettant le mouvement en toute s�curit� aux voitures 
	// venant de l'est dans une intersection en croix
	public ActionController getCrossIntersectionMovement() {
		Action[]actionsWhenPriority = new Action[]{Action.TURN_LEFT, Action.CONTINUE, Action.TURN_RIGHT};
		Action[] actionsWhenOppositeSideOn = new Action[] {Action.CONTINUE, Action.TURN_RIGHT};
		
		return new ActionController(actionsWhenPriority, actionsWhenOppositeSideOn);
	}
	
	
}

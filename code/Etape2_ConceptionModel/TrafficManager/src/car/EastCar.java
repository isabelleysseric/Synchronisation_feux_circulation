package ca.ulaval.tp2.glo3004.car;


import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.road.Direction;


/*
 * Classe permettant le passage des voitures venant de l'est vers les differentes 
 * directions en toute securite
 */
public class EastCar extends Car {

	public EastCar(IntersectionType intersectionType){
		super(Direction.EAST, intersectionType);
	}

	// Methode permettant le mouvement en toute securite aux voitures 
	// venant de l'est dans une intersection en T
	public Movement getThreeWayIntersectionMovement() {
		Action[] actionsWhenPriority = new Action[]{Action.TURN_LEFT, Action.CONTINUE};
		Action[] actionsWhenOppositeSideOn = new Action[] {Action.CONTINUE};
		
		return new Movement(actionsWhenPriority, actionsWhenOppositeSideOn);
	}
	
	// Methode permettant le mouvement en toute securite aux voitures 
	// venant de l'est dans une intersection en croix
	public Movement getCrossIntersectionMovement() {
		Action[]actionsWhenPriority = new Action[]{Action.TURN_LEFT, Action.CONTINUE, Action.TURN_RIGHT};
		Action[] actionsWhenOppositeSideOn = new Action[] {Action.CONTINUE, Action.TURN_RIGHT};
		
		return new Movement(actionsWhenPriority, actionsWhenOppositeSideOn);
	}
	
	
}

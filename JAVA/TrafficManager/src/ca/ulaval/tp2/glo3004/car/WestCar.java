package ca.ulaval.tp2.glo3004.car;


import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.road.Direction;

/**
 * Classe permettant le passage des voitures venant de l'ouest vers les differentes 
 * directions en toute securite.
 */
public class WestCar extends Car{
	
	// Constructeur avec parametres d'une voiture venant de l'ouest
	public WestCar(IntersectionType intersectionType) {
		super(Direction.WEST, intersectionType);
	}

	// Methode permettant le mouvement en toute securite aux voitures 
	// venant de l'ouest dans une intersection en T
	public Movement getThreeWayIntersectionMovement() {
		Action[] actionsWhenPriority = new Action[]{Action.CONTINUE, Action.TURN_RIGHT};
		Action[] actionsWhenOppositeSideOn = new Action[]{Action.CONTINUE, Action.TURN_RIGHT};
		
		return new Movement(actionsWhenPriority, actionsWhenOppositeSideOn);
	}
	
	// Methode permettant le mouvement en toute securite aux voitures 
	// venant de l'ouest dans une intersection en croix
	public Movement getCrossIntersectionMovement() {
		Action[]actionsWhenPriority = new Action[]{Action.CONTINUE, Action.TURN_RIGHT, Action.TURN_LEFT};
		Action[] actionsWhenOppositeSideOn = new Action[] {Action.CONTINUE, Action.TURN_RIGHT};
		
		return new Movement(actionsWhenPriority, actionsWhenOppositeSideOn);
	}
}

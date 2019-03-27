package ca.ulaval.tp2.glo3004.car.implementation;


import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.car.Action;
import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.car.Movement;
import ca.ulaval.tp2.glo3004.control.IntersectionType;
import ca.ulaval.tp2.glo3004.view.StateView;

/**
 * Classe permettant le passage des voitures venant de l'est vers les differentes 
 * directions en toute securite
 */
public class EastCar extends Car {

	// Constructeur avec parametres d'une voiture venant de l'est
	public EastCar(IntersectionType intersectionType, StateView stateView){
		super(Direction.EAST, intersectionType, stateView);
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

package ca.ulaval.tp2.glo3004.car;

import java.util.Random;

import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.road.Direction;

/**
 * Classe abstraite d�finissant les actions possibles des voitures
 */
public abstract class Car {

	private Direction direction;
	private ActionController movement;
	private IntersectionType intersectionType;
	private IntersectionType nextIntersectionType;
	private String typeCar;
	private Action previousAction;
	private Action action;
	
	public Car(Direction direction, IntersectionType intersectionType) {
		this.direction = direction;
		this.intersectionType = intersectionType;
		initializeMovement(intersectionType);
	}

	public void setNextIntersectionType(IntersectionType nextIntersectionType) {
		this.nextIntersectionType = nextIntersectionType;
	}

	public void setPreviousAction() {
		this.previousAction = action;
	}

	/*
	 * PRODUCTEURS=> 3-WAY: EST-CONTINUE, SUD:GAUCHE PRODUCTEURS => CROSS:
	 * NORD-GAUCHE, OUEST-CONTINUE, SUD:DROITE
	 */
	public boolean canMoveToNextIntersection() throws Exception {
		
		if (this.intersectionType.equals(IntersectionType.THREE_WAY) 
				&& this.action.equals(Action.CONTINUE)
				&& direction.equals(Direction.EAST)) {
			return true;
		} 
		else if (this.intersectionType.equals(IntersectionType.THREE_WAY) 
				&& this.action.equals(Action.TURN_LEFT)
				&& direction.equals(Direction.SOUTH)) {

			return true;
		} else if (this.intersectionType.equals(IntersectionType.CROSS) 
				&& this.action.equals(Action.TURN_LEFT)
				&& direction.equals(Direction.NORTH)) {

			return true;

		} else if (this.intersectionType.equals(IntersectionType.CROSS) 
				&& this.action.equals(Action.CONTINUE)
				&& direction.equals(Direction.WEST)) {

			return true;

		} else if (this.intersectionType.equals(IntersectionType.CROSS) 
				&& this.action.equals(Action.TURN_RIGHT)
				&& direction.equals(Direction.SOUTH)) {

			return true;

		} else {
			return false;
		}
	}

	public void setTypeCar(String typeCar) {
		this.typeCar = typeCar;
	}

	public String getTypeCar() {
		return this.typeCar;
	}

	public Action getPreviousAction() {
		return this.previousAction;
	}

	public Action getCurrentAction() {
		return this.action;
	}

	public IntersectionType getIntersectionType() {
		return this.intersectionType;
	}

	public IntersectionType getNextIntersectionType() {
		return this.nextIntersectionType;
	}

	public Direction getDirection() {
		return this.direction;
	}

	// M�thode initialisant les mouvements selon le type d'intersection
	private void initializeMovement(IntersectionType intersectionType) {

		switch (intersectionType) {
		case THREE_WAY:
			this.movement = this.getThreeWayIntersectionMovement();
			break;
		case CROSS:
			this.movement = this.getCrossIntersectionMovement();
			break;
		default:
			this.movement = this.getCrossIntersectionMovement();
			break;
		}
	}

	
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
		Action[] allowedActions = (oppositeSideOn) ? movement.getActionWhenOppositeSideOn()
				: movement.getActionWhenPriority();

		Action randomAction = allowedActions[random.nextInt(allowedActions.length)];

		this.action = randomAction;
	}

	public abstract ActionController getThreeWayIntersectionMovement();

	public abstract ActionController getCrossIntersectionMovement();

	public ActionController getMovement() {
		return this.getMovement();
	}
}

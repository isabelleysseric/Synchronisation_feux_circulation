package ca.ulaval.tp2.glo3004.car;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.control.IntersectionType;

/**
 * Classe abstraite d�finissant les actions possibles des voitures
 */
public abstract class Car {

	// Attributs priv�s
	private Direction direction;
	private Movement movement;
	private IntersectionType intersectionType;
	private IntersectionType nextIntersectionType;
	private String typeCar;
	private Action previousAction;
	private Action action;
	
	
	// Constructeur avec parametres
	public Car(Direction direction, IntersectionType intersectionType) {
		this.direction = direction;
		initializeMovement(intersectionType);
		this.intersectionType = intersectionType;
	}
	
	public void setNextIntersectionType(IntersectionType nextIntersectionType) {
		this.nextIntersectionType = nextIntersectionType;
	}
	
	public void setPreviousAction() {
		this.previousAction = action;
	}
	
	public boolean canMoveToNextIntersection() throws Exception {
		List<Action> crossActions = new ArrayList<>();
		
		if(this.intersectionType.equals(IntersectionType.THREE_WAY)) {
			crossActions = Arrays.asList(new Action[] {Action.CONTINUE, Action.TURN_LEFT});
			
		}
		else if(this.intersectionType.equals(IntersectionType.CROSS)){
			crossActions = Arrays.asList(new Action[] {Action.CONTINUE, Action.TURN_RIGHT});
		
		}
		else {
			throw new Exception("Only 2 intersections available....");
		}
		
		return crossActions.contains(this.action);
	}
	
	public void setTypeCar(String typeCar) {
		this.typeCar = typeCar;
	}
	
	// M�thode initialisant les mouvements selon le type d'intersection
	private void initializeMovement(IntersectionType intersectionType) {
		
		switch(intersectionType) {
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
	
	// M�thode qui affiche les mouvements des voitures
	protected void printMovement(Action action) {
		String movement = "";
		if(nextIntersectionType == null) {
			 movement = String.format("🚙 CAR:%s::%s:%s", direction, intersectionType, action);
			
		}
		else {
			 movement = String.format("🚙( %s)-CAR:%s::(%s:%s -> %s:%s)", typeCar, direction, 
						intersectionType, previousAction, action, nextIntersectionType);
		}
		System.out.println(movement);
		
	}

	//
	 public void randomMoveWithOppositeSideOn() throws InvalidCarActionException {
		 boolean oppositeSideOn = true;
		 this.randomMove(oppositeSideOn);
	   }
	 
	 public void  randomMoveWithPriority() throws InvalidCarActionException {
		 boolean oppositeSideOn = false;
		 this.randomMove(oppositeSideOn);
	 }
	 
	 private void randomMove(boolean oppositeSideOn) throws InvalidCarActionException {
	        Random random = new Random();
	        Action[] allowedActions = (oppositeSideOn)? movement.getActionWhenOppositeSideOn(): movement.getActionWhenPriority();
			
	        Action randomAction = allowedActions[random.nextInt(allowedActions.length)];
	        
	        this.action = randomAction;
	        this.printMovement(randomAction);
	   }
	 
	 public abstract Movement getThreeWayIntersectionMovement();
	 public abstract Movement getCrossIntersectionMovement();

	public Movement getMovement() {
		// TODO Auto-generated method stub
		return this.getMovement();
	}
}

package ca.ulaval.tp2.glo3004.car;

import java.util.ArrayList;
import java.util.Arrays;

import ca.ulaval.tp2.glo3004.Direction;

public class WestCar extends Car{
	
	public WestCar(boolean isForCrossIntersection) {
		super(Direction.WEST, isForCrossIntersection);
	}

	public void initializeAllowedActions(boolean isForCrossIntersection){
		Action[] values = new Action[]{Action.CONTINUE, Action.TURN_RIGHT};
		
		this.allowedActions = new ArrayList<>(Arrays.asList(values)) ;
		
		if(isForCrossIntersection) {
			this.allowedActions.add(Action.TURN_LEFT);
		}
	
	}

}

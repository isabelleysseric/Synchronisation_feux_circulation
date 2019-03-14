package ca.ulaval.tp2.glo3004.car;

import java.util.ArrayList;
import java.util.Arrays;

import ca.ulaval.tp2.glo3004.Direction;

public class SouthCar extends Car{

	public SouthCar(boolean forCrossIntersection) {
		super(Direction.SOUTH, forCrossIntersection);
	}

	public void initializeAllowedActions(boolean isForCrossIntersection){
		Action[] values = new Action[]{Action.TURN_LEFT, Action.TURN_RIGHT};
		
		this.allowedActions = new ArrayList<>(Arrays.asList(values)) ;
		
		if(isForCrossIntersection) {
			this.allowedActions.add(Action.CONTINUE);
		}
	
	}
}

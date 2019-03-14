package ca.ulaval.tp2.glo3004.car;

import java.util.ArrayList;
import java.util.Arrays;

import ca.ulaval.tp2.glo3004.Direction;

public class EastCar extends Car {

	public EastCar(boolean forCrossIntersection){
		super(Direction.EAST, forCrossIntersection);
	}

	@Override
	public void initializeAllowedActions(boolean isForCrossIntersection){
		Action[] defaultActions = new Action[]{Action.TURN_LEFT, Action.CONTINUE};
		
		this.allowedActions = new ArrayList<>(Arrays.asList(defaultActions)) ;
		
		if(isForCrossIntersection) {
			this.allowedActions.add(Action.TURN_RIGHT);
		}
	
	}
	
}

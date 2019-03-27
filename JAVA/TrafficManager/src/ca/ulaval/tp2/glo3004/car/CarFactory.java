package ca.ulaval.tp2.glo3004.car;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.car.implementation.EastCar;
import ca.ulaval.tp2.glo3004.car.implementation.NorthCar;
import ca.ulaval.tp2.glo3004.car.implementation.SouthCar;
import ca.ulaval.tp2.glo3004.car.implementation.WestCar;
import ca.ulaval.tp2.glo3004.control.IntersectionType;
import ca.ulaval.tp2.glo3004.view.StateView;


/**
 * Classe permettant la cr�ation de voitures
 */
public class CarFactory {

	private StateView stateView;
	public CarFactory(StateView stateView) {
		this.stateView = stateView;
	}
	// Constructeur avec parametres
	public Car createCar(Direction direction, 
			IntersectionType intersectionType) throws Exception {
		Car car = null;
		
		switch(direction) {
		case EAST:
			car = new EastCar(intersectionType, stateView);
			break;
		case WEST:
			car = new WestCar(intersectionType, stateView);
			break;
		case SOUTH:
			car = new SouthCar(intersectionType, stateView);
			break;
		case NORTH:
			car = new NorthCar(intersectionType, stateView);
			break;
		}
		
		return car;
	}
	
}

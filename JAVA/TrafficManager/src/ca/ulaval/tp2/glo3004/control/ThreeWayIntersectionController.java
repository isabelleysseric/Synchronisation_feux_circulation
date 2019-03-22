package ca.ulaval.tp2.glo3004.control;

import java.util.HashMap;
import java.util.Map;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.car.CarFactory;

/**
 * Classe permettant le controle de l'intersection en T
 */
public class ThreeWayIntersectionController extends TraficController{

	// Constructeur avec parametres pour le controle de l'intersection en T
	public ThreeWayIntersectionController(CarFactory carFactory, ExecutionParameters parameters) {
		super(carFactory, IntersectionType.THREE_WAY, parameters);
	}
	
	// Methode permettant la lecture des directions opposées accessibles dans l'intersection en T
	public Map<Direction, Direction> getOppositeDirectionMap() {
		Map<Direction, Direction> oppositeDirectionMap = new HashMap<Direction, Direction>();

		oppositeDirectionMap.put(Direction.EAST, Direction.WEST);
		oppositeDirectionMap.put(Direction.WEST, Direction.EAST);
		oppositeDirectionMap.put(Direction.SOUTH, null);
		
		return oppositeDirectionMap;
	}
	
	// Methode permettant la lecture d'une nouvelle direction dans l'intersection en T
	public Direction[] getDirections() {
		return new Direction[] {Direction.EAST, Direction.SOUTH, Direction.WEST};
	}
	
}

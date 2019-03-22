package ca.ulaval.tp2.glo3004.control;

import java.util.HashMap;
import java.util.Map;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.car.CarFactory;

/**
 * Classe permettant le controle de l'intersection en croix
 */
public class CrossIntersectionController extends TraficController{

	// Constructeur avec paramettres pour le controle de l'intersection en croix
	public CrossIntersectionController(CarFactory carFactory, ExecutionParameters parameters) {
		super(carFactory, IntersectionType.CROSS, parameters);
	}
	
	// Methode permettant la lecture des directions opposées accessibles dans l'intersection en croix
	public Map<Direction, Direction> getOppositeDirectionMap() {
		Map<Direction, Direction> oppositeDirectionMap = new HashMap<Direction, Direction>();

		oppositeDirectionMap.put(Direction.EAST, Direction.WEST);
		oppositeDirectionMap.put(Direction.WEST, Direction.EAST);
		oppositeDirectionMap.put(Direction.SOUTH, Direction.NORTH);
		oppositeDirectionMap.put(Direction.NORTH, Direction.SOUTH);
		
		return oppositeDirectionMap;
	}
	
	// Methode permettant la lecture d'une nouvelle direction dans l'intersection en croix
	public Direction[] getDirections() {
		return new Direction[] {Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH};
	}
	
}

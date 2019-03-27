package ca.ulaval.tp2.glo3004.control;

import java.util.HashMap;
import java.util.Map;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.LightView;

import ca.ulaval.tp2.glo3004.car.CarFactory;

import ca.ulaval.tp2.glo3004.light.LightController;

/**
 * Classe permettant le controle de l'intersection en T
 */
public class ThreeWayIntersectionController extends TraficController {

	
	public ThreeWayIntersectionController(CarFactory carFactory, 
			ExecutionParameters parameters, LightView panelComponent, 
			 LightController lightController) {
		
		super(carFactory, IntersectionType.THREE_WAY, parameters, panelComponent, lightController);
	
	}

	// Methode permettant la lecture des directions oppos�es accessibles dans
	// l'intersection en T
	public Map<Direction, Direction> getOppositeDirectionMap() {

		Map<Direction, Direction> oppositeDirectionMap = new HashMap<Direction, Direction>() {
			private static final long serialVersionUID = 1L;

			{
				put(Direction.EAST, Direction.WEST);
				put(Direction.WEST, Direction.EAST);
				put(Direction.SOUTH, Direction.NORTH);
			}
		};

		return oppositeDirectionMap;
	}

	// Methode permettant la lecture d'une nouvelle direction dans l'intersection en
	// T
	public Direction[] getDirections() {
		return new Direction[] { Direction.EAST, Direction.SOUTH, Direction.WEST };
	}

	@Override
	public Map<Direction, Direction[]> getAdjacenceMap() {
		
		Map<Direction, Direction[]> adjacenceMap = new HashMap<Direction, Direction[]>() {
			private static final long serialVersionUID = 1L;

			{
				put(Direction.EAST, new Direction[] { Direction.SOUTH, Direction.NORTH });
				put(Direction.WEST, new Direction[] { Direction.SOUTH, Direction.NORTH });
				put(Direction.SOUTH, new Direction[] { Direction.EAST, Direction.WEST });
			}

		};

		return adjacenceMap;
	}

}

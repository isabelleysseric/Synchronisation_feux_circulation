package ca.ulaval.tp2.glo3004.control;

import java.util.HashMap;
import java.util.Map;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.car.CarFactory;

/**
 * Classe permettant le controle de l'intersection en croix
 */
public class CrossIntersectionController extends TraficController {

	public CrossIntersectionController(CarFactory carFactory, ExecutionParameters parameters) {
		super(carFactory, IntersectionType.CROSS, parameters);
	}

	// Methode permettant la lecture des directions oppos�es accessibles dans
	// l'intersection en croix
	public Map<Direction, Direction> getOppositeDirectionMap() {

		Map<Direction, Direction> oppositeDirectionMap = new HashMap<Direction, Direction>() {
			private static final long serialVersionUID = 1L;
			{
				put(Direction.EAST, Direction.WEST);
				put(Direction.WEST, Direction.EAST);
				put(Direction.SOUTH, Direction.NORTH);
				put(Direction.NORTH, Direction.SOUTH);
			}
		};

		return oppositeDirectionMap;
	}

	// Methode permettant la lecture d'une nouvelle direction dans l'intersection en
	// croix
	public Direction[] getDirections() {
		return new Direction[] { Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH };
	}

	@Override
	public Map<Direction, Direction[]> getAdjacenceMap() {

		Map<Direction, Direction[]> oppositeDirectionMap = new HashMap<Direction, Direction[]>() {

			private static final long serialVersionUID = 1L;

			{
				put(Direction.EAST, new Direction[] { Direction.SOUTH, Direction.NORTH });
				put(Direction.WEST, new Direction[] { Direction.SOUTH, Direction.NORTH });
				put(Direction.SOUTH, new Direction[] { Direction.EAST, Direction.WEST });
				put(Direction.NORTH, new Direction[] { Direction.EAST, Direction.WEST });

			}
		};

		return oppositeDirectionMap;

	}

}

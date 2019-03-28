package ca.ulaval.tp2.glo3004.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import ca.ulaval.tp2.glo3004.ExecutionParameters;
import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.intersection.Intersection;
import ca.ulaval.tp2.glo3004.light.LightColor;
import ca.ulaval.tp2.glo3004.road.Direction;
import ca.ulaval.tp2.glo3004.view.StateView;

/**
 * Classe permettant le controle du traffic des voitures, pietons et lumieres
 * aux intersection en T et en croix
 */
public class TraficController {

	private StateView stateView;
	private Intersection intersection;
	private ExecutionParameters parameters;
	private LightController lightController;
	
	private boolean IN_SYNCHRO = false;
	private int WAITING_TIME = 2000;
	private Object lock = new Object();
	
	private CyclicBarrier pedestrianBarrier;
	
	private Map<Direction, Boolean> timeOutMaps = new HashMap<Direction, Boolean>();
	private Map<Direction, Object> directionLocks = new HashMap<Direction, Object>();
	
	// Constructeur avec parametres pour le controle du traffic sur une intersection
	// en T ou en croix
	public TraficController( Intersection intersection, 
			ExecutionParameters parameters,
			StateView stateView,
			LightController lightController,
			CyclicBarrier pedestrianBarriers) {
		
		this.intersection = intersection;
		this.parameters = parameters;
		this.stateView = stateView;
		this.pedestrianBarrier = pedestrianBarriers;
		this.lightController = lightController;
		initializeDirectionVariables();
	}

	// Methode qui initialise les lumieres des interesections
	private void initializeDirectionVariables() {

		for (Direction direction : intersection.getAllDirections()) {
			timeOutMaps.put(direction, false);
			directionLocks.put(direction, new Object());
		}
	}

	// Methode qui empeche les lumieres de l'est d'etre vertes en meme temps que
	// celles du sud
	public void controlLight(Direction direction) throws Exception {
		synchronized (lock) {

			while (atLeastOneNeighBoorIsGreen(direction)) {

				lock.wait();
			}

			switchLight(direction, LightColor.GREEN);
		}

		switchBackToRedAfterCarCirculation(direction);
	}
	

	private boolean atLeastOneNeighBoorIsGreen(Direction direction) {

		List<Direction> neighboors = this.intersection.getAllNeighboors(direction);
		return this.lightController.atLeastOneNeighBoorIsGreen(neighboors, direction);
	}

	// Methode qui change la couleur des lumieres suivant les directions
		private void switchBackToRedAfterCarCirculation(Direction direction) throws InterruptedException {
			Object directionLock = directionLocks.get(direction);

			synchronized (directionLock) {
				while (!timeOutMaps.get(direction)) {
					directionLock.wait();
				}
			}
			
			switchLight(direction, LightColor.RED);
			
			pedestrianCanPass();
			
			synchronized (directionLock) {
				timeOutMaps.put(direction, false);
				directionLock.notifyAll();
			}
		}

		private void pedestrianCanPass() {
			
			try {
				pedestrianBarrier.await();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	private void switchLight(Direction direction, LightColor color) {

		synchronized (lock) {
			try {
				Thread.sleep(WAITING_TIME);
			} catch (InterruptedException e) {
				System.out.println("LIGHT THREAD STOP");
			}

			lightController.switchLight(direction, color);
			
			lock.notifyAll();
		}
	}
	
	// Methode qui synchronise la circulation des voitures aux intersections
	public void carMove(Direction direction) throws Exception {
		
		synchronized (lock) {
			
			while (lightController.getLight(direction).isRed()) {
				lock.wait();
			}
		}
		
		Car car  = intersection.getCar(direction);
		Direction oppositeDirection = this.intersection.getOppositeDirection(direction);

		for (int i = 0; i < parameters.getNumberOfCars(); i++) {
			
			synchronized (lock) {
				if (oppositeDirection == null || lightController.getLight(oppositeDirection).isRed()) {
					car.randomMoveWithPriority();
				} else {
					car.randomMoveWithOppositeSideOn();
				}
				stateView.displayCarState(car, IN_SYNCHRO);
			}
		}

		synchronized (directionLocks.get(direction)) {
			timeOutMaps.put(direction, true);
			directionLocks.get(direction).notifyAll();
		}

	}

}

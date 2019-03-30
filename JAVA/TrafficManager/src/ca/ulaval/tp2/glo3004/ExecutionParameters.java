package ca.ulaval.tp2.glo3004;

import java.util.HashMap;
import java.util.Map;

import ca.ulaval.tp2.glo3004.road.Direction;

/**
 * Classe permettant l'acc�s et la modification du nombre de pietons et de voitures 
 */
public class ExecutionParameters {

	private int numberOfPedestrians;
	private int numberOfCars;
	
	private Map<Direction, Integer> lightFrequencies;
	
	public ExecutionParameters(int numberOfCars, int numberOfPedestrians) {
		this.numberOfCars = numberOfCars;
		this.numberOfPedestrians = numberOfPedestrians;
		this.lightFrequencies = new HashMap<Direction, Integer>();
	}
	
	public int getNumberOfCars(){
		return numberOfCars;
	}
	
	public void setNumberOfCars(int newNumberOfCars){
		numberOfCars = newNumberOfCars;
	}
	
	public int getNumberOfPedestrians(){
		return numberOfPedestrians;
	}
	
	public void setNumberOfPedestrians(int newNumberOfPedestrians){
		numberOfPedestrians = newNumberOfPedestrians;
	}
	
	public void addLightFrequency(Direction direction, int frequency) {
		this.lightFrequencies.put(direction, frequency);
	}
	
	public int getLightFrequency(Direction direction) {
		return this.lightFrequencies.get(direction);
	}
	
	
}

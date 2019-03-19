package ca.ulaval.tp2.glo3004.control;

public class ExecutionParameters {

	private int numberOfPedestrians;
	private int numberOfCars;
	
	public ExecutionParameters(int numberOfCars, int numberOfPedestrians) {
		this.numberOfCars = numberOfCars;
		this.numberOfPedestrians = numberOfPedestrians;
	}
	
	public int getNumberOfCars(){
		return numberOfCars;
	}
	
	public int getNumberOfPedestrians(){
		return numberOfPedestrians;
	}
}

package ca.ulaval.tp2.glo3004;

/**
 * Classe permettant l'acc�s et la modification du nombre de pietons et de voitures 
 */
public class ExecutionParameters {

	// Attributs priv�s de la classe
	private int numberOfPedestrians;
	private int numberOfCars;
	
	// Constructeur avec parametres 
	public ExecutionParameters(int numberOfCars, int numberOfPedestrians) {
		this.numberOfCars = numberOfCars;
		this.numberOfPedestrians = numberOfPedestrians;
	}
	
	// Methode permettant de lire l'attribut de numberOfCars
	public int getNumberOfCars(){
		return numberOfCars;
	}
	
	// Methode permettant de modifier l'attribut numberOfCars
	public void setNumberOfCars(int newNumberOfCars){
		numberOfCars = newNumberOfCars;
	}
	
	// Methode permettant de lire l'attribut de numberOfPedestrians
	public int getNumberOfPedestrians(){
		return numberOfPedestrians;
	}
	
	// Methode permettant de modifier l'attribut numberOfPedestrians
	public void setNumberOfPedestrians(int newNumberOfPedestrians){
		numberOfPedestrians = newNumberOfPedestrians;
	}
}

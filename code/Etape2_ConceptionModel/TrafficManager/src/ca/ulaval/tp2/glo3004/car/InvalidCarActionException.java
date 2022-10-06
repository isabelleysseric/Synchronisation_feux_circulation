package ca.ulaval.tp2.glo3004.car;

public class InvalidCarActionException extends Exception{

	private static final long serialVersionUID = 1L;
	private String errorMessage;
	
	public InvalidCarActionException(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String toString() {
		return this.errorMessage;
	}
}

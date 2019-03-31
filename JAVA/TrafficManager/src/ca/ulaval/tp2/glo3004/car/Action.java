package ca.ulaval.tp2.glo3004.car;

/*
 * Enumeration des differents icons des actions possibles
 */
public enum Action {

	CONTINUE, TURN_LEFT, TURN_RIGHT;
	 
	 public String toString() {
		 return String.format("%s", this.name());
	 }
    
}

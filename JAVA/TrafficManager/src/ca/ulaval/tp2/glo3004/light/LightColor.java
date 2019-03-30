package ca.ulaval.tp2.glo3004.light;

/*
 * Enumeration des differents icons associés aux couleurs des lumieres
 */
public enum LightColor {
	// Enumeration
	RED, GREEN;

	// Methode renvoyant les informations de la couleur de la lumiere
	public String toString() {
		return String.format("%s%s", this.name());
	}

	public boolean isGreen() {
		return this.equals(GREEN);
	}
	
	public boolean isRed() {
		return this.equals(RED);
	}
}

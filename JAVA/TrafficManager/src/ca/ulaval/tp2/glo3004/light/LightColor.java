package ca.ulaval.tp2.glo3004.light;

/** 
 * Enumeration des differents icons associés aux couleurs des lumieres
 */
public enum LightColor {
	// Enumeration
	RED("⛔"), GREEN("✅");

	// Attributs privés
	private String icon;

	// Constructeur avec parametre
	LightColor(String icon) {
		this.icon = icon;
	}

	// Methode permettant la visualisation de la couleur de la lumiere
	public String getIcon() {
		return icon;
	}

	// Methode renvoyant les informations  de la couleur de la lumiere
	public String toString() {
		return String.format("%s%s", this.name(), this.icon);
	}

	public boolean isGreen() {
		return this.equals(GREEN);
	}
	
	public boolean isRed() {
		return this.equals(RED);
	}
}

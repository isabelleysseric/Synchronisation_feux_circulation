package ca.ulaval.tp2.glo3004.intersection;

/*
 * Enumeration des differents types d'intersections possibles
 */
public enum IntersectionType {

	THREE_WAY, CROSS, SYNCHRO, SYNCHRO_TEST;

	public String toString() {
		return String.format("%s", this.name()); 
	}

}

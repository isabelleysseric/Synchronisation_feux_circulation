package ca.ulaval.tp2.glo3004.intersection;

/**
 * Enumeration des differents types d'intersections possibles
 */
public enum IntersectionType {

	THREE_WAY("╣"), CROSS("╬"), SYNCHRO(""), SYNCHRO_TEST("");
	
	
	private String icon;
	 
	IntersectionType(String icon) {
	        this.icon = icon;
	 }
	 
	 public String getIcon() {
		 return icon;
	  }
	 
	 public String toString() {
		 return String.format("%s%s", this.name(), this.icon);
	 }
   
}

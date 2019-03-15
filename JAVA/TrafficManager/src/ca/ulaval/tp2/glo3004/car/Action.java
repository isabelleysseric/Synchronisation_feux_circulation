package ca.ulaval.tp2.glo3004.car;

public enum Action {

	CONTINUE("➜"), TURN_LEFT("⤴"), TURN_RIGHT("⤵");
	 private String icon;
	 
	 Action(String icon) {
	        this.icon = icon;
	 }
	 
	 public String getIcon() {
		 return icon;
	  }
	 
	 public String toString() {
		 return String.format("%s%s", this.name(), this.icon);
	 }
    
}

package ca.ulaval.tp2.glo3004.light;

public enum LightColor {

	RED("⛔"), GREEN("✅");
	
	private String icon;
	 
	 LightColor(String icon) {
	        this.icon = icon;
	 }
	 
	 public String getIcon() {
		 return icon;
	  }
	 
	 public String toString() {
		 return String.format("%s%s", this.name(), this.icon);
	 }
}

package ca.ulaval.tp2.glo3004.light;

import ca.ulaval.tp2.glo3004.Direction;

public class Light {

	private Direction direction;
	private LightColor color;
	
	public Light(Direction direction) {
		this.direction = direction;
		this.color = LightColor.RED;
	}
	
	public void switchTo(LightColor color) {
			this.color = color;
			printCurrentState();
	}

	public Direction getDirection() {
		return direction;
	}

	public LightColor getColor() {
		return color;
	}
	
	private void printCurrentState() {
		String status = String.format("%s *LIGHT* is %s", direction, color);
		System.out.println(status);
	}

	public boolean isGreen() {
		return color == LightColor.GREEN;
	}

	public boolean isRed() {
		
		return color == LightColor.RED;
	}
	
}

package ca.ulaval.tp2.glo3004.light;

import ca.ulaval.tp2.glo3004.road.Direction;

public class Light {

	private LightColor color;
	private Direction direction;
	private Object lock = new Object();

	public Light(Direction direction) {
		this.direction = direction;
		this.color = LightColor.RED;
	}

	public void switchTo(LightColor color) {
		synchronized (lock) {
			this.color = color;
		}
	}

	public Direction getDirection() {
		return direction;
	}

	public synchronized LightColor getColor() {
		synchronized (lock) {
			return color;
		}
	}

	public String getState() {
		String state = String.format("%s::%s", direction, color);
		return state;
	}

	public boolean isGreen() {
		synchronized (lock) {
			return color == LightColor.GREEN;
		}
	}

	public boolean isRed() {
		synchronized (lock) {
			return color == LightColor.RED;
		}
	}

}

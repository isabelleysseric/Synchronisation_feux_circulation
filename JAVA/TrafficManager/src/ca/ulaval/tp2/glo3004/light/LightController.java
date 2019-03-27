package ca.ulaval.tp2.glo3004.light;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;

import ca.ulaval.tp2.glo3004.Direction;

public class LightController {

	private static Light eastLight = new Light(Direction.EAST);
	private static Light westLight = new Light(Direction.WEST);
	private static Light southLight = new Light(Direction.SOUTH);
	private static Light northLight = new Light(Direction.NORTH);
	
	private Map<Direction, Light> lights = new HashMap<>();

	public LightController() {
		initializeLights();
	}

	private void initializeLights() {
		lights = new HashMap<Direction, Light>() {
			private static final long serialVersionUID = 1L;

			{
				put(Direction.EAST, eastLight);
				put(Direction.WEST, westLight);
				put(Direction.SOUTH, southLight);
				put(Direction.NORTH, northLight);
			}
		};

	}

	public synchronized void switchLight(Direction direction, LightColor color) {
		this.lights.get(direction).switchTo(color);
	}

	public synchronized Light getLight(Direction direction) {
		return this.lights.get(direction);
	}

	public synchronized Map<Direction, Light> getLights() {
		return this.lights;
	}

	public synchronized String getLightStates() {

		StringBuilder lightStates = new StringBuilder();
		//lightStates.append(String.format("🚥 %s:", Thread.currentThread().getName()));

		lights.forEach((direction, light) -> {
			lightStates.append(String.format(" %s:%s ", direction, light.getColor()));
		});

		return lightStates.toString();
	}

	public boolean atLeastOneLightIsGreen() {
		return lights.values().stream().anyMatch(light -> light.isGreen());

	}

	public boolean atLeastOneNeighBoorIsGreen(List<Direction> neighboors, Direction direction) {
		List<Light> neighboorsLights = neighboors.stream().map(neighboor -> this.lights.get(neighboor))
				.collect(Collectors.toList());

		return neighboorsLights.stream().anyMatch(light -> light.isGreen());
	}

}

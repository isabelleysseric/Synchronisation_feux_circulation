package ca.ulaval.tp2.glo3004.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ca.ulaval.tp2.glo3004.intersection.Intersection;
import ca.ulaval.tp2.glo3004.light.Light;
import ca.ulaval.tp2.glo3004.light.LightColor;
import ca.ulaval.tp2.glo3004.road.Direction;
import ca.ulaval.tp2.glo3004.view.LightView;

public class AllLightController {

	private final Map<Direction, Light> lights = new HashMap<>();
	private final Intersection intersection;
	private final LightView lightView;

	public AllLightController(Intersection intersection, LightView lightView) {

		Direction[] directions = intersection.getAllDirections();

		for (Direction direction : directions) {
			lights.put(direction, new Light(direction));
		}

		this.intersection = intersection;
		this.lightView = lightView;
	}

	public synchronized boolean lightIsRed(Direction direction) {

		return this.lights.get(direction).isRed();
	}

	public synchronized boolean lightIsGreen(Direction direction) {

		return this.lights.get(direction).isGreen();
	}

	public synchronized void switchLight(Direction direction, LightColor color) {

		while (color.equals(LightColor.GREEN) && this.atLeastOneNeighBoorIsGreen(direction)) {
			try {
				System.out.println(direction + " is waiting...");

				wait();

			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		;

		this.lights.get(direction).switchTo(color);
		notifyAll();
		this.printLights();
		lightView.setLights(lights);

	}

	public synchronized void printLights() {
		StringBuilder stringBuilder = new StringBuilder();

		lights.forEach((direction, light) -> {
			String state = String.format("%s:%s -", direction, light.getColor());
			stringBuilder.append(state);
		});

		System.out.println(stringBuilder.toString());
	}

	public synchronized boolean oppositeSideIsGreen(Direction direction) throws Exception {

		Direction oppositeSide = this.intersection.getOppositeDirection(direction);

		if (oppositeSide == null) {
			return false;
		} else {
			return this.lights.get(oppositeSide).isGreen();
		}

	}

	public synchronized boolean atLeastOneLightIsGreen() {
		return lights.values().stream().anyMatch(light -> light.isGreen());

	}

	public synchronized boolean atLeastOneNeighBoorIsGreen(Direction direction) {
		List<Direction> neighboors = this.intersection.getAllNeighboors(direction);
		List<Light> neighboorsLights = neighboors.stream().map(neighboor -> this.lights.get(neighboor))
				.collect(Collectors.toList());

		return neighboorsLights.stream().anyMatch(light -> light.isGreen());
	}

}

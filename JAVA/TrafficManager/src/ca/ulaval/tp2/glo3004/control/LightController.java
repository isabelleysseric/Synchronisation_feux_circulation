package ca.ulaval.tp2.glo3004.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ca.ulaval.tp2.glo3004.intersection.Intersection;
import ca.ulaval.tp2.glo3004.light.Light;
import ca.ulaval.tp2.glo3004.light.LightColor;
import ca.ulaval.tp2.glo3004.road.Direction;
import ca.ulaval.tp2.glo3004.view.LightView;

public class LightController {

	private LightView lightView;
	private LightView secondLightView;
	
	private Map<Direction, Light> lights = new HashMap<>();

	public LightController(Intersection intersection, LightView lightView) {
		//this.intersection = intersection;
		this.lightView = lightView;
		initializeLights(intersection.getAllDirections());
	}
	
	public LightController() {
		Direction[] directions = new Direction[] {Direction.EAST, Direction.WEST,
				Direction.NORTH, Direction.SOUTH};
		
		this.initializeLights(directions);
	}

	private void initializeLights(Direction[] directions) {
		for(Direction directionType: directions) {
			lights.put(directionType, new Light(directionType));
		}
	}

	// Methode qui affiche le statut des lumieres en fonction des intersections
	public synchronized void switchLight(Direction direction, LightColor color) {
		if(this.lightView !=null) lightView.setLights(this.lights);
		this.lights.get(direction).switchTo(color);
	}

	public synchronized boolean allLightsRed() {
		return lights.values().stream().allMatch(light -> light.isRed());
	}

	public synchronized Light getLight(Direction direction) {
		return this.lights.get(direction);
	}

	public synchronized Map<Direction, Light> getLights() {
		return this.lights;
	}

	public synchronized String getLightStates() {
		StringBuilder lightStates = new StringBuilder();

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

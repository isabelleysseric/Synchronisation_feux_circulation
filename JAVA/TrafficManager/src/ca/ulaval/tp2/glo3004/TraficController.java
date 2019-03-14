package ca.ulaval.tp2.glo3004;

import ca.ulaval.tp2.glo3004.light.Light;
import ca.ulaval.tp2.glo3004.light.LightColor;

public class TraficController {	
		
	private Light eastLight;
	private Light westLight;
	private Light southLight;
	
	private Object eastLock = new Object();
	private Object southLock = new Object();
	private Object westLock = new Object();
	
	private Circulation eastCirculation;
	private Circulation westCirculation;
	private Circulation southCirculation;
	
	private CirculationFactory factory = new CirculationFactory();
	
	public TraficController() {
		initializeLights();
		initializeTIntersectionRoads();
	}
	
	private void initializeLights() {
		eastLight = new Light(Direction.EAST, eastLock);
		westLight = new Light(Direction.WEST, southLock);
		southLight = new Light(Direction.SOUTH, westLock);
	}
	
	private void initializeTIntersectionRoads() {
		boolean forCrossIntersection = false;
		eastCirculation = factory.createCirculation(Direction.EAST, eastLight, forCrossIntersection);
		westCirculation = factory.createCirculation(Direction.WEST, westLight, forCrossIntersection);
		southCirculation = factory.createCirculation(Direction.SOUTH, southLight, forCrossIntersection);
	}
	
	public void testWithEastCirculation() {
		eastLight.switchTo(LightColor.GREEN);
		eastCirculation.run();
		
		eastLight.switchTo(LightColor.RED);
		eastCirculation.run();
	}
	
	
}

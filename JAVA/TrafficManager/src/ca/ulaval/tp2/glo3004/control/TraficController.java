package ca.ulaval.tp2.glo3004.control;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.intersection.Intersection;
import ca.ulaval.tp2.glo3004.light.Light;
import ca.ulaval.tp2.glo3004.light.LightColor;

public class TraficController {	
		
	private Light eastLight;
	private Light westLight;
	private Light southLight;
		
	private Intersection intersection;
	
	public TraficController(Intersection intersection) {
		initializeLights();
		this.intersection = intersection;
	}
	
	private void initializeLights() {
		eastLight = new Light(Direction.EAST);
		westLight = new Light(Direction.WEST);
		southLight = new Light(Direction.SOUTH);
	}
	
	public synchronized void pedestrianPass() throws InterruptedException {
		while(eastLight.isGreen() || westLight.isGreen() || southLight.isGreen()) {
			wait();
		}
		System.out.println("🚶 PEDESTRIANS::CROSS");
		
		notifyAll();
	}
	
	public synchronized void eastSwitchToGreen() throws Exception {
		while(southLight.isGreen()) wait();
	
		eastLight.switchTo(LightColor.GREEN);
		
		printLightStates();
		intersection.carMove(Direction.EAST);
		notifyAll();
	}
	
	public synchronized void eastSwitchToRed() throws InterruptedException {
		eastLight.switchTo(LightColor.RED);
		
		printLightStates();
		notifyAll();
	}
	
	public synchronized void westSwitchToGreen() throws Exception {
		while(southLight.isGreen()) {
			wait();
		}
		
		westLight.switchTo(LightColor.GREEN);
		printLightStates();
		intersection.carMove(Direction.WEST);
		notifyAll();
	}
	
	public synchronized void westSwitchToRed() throws InterruptedException {
		westLight.switchTo(LightColor.RED);
		printLightStates();
		
		notifyAll();
	}
	
	public synchronized void southSwitchToGreen() throws Exception {
		while(westLight.isGreen() || eastLight.isGreen()) {
			wait();
		}
		
		southLight.switchTo(LightColor.GREEN);
		printLightStates();
		intersection.carMove(Direction.SOUTH);
		
		notifyAll();
	}
	
	public synchronized void southSwitchToRed() throws InterruptedException {
		southLight.switchTo(LightColor.RED);
		printLightStates();
		
		notifyAll();
	}
	
	private void printLightStates() {
		String lightStates = String.format("🚥 EAST:%s WEST:%s SOUTH:%s", eastLight.getColor(), westLight.getColor(), southLight.getColor());
		
		System.out.println(lightStates);
	}
}

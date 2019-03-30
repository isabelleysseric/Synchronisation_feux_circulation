package ca.ulaval.tp2.glo3004.controller;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import ca.ulaval.tp2.glo3004.light.LightColor;
import ca.ulaval.tp2.glo3004.road.Direction;

public class LightController {

	private CyclicBarrier carBarrier;

	private CyclicBarrier pedestrianBarrier;

	private final AllLightController allLightController;

	public LightController(CyclicBarrier carBarrier, CyclicBarrier pedestrianBarrier,
			AllLightController allLightController) {

		this.carBarrier = carBarrier;
		this.pedestrianBarrier = pedestrianBarrier;
		this.allLightController = allLightController;
	}

	public synchronized void switchLightState(Direction direction) {

		boolean lightIsGreen = this.allLightController.lightIsGreen(direction);

		if (lightIsGreen) {

			allLightController.switchLight(direction, LightColor.RED);

			try {
				System.out.println(direction + " PEDESTRIANS AWAIT");
				pedestrianBarrier.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
		} else {
			allLightController.switchLight(direction, LightColor.GREEN);

			try {
				// System.out.println(direction + " ==> BEFORE BARRIER AWAKE GREEN LIGHT...");
				carBarrier.await();

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {

				e.printStackTrace();
			}

		}

	}

}

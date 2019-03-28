package ca.ulaval.tp2.glo3004.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.light.Light;
import ca.ulaval.tp2.glo3004.road.Direction;

/**
 * 
 */
public class LightView extends JComponent {

	private static final long serialVersionUID = 1L;
	private static float START_X = 5;
	private static float START_Y = 50;
	private static float LIGHT_MARGIN = 50;
	private static float LIGHT_PADDING = 10;
	private static float LIGHT_DIAMETER = 30;
	private static float LIGHT_CONTAINER_WIDTH = 50;
	private static float LIGHT_CONTAINER_HEIGHT = 100;
	private static Color LIGHT_OFF_COLOR = Color.GRAY;
	private static Map<Direction, Integer> lightPositions;

	private Object lock = new Object();
	private IntersectionType intersectionType;
	private Map<Direction, Light> lights = new HashMap<>();

	public LightView(IntersectionType intersectionType) {		
		this.setPreferredSize(new Dimension(300, 200));
		this.intersectionType = intersectionType;
		reinitialize();
	}

	public void reinitialize() {
		initializeLightPositions();
		initializeLights();
	}

	public String getName() {
		return String.format("%s INTERSECTION", intersectionType);
	}

	private void initializeLightPositions() {
		lightPositions = new HashMap<Direction, Integer>();
		lightPositions.put(Direction.EAST, 1);
		lightPositions.put(Direction.SOUTH, 2);
		lightPositions.put(Direction.WEST, 3);
		lightPositions.put(Direction.NORTH, 4);
	}

	private void initializeLights() {
		this.lights = new HashMap<Direction, Light>();
		this.lights.put(Direction.EAST, new Light(Direction.EAST));
		this.lights.put(Direction.WEST, new Light(Direction.WEST));
		this.lights.put(Direction.SOUTH, new Light(Direction.SOUTH));

		if (intersectionType.equals(IntersectionType.CROSS)) {
			this.lights.put(Direction.NORTH, new Light(Direction.NORTH));
		};
	}

	public void setLights(Map<Direction, Light> lights) {
		synchronized (lock) {
			this.lights = lights;
		}
		repaint();
	}

	public void paint(Graphics g) {

		lights.forEach((direction, light) -> {

			if(direction.equals(Direction.NORTH) && this.intersectionType.equals(IntersectionType.THREE_WAY)) {
				return;
			}
			Graphics2D g2 = (Graphics2D) g;
			int position = lightPositions.get(direction);
			float lightX = position * START_X + position * LIGHT_CONTAINER_WIDTH;

			drawLightContainer(g2, lightX, START_Y);
			drawLights(g2, lightX, START_Y, light);
			drawText(g2, lightX, START_Y, direction);		
		});
	}

	private void drawLightContainer(Graphics2D g2, float x, float y) {
		Rectangle2D rectangle = new Rectangle2D.Double(x, y, LIGHT_CONTAINER_WIDTH, LIGHT_CONTAINER_HEIGHT);
		g2.setPaint(Color.DARK_GRAY);
		g2.fill(rectangle);
		g2.draw(rectangle);
	}

	private void drawLights(Graphics2D g2, float x, float y, Light light) {
		float lightX = x + LIGHT_PADDING;
		float redY = y + LIGHT_PADDING;
		float greenY = redY + LIGHT_MARGIN;

		Color redColor = (light.isRed()) ? Color.RED : LIGHT_OFF_COLOR;
		Color greenColor = (light.isGreen()) ? Color.GREEN : LIGHT_OFF_COLOR;

		this.drawLight(g2, lightX, redY, redColor);
		this.drawLight(g2, lightX, greenY, greenColor);
	}

	private void drawLight(Graphics2D g2, double x, double y, Color color) {
		Ellipse2D.Double circle = new Ellipse2D.Double(x, y, LIGHT_DIAMETER, LIGHT_DIAMETER);
		g2.setPaint(color);
		g2.fill(circle);
	}

	private void drawText(Graphics2D g2, float x, float y, Direction direction) {
		g2.setPaint(Color.WHITE);
		g2.drawString(direction.name(), x, y - 5);
	}
}

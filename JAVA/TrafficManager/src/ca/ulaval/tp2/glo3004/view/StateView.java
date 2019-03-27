package ca.ulaval.tp2.glo3004.view;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import ca.ulaval.tp2.glo3004.Direction;
import ca.ulaval.tp2.glo3004.car.Action;
import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.control.IntersectionType;

public class StateView extends JTextPane {

	private static final long serialVersionUID = 1L;
	private static DefaultStyledDocument document = new DefaultStyledDocument();
	private static Color PEDESTRIAN_TEXT_COLOR = Color.YELLOW;
	private static Color CROSS_TEXT_COLOR = Color.CYAN;
	private static Color THREE_WAY_TEXT_COLOR = Color.WHITE;

	public StateView() {
		super(document);
		this.setEditable(false);
		this.setBackground(Color.BLACK);
	}

	private void appendText(String text, Color color) {
		StyleContext context = new StyleContext();
		Style style = context.addStyle(text, null);
		StyleConstants.setForeground(style, color);
		try {
			document.insertString(0, text, style);
		} catch (BadLocationException e) {
			e.printStackTrace();
			return;
		}
	}

	public void displayPedestriansState(IntersectionType intersectionType) {
		String pedestrianState = String.format("\n 유 PEDESTRIANS::%s:GO \n", intersectionType);

		System.out.println("🚶 PEDESTRIANS::GO");

		appendText(pedestrianState, PEDESTRIAN_TEXT_COLOR);
	}

	public void displayCarState(Car car) {
		String carState = "";

		Direction direction = car.getDirection();
		IntersectionType intersectionType = car.getIntersectionType();
		IntersectionType nextIntersectionType = car.getNextIntersectionType();
		Action action = car.getCurrentAction();
		String typeCar = car.getTypeCar();

		if (nextIntersectionType == null || typeCar == null) {
			carState = String.format("⚑CAR:%s::%s:%s", direction, intersectionType, action);

		} else {
			Action previousAction = car.getPreviousAction();
			carState = String.format("⚑CAR:%s:%s::%s -> %s:%s", direction, intersectionType,
					previousAction, nextIntersectionType, action);
		}
		System.out.println(carState);

		Color intersectionColor = (isCross(intersectionType)) ? CROSS_TEXT_COLOR : THREE_WAY_TEXT_COLOR;
		appendText(String.format("\n %s \n", carState), intersectionColor);
	}

	private boolean isCross(IntersectionType intersectionType) {
		return intersectionType.equals(IntersectionType.CROSS);
	}

}

package ca.ulaval.tp2.glo3004.view;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import ca.ulaval.tp2.glo3004.car.Action;
import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.road.Direction;

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

	public void displayPedestrians() {
		String pedestrianState = String.format("\n PEDESTRIANS::GO \n");
		System.out.println("PEDESTRIANS::GO");
		appendText(pedestrianState, PEDESTRIAN_TEXT_COLOR);
	}

	public void displayCarState(Car car, boolean isSynchro) throws Exception {
		Direction direction = car.getDirection();
		IntersectionType intersectionType = car.getIntersectionType();
		IntersectionType nextIntersectionType = car.getNextIntersectionType();
		Action action = car.getCurrentAction();		
		StringBuilder carStateBuilder = new StringBuilder();
		carStateBuilder.append("\n");

		if (nextIntersectionType == null) {
			String singleMoveCarState = String.format("CAR:%s::%s:%s", 
													  direction, intersectionType, action);
			carStateBuilder.append(singleMoveCarState);

			if (isSynchro) {
				String endMoveFlag = (car.canMoveToNextIntersection()) ? "->NEXT" : "->END";
				carStateBuilder.append(endMoveFlag);
			}
		} else {
			Action previousAction = car.getPreviousAction();
			String doubleMoveCarState = String.format("CAR:%s:%s::%s -> %s:%s", 
													   direction, intersectionType,
													   previousAction, nextIntersectionType, action);
			carStateBuilder.append(doubleMoveCarState);
		}

		carStateBuilder.append("\n");

		System.out.println(carStateBuilder.toString());

		Color intersectionColor = (isCross(intersectionType)) ? CROSS_TEXT_COLOR : THREE_WAY_TEXT_COLOR;
		appendText(carStateBuilder.toString(), intersectionColor);
	}

	private boolean isCross(IntersectionType intersectionType) {
		return intersectionType.equals(IntersectionType.CROSS);
	}
	
	public void clearDocument() {
		this.setText("");
	}

}

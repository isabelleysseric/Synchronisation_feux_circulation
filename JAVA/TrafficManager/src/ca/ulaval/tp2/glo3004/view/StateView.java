package ca.ulaval.tp2.glo3004.view;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import ca.ulaval.tp2.glo3004.car.Car;
import ca.ulaval.tp2.glo3004.intersection.IntersectionType;

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

	public void displayPedestrianState(IntersectionType intersectionType) {
		String pedestrianState = String.format("\n %s:PEDESTRIANS::GO \n", intersectionType);
		System.out.println(pedestrianState);
		appendText(pedestrianState, PEDESTRIAN_TEXT_COLOR);
	}

	public void displayCarState(Car car, boolean isSynchro) throws Exception {
		
		IntersectionType intersectionType = car.getIntersectionType();
		StringBuilder carStateBuilder = new StringBuilder();
		
		carStateBuilder.append("\n");
 
		if(!isSynchro) {
			carStateBuilder.append(car.singleIntersectionFormat());
		}
		else {
			carStateBuilder.append(car.synchroIntersectionFormat());
		}
		
		
		carStateBuilder.append("\n");

		
		Color intersectionColor = (isCross(intersectionType)) ? CROSS_TEXT_COLOR : THREE_WAY_TEXT_COLOR;
		appendText(carStateBuilder.toString(), intersectionColor);
	}
	
	public void displayyncCarState(Car car) throws Exception {
		
		String carState = car.synchroIntersectionFormat();
		IntersectionType intersectionType = car.getIntersectionType();
		System.out.println(carState);

		Color intersectionColor = (isCross(intersectionType)) ? CROSS_TEXT_COLOR : THREE_WAY_TEXT_COLOR;
		appendText(carState.toString(), intersectionColor);
	}

	private boolean isCross(IntersectionType intersectionType) {
		return intersectionType.equals(IntersectionType.CROSS);
	}

	public void clearDocument() {
		this.setText("");
	}
	
}

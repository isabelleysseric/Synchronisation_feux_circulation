package ca.ulaval.tp2.glo3004.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import ca.ulaval.tp2.glo3004.ExecutionParameters;
import ca.ulaval.tp2.glo3004.controller.TraficController;
import ca.ulaval.tp2.glo3004.intersection.IntersectionType;
import ca.ulaval.tp2.glo3004.road.Direction;

public class MainView {

	private static int MIN_VALUE = 1;
	private static int STEP = 1;
	private static int MAX_VALUE = 1000;
	

	private static int FREQUENCY_STEP = 500;
	private static int MAX_FREQUENCY = 10000;
	
	private static IntersectionType[] CHOICE_INTERSECTION = { IntersectionType.THREE_WAY, IntersectionType.CROSS,
			IntersectionType.SYNCHRO };
	
	private static SpinnerModel CAR_DEFAULT_SPINNER_MODEL = new SpinnerNumberModel(MIN_VALUE, MIN_VALUE, MAX_VALUE, STEP);
	private static SpinnerModel PEDESTRIAN_DEFAULT_SPINNER_MODEL = new SpinnerNumberModel(MIN_VALUE, MIN_VALUE, MAX_VALUE, STEP);

	private static int EAST_DEFAULT_FREQUENCY = 1000;
	private static int WEST_DEFAULT_FREQUENCY = 2000;
	private static int SOUTH_DEFAULT_FREQUENCY = 2000;
	private static int NORTH_DEFAULT_FREQUENCY = 3000;
	
	private static SpinnerModel EAST_DEFAULT_SPINNER_MODEL = new SpinnerNumberModel(EAST_DEFAULT_FREQUENCY, MIN_VALUE, MAX_FREQUENCY, FREQUENCY_STEP);
	private static SpinnerModel WEST_DEFAULT_SPINNER_MODEL = new SpinnerNumberModel(WEST_DEFAULT_FREQUENCY, MIN_VALUE, MAX_FREQUENCY, FREQUENCY_STEP);
	private static SpinnerModel SOUTH_DEFAULT_SPINNER_MODEL = new SpinnerNumberModel(SOUTH_DEFAULT_FREQUENCY, MIN_VALUE, MAX_FREQUENCY, FREQUENCY_STEP);
	private static SpinnerModel NORTH_DEFAULT_SPINNER_MODEL = new SpinnerNumberModel(NORTH_DEFAULT_FREQUENCY, MIN_VALUE, MAX_FREQUENCY, FREQUENCY_STEP);

	private static LightView threeWayLightView;
	private static LightView crossLightView;

	private static StateView stateView = new StateView();
	private static List<Thread> threads;

	private JSpinner numberOfCarSpinner;
	private JSpinner numberOfPedestrianSpinner;
	
	private JSpinner eastFrequencySpinner;
	private JSpinner westFrequencySpinner;
	private JSpinner southFrequencySpinner;
	private JSpinner northFrequencySpinner;
	
	private JComboBox<IntersectionType> intersectionComboBox;
	private boolean appIsPaused;
	private boolean appIsRunning;
	
	private JButton pauseButtonGlobal;

	public MainView() {
		this.appIsPaused = false;
		this.appIsRunning = false;
		this.intersectionComboBox = new JComboBox<IntersectionType>(CHOICE_INTERSECTION);
		this.numberOfCarSpinner = new JSpinner(CAR_DEFAULT_SPINNER_MODEL);
		this.numberOfPedestrianSpinner = new JSpinner(PEDESTRIAN_DEFAULT_SPINNER_MODEL);
		
		this.eastFrequencySpinner = new JSpinner(EAST_DEFAULT_SPINNER_MODEL);
		
		this.westFrequencySpinner = new JSpinner(WEST_DEFAULT_SPINNER_MODEL);
		this.southFrequencySpinner = new JSpinner(SOUTH_DEFAULT_SPINNER_MODEL);
		this.northFrequencySpinner = new JSpinner(NORTH_DEFAULT_SPINNER_MODEL);
		
		
		threeWayLightView = new LightView(IntersectionType.THREE_WAY);
		crossLightView = new LightView(IntersectionType.CROSS);
	}
	
	public void render() {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(30, 30, 1100, 700);

		BorderLayout mainContent = new BorderLayout();
		JPanel lightsPanel = createBothIntersectionPanels();
		JScrollPane stateViewScroll = new JScrollPane(stateView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JPanel actionPanel = createActionPanel();

		JPanel userChoicePanel = createUserChoicePanel();

		userChoicePanel.setBackground(Color.DARK_GRAY);
		
		window.getContentPane().setLayout(mainContent);
		window.getContentPane().add(actionPanel, BorderLayout.PAGE_START);
		window.getContentPane().add(lightsPanel, BorderLayout.LINE_START);
		window.getContentPane().add(userChoicePanel, BorderLayout.LINE_END);
		window.getContentPane().add(stateViewScroll, BorderLayout.CENTER);

		window.setVisible(true);
	}

	public void startExecution() {

		IntersectionType intersectionType = (IntersectionType) this.intersectionComboBox.getSelectedItem();
		
		ExecutionParameters parameters = this.getExecutionParameters();

		
		/*parameters.addLightFrequency(Direction.WEST, 2000);
		parameters.addLightFrequency(Direction.EAST, 1000);
		parameters.addLightFrequency(Direction.SOUTH, 2000);
		parameters.addLightFrequency(Direction.NORTH, 3000);*/
		
		/*OldIntersectionControllerFactory controllerFactory = new OldIntersectionControllerFactory(threeWayLightView,
				crossLightView, stateView);*/
		
		TraficController controllerFactory = new TraficController(threeWayLightView,
				crossLightView, stateView);

		threads = controllerFactory.createIntersectionControllerThreads(intersectionType, parameters);

		threads.forEach(thread -> thread.start());

		appIsRunning = true;
	}

	private ExecutionParameters getExecutionParameters() {
		int numberOfCars = (Integer) this.numberOfCarSpinner.getValue();
		int numberOfPedestrians = (Integer) this.numberOfPedestrianSpinner.getValue();
		int eastFrequency = (Integer) this.eastFrequencySpinner.getValue();
		int westFrequency = (Integer) this.westFrequencySpinner.getValue();
		int northFrequency = (Integer) this.northFrequencySpinner.getValue();
		int southFrequency = (Integer) this.southFrequencySpinner.getValue();
		
		ExecutionParameters parameters = new ExecutionParameters(numberOfCars, numberOfPedestrians);

		
		parameters.addLightFrequency(Direction.WEST, westFrequency);
		parameters.addLightFrequency(Direction.EAST, eastFrequency);
		parameters.addLightFrequency(Direction.SOUTH, southFrequency);
		parameters.addLightFrequency(Direction.NORTH, northFrequency);
		
		
		return parameters;
	}
	
	private JPanel createBothIntersectionPanels() {
		GridLayout lightLayout = new GridLayout(2, 0, 30, 20);

		JPanel lightsPanel = new JPanel();
		JPanel threeWayPanel = createIntersectionPanel(threeWayLightView);
		JPanel crossPanel = createIntersectionPanel(crossLightView);
	
		lightsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		lightsPanel.setLayout(lightLayout);
		lightsPanel.setBackground(Color.DARK_GRAY);

		lightsPanel.add(threeWayPanel);
		lightsPanel.add(crossPanel);

		return lightsPanel;
	}

	private JPanel createUserChoicePanel() {

		JPanel choicePanel = new JPanel();

		choicePanel.setBorder(new EmptyBorder(30, 10, 10, 10));
		
		BorderLayout borderLayout = new BorderLayout();
		choicePanel.setLayout(borderLayout);

		JPanel parameterPanel = createParameterPanel();

		choicePanel.add(parameterPanel, BorderLayout.CENTER);

		return choicePanel;
	}

	private JPanel createParameterPanel() {

		JPanel parametersPanel = new JPanel();
		parametersPanel.setBackground(Color.DARK_GRAY);
		parametersPanel.setForeground(Color.WHITE);

		GridLayout parameterLayout = new GridLayout(10, 1, 30, 20);

		parametersPanel.setLayout(parameterLayout);

		JLabel intersectionLabel = createLabel("Which intersection do you want to run?");
		JPanel choiceIntersection = createSubChoicePanel();
		choiceIntersection.add(intersectionLabel, BorderLayout.PAGE_START);
		choiceIntersection.add(intersectionComboBox, BorderLayout.CENTER);

		JLabel numberOfCarLabel = createLabel("How many cars do you want ?");
		JPanel choiceCar = createSubChoicePanel();
		choiceCar.add(numberOfCarLabel, BorderLayout.PAGE_START);
		choiceCar.add(numberOfCarSpinner, BorderLayout.CENTER);

		JLabel numberOfPedestrianLabel = createLabel("How many pedestrians do you want ? ");
		JPanel choicePedestrian = createSubChoicePanel();
		choicePedestrian.add(numberOfPedestrianLabel, BorderLayout.PAGE_START);
		choicePedestrian.add(numberOfPedestrianSpinner, BorderLayout.CENTER);

		JLabel frequencyOfEastLightLabel = createLabel("East light frequency (ms) ");
		JPanel frequencyOfEastLightPanel = createSubChoicePanel();
		frequencyOfEastLightPanel.add(frequencyOfEastLightLabel, BorderLayout.PAGE_START);
		frequencyOfEastLightPanel.add(eastFrequencySpinner, BorderLayout.CENTER);
		
		JLabel frequencyOfWestLightLabel = createLabel("West light frequency (ms) ");
		JPanel frequencyOfWestLightPanel = createSubChoicePanel();
		frequencyOfWestLightPanel.add(frequencyOfWestLightLabel, BorderLayout.PAGE_START);
		frequencyOfWestLightPanel.add(westFrequencySpinner, BorderLayout.CENTER);

		JLabel frequencyOfSouthLightLabel = createLabel("South light frequency (ms) ");
		JPanel frequencyOfSouthLightPanel = createSubChoicePanel();
		frequencyOfSouthLightPanel.add(frequencyOfSouthLightLabel, BorderLayout.PAGE_START);
		frequencyOfSouthLightPanel.add(southFrequencySpinner, BorderLayout.CENTER);


		JLabel frequencyOfNorthLightLabel = createLabel("North light frequency (ms) ");
		JPanel frequencyOfNorthLightPanel = createSubChoicePanel();
		frequencyOfNorthLightPanel.add(frequencyOfNorthLightLabel, BorderLayout.PAGE_START);
		frequencyOfNorthLightPanel.add(northFrequencySpinner, BorderLayout.CENTER);

		parametersPanel.add(choiceIntersection);
		parametersPanel.add(choiceCar);
		parametersPanel.add(choicePedestrian);
		
		parametersPanel.add(frequencyOfEastLightPanel);
		parametersPanel.add(frequencyOfWestLightPanel);
		parametersPanel.add(frequencyOfNorthLightPanel);
		parametersPanel.add(frequencyOfSouthLightPanel);

		return parametersPanel;
	}

	private JLabel createLabel(String message) {
		JLabel label = new JLabel(message);
		label.setForeground(Color.WHITE);

		return label;
	}

	private JPanel createSubChoicePanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		panel.setLayout(new BorderLayout());

		return panel;
	}

	private JPanel createIntersectionPanel(LightView lightComponent) {

		JPanel intersectionPanel = new JPanel();

		intersectionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		intersectionPanel.setBackground(Color.BLACK);

		BorderLayout borderLayout = new BorderLayout();
		intersectionPanel.setLayout(borderLayout);

		String intersectionName = lightComponent.getName();
		JLabel intersectionLabel = new JLabel(intersectionName);
		intersectionLabel.setForeground(Color.WHITE);
		
		
		intersectionPanel.add(intersectionLabel, BorderLayout.PAGE_START);
		intersectionPanel.add(lightComponent, BorderLayout.CENTER);

		return intersectionPanel;
	}

	private JPanel createActionPanel() {
		JPanel actionPanel = new JPanel();
		actionPanel.setBackground(Color.LIGHT_GRAY);

		JButton pauseButton = createPauseButton();
		JButton startButton = createStartButton();
		JButton quitButton = createQuitButton();

		actionPanel.add(pauseButton);
		actionPanel.add(startButton);
		actionPanel.add(quitButton);

		return actionPanel;
	}

	private JButton createStartButton() {

		JButton startButton = new JButton("START");

		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (!appIsRunning) {
					startExecution();
					startButton.setText("RESTART");
				} else {
					appIsRunning = false;
					appIsPaused = false;
					restartExecution();
					startButton.setText("START");
					pauseButtonGlobal.setText("PAUSE");
				}

			}
		});
		return startButton;
	}

	private JButton createPauseButton() {

		JButton pauseButton = new JButton("PAUSE");
		pauseButtonGlobal = pauseButton;

		pauseButton.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!appIsPaused) {
					appIsPaused = true;
					pauseExecution();
					pauseButton.setText("RESUME");
				} else {
					appIsPaused = false;
					resumeExecution();
					pauseButton.setText("PAUSE");
				}
			}
		});
		return pauseButton;
	}

	private void pauseExecution() {
		System.out.println(" \n" + "**************************************** \n" + "MESSAGE: Interrupting threads \n"
				+ "ACTION:  PAUSE \n" + "Click on \"Resume\" to continue \n"
				+ "**************************************** \n" + "\n");
		threads.forEach(thread -> thread.suspend());
	}

	private void resumeExecution() {
		System.out.println(" \n" + "**************************************** \n" + "MESSAGE: Interrupting threads \n"
				+ "ACTION:  PAUSE \n" + "Click on \"unpaused\" to continue \n"
				+ "**************************************** \n" + "\n");
		threads.forEach(thread -> thread.resume());
	}


	private void restartExecution() {
		System.out.println(" \n" + "**************************************** \n" + "MESSAGE: Interrupting threads \n"
				+ "ACTION: RESTART \n" + "We start again !\n" + "**************************************** \n");

		threads.forEach(thread -> thread.interrupt());
		threads.clear();
		reinitializeViews();
		
		System.console();
	}

	public void reinitializeViews() {
		resetLightView(threeWayLightView);
		resetLightView(crossLightView);
		
		stateView.clearDocument();
	}
	
	private void resetLightView(LightView lightView) {
		lightView.reinitialize();
		lightView.repaint();
	}

	private JButton createQuitButton() {
		JButton quitButton = new JButton("QUIT");

		quitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.print(" \n" + "**************************************** \n"
						+ "MESSAGE: The list of threads is empty  \n" + "ACTION:  QUIT \n" + "See you next time !\n"
						+ "**************************************** \n");
				threads.forEach(thread -> thread.interrupt());
				threads.clear();
				System.exit(0);
			}
		});

		return quitButton;
	}

	
	
}

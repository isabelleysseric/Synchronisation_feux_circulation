package ca.ulaval.tp2.glo3004.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import ca.ulaval.tp2.glo3004.ExecutionParameters;
import ca.ulaval.tp2.glo3004.control.IntersectionControllerFactory;
import ca.ulaval.tp2.glo3004.intersection.IntersectionType;

public class MainView {

	private static int MIN_VALUE = 1;
	private static int STEP = 1;
	private static int MAX_VALUE = 1000;
	private static IntersectionType[] CHOICE_INTERSECTION = { IntersectionType.THREE_WAY, IntersectionType.CROSS, IntersectionType.SYNCHRO };

	private static LightView threeWayLightView = new LightView(IntersectionType.THREE_WAY);
	private static LightView crossLightView = new LightView(IntersectionType.CROSS);
	
	private static StateView stateView = new StateView();
	private static List<Thread> threads;
	
	private JSpinner numberOfCarSpinner ;
	private JSpinner numberOfPedestrianSpinner;
	private JComboBox<IntersectionType> intersectionComboBox;
	 
	
	public MainView() {
		this.intersectionComboBox = new JComboBox<IntersectionType>(CHOICE_INTERSECTION);
		this.numberOfCarSpinner = new JSpinner(new SpinnerNumberModel(MIN_VALUE, MIN_VALUE, MAX_VALUE, STEP));
		this.numberOfPedestrianSpinner = new JSpinner(new SpinnerNumberModel(MIN_VALUE, MIN_VALUE, MAX_VALUE, STEP));
	}

	public void initialize() {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(30, 30, 900, 700);
		
		BorderLayout mainContent = new BorderLayout();
		JPanel lightsPanel = createBothIntersectionPanels();
		JScrollPane stateViewScroll = new JScrollPane(stateView, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JPanel actionPanel = createActionPanel();

		window.getContentPane().setLayout(mainContent);
		window.getContentPane().add(actionPanel, BorderLayout.PAGE_START);
		window.getContentPane().add(lightsPanel, BorderLayout.LINE_START);
		window.getContentPane().add(stateViewScroll, BorderLayout.CENTER);

		window.setVisible(true);
	}

	public void startExecution() {

		IntersectionType intersectionType = (IntersectionType) this.intersectionComboBox.getSelectedItem();
		int numberOfCars = (Integer) this.numberOfCarSpinner.getValue();
		int numberOfPedestrians = (Integer) this.numberOfPedestrianSpinner.getValue();
		ExecutionParameters parameters = new ExecutionParameters(numberOfCars, numberOfPedestrians);
		
		IntersectionControllerFactory controllerFactory = new IntersectionControllerFactory(
				threeWayLightView,
				crossLightView, stateView);

		threads = controllerFactory.createIntersectionControllerThreads(
				intersectionType, 
				parameters);

		threads.forEach(thread -> thread.start());
	}

	private JPanel createBothIntersectionPanels() {
		GridLayout lightLayout = new GridLayout(3, 0, 30, 20);		
		
		JPanel lightsPanel = new JPanel();
		JPanel threeWayPanel = createIntersectionPanel(threeWayLightView);
		JPanel crossPanel = createIntersectionPanel(crossLightView);
		JPanel userChoicePanel = createUserChoicePanel();
		
		
		lightsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		lightsPanel.setLayout(lightLayout);
		lightsPanel.setBackground(Color.DARK_GRAY);
		
		lightsPanel.add(userChoicePanel);
		lightsPanel.add(threeWayPanel);
		lightsPanel.add(crossPanel);

		return lightsPanel;
	}

	private JPanel createUserChoicePanel() {

		JPanel choicePanel = new JPanel();
		
		
		choicePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		choicePanel.setBackground(Color.BLACK);

		
		BorderLayout borderLayout = new BorderLayout();
		choicePanel.setLayout(borderLayout);
		
		
		JPanel parameterPanel =  createParameterPanel();
		
		choicePanel.add(parameterPanel, BorderLayout.CENTER);
		
		return choicePanel;
	}
	
	
	private JPanel createParameterPanel() {

		JPanel parametersPanel = new JPanel();
		parametersPanel.setBackground(Color.BLACK);
		parametersPanel.setForeground(Color.WHITE);
		
		GridLayout parameterLayout = new GridLayout(3, 2, 30, 20);		
		
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
	

		parametersPanel.add(choiceIntersection);
		parametersPanel.add(choiceCar);
		parametersPanel.add(choicePedestrian);

		
		return parametersPanel;
	}
	
	private JLabel createLabel(String message) {
		JLabel label = new JLabel(message);
		label.setForeground(Color.WHITE);
		
		return label;
	}
	
	private JPanel createSubChoicePanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
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

		JButton pauseButton    = createPauseButton();
		JButton unpausedButton = createUnPausedButton();
		JButton restartButton  = createRestartButton();
		JButton startButton    = createStartButton();
		JButton quitButton     = createQuitButton();

		actionPanel.add(pauseButton);
		actionPanel.add(unpausedButton);
		actionPanel.add(startButton);
		actionPanel.add(restartButton);
		actionPanel.add(quitButton);

		return actionPanel;
	}

private JButton createStartButton() {
		
		JButton startButton = new JButton("START");

		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				startExecution();
				
			}
		});
		return startButton;
	}
	
	private JButton createPauseButton() {
		
		JButton pauseButton = new JButton("PAUSE");

		pauseButton.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(" \n"
								 + "**************************************** \n"
				 		 		 + "MESSAGE: Interrupting threads \n"
						 		 + "ACTION:  PAUSE \n"
						 		 + "Click on \"unpaused\" to continue \n"
						 		 + "**************************************** \n"
						 		 + "\n");
				threads.forEach(thread -> thread.suspend());
			}
		});
		return pauseButton;
	}
	
	private JButton createUnPausedButton() {
		JButton unpausedButton = new JButton("UNPAUSED");
		unpausedButton.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				
				System.out.println(" \n"
								 + "**************************************** \n"
						 		 + "MESSAGE: Interrupting threads \n"
								 + "ACTION: UNPAUSED \n"
						         + "We continue...   \n"   
								 + "**************************************** \n" 
						         + "\n");
				threads.forEach(thread -> thread.resume());
			}
		});
		return unpausedButton;
	}
	
	private JButton createRestartButton() {
		JButton restartButton = new JButton("RESTART");

		restartButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(" \n"
								 + "**************************************** \n"
								 + "MESSAGE: Interrupting threads \n"
							 	 + "ACTION: RESTART \n"
						 		 + "We start again !\n"
						 		 + "**************************************** \n");
				threads.clear();
				System.console ();
			}
		});
		return  restartButton;
	}

	private JButton createQuitButton() {
		JButton quitButton = new JButton("QUIT");

		quitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.print(" \n"
								 + "**************************************** \n"
						 		 + "MESSAGE: The list of threads is empty  \n"
								 + "ACTION:  QUIT \n"
				         		 + "See you next time !\n"
								 + "**************************************** \n");
				threads.clear();
				System.exit(0);
			}
		});

		return quitButton;
	}

	@SuppressWarnings("unused")
	private void resetLightView(LightView lightView) {
		lightView.reinitialize();
		lightView.repaint();
	}
}

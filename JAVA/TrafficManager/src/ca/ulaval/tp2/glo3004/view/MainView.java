package ca.ulaval.tp2.glo3004.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import ca.ulaval.tp2.glo3004.ExecutionParameters;
import ca.ulaval.tp2.glo3004.control.IntersectionControllerFactory;
import ca.ulaval.tp2.glo3004.intersection.IntersectionType;

public class MainView {

	private static LightView threeWayLightView;
	private static LightView crossLightView;
	private static StateView stateView = new StateView();

	private static List<Thread> threads;

	private ExecutionParameters parameters;
	private IntersectionType intersectionType;
	
	public MainView(IntersectionType intersectionType, ExecutionParameters parameters) {
		this.parameters = parameters;
		this.intersectionType = intersectionType;
		threeWayLightView = new LightView(IntersectionType.THREE_WAY);
		crossLightView = new LightView(IntersectionType.CROSS);
	}

	public void initialize() {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(30, 30, 900, 700);

		BorderLayout mainContent = new BorderLayout();

		JPanel lightsPanel = createBothIntersectionPanels();

		JScrollPane stateViewScroll = new JScrollPane(stateView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		JPanel actionPanel = createActionPanel();

		window.getContentPane().setLayout(mainContent);
		window.getContentPane().add(actionPanel, BorderLayout.PAGE_START);
		window.getContentPane().add(lightsPanel, BorderLayout.LINE_START);
		window.getContentPane().add(stateViewScroll, BorderLayout.CENTER);

		window.setVisible(true);
	}

	public void startExecution() {

		IntersectionControllerFactory controllerFactory = new IntersectionControllerFactory(threeWayLightView,
				crossLightView, stateView);

		threads = controllerFactory.createIntersectionControllerThreads(intersectionType, parameters);

		this.initialize();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threads.forEach(thread -> thread.start());

	}

	private JPanel createBothIntersectionPanels() {
		GridLayout lightLayout = new GridLayout(2, 0, 30, 20);
		
		JPanel lightsPanel = new JPanel();
		lightsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		lightsPanel.setLayout(lightLayout);
		lightsPanel.setBackground(Color.DARK_GRAY);

		JPanel threeWayPanel = createIntersectionPanel(threeWayLightView);
		JPanel crossPanel = createIntersectionPanel(crossLightView);

		lightsPanel.add(threeWayPanel);
		lightsPanel.add(crossPanel);

		return lightsPanel;
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
		JButton quitButton     = createQuitButton();

		actionPanel.add(pauseButton);
		actionPanel.add(unpausedButton);
		actionPanel.add(restartButton);
		actionPanel.add(quitButton);

		return actionPanel;
	}

	private JButton createPauseButton() {
		
		JButton pauseButton = new JButton("PAUSE");

		pauseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(" \n"
								 + "**************************************** \n"
				 		 		 + "MESSAGE: Interuption des threads \n"
						 		 + "ACTION:  PAUSE \n"
						 		 + "Cliquer sur \"unpaused\" pour continuer  \n"
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

			public void actionPerformed(ActionEvent e) {
				
				System.out.println(" \n"
								 + "**************************************** \n"
						 		 + "MESSAGE: Interuption des threads \n"
								 + "ACTION: UNPAUSED \n"
						         + "On continue...   \n"   
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
								 + "MESSAGE: Interuption des threads \n"
							 	 + "ACTION: RESTART \n"
						 		 + "We start again !\n"
						 		 + "**************************************** \n");
				threads.clear();	
				System.console ();
			}
		});

		return restartButton;
	}
	private JButton createQuitButton() {
		JButton quitButton = new JButton("QUIT");

		quitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.print(" \n"
								 + "**************************************** \n"
						 		 + "MESSAGE: La liste des threads est vid�e  \n"
								 + "ACTION:  QUIT \n"
				         		 + "See you next time !\n"
								 + "**************************************** \n");
				threads.clear();
				System.exit(0);
			}
		});

		return quitButton;
	}

	private void resetLightView(LightView lightView) {
		lightView.reinitialize();
		lightView.repaint();
	}
}

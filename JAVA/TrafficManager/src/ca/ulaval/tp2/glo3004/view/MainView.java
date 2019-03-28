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

import ca.ulaval.tp2.glo3004.control.ExecutionParameters;
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
		window.setBounds(30, 30, 700, 500);

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

		JButton stopButton = createStopButton();
		JButton restartButton = createRestartButton();

		actionPanel.add(stopButton);
		actionPanel.add(restartButton);

		return actionPanel;
	}

	private JButton createStopButton() {
		JButton stopButton = new JButton("STOP");

		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				threads.forEach(thread -> thread.interrupt());
				initialize();
				resetLightView(threeWayLightView);
				resetLightView(crossLightView);

			}
		});

		return stopButton;
	}

	private void resetLightView(LightView lightView) {
		lightView.reinitialize();
		lightView.repaint();
	}

	private JButton createRestartButton() {
		JButton stopButton = new JButton("RESTART");

		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				startExecution();
			}
		});

		return stopButton;
	}

}

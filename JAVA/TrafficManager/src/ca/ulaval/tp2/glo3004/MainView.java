package ca.ulaval.tp2.glo3004;

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
import javax.swing.border.EmptyBorder;

import ca.ulaval.tp2.glo3004.control.ExecutionParameters;
import ca.ulaval.tp2.glo3004.control.IntersectionType;

public class MainView {

	private static LightView threeWayLightComponent = new LightView();
	private static LightView crossLightComponent = new LightView();

	private static List<Thread> threads;

	private ExecutionParameters parameters;
	private IntersectionType intersectionType;

	public MainView(IntersectionType intersectionType, ExecutionParameters parameters) {
		this.parameters = parameters;
		this.intersectionType = intersectionType;
	}

	public void initialize() {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(30, 30, 700, 450);

		GridLayout mainContent = new GridLayout(0, 2);

		JPanel lightsPanel = createBothIntersectionPanels();
		JPanel actionPanel = createActionPanel();

		window.getContentPane().setLayout(mainContent);
		window.getContentPane().add(lightsPanel);
		window.getContentPane().add(actionPanel);

		window.setVisible(true);
	}

	public void startExecution() {

		IntersectionControllerFactory controllerFactory = new IntersectionControllerFactory(threeWayLightComponent,
				crossLightComponent);

		threads = controllerFactory.createIntersectionControllerThreads(intersectionType, parameters);

		threads.forEach(thread -> thread.start());

		this.initialize();
	}

	private JPanel createBothIntersectionPanels() {
		GridLayout lightLayout = new GridLayout(2, 0, 30, 20);

		JPanel lightsPanel = new JPanel();
		lightsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		lightsPanel.setLayout(lightLayout);

		JPanel threeWayPanel = createIntersectionPanel("THREE-WAY INTERSECTION", threeWayLightComponent);
		JPanel crossPanel = createIntersectionPanel("CROSS INTERSECTION", crossLightComponent);

		lightsPanel.add(threeWayPanel);
		lightsPanel.add(crossPanel);

		return lightsPanel;
	}

	private JPanel createIntersectionPanel(String intersectionName, LightView lightComponent) {

		JPanel intersectionPanel = new JPanel();
		intersectionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		intersectionPanel.setBackground(Color.WHITE);

		BorderLayout borderLayout = new BorderLayout();
		intersectionPanel.setLayout(borderLayout);

		intersectionPanel.add(new JLabel(intersectionName), BorderLayout.PAGE_START);
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
				resetLightView(threeWayLightComponent);
				resetLightView(crossLightComponent);

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

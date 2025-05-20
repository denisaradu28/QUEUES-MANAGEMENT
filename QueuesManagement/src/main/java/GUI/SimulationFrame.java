package GUI;

import BusinessLogic.SimulationManager;

import javax.swing.*;
import java.awt.*;

public class SimulationFrame extends JFrame {

    private JTextField clientField;
    private JTextField queueField;
    private JTextField simulationTimeField;
    private JTextField minArrivalField;
    private JTextField maxArrivalField;
    private JTextField minServiceField;
    private JTextField maxServiceField;

    private JComboBox<String> strategyComboBox;
    private JButton startButton;

    public SimulationFrame() {
        setTitle("Queue Simulation");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));

        clientField = new JTextField();
        queueField = new JTextField();
        simulationTimeField = new JTextField();
        minArrivalField = new JTextField();
        maxArrivalField = new JTextField();
        minServiceField = new JTextField();
        maxServiceField = new JTextField();
        strategyComboBox = new JComboBox<>(new String[]{"SHORTEST_QUEUE", "SHORTEST_TIME"});
        startButton = new JButton("Start Simulation");

        inputPanel.add(new JLabel("Number of clients:"));
        inputPanel.add(clientField);
        inputPanel.add(new JLabel("Number of queues:"));
        inputPanel.add(queueField);
        inputPanel.add(new JLabel("Simulation time:"));
        inputPanel.add(simulationTimeField);
        inputPanel.add(new JLabel("Min arrival time:"));
        inputPanel.add(minArrivalField);
        inputPanel.add(new JLabel("Max arrival time:"));
        inputPanel.add(maxArrivalField);
        inputPanel.add(new JLabel("Min service time:"));
        inputPanel.add(minServiceField);
        inputPanel.add(new JLabel("Max service time:"));
        inputPanel.add(maxServiceField);
        inputPanel.add(new JLabel("Strategy:"));
        inputPanel.add(strategyComboBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        startButton.addActionListener(e -> {
            try {
                int clients = Integer.parseInt(clientField.getText());
                int queues = Integer.parseInt(queueField.getText());
                int simulationTime = Integer.parseInt(simulationTimeField.getText());
                int minArrival = Integer.parseInt(minArrivalField.getText());
                int maxArrival = Integer.parseInt(maxArrivalField.getText());
                int minService = Integer.parseInt(minServiceField.getText());
                int maxService = Integer.parseInt(maxServiceField.getText());
                String strategy = (String) strategyComboBox.getSelectedItem();

                AnimationFrame animationFrame = new AnimationFrame(clients, queues);
                animationFrame.setVisible(true);

                SimulationManager manager = new SimulationManager(
                        simulationTime, queues, clients,
                        minArrival, maxArrival,
                        minService, maxService,
                        strategy, animationFrame
                );
                Thread simulationThread = new Thread(manager);
                simulationThread.start();
            }catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number");
            }
        });

    }
}

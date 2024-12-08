package proyecto.gui;

import proyect.Server;

import javax.swing.*;
import java.awt.*;

public class ServerGUI extends JFrame {
    private JLabel ipLabel;
    private JLabel portLabel;
    private JLabel maxQueueLabel;
    private JLabel queueSizeLabel;
    private JLabel statusLabel;
    private JButton startButton;
    private JButton stopButton;
    private JTextField maxQueueField;
    private Server server;

    public ServerGUI(String ip, int port, int maxQueueSize) {
        setTitle("SERVIDOR - Control");
        setLayout(new GridLayout(7, 1, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ipLabel = new JLabel("DIRECCIÓN IP: " + ip);
        ipLabel.setFont(new Font("Arial", Font.BOLD, 14));
        ipLabel.setForeground(Color.ORANGE);
        add(ipLabel);

        portLabel = new JLabel("PUERTO: " + port);
        portLabel.setFont(new Font("Arial", Font.BOLD, 14));
        portLabel.setForeground(Color.ORANGE);
        add(portLabel);

        maxQueueLabel = new JLabel("TAMAÑO MÁXIMO DE LA COLA: " + maxQueueSize);
        maxQueueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        maxQueueLabel.setForeground(Color.ORANGE);
        add(maxQueueLabel);

        queueSizeLabel = new JLabel("0");
        queueSizeLabel.setFont(new Font("Arial", Font.BOLD, 48));
        queueSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        queueSizeLabel.setForeground(Color.BLUE);
        add(queueSizeLabel);

        JPanel maxQueuePanel = new JPanel();
        maxQueuePanel.setLayout(new FlowLayout());
        maxQueuePanel.add(new JLabel("Nuevo Tamaño Máximo:"));
        maxQueueField = new JTextField(5);
        maxQueuePanel.add(maxQueueField);
        JButton updateQueueSizeButton = new JButton("Actualizar");
        updateQueueSizeButton.addActionListener(e -> updateMaxQueueSize());
        maxQueuePanel.add(updateQueueSizeButton);
        add(maxQueuePanel);

        statusLabel = new JLabel("Estado: Servidor detenido.");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        add(statusLabel);

        JPanel controlPanel = new JPanel();
        startButton = new JButton("Iniciar Servidor");
        stopButton = new JButton("Detener Servidor");
        stopButton.setEnabled(false);

        startButton.addActionListener(e -> startServer(ip, port, maxQueueSize));
        stopButton.addActionListener(e -> stopServer());

        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        add(controlPanel);

        setSize(400, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startServer(String ip, int port, int maxQueueSize) {
        if (server == null) {
            server = new Server(this, port, maxQueueSize);
            server.start();
            statusLabel.setText("Estado: Servidor en ejecución.");
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        }
    }

    private void stopServer() {
        if (server != null) {
            server.stop();
            server = null;
            statusLabel.setText("Estado: Servidor detenido.");
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
    }

    public void updateQueueSize(int size) {
        queueSizeLabel.setText(String.valueOf(size));
    }

    public void updateMaxQueueSizeLabel(int size) {
        maxQueueLabel.setText("TAMAÑO MÁXIMO DE LA COLA: " + size);
    }

    private void updateMaxQueueSize() {
        try {
            int newSize = Integer.parseInt(maxQueueField.getText());
            if (server != null) {
                server.updateMaxQueueSize(newSize);
                JOptionPane.showMessageDialog(this, "Tamaño máximo actualizado a: " + newSize);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateStatus(String message) {
        statusLabel.setText("Estado: " + message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ServerGUI("127.0.0.1", 12345, 10));
    }
}

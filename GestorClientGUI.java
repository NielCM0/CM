package proyecto.gui;

import proyect.GestorClient;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestorClientGUI extends JFrame {
    private JLabel queueSizeLabel;
    private JLabel averageTimeLabel;
    private JButton refreshButton;
    private List<Integer> attentionTimes; 

    public GestorClientGUI() {
        setTitle("Gestor de Cola");
        setLayout(new GridLayout(4, 1, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        attentionTimes = new ArrayList<>();

        queueSizeLabel = new JLabel("Tamaño de la Cola: 0", SwingConstants.CENTER);
        queueSizeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(queueSizeLabel);

        averageTimeLabel = new JLabel("Tiempo Promedio de Atención: 0 minutos", SwingConstants.CENTER);
        averageTimeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(averageTimeLabel);

        refreshButton = new JButton("Actualizar Datos");
        refreshButton.addActionListener(e -> refreshData());
        add(refreshButton);

        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(e -> dispose());
        add(closeButton);

        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void refreshData() {
        try {
            GestorClient client = new GestorClient();

            String queueSize = client.getQueueSize();
            queueSizeLabel.setText("Tamaño de la Cola: " + queueSize);

            double averageTime = calculateAverageAttentionTime();
            averageTimeLabel.setText(String.format("Tiempo Promedio de Atención: %.2f minutos", averageTime));

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addAttentionTime(int minutes) {
        attentionTimes.add(minutes);
        refreshData(); 
    }

    private double calculateAverageAttentionTime() {
        if (attentionTimes.isEmpty()) {
            return 0.0;
        }
        return attentionTimes.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GestorClientGUI::new);
    }
}

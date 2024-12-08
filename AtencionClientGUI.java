package proyecto.gui;

import proyect.AtencionClient;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class AtencionClientGUI extends JFrame {
    private static final AtomicInteger VENTANILLA_COUNTER = new AtomicInteger(1);
    private int ventanillaNumber;
    private JLabel ventanillaLabel;
    private JLabel responseLabel;
    private JButton attendButton;
    private long startTime;
    private boolean isAttending;
    private GestorClientGUI gestorGUI;

    public AtencionClientGUI(GestorClientGUI gestorGUI) {
        this.gestorGUI = gestorGUI; 
        ventanillaNumber = VENTANILLA_COUNTER.getAndIncrement();

        setTitle("Atención al Cliente - Ventanilla " + ventanillaNumber);
        setLayout(new GridLayout(5, 1, 10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ventanillaLabel = new JLabel("Ventanilla " + ventanillaNumber, SwingConstants.CENTER);
        ventanillaLabel.setFont(new Font("Arial", Font.BOLD, 18));
        ventanillaLabel.setForeground(Color.BLUE);
        add(ventanillaLabel);

        attendButton = new JButton("Atender Cliente");
        attendButton.addActionListener(e -> atenderCliente());
        add(attendButton);

        responseLabel = new JLabel("Esperando cliente...", SwingConstants.CENTER);
        responseLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        responseLabel.setForeground(Color.BLACK);
        add(responseLabel);

        JButton closeButton = new JButton("Cerrar Ventanilla");
        closeButton.addActionListener(e -> dispose());
        add(closeButton);

        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        isAttending = false;
    }

    private void atenderCliente() {
        try {
            AtencionClient client = new AtencionClient();

            if (!isAttending) {
              
                startTime = System.currentTimeMillis();
                String response = client.attendNext();

                if (response.equals("NO_CLIENT")) {
                    responseLabel.setText("No hay clientes en la cola.");
                    responseLabel.setForeground(Color.RED);
                } else {
                    responseLabel.setText("Atendiendo a DNI: " + response);
                    responseLabel.setForeground(Color.ORANGE);
                    isAttending = true;
                    attendButton.setText("Finalizar Atención");
                }
            } else {
                
                long endTime = System.currentTimeMillis();
                int minutes = (int) ((endTime - startTime) / 60000);
                if (minutes == 0) minutes = 1;

                responseLabel.setText("Cliente atendido en " + minutes + " minutos.");
                responseLabel.setForeground(Color.GREEN);
                isAttending = false;
                attendButton.setText("Atender Cliente");

              
                if (gestorGUI != null) {
                    gestorGUI.addAttentionTime(minutes);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        
        GestorClientGUI gestorGUI = new GestorClientGUI();
        SwingUtilities.invokeLater(() -> new AtencionClientGUI(gestorGUI));
    }
}

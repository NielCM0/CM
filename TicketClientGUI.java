package proyecto.gui;

import proyect.TicketClient;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TicketClientGUI extends JFrame {
    private JTextField dniField;
    private JLabel responseLabel;
    private JLabel personDataLabel;
    private JButton generateTicketButton;

    public TicketClientGUI() {
        setTitle("Cliente - Tickets");
        setLayout(new GridLayout(5, 1, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("Pase su DNI por el lector:", SwingConstants.CENTER));

        dniField = new JTextField();
        dniField.setHorizontalAlignment(SwingConstants.CENTER);
        add(dniField);

        responseLabel = new JLabel("Ingrese su DNI para generar un ticket.");
        responseLabel.setHorizontalAlignment(SwingConstants.CENTER);
        responseLabel.setFont(new Font("Arial", Font.BOLD, 14));
        responseLabel.setForeground(Color.BLACK);
        add(responseLabel);

        personDataLabel = new JLabel("");
        personDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
        personDataLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        personDataLabel.setForeground(Color.BLUE);
        add(personDataLabel);

        generateTicketButton = new JButton("Generar Ticket");
        generateTicketButton.addActionListener(e -> generarTicket());
        add(generateTicketButton);

        setSize(400, 350);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void generarTicket() {
        String dni = dniField.getText();

        try {
            TicketClient client = new TicketClient();
            String response = client.requestTicket(dni);

            switch (response.split(":")[0]) {
                case "SUCCESS":
                    String personData = response.split(":")[1];
                    responseLabel.setText("Ticket generado con éxito.");
                    responseLabel.setForeground(Color.GREEN);
                    personDataLabel.setText("Cliente: " + personData);
                    break;

                case "ALREADY_REGISTERED":
                    responseLabel.setText("El DNI " + dni + " ya ha sido ingresado.");
                    responseLabel.setForeground(Color.ORANGE);
                    personDataLabel.setText("");
                    break;

                case "INVALID_DNI":
                    responseLabel.setText("DNI no identificado. Formato inválido.");
                    responseLabel.setForeground(Color.RED);
                    personDataLabel.setText("");
                    break;

                case "NOT_FOUND":
                    responseLabel.setText("DNI no identificado en la base de datos.");
                    responseLabel.setForeground(Color.RED);
                    personDataLabel.setText("");
                    break;

                case "FULL":
                    responseLabel.setText("La cola está llena. Intente más tarde.");
                    responseLabel.setForeground(Color.RED);
                    personDataLabel.setText("");
                    break;

                default:
                    responseLabel.setText("Error desconocido.");
                    responseLabel.setForeground(Color.RED);
                    personDataLabel.setText("");
                    break;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicketClientGUI::new);
    }
}

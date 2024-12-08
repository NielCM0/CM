package proyect;

import java.io.*;
import java.net.*;
import java.util.*;

public class TicketClient {
    private String serverAddress = "127.0.0.1";
    private int serverPort = 12345;

    
    private static final Map<String, String> personDatabase = new HashMap<>();
    private static Set<String> registeredDni = new HashSet<>();

    static {
        
        personDatabase.put("12345678", "Díaz Rojas, Jorge Luis");
        personDatabase.put("87654321", "Martínez López, Ana María");
        personDatabase.put("11223344", "García Torres, Carlos Alberto");
        personDatabase.put("55667788", "Pérez González, Laura Isabel");
    }

    public String requestTicket(String personDni) throws IOException {
        if (!isValidDni(personDni)) {
            return "INVALID_DNI"; 
        }

        if (registeredDni.contains(personDni)) {
            return "ALREADY_REGISTERED";
        }

        if (!personDatabase.containsKey(personDni)) {
            return "NOT_FOUND";
        }

        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("ADD:" + personDni);
            String response = in.readLine();

            if (response.equals("SUCCESS")) {
                registeredDni.add(personDni);
                return "SUCCESS:" + personDatabase.get(personDni);
            } else {
                return "FULL";
            }
        }
    }

    private boolean isValidDni(String dni) {
        
        return dni.matches("\\d{8}");
    }
}

package proyect;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class GestorClient {
    private String serverAddress = "127.0.0.1";
    private int serverPort = 12345;

    public String getQueueSize() throws IOException {
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("QUEUE_SIZE");
            return in.readLine();
        }
    }

    public String getClientServed(int minutes) {
        
        return "Cliente atendido en " + minutes + " minutos";
    }
}

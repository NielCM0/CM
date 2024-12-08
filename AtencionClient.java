package proyect;

import java.io.*;
import java.net.*;

public class AtencionClient {
    private String serverAddress = "127.0.0.1";
    private int serverPort = 12345;

    public String attendNext() throws IOException {
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("REMOVE"); 
            String response = in.readLine();

            if (response == null || response.equals("EMPTY")) {
                return "NO_CLIENT";
            } else {
                return response;
            }
        }
    }
}

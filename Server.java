package proyect;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import proyecto.gui.ServerGUI;

public class Server {
    private int port;
    private int maxQueueSize;
    private Queue<String> queue;
    private Set<ClientHandler> clients;
    private ServerGUI serverGUI;
    private ServerSocket serverSocket;
    private boolean running;

    public Server(ServerGUI serverGUI, int port, int maxQueueSize) {
        this.serverGUI = serverGUI;
        this.port = port;
        this.maxQueueSize = maxQueueSize;
        this.queue = new LinkedList<>();
        this.clients = ConcurrentHashMap.newKeySet();
    }

    public void start() {
        running = true;
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                serverGUI.updateStatus("Servidor iniciado en el puerto " + port);
                while (running) {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                    clients.add(clientHandler);
                    new Thread(clientHandler).start();
                }
            } catch (IOException e) {
                if (running) {
                    serverGUI.updateStatus("Error al iniciar el servidor: " + e.getMessage());
                }
            }
        }).start();
    }

    public void stop() {
        try {
            running = false;
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            serverGUI.updateStatus("Servidor detenido.");
        } catch (IOException e) {
            serverGUI.updateStatus("Error al detener el servidor: " + e.getMessage());
        }
    }

    public synchronized boolean addToQueue(String personId) {
        if (queue.size() < maxQueueSize) {
            queue.add(personId);
            updateGUI();
            return true;
        }
        return false;
    }

    public synchronized String removeFromQueue() {
        String person = queue.poll();
        updateGUI();
        return person;
    }

    public synchronized int getQueueSize() {
        return queue.size();
    }

    public synchronized void updateMaxQueueSize(int newSize) {
        this.maxQueueSize = newSize;
        if (serverGUI != null) {
            serverGUI.updateMaxQueueSizeLabel(newSize);
        }
    }

    private void updateGUI() {
        if (serverGUI != null) {
            serverGUI.updateQueueSize(queue.size());
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private Server server;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            String request;
            while ((request = in.readLine()) != null) {
                if (request.startsWith("ADD")) {
                    String personId = request.split(":")[1];
                    boolean success = server.addToQueue(personId);
                    out.println(success ? "SUCCESS" : "FULL");
                } else if (request.equals("REMOVE")) {
                    String person = server.removeFromQueue();
                    out.println(person != null ? person : "EMPTY");
                } else if (request.equals("QUEUE_SIZE")) {
                    out.println(server.getQueueSize());
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

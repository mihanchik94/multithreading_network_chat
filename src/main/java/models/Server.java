package models;

import multithreading.ClientThread;
import service_files.Config;
import service_files.MyServerLogger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Server {
    private final String CONFIG_PATH = "server_settings/settings.txt";
    private final Config config = new Config(CONFIG_PATH);
    private final int PORT = Integer.parseInt(config.getResource("port"));
    private final MyServerLogger logger = MyServerLogger.getInstance();
    private final List<ClientThread> clients = new ArrayList<>();


    public void start() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            logger.log("Сервер начал работу");
            while (true) {
                Socket socket = server.accept();
                ClientThread client = new ClientThread(socket, this);
                clients.add(client);
                new Thread(client).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToChat(String msg) {
        for (ClientThread client : clients) {
            client.sendMessageToUser(msg);
        }
    }

    public void removeClient(ClientThread client) {
        clients.remove(client);
    }
}
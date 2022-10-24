package multithreading;

import models.Server;
import service_files.MyServerLogger;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable {
    private final Socket socket;
    private final Server server;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final Scanner scanner;
    private final MyServerLogger logger = MyServerLogger.getInstance();

    public ClientThread(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        String userMessage;
        try {
            try {
                while (true) {
                    userMessage = reader.readLine();
                    if (userMessage.equals(null)) {
                        this.exitClient();
                        break;
                    }
                    server.sendMessageToChat(userMessage);
                    logger.log(userMessage);
                }
            } catch (NullPointerException ignored) {
            }
        } catch (Exception ignored) {

        } finally {
            try {
                this.exitClient();
            } catch (IOException ignored) {

            }
        }
    }


    public void sendMessageToUser (String msg) {
        try {
            writer.write(msg + "\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exitClient () throws IOException {
        if (!socket.isClosed()) {
            socket.close();
            reader.close();
            writer.close();
            scanner.close();
            server.removeClient(this);
        }
    }
}
package models;

import service_files.Config;
import service_files.MyServerLogger;

import java.io.*;
import java.net.Socket;

public class Client {
    private final String CONFIG_PATH = "server_settings/settings.txt";
    private final Config config = new Config(CONFIG_PATH);
    private final String HOST = config.getResource("host");
    private final int PORT = Integer.parseInt(config.getResource("port"));
    private final MyServerLogger logger = MyServerLogger.getInstance();
    private final String EXIT = "exit";
    private Socket socket;
    private BufferedReader consoleReader;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String userName;


    public void start() {
        try {
            socket = new Socket(HOST, PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            consoleReader = new BufferedReader(new InputStreamReader(System.in));
            addUserName();
            new Thread(new MessageReader()).start();
            new Thread(new MessageSender()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUserName() {
        try {
            System.out.println("Введите ваше имя для пользования чатом");
            userName = consoleReader.readLine();
            logger.log(userName + ", добро пожаловать в чат");
            writer.flush();
        } catch (IOException e) {
            exitFromChat();
        }
    }

    private void exitFromChat() {
        if (!socket.isClosed()) {
            try {
                socket.close();
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class MessageReader implements Runnable {
        @Override
        public void run() {
            String msg;
            try {
                while (true) {
                    msg = reader.readLine();
                    if (msg.equals(EXIT)) {
                        exitFromChat();
                        break;
                    }
                    System.out.println(msg);
                }
            } catch (IOException e) {
                exitFromChat();
            }
        }
    }

    private class MessageSender implements Runnable {
        public void run() {
            while (true) {
                String msg;
                try {
                    msg = consoleReader.readLine();
                    if (msg.equals(EXIT)) {
                        writer.write(String.format("%s вышел из чата %n", userName));
                        writer.flush();
                        exitFromChat();
                        break;
                    } else {
                        writer.write(String.format("%s: %s%n", userName, msg));
                    }
                    writer.flush();
                } catch (IOException e) {
                    exitFromChat();
                }
            }
        }
    }
}
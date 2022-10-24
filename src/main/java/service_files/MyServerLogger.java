package service_files;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyServerLogger {
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final LocalDateTime localDateTime = LocalDateTime.now();
    private static final String SERVER_LOG_PATH = "logs/server_log.log";
    private static final String CLIENT_LOG_PATH = "logs/client_log.log";

    private MyServerLogger() {

    }

    private static class LoggerHolder {
        private static final MyServerLogger INSTANCE = new MyServerLogger();
    }

    public static MyServerLogger getInstance() {
        return LoggerHolder.INSTANCE;
    }

    public void log(String message) {
        StringBuilder builder = new StringBuilder();
        builder.append(localDateTime.format(FORMAT)).append(" ").append(message).append("\n");
        System.out.print(builder);
        try (FileWriter serverWriter = new FileWriter(SERVER_LOG_PATH, true);
             FileWriter clientWriter = new FileWriter(CLIENT_LOG_PATH, true)) {
            serverWriter.write(builder.toString());
            clientWriter.write(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
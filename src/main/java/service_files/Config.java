package service_files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private final String path;
    private final Map<String, String> values = new HashMap<>();

    public Config(String path) {
        this.path = path;
        loadConfig();
    }

    private void loadConfig() {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line.isEmpty()) {
                    continue;
                }
                if (validate(line)) {
                    String[] parts = line.split("=");
                    values.put(parts[0], parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validate(String line) {
        String[] parts = line.split("=", 2);
        if (parts.length != 2 || parts[0].length() == 0 || parts[1].length() == 0) {
        throw new IllegalArgumentException("Settings are wrong! Check " + path + "Format of settings should be: key=value. Every section should start from new line");
        }
        return true;
    }

    public String getResource(String key) {
        return values.get(key);
    }
}
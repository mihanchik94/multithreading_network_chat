package service_files;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ConfigTest {
    @Test
    public void whenCorrectSettings() {
        Config config = new Config("test_server_settings/when_correct_settings.txt");
        assertThat(config.getResource("host")).isEqualTo("host");
        assertThat(config.getResource("port")).isEqualTo("3000");
    }

    @Test
    public void whenEmptyLines() {
        Config config = new Config("test_server_settings/when_empty_lines.txt");
        assertThat(config.getResource("host")).isEqualTo("localhost");
        assertThat(config.getResource("port")).isEqualTo("9000");
    }
}
package persistence;

import model.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// modelled after persistence.JsonReaderTest from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

public class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/nonExistentFile.json");
        try {
            Player player = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderPlayer() {
        JsonReader reader = new JsonReader("./data/testReaderPlayer.json");
        try {
            Player player = reader.read();
            assertEquals(54321, player.getBalance());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}

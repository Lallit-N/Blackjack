package persistence;

import model.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// modelled after persistence.JsonWriterTest from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Player player = new Player(12345);
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterPlayer() {
        try {
            Player player = new Player(12345);
            JsonWriter writer = new JsonWriter("./data/testWriterPlayer.json");
            writer.open();
            writer.write(player);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterPlayer.json");
            player = reader.read();
            assertEquals(12345, player.getBalance());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}

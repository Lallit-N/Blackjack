package persistence;

// modelled after persistence.Writable from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

import org.json.JSONObject;

// represents the Player as a JSON object
public interface Writable {

    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}

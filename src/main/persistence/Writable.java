package persistence;

// modelled after persistence.Writable from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

import org.json.JSONObject;

public interface Writable {

    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}

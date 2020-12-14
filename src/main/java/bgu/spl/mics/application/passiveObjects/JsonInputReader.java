package bgu.spl.mics.application.passiveObjects;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


/**
 * JsonInputReader class translates the input json file into objects in java, and inserts it to the fields
 * of {@link Input}.
 */
public class JsonInputReader {
    public static Input getInputFromJson(String filePath) throws IOException {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, Input.class);
        }
    }
}


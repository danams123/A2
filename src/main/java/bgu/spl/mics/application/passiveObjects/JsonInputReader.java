package bgu.spl.mics.application.passiveObjects;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class JsonInputReader {
    public static Input getInputFromJson(String filePath) throws IOException {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, Input.class);
        }
    }
}

//    Gson gson = new Gson();
//    Input input = null;
//        try {
//                BufferedReader reader = new BufferedReader(new FileReader(filePath));
//                input = gson.fromJson(reader, Input.class);
//        } catch (IOException i) {
//        }
//        return input;

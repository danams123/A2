package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.passiveObjects.Attack;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class GeneratedTests {

    public static void toJson(GeneratedInput input, int counter) {
        for (int i = 0; i < counter; i++) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Collection collection = new ArrayList();
            collection.add(input);
            Writer writer = null;
            try {
                writer = Files.newBufferedWriter(Paths.get("GeneratedTest" + i +".json"));
                writer.write(gson.toJson(collection));
                writer.flush();
                writer.close();
            } catch (IOException e) {
            }
        }
    }

    public static void GenerateTest(int counter){
        int Ewoks = (int) ((Math.random() * 10) + 1);
        int numAttacks = (int) ((Math.random() * 5) + 1);
        Attack[] attacks = new Attack[numAttacks];
        for (int i = 0; i < numAttacks; i++) {
            List<Integer> serials = new ArrayList(Ewoks);
            for (int j = 0; j < Ewoks; j++) {
                serials.add(j + 1);
            }
            attacks[i] = new Attack((shuffleArray(serials)), ((int) ((Math.random() * 10000) + 1)));
        }
        long R2D2 = (long) ((Math.random() * 10000) + 1);
        long Lando = (long) ((Math.random() * 10000) + 1);
        toJson(new GeneratedInput(attacks, R2D2, Lando, Ewoks), counter);
    }

    public static List<Integer> shuffleArray(List<Integer> list) {
        int[] ar = new int[list.size()];
        int in = 0;
        for(Integer elem: list){
            ar[in] = elem;
            in ++;
        }
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = (int) (Math.random() * ar.length);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        List<Integer> output = new LinkedList<>();
        for (int i = 0; i < ar.length; i++) {
            output.add(ar[i]);
        }
        return output;
    }

}
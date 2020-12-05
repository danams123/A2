package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;


public class GeneratedTests {

    private Writer writer;
    private Gson gson;
    private int counter;
    private Collection output;

    public GeneratedTests(int counter) throws IOException {
        writer  = Files.newBufferedWriter(Paths.get("testOutput.json"));
        gson = new GsonBuilder().setPrettyPrinting().create();
        this.counter = counter;
        output = new ArrayList();
    }

    public void write(Diary d, GeneratedInput input, int iter) throws IOException {
        output.add("Test number: " + iter);
        output.add("Input: ");
        output.add(input);
        output.add("Output: ");
        output.add(new Event(d.getTotalAttacks(), d.getHanSoloFinish(), d.getC3POFinish(), d.getR2D2Deactivate(), d.getLeiaTerminate(),
                d.getHanSoloTerminate(), d.getC3POTerminate(), d.getR2D2Terminate(), d.getLandoTerminate()));
        output.add("Expected: ");
        long exFinish = input.getLando() + input.getR2D2();
        for(Attack elem: input.getAttacks()){
            exFinish += elem.duration;
        }
        long R2 = exFinish - input.getLando();
        output.add(new Event(input.getAttacks().length, 0, 0, R2, exFinish,
                exFinish, exFinish, exFinish, exFinish));
        output.add("Finish Test!");
      if(iter == counter){
          writer.write(gson.toJson(output));
          writer.flush();
          writer.close();
      }
    }

    public void toJson(GeneratedInput input, int iter) throws IOException {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Collection collection = new ArrayList();
            collection.add(input);
            Writer writer = null;
            try {
                writer = Files.newBufferedWriter(Paths.get("GeneratedTest.json"));
                writer.write(gson.toJson(collection));
                writer.flush();
                writer.close();
            } catch (IOException e) {}
    }

    public void GenerateTest(int iter) throws IOException {
        int Ewoks = (int) ((Math.random() * 10) + 1);
        int numAttacks = (int) ((Math.random() * 5) + 1);
        Attack[] attacks = new Attack[numAttacks];
        for (int i = 0; i < numAttacks; i++) {
            int serialSize = (int) ((Math.random() * Ewoks) + 1);
            List<Integer> serials = new ArrayList();
            for (int j = 0; j < serialSize; j++) {
                serials.add(j + 1);
            }
            attacks[i] = new Attack((shuffleArray(serials)), ((int) ((Math.random() * 10000) + 1)));
        }
        long R2D2 = (long) ((Math.random() * 10000) + 1);
        long Lando = (long) ((Math.random() * 10000) + 1);
        GeneratedInput input = new GeneratedInput(attacks, R2D2, Lando, Ewoks);
        toJson(input, iter);
        toTest(input, iter);
    }

    public List<Integer> shuffleArray(List<Integer> list) {
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

    public void runTest() throws IOException {
        List<Integer> a1 = new LinkedList<>();
        a1.add(1); a1.add(8); a1.add(3); a1.add(4); a1.add(9); a1.add(2); a1.add(5); a1.add(7); a1.add(6);
        Attack a = new Attack(a1, 1201);
        List<Integer> b1 = new LinkedList<>();
        b1.add(1); b1.add(2); b1.add(6); b1.add(7); b1.add(5); b1.add(3); b1.add(4);
        Attack b = new Attack(b1, 1685);
        List<Integer> c1 = new LinkedList<>();
        c1.add(1); c1.add(2); c1.add(5); c1.add(7); c1.add(3); c1.add(4); c1.add(6);
        Attack c = new Attack(c1, 9846);
        List<Integer> d1 = new LinkedList<>();
        d1.add(1); d1.add(5); d1.add(7); d1.add(9); d1.add(3); d1.add(4); d1.add(8); d1.add(6); d1.add(2);
        Attack d = new Attack(d1, 1199);
        List<Integer> e1 = new LinkedList<>();
        e1.add(4); e1.add(7); e1.add(5); e1.add(1); e1.add(2); e1.add(3); e1.add(6);
        Attack e = new Attack(e1, 3281);
        Attack[] a2 = new Attack[5];
        a2[0] = a; a2[1] = b; a2[2] = c; a2[3] = d; a2[4] = e;
        GeneratedInput g = new GeneratedInput(a2, 7081, 5986, 10);
        toTest(g,1);
    }

    public void toTest(GeneratedInput input, int iter) throws IOException {
        System.out.println("----------------Test number: " + iter +" ------------------");
        System.out.println("Star Wars - Episode VI: Return of the Jedi");
        System.out.println("In a galaxy FAR FAR AWAY ...");
        Diary d = Diary.getInstance();
        Ewoks ewoks = Ewoks.getInstance();
        for (int i = 1; i <= input.getEwoks(); i++) {
            ewoks.add(new Ewok(i));
        }
        CountDownLatch latch = new CountDownLatch(4);

        LeiaMicroservice Leia = new LeiaMicroservice(input.getAttacks(), ewoks);
        System.out.println("Leia arrived");
        HanSoloMicroservice Han = new HanSoloMicroservice(latch);
        System.out.println("Han arrived");
        C3POMicroservice C3PO = new C3POMicroservice(latch);
        System.out.println("C3PO arrived");
        R2D2Microservice R2D2 = new R2D2Microservice(input.getR2D2(), latch);
        System.out.println("R2D2 arrived");
        LandoMicroservice Lando = new LandoMicroservice(input.getLando(), latch);
        System.out.println("Lando arrived");

        d.setStartTime(System.currentTimeMillis());
        System.out.println("start time set");

        Thread t1 = new Thread(Leia);
        Thread t2 = new Thread(Han);
        Thread t3 = new Thread(C3PO);
        Thread t4 = new Thread(R2D2);
        Thread t5 = new Thread(Lando);

        t2.start();
        t3.start();
        t4.start();
        t5.start();

        try {
            latch.await();
        } catch (InterruptedException e) {
        }
        t1.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
        } catch (InterruptedException i) {
        }
        ewoks.clear();
        System.out.println("Mission Accomplished! printing Diary");

        //output json
        write(d, input, iter);
        d.setTotalAttacks(0);
    }


}

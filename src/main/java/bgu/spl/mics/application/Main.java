package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;

import java.io.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {

	public static void main(String[] args){
		//input json
		Input input = null;
		try {
			input = JsonInputReader.getInputFromJson(args[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Ewoks init
		Ewoks ewoks = Ewoks.getInstance();
		for (int i = 1; i <= input.getEwoks(); i++) {
			ewoks.add(new Ewok(i));
		}

		//Microservices init
		LeiaMicroservice Leia = new LeiaMicroservice(input.getAttacks());
		HanSoloMicroservice Han = new HanSoloMicroservice();
		C3POMicroservice C3PO = new C3POMicroservice();
		R2D2Microservice R2D2 = new R2D2Microservice();
		LandoMicroservice Lando = new LandoMicroservice();

		//Diary Init
		//holding the start time for later uses in the microservices and passing it through the Singleton Diary
		Diary d = Diary.getInstance();
		long startTime = System.currentTimeMillis();
		d.setR2D2Deactivate(input.getR2D2());
		d.setLeiaTerminate(input.getLando());
		d.setHanSoloTerminate(startTime);
		d.setC3POTerminate(startTime);
		d.setR2D2Terminate(startTime);
		d.setLandoTerminate(startTime);

		//Threads init
		Thread t1 = new Thread(Leia);
		Thread t2 = new Thread(Han);
		Thread t3 = new Thread(C3PO);
		Thread t4 = new Thread(R2D2);
		Thread t5 = new Thread(Lando);

		t2.start();
		t3.start();
		t4.start();
		t5.start();

		//using CountDownLatch so that Leia won't send events before the rest of the microservices will subscribe to
		//the events.
		CountDownLatch latch = CountDownLatch.getInstance();
		try {
			latch.await();
		}
		catch (InterruptedException e) { }
		t1.start();

		//waiting for all the threads to terminate
		try {
			t1.join();
			t2.join();
			t3.join();
			t4.join();
			t5.join();
		}
		catch(InterruptedException i){}

		//clearing Ewoks for multiple uses
		ewoks.clear();

		//output json
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
		FileWriter writer = new FileWriter(args[1]);
		gson.toJson(d, writer);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		d.setTotalAttacks(0);
	}
}

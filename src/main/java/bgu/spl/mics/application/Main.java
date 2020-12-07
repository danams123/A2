package bgu.spl.mics.application;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.passiveObjects.Event;
import bgu.spl.mics.application.services.*;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {

	public static void main(String[] args){
//		System.out.println("Choose number of Tests:");
//		Scanner sc = new Scanner(System.in);
//		int counter  = sc.nextInt();
//		try {
//			GeneratedTests g = new GeneratedTests(counter);
//			for (int i = 1; i <= counter ; i++) {
//				g.GenerateTest(i);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		try {
			GeneratedTests g = new GeneratedTests(1);
			g.runTest();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		System.out.println("Star Wars - Episode VI: Return of the Jedi");
//		Input input = null;
//		try {
//			input = JsonInputReader.getInputFromJson(args[0]);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("In a galaxy FAR FAR AWAY ...");
//
//		Ewoks ewoks = Ewoks.getInstance();
//		for (int i = 1; i <= input.getEwoks(); i++) {
//			ewoks.add(new Ewok(i));
//		}
//		Diary d = Diary.getInstance();
//		long startTime = System.currentTimeMillis();
//		d.setR2D2Deactivate(input.getR2D2());
//		d.setLeiaTerminate(input.getLando());
//		d.setHanSoloTerminate(startTime);
//		d.setC3POTerminate(startTime);
//		d.setR2D2Terminate(startTime);
//		d.setLandoTerminate(startTime);
//		LeiaMicroservice Leia = new LeiaMicroservice(input.getAttacks());
//		System.out.println("Leia arrived");
//		HanSoloMicroservice Han = new HanSoloMicroservice();
//		System.out.println("Han arrived");
//		C3POMicroservice C3PO = new C3POMicroservice();
//		System.out.println("C3PO arrived");
//		R2D2Microservice R2D2 = new R2D2Microservice();
//		System.out.println("R2D2 arrived");
//		LandoMicroservice Lando = new LandoMicroservice();
//		System.out.println("Lando arrived");
//
//
//		System.out.println("start time set");
//
//		Thread t1 = new Thread(Leia);
//		Thread t2 = new Thread(Han);
//		Thread t3 = new Thread(C3PO);
//		Thread t4 = new Thread(R2D2);
//		Thread t5 = new Thread(Lando);
//
//		t2.start();
//		t3.start();
//		t4.start();
//		t5.start();
//
//		CountDownLatch latch = CountDownLatch.getInstance();
//		try {
//			latch.await();
//		}
//		catch (InterruptedException e) { }
//		t1.start();
//
//		try {
//			t1.join();
//			t2.join();
//			t3.join();
//			t4.join();
//			t5.join();
//		}
//		catch(InterruptedException i){}
//		ewoks.clear();
//		System.out.println("Mission Accomplished! printing Diary");
//		//output json
//		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//
////		Collection output = new ArrayList();
////		output.add(new Event(d.getTotalAttacks(), d.getHanSoloFinish(), d.getC3POFinish(), d.getR2D2Deactivate(), d.getLeiaTerminate(),
////				 d.getHanSoloTerminate(), d.getC3POTerminate(), d.getR2D2Terminate(), d.getLandoTerminate()));
////		Writer writer = null;
////		try {
////			writer = Files.newBufferedWriter(Paths.get("output.json"));
////			writer.write(gson.toJson(output));
////			writer.flush();
////			writer.close();}
////		catch (IOException e) {}
//		try {
//		FileWriter writer = new FileWriter("output.json");
//		gson.toJson(d, writer);
//			writer.flush();
//				writer.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		d.setTotalAttacks(0);
	}
}

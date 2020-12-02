package bgu.spl.mics.application;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.Math;

/** This is the Main class of the application. You should parse the input file, 
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args){
		System.out.println("Star Wars - Episode VI: Return of the Jedi");
		Input input = null;
		try {
			input = JsonInputReader.getInputFromJson(args[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("In a galaxy FAR FAR AWAY ...");
		Diary d = new Diary();
		d.setTotalAttacks(input.getAttacks().length);
		LeiaMicroservice Leia = new LeiaMicroservice(input.getAttacks(),new Ewoks(input.getEwoks()),d);
		System.out.println("Leia arrived");
		HanSoloMicroservice Han = new HanSoloMicroservice(d);
		System.out.println("Han arrived");
		C3POMicroservice C3PO = new C3POMicroservice(d);
		System.out.println("C3PO arrived");
		R2D2Microservice R2D2 = new R2D2Microservice(input.getR2D2(),d);
		System.out.println("R2D2 arrived");
		LandoMicroservice Lando = new LandoMicroservice(input.getLando(),d);
		System.out.println("Lando arrived");

		d.setStartTime(System.currentTimeMillis());
		System.out.println("start time set");

		Thread t1 = new Thread(Leia);
		Thread t2 = new Thread(Han);
		Thread t3 = new Thread(C3PO);
		Thread t4 = new Thread(R2D2);
		Thread t5 = new Thread(Lando);

		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();

		try {
			t1.join();
			t2.join();
			t3.join();
			t4.join();
			t5.join();
		}
		catch(InterruptedException i){}

		System.out.println("Mission Accomplished! printing Diary");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Writer writer = null;
		try {
			writer = Files.newBufferedWriter(Paths.get("output.json"));
//			writer.write("There are " + d.getTotalAttacks() + " attacks. \n");
//			long attacksEnd = (d.getC3POFinish() - d.getHanSoloFinish());
//			writer.write("HanSolo and C3PO finish their tasks " +  attacksEnd + " miliseconds one after the other. \n");
//			//miliseconds as Timeunits?
//			long deactivateEnd = (d.getR2D2Deactivate() - attacksEnd);
//			writer.write("R2D2 has deactivated the force shield " + deactivateEnd + " miliseconds after the attacks. \n");
//			long terminateTime = Math.max(Math.max(Math.max(Math.max(d.getLeiaTerminate(), d.getC3POTerminate()),
//					d.getHanSoloTerminate()),d.getR2D2Terminate()),d.getLandoTerminate()) - deactivateEnd;
//			//maybe there is a better way with timestamp?
//			writer.write("All threads terminate " + terminateTime + " milliseconds later.");
			writer.flush();
			writer.write("totalAttacks: " + d.getTotalAttacks());
			writer.write("\n HanSoloFinish: " + d.getHanSoloFinish());
			writer.write("\n C3POFinish: " + d.getC3POFinish());
			writer.write("\n R2D2Deactivate: " + d.getR2D2Deactivate());
			writer.write("\n LeiaTerminate: " + d.getLeiaTerminate());
			writer.write("\n HanSoloTerminate: " + d.getHanSoloTerminate());
			writer.write("\n C3POTerminate: " + d.getC3POTerminate());
			writer.write("\n R2D2Terminate: " + d.getR2D2Terminate());
			writer.write("\n LandoTerminate: " + d.getLandoTerminate());
			writer.close();}
		catch (IOException e) {}
		//		Type diaryType = new TypeToken<Diary>() {}.getType();
		gson.toJson(writer);
		//do we need d in this?
	}
}

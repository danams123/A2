package bgu.spl.mics.application.passiveObjects;

import java.util.ArrayList;
import java.util.List;



/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
//singleton and finalize
final public class Ewoks {
    final private ArrayList<Ewok> EwoksList;
    private static Ewoks instance = null; //no final ok?

    private Ewoks(int EwokNum) {
        EwoksList = new ArrayList<>(EwokNum);
        for (int i = 1; i <= EwokNum; i++) {
            EwoksList.add(new Ewok(i));
        }
    }

    public static Ewoks getInstance(int EwokNum) {
        if (instance == null){
            instance = new Ewoks(EwokNum);
        }
        return instance;
    }

    public ArrayList<Ewok> getEwoksList(){return EwoksList;}
}

package bgu.spl.mics.application.passiveObjects;

import java.util.ArrayList;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */

public class Ewoks {
    final private ArrayList<Ewok> EwoksList;

    private static class SingletonHolder {
        private static Ewoks instance = new Ewoks();
    }

    public Ewoks() {
        EwoksList = new ArrayList<>();
    }


    public static Ewoks getInstance() {
        return SingletonHolder.instance;
    }

    public final ArrayList<Ewok> getEwoksList(){return EwoksList;}

    public void add(Ewok e){
        EwoksList.add(e);
    }
}

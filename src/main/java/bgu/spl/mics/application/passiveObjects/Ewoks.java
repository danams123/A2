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

    public Ewoks(int EwokNum) {
        EwoksList = new ArrayList<>(EwokNum);
        for (int i = 1; i <= EwokNum; i++) {
            EwoksList.add(new Ewok(i));
        }
    }


    public ArrayList<Ewok> getEwoksList(){return EwoksList;}
}

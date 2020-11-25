package bgu.spl.mics.application.passiveObjects;

import java.awt.*;
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
    private ArrayList <Ewok> EwoksList;
    private int size;

    public Ewoks (int EwokNum)
    {
        EwoksList= new ArrayList<>(EwokNum);
        for (int i = 1; i <= EwokNum ; i++) {
            EwoksList.add(new Ewok(i));
        }
        size=EwokNum;

    }

     public int size (){return size;}

     public ArrayList <Ewok> getEwoksList (){
                return this.EwoksList;
     }

}

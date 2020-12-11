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
    private ArrayList<Integer> waiters;

    private static class SingletonHolder {
        private static Ewoks instance = new Ewoks();
    }

    public Ewoks() {
        EwoksList = new ArrayList<Ewok>();
        waiters = new ArrayList<Integer>();

    }

    public static Ewoks getInstance() {
        return SingletonHolder.instance;
    }

    public boolean checkWaiters(int Ewoknum, int sign){
        waiters.set(Ewoknum - 1, (waiters.get(Ewoknum - 1) + sign));
        int counter = 0;
        for (Integer elem: waiters){
            if(elem == 3){
                counter ++;
            }
            if((counter > 1) ||(waiters.size() == 1 && counter == 1)){
                return true;
            }
        }
        return false;
    }


    public void releaseAll(int sign, int Case){
        for (int i = 0; i < EwoksList.size(); i++) {
            if ((i + 1 != Case) && (waiters.get(i) == sign || waiters.get(i) == 3)) {
                waiters.set(i, waiters.get(i) - sign);
                EwoksList.get(i).release();
            }
        }
    }

    public final ArrayList<Ewok> getEwoksList(){return EwoksList;}

    public void add(Ewok e){
        EwoksList.add(e);
        waiters.add(0);
    }

    public void clear(){
        EwoksList.clear();
        waiters.clear();
    }
}

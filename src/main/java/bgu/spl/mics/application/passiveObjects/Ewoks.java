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
        //before the function checks waiters, we update the sign so it would look as if the thread did acquire
        //and so see if two threads called acquire simultaneously
        waiters.set(Ewoknum - 1, (waiters.get(Ewoknum - 1) + sign));
        int counter = 0;
        for (Integer elem: waiters){
            //1 signals Han got the ewok and 2 if C3PO, 3 is both of them, that's our signal for a possible deadlock
            if(elem == 3){
                counter ++;
            }
            //if there are more than 2 ewoks that both Han and C3PO are trying to acquire, that is a possible deadlock.
            //also, checking the edge case of a 1 sized array in which we check if the only ewok is trying to be
            //acquired by both of the threads.
            if((counter > 1) ||(waiters.size() == 1 && counter == 1)){
                return true;
            }
        }
        return false;
    }


    public void releaseAll(int sign, int Case){
        //release all the ewoks the microservice use, the sign signals which microservice tries to call the func, 1 for
        //Han, 2 for C3PO. Case is for signaling if the func was called because of a finished attack or due to a
        //possible deadlock. -1 is for a finished attack, the rest is for deadlock.
        for (int i = 0; i < EwoksList.size(); i++) {
            //Case represents the troublesome ewok in a deadlock, the one that after calling releaseAll(), the thread
            //will keep waiting on until the other finishes it's attack, so releasing it isn't necessary
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

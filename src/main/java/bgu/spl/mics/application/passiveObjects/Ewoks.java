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
    private ArrayList<Integer> deadlock;
    private ArrayList<Ewok> toRetrieve;

    private static class SingletonHolder {
        private static Ewoks instance = new Ewoks();
    }

    public Ewoks() {
        EwoksList = new ArrayList<>();
        deadlock = new ArrayList<>(10); //TODO: change it later to default constructor!
        toRetrieve = new ArrayList<>();
    }

    public void acquire(int Ewoknum, int sign){
        if(sign == 1) {
            System.out.println("Han is in acquire of Ewoks for ewok " + Ewoknum);
        }
        else{
            System.out.println("C3PO is in acquire of Ewoks for ewok " + Ewoknum);
        }
        System.out.println("deadlock: ");
        for (int elem: deadlock){
            System.out.println(elem + ",");
        }
        deadlock.set( Ewoknum - 1,(deadlock.get(Ewoknum - 1) + sign));
        System.out.println("deadlock: ");
        for (int elem: deadlock){
            System.out.println(elem + ",");
        }
        if(checkDeadlock()) {
            wait(sign);
        }
        try {
            EwoksList.get(Ewoknum - 1).acquire();}
        catch (InterruptedException i) {
        }
    }

    public synchronized void wait(int sign){
        System.out.println(Thread.currentThread().getName() + " is in wait of Ewoks");
        releaseAll(sign);
        try {
            wait();}
        catch (InterruptedException e) {}
        acquireAll(sign);
    }

    public synchronized void release(int Ewoknum, int sign){

        if(sign == 1) {
            System.out.println("Han is in release of Ewoks for ewok " + Ewoknum);
        }
        else{
            System.out.println("C3PO is in release of Ewoks for ewok " + Ewoknum);
        }
        System.out.println("deadlock: ");
        for (int elem: deadlock){
            System.out.println(elem + ",");
        }
        deadlock.set( Ewoknum - 1, deadlock.get(Ewoknum - 1) - sign);
        System.out.println("deadlock: ");
        for (int elem: deadlock){
            System.out.println(elem + ",");
        }
        EwoksList.get(Ewoknum - 1).release();
        notifyAll();
    }

    public boolean checkDeadlock(){
        System.out.println(Thread.currentThread().getName() + " is in checkDeadlock of Ewoks");
        int counter = 0;
        for (Integer elem: deadlock){
            if(elem == 3){
                counter ++;
            }
            if(counter > 1){
                return true;
            }
        }
        return false;
    }

    public void releaseAll(int sign){
        System.out.println(Thread.currentThread().getName() + " is in releaseAll of Ewoks");
        for (int i = 0; i < deadlock.size(); i++) {
            if(deadlock.get(i) == sign){
                release(i , sign);
                toRetrieve.add(EwoksList.get(i));
            }
        }
    }

    public void acquireAll(int sign){
        System.out.println(Thread.currentThread().getName() + " is in acquireAll of Ewoks");
        for (Ewok elem: toRetrieve){
            acquire(elem.serialNumber, sign);
        }
    }

    public static Ewoks getInstance() {
        return SingletonHolder.instance;
    }

    public final ArrayList<Ewok> getEwoksList(){return EwoksList;}

    public void add(Ewok e){
        EwoksList.add(e);
        deadlock.add(0);
    }
}

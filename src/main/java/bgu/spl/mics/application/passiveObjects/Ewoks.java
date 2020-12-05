package bgu.spl.mics.application.passiveObjects;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private AtomicBoolean isWaiting;
    private Object lock;

    private static class SingletonHolder {
        private static Ewoks instance = new Ewoks();
    }

    public Ewoks() {
        EwoksList = new ArrayList<>();
        deadlock = new ArrayList<>(10); //TODO: change it later to default constructor!
        isWaiting = new AtomicBoolean(false);
        lock = new Object();
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

        if(checkDeadlock()) {
            wait(sign);
        }
        else {
            try {
                EwoksList.get(Ewoknum - 1).acquire();
            } catch (InterruptedException i) {
            }
        }
        System.out.println("deadlock: ");
        for (int i = 0; i < deadlock.size(); i++) {
            System.out.println(deadlock.get(i));
            System.out.println(EwoksList.get(i).available);
        }
    }

    public void wait(int sign){
        if(!isWaiting.get()) {
            synchronized (this) {
                isWaiting.compareAndSet(false, true);
                ArrayList<Ewok> toRetrieve = new ArrayList<>();
                System.out.println(Thread.currentThread().getName() + " is in wait of Ewoks");
                releaseAll(sign, toRetrieve, 1);
                try {
                    wait();
                } catch (InterruptedException e) {
                }
                acquireAll(sign, toRetrieve);
                isWaiting.compareAndSet(true, false);
                notifyAll();
            }
        }
    }

//    public void release(int Ewoknum, int sign){
//        if(sign == 1) {
//            System.out.println("Han is in release of Ewoks for ewok " + Ewoknum);
//        }
//        else{
//            System.out.println("C3PO is in release of Ewoks for ewok " + Ewoknum);
//        }
//        System.out.println("deadlock: ");
//        for (int elem: deadlock){
//            System.out.println(elem + ",");
//        }
//
//        System.out.println("deadlock: ");
//        for (int i = 0; i < deadlock.size(); i++) {
//            System.out.println(deadlock.get(i));
//            System.out.println(EwoksList.get(i).available);
//        }
//    }

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

    public void releaseAll(int sign, ArrayList<Ewok> toRetrieve, int sender){
        synchronized (lock) {
            System.out.println(Thread.currentThread().getName() + " is in releaseAll of Ewoks");
            for (int i = 0; i < EwoksList.size(); i++) {
                if (deadlock.get(i) == sign || deadlock.get(i) == 3) {
//                release(i + 1 , sign);
                    deadlock.set(i, deadlock.get(i) - sign);
                    EwoksList.get(i).release();
                    toRetrieve.add(EwoksList.get(i));
                }
            }
            synchronized (this) {
                if (sender == 2) {
                    notifyAll();
                }
            }
        }
    }

//    public synchronized void notified(){
//
//    }

    public void acquireAll(int sign, ArrayList<Ewok> toRetrieve){
        System.out.println(Thread.currentThread().getName() + " is in acquireAll of Ewoks");
        for (Ewok elem: toRetrieve){
            System.out.println(elem.getNum());
        }
        for (Ewok elem: toRetrieve){
            acquire(elem.getNum(), sign);
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

    public void clear(){
        EwoksList.clear();
        deadlock.clear();
    }
}

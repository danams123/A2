package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */

public class Ewok {
	int serialNumber;
	boolean available;

    public Ewok(int serialNumber){
        this.serialNumber = serialNumber;
        this.available = true;
    }

    /**
     * Acquires an Ewok
     */
    //check if works without sync here
    public synchronized void acquire() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " is in acquire of Ewok for " + serialNumber);
        while(!available){
            wait();
        }
        available = false;
    }

    /**
     * release an Ewok
     */
    //check if works without sync here
    public synchronized void release() {
        System.out.println(Thread.currentThread().getName() + " is in release of Ewok for " + serialNumber);
        available = true;
        System.out.println(this + " has been released and notifyall was called!");
        notifyAll();
    }

    public boolean getAvailable(){return available;}

    public int getNum(){return serialNumber;}
}

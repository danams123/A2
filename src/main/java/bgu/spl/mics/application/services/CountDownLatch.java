package bgu.spl.mics.application.services;


/**
 * The CountDownLatch is a Singleton that implements the option of a thread (or multiple threads) waiting for other
 * threads to finish certain tasks. The class is a Singleton so it could be used as one instance through multiple
 * microservices {@link bgu.spl.mics.MicroService}.
 */
public class CountDownLatch {
    private static class SingletonHolder {
        private static CountDownLatch instance = new CountDownLatch();
    }
    private int counter;

    //count 4 for Han, C3PO, R2D2, Lando
    public CountDownLatch(){
        counter = 4;
    }

    public static CountDownLatch getInstance() {
        return CountDownLatch.SingletonHolder.instance;
    }

    /**
     * reduces the value of the private field counter, and if it's value gets to 0, it calls notifyAll(), so the
     * threads waiting could continue their run.
     */
    public synchronized void countDown(){
        counter --;
        if(counter == 0){
            notifyAll();
        }
    }

    /**
     * calls wait() as long as counter is bigger than the value 0.
     */
    public synchronized void await() throws InterruptedException {
        if(counter > 0) {
            wait();
        }
    }
}


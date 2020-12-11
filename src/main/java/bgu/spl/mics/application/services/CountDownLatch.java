package bgu.spl.mics.application.services;



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

    public synchronized void countDown(){
        counter --;
        if(counter == 0){
            notifyAll();
        }
    }

    public synchronized void await() throws InterruptedException {
        if(counter > 0) {
            wait();
        }
    }
}


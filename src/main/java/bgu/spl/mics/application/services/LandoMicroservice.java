package bgu.spl.mics.application.services;


import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;


/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {

    private Diary d;
    private CountDownLatch latch;

    public LandoMicroservice() {
        super("Lando");
        d = Diary.getInstance();
        latch = CountDownLatch.getInstance();
    }

    @Override
    protected void initialize() {
        this.subscribeEvent(BombDestroyerEvent.class, c -> {
            System.out.println("BombDestroyerCall was called for " + this.getName());
            try{
                long duration = d.getLeiaTerminate();
                d.setLeiaTerminate(d.getLandoTerminate());
            Thread.sleep(duration);}
            catch(InterruptedException i){}
            this.complete(c,c.getResult());
        });
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            System.out.println("TerminateCall was called for " + this.getName());
            d.setLandoTerminate(System.currentTimeMillis() - d.getLandoTerminate());
            this.terminate();
        });
        latch.countDown();
    }
}

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

    /**
     * Initialize() of LandoMicroservices calls subscribeEvent() for BombDestroyerEvent and subscribeBroadcast() for
     * TerminateBroadcast. In this functions , they create the equivalent callbacks using lambda expressions.
     * Callback of BombDestroyerEvent sleeps for the input time and calls complete().
     * Callback of TerminationBroadcast calls Terminate().
     */
    @Override
    protected void initialize() {
        this.subscribeEvent(BombDestroyerEvent.class, c -> {
            //BombDestroyerCallback
            try{
                //getLeiaTerminate() holds the duration Lando needs to sleep, for now
            Thread.sleep(d.getLeiaTerminate());
            }
            catch(InterruptedException i){}
            this.complete(c,c.getResult());
        });
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            //TerminateCallback
            d.setLandoTerminate(System.currentTimeMillis());
            this.terminate();
        });
        latch.countDown();
    }
}

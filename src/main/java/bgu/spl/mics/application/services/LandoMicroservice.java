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

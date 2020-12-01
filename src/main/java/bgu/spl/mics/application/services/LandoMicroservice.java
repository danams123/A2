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

    private long duration;
    private Diary d;

    public LandoMicroservice(long _duration, Diary _d) {
        super("Lando");
        duration = _duration;
        d = _d;
    }

    @Override
    protected void initialize() {
        this.subscribeEvent(BombDestroyerEvent.class, c -> {
            try{
            Thread.sleep(duration);}
            catch(InterruptedException i){}
            this.complete(c,c.getResult());
        });
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            d.setLandoTerminate(System.currentTimeMillis() - d.getStartTime());
            this.terminate();
        });
    }
}

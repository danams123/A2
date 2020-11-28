package bgu.spl.mics.application.services;


import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombCallback;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TerminateCallback;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {

    private long duration;
    private BombCallback b;
    private TerminateCallback t;

    public LandoMicroservice(long _duration) {
        super("Lando");
        duration = _duration;
        b = new BombCallback();
        t = new TerminateCallback();
    }

    @Override
    protected void initialize() {
        this.subscribeEvent(BombDestroyerEvent.class, b);
        this.subscribeBroadcast(TerminateBroadcast.class, t);
    }
}

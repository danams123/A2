package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    private long duration;

    public R2D2Microservice(long _duration) {
        super("R2D2");
        duration = _duration;
    }

    @Override
    protected void initialize() {
        this.subscribeEvent(DeactivationEvent.class , new DeactivationCallback(duration));
        this.subscribeBroadcast(TerminateBroadcast.class , new TerminateCallback());
    }
}

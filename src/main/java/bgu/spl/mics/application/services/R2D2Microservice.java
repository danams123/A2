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
    private Diary d;

    public R2D2Microservice(long _duration, Diary _d) {
        super("R2D2");
        duration = _duration;
        d = _d;
    }

    @Override
    protected void initialize() {
        this.subscribeEvent(DeactivationEvent.class, c -> {
            try{
                Thread.sleep(duration);}
            catch(InterruptedException i){}
            d.setR2D2Deactivate(System.currentTimeMillis() - d.getStartTime());
            this.complete(c,c.getResult());
        });
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            d.setR2D2Terminate(System.currentTimeMillis() - d.getStartTime());
            this.terminate();
        });
    }
}

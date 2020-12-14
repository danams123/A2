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

    private Diary d;
    private CountDownLatch latch;

    public R2D2Microservice() {
        super("R2D2");
        d = Diary.getInstance();
        latch = CountDownLatch.getInstance();
    }

    @Override
    protected void initialize() {
        this.subscribeEvent(DeactivationEvent.class, c -> {
            //DeactivationCallback
            try{
                //getR2D2Deactivate() holds the duration that R2D2 needs to sleep, for now
                Thread.sleep(d.getR2D2Deactivate());
            }
            catch(InterruptedException i){}
            d.setR2D2Deactivate(System.currentTimeMillis());
            this.complete(c,c.getResult());
        });
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            //TerminateCallback
            d.setR2D2Terminate(System.currentTimeMillis());
            this.terminate();
        });
        latch.countDown();
    }
}

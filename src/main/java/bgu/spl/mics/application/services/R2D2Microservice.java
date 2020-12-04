package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.concurrent.CountDownLatch;

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
    private CountDownLatch latch;

    public R2D2Microservice(long _duration, CountDownLatch latch) {
        super("R2D2");
        duration = _duration;
        d = Diary.getInstance();
        this.latch = latch;
    }

    @Override
    protected void initialize() {
        this.subscribeEvent(DeactivationEvent.class, c -> {
            System.out.println("DeactivationCall was called for " + this.getName());
            try{
                long time = System.currentTimeMillis();
                Thread.sleep(duration);
                System.out.println(this.getName() + " Deactivated the force shield successfully and slept for " + (System.currentTimeMillis() - time)
                        + " and the expected sleep duration is: " + duration);
            }
            catch(InterruptedException i){}
            d.setR2D2Deactivate(System.currentTimeMillis() - d.getStartTime());
            this.complete(c,c.getResult());
        });
        latch.countDown();
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            System.out.println("TerminateCall was called for " + this.getName());
            d.setR2D2Terminate(System.currentTimeMillis() - d.getStartTime());
            this.terminate();
        });
    }
}

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
//            System.out.println("DeactivationCall was called for " + this.getName());
            try{
//                long time = System.currentTimeMillis();
                Thread.sleep(d.getR2D2Deactivate());
//                System.out.println(this.getName() + " Deactivated the force shield successfully and slept for " + (System.currentTimeMillis() - time)
//                        + " and the expected sleep duration is: " + d.getR2D2Deactivate());
            }
            catch(InterruptedException i){}
            d.setR2D2Deactivate(System.currentTimeMillis() - d.getR2D2Terminate());
//            System.out.println("set time for R2D2deactivate: " + (System.currentTimeMillis()) + "-" + d.getR2D2Terminate());
            this.complete(c,c.getResult());
        });
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
//            System.out.println("TerminateCall was called for " + this.getName());
            d.setR2D2Terminate(System.currentTimeMillis() - d.getR2D2Terminate());
//            System.out.println("set time for R2D2terminate: " + (System.currentTimeMillis()) + "-" + d.getR2D2Terminate());
            this.terminate();
        });
        latch.countDown();
    }
}

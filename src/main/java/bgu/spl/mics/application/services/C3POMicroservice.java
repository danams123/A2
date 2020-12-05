package bgu.spl.mics.application.services;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewok;



/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {

    private Diary d;
    private CountDownLatch latch;

    public C3POMicroservice(CountDownLatch latch) {
        super("C3PO");
        d = Diary.getInstance();
        this.latch = latch;
    }

    @Override
    protected void initialize() {
        this.subscribeEvent(AttackEvent.class, c -> {
            System.out.println("AttackCall " + c.getDuration() + " was called for " + this.getName());
            List<Ewok> toRelease = new LinkedList<>();
            for(int serial : c.getSerials()){
                Ewok e = c.getEwoks().getEwoksList().get(serial -1);
                long time = System.currentTimeMillis();
                c.getEwoks().acquire(e.getNum(), 2);
                System.out.println(this.getName() + ": The Ewok " + e.getNum() + " was acquired after waiting " + (System.currentTimeMillis() - time));
                toRelease.add(e);
            }
            try{
                long time = System.currentTimeMillis();
                Thread.sleep(c.getDuration());
                d.setTotalAttacks(d.getTotalAttacks() + 1);
                System.out.println(this.getName() + " Attacked Endor successfully and slept for " + (System.currentTimeMillis() - time)
                 + " and the expected sleep duration is: " + c.getDuration());
            }
            catch(InterruptedException i){}
            d.setC3POFinish(System.currentTimeMillis() - d.getStartTime());
            for(Ewok elem : toRelease){
                c.getEwoks().release(elem.getNum(), 2);
            }
            this.complete(c,c.getResult());
        });
        latch.countDown();
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            System.out.println("TerminateCall was called for " + this.getName());
            d.setC3POTerminate(System.currentTimeMillis() - d.getStartTime());
            this.terminate();
        });
    }
}

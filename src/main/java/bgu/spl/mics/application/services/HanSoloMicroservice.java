package bgu.spl.mics.application.services;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import bgu.spl.mics.MicroService;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewok;


/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {
    private Diary d;
    private CountDownLatch latch;

    public HanSoloMicroservice(CountDownLatch latch) {
        super("Han");
        d = Diary.getInstance();
        this.latch = latch;
    }


    @Override
    protected void initialize() {
        this.subscribeEvent(AttackEvent.class, c -> {
            System.out.println("AttackCall was called for " + this.getName());
            List<Ewok> toRelease = new LinkedList<>();
           for(int serial : c.getSerials()){
                Ewok e = c.getEwoks().getEwoksList().get(serial -1);
                try {
                    long time = System.currentTimeMillis();
                    e.acquire();
                    System.out.println("The Ewok " + e.getNum() + " was acquired after waiting " + (System.currentTimeMillis() - time));;
                }
                catch(InterruptedException i){}
               toRelease.add(e);
            }
            try{
                long time = System.currentTimeMillis();
                Thread.sleep(c.getDuration());
                System.out.println(this.getName() + " Attacked Endor successfully and slept for " + (System.currentTimeMillis() - time)
                        + " and the expected sleep duration is: " + c.getDuration());
            }
            catch(InterruptedException i){}
            d.setHanSoloFinish(System.currentTimeMillis() - d.getStartTime());
            for(Ewok elem : toRelease){
                elem.release();
            }
            this.complete(c,c.getResult());
        });
        latch.countDown();
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            System.out.println("TerminateCall was called for " + this.getName());
            d.setHanSoloTerminate(System.currentTimeMillis() - d.getStartTime());
            this.terminate();
        });
    }

}
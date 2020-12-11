package bgu.spl.mics.application.services;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


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

    public C3POMicroservice() {
        super("C3PO");
        d = Diary.getInstance();
        latch = CountDownLatch.getInstance();
    }

    @Override
    protected void initialize() {
        this.subscribeEvent(AttackEvent.class, c -> {
//            System.out.println("AttackCall " + c.getDuration() + " was called for " + this.getName());
//            ArrayList<Ewok> toRelease = new ArrayList<>();
//            for(int serial : c.getSerials()){
//                Ewok e = c.getEwoks().getEwoksList().get(serial -1);
//                long time = System.currentTimeMillis();
//                c.getEwoks().acquire(e.getNum(), 2);
//                System.out.println(this.getName() + ": The Ewok " + e.getNum() + " was acquired after waiting " + (System.currentTimeMillis() - time));
//                toRelease.add(e);
//            }
            int check = - 1;
            for (int i = 0; i < c.getSerials().size(); i++) {
                int serial = c.getSerials().get(i);
                Ewok e = c.getEwoks().getEwoksList().get(serial - 1);
                if (check != i) {
                    if (!c.getEwoks().checkWaiters(e.getNum(), 2)) {
                        try {
                            e.acquire();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    } else {
//                        System.out.println(this.getName() + " is in deadlock");
                        c.getEwoks().releaseAll(2, e.getNum());
                        check = i;
                        i = 0;
                        try {
//                            System.out.println(this.getName() + " waiting");
                            e.acquire();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
//            System.out.println(this.getName() + " finished the acquire loop");
            try{
//                long time = System.currentTimeMillis();
                Thread.sleep(c.getDuration());
                d.setTotalAttacks(d.getTotalAttacks() + 1);
//                System.out.println(this.getName() + " Attacked Endor successfully and slept for " + (System.currentTimeMillis() - time)
//                 + " and the expected sleep duration is: " + c.getDuration());
            }
            catch(InterruptedException i){}
            d.setC3POFinish(System.currentTimeMillis() - d.getC3POTerminate());
//            System.out.println("set time for C3PO finish: " + (System.currentTimeMillis()) + "-" + d.getC3POTerminate());

//            for (int i = 0; i < toRelease.size(); i++) {
//                c.getEwoks().release(toRelease.get(i).getNum(),2);
//            c.getEwoks().releaseAll(2, toRelease,2);
//            }
            c.getEwoks().releaseAll(2, -1);
            this.complete(c,c.getResult());
        });
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
//            System.out.println("TerminateCall was called for " + this.getName());
            d.setC3POTerminate(System.currentTimeMillis() - d.getC3POTerminate());
//            System.out.println("set time for C3PO terminate: " + (System.currentTimeMillis()) + "-" + d.getC3POTerminate());
            this.terminate();
        });
        latch.countDown();
    }
}

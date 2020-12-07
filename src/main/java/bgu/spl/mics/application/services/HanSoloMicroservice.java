package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    //D is created in initialize for everyone, start time will be saved in d in the fields
    //countdownlatch the same, i need to create a singleton

    public HanSoloMicroservice() {
        super("Han");
        d = Diary.getInstance();
        latch = CountDownLatch.getInstance();
    }


    @Override
    protected void initialize() {
        this.subscribeEvent(AttackEvent.class, c -> {
            System.out.println("AttackCall " + c.getDuration() + " was called for " + this.getName());
//            ArrayList<Ewok> toRelease = new ArrayList<>();
//           for(int serial : c.getSerials()){
//               //maybe we can change that to nums only instead of Ewoks? in C3PO as well
//                Ewok e = c.getEwoks().getEwoksList().get(serial - 1);
//                long time = System.currentTimeMillis();
//                c.getEwoks().acquire(e.getNum(),1);
//                System.out.println(this.getName() + ": The Ewok " + e.getNum() + " was acquired after waiting " + (System.currentTimeMillis() - time));;
//               toRelease.add(e);
//            }
            int check = -1;
            for (int i = 0; i < c.getSerials().size() && i != check; i++) {
                int serial = c.getSerials().get(i);
                Ewok e = c.getEwoks().getEwoksList().get(serial - 1);
                if(!c.getEwoks().checkDeadlock(e.getNum(), 1)) {
                    try {
                        e.acquire();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                else{
                    System.out.println(this.getName() + " is in deadlock");
                    c.getEwoks().releaseAll(1);
                    check = i;
                    i = 0;
                    try {
                        System.out.println(this.getName() + " waiting");
                        e.acquire();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            System.out.println(this.getName() + " finished the acquire loop");

            try{
                long time = System.currentTimeMillis();
                Thread.sleep(c.getDuration());
                d.setTotalAttacks(d.getTotalAttacks() + 1);
                System.out.println(this.getName() + " Attacked Endor successfully and slept for " + (System.currentTimeMillis() - time)
                        + " and the expected sleep duration is: " + c.getDuration());
            }
            catch(InterruptedException i){}
            d.setHanSoloFinish(System.currentTimeMillis() - d.getHanSoloTerminate());
//            for (int i = 0; i < toRelease.size(); i++) {
//                c.getEwoks().release(toRelease.get(i).getNum(),1);
//            }
//            c.getEwoks().releaseAll(1, toRelease,2);
            c.getEwoks().releaseAll(1);
            this.complete(c,c.getResult());
        });

        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            System.out.println("TerminateCall was called for " + this.getName());
            d.setHanSoloTerminate(System.currentTimeMillis() - d.getHanSoloTerminate());
            this.terminate();
        });
        latch.countDown();
    }

}
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

    public HanSoloMicroservice() {
        super("Han");
        d = Diary.getInstance();
        latch = CountDownLatch.getInstance();
    }


    @Override
    protected void initialize() {
        this.subscribeEvent(AttackEvent.class, c -> {
            int check = - 1;
            for (int i = 0; i < c.getSerials().size(); i++) {
                int serial = c.getSerials().get(i);
                Ewok e = c.getEwoks().getEwoksList().get(serial - 1);
                if (check != i) {
                    if (!c.getEwoks().checkWaiters(e.getNum(), 1)) {
                        try {
                            e.acquire();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        c.getEwoks().releaseAll(1, e.getNum());
                        check = i;
                        i = 0;
                        try {
                            e.acquire();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            try{
                Thread.sleep(c.getDuration());
                d.setTotalAttacks(d.getTotalAttacks() + 1);
            }
            catch(InterruptedException i){}
            d.setHanSoloFinish(System.currentTimeMillis() - d.getHanSoloTerminate());
            c.getEwoks().releaseAll(1, -1);
            this.complete(c,c.getResult());
        });

        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            d.setHanSoloTerminate(System.currentTimeMillis() - d.getHanSoloTerminate());
            this.terminate();
        });
        latch.countDown();
    }

}
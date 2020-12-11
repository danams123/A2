package bgu.spl.mics.application.services;

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
                        c.getEwoks().releaseAll(2, e.getNum());
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
            d.setC3POFinish(System.currentTimeMillis() - d.getC3POTerminate());
            c.getEwoks().releaseAll(2, -1);
            this.complete(c,c.getResult());
        });
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            d.setC3POTerminate(System.currentTimeMillis() - d.getC3POTerminate());
            this.terminate();
        });
        latch.countDown();
    }
}

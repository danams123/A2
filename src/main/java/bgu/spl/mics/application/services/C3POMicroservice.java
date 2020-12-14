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

    /**
     * Initialize() of C3POMicroservices calls subscribeEvent() for AttackEvent and subscribeBroadcast() for
     * TerminateBroadcast. In this functions , they create the equivalent callbacks using lambda expressions.
     * Callback of AttackEvent adds the required Ewoks for attack and sleeps for the input time, than calls complete().
     * Callback of TerminationBroadcast calls Terminate().
     */    @Override
    protected void initialize() {
        this.subscribeEvent(AttackEvent.class, c -> {
            //AttackCallback
            int check = - 1;
            //Ewoks aquire loop, won't stop until all ewoks needed are acquired
            for (int i = 0; i < c.getSerials().size(); i++) {
                int serial = c.getSerials().get(i);
                Ewok e = c.getEwoks().getEwoksList().get(serial - 1);
                if (check != i) {
                    //if checkWaiters() returns false, it's safe to acquire the ewok
                    if (!c.getEwoks().checkWaiters(e.getNum(), 2)) {
                        try {
                            e.acquire();
                        }
                        catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    else {
                        //if checkWaiters() returns true, the microservice releases all the ewoks it holds and calls
                        //acquire on the ewok that is occupied so it could use it's wait() func until the other
                        //microservice finishes it's attack, the thread will be notified when the attack ends.
                        c.getEwoks().releaseAll(2, e.getNum());
                        //after releasing all the ewoks, we reset the 'i' count to 0 so that the microservice will
                        //restart it's acquire loop, now that it holds the current ewok (after waiting on it for the
                        //attack to end), we save it on 'check' so it won't try to acquire it again after we reset 'i'.
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
            d.setC3POFinish(System.currentTimeMillis());
            c.getEwoks().releaseAll(2, -1);
            this.complete(c,c.getResult());
        });
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            //TerminateCallback
            d.setC3POTerminate(System.currentTimeMillis());
            this.terminate();
        });
        latch.countDown();
    }
}

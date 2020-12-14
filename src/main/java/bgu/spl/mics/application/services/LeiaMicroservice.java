package bgu.spl.mics.application.services;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ConcurrentLinkedDeque;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
    private ConcurrentLinkedDeque<Future> futures;
    private Attack[] attacks;
    private Diary d;

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
        d = Diary.getInstance();
        futures = new ConcurrentLinkedDeque<Future>();
    }

    /**
     * Initialize() of LeiaMicroservices calls sendEvent() for all the attack events, deactivation event,
     * BombDestroyer event and Terminate broadcast. on top of that is calls subscribeBroadcast() for
     * TerminateBroadcast. In this function, they create the equivalent callbacks using lambda expressions.
     * In each sendEvent(), it saves the equivalent future and uses the get() function which makes it wait until
     * all the futures are resolved.
     * Callback of TerminationBroadcast calls Terminate().
     */
    @Override
    protected void initialize() {
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            //TerminateCallback
            d.setLeiaTerminate(System.currentTimeMillis());
            this.terminate();
        });
        //sort of attacks for better runtime
        Arrays.sort(attacks, Comparator.comparingInt(o -> (o.getDuration() + (o.getSerials().size() * 1000))));
        for(Attack elem : attacks){
            futures.add(this.sendEvent(new AttackEvent(elem.getDuration(),elem.getSerials(), null)));
        }
        //futures queue helps to send all the events and than call get() for all the futures
        for(Future f: futures){
            try {
                f.get();
            } catch (InterruptedException e) {}
        }
        try{
            this.sendEvent(new DeactivationEvent()).get();}
        catch(InterruptedException i){}
        try{
            this.sendEvent(new BombDestroyerEvent()).get();}
        catch(InterruptedException i){}
        this.sendBroadcast(new TerminateBroadcast());
    }

}

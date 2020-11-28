package bgu.spl.mics.application.services;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
    private Attack[] attacks;
    private TerminateCallback t;
    private ConcurrentLinkedDeque<Future> futures;

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
        t = new TerminateCallback();
        futures = new ConcurrentLinkedDeque<>();
    }

    @Override
    protected void initialize() {
        this.subscribeBroadcast(TerminateBroadcast.class, t);
        for(Attack elem : attacks){
            futures.add(this.sendEvent(new AttackEvent(elem.getDuration(),elem.getSerials())));
        }
        while(!futures.isEmpty()){
            Future f = futures.poll();
            f.get(); //is it good?
        }
        futures.add(this.sendEvent(new DeactivationEvent()));
        futures.poll().get();//is it good?
        futures.add(this.sendEvent(new BombDestroyerEvent()));
        futures.poll().get();//is it good?
        this.sendBroadcast(new TerminateBroadcast());
    }
}

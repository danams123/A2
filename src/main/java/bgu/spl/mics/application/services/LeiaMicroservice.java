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
    private TerminateCallback t; //will be added to a Hashmap of Microservice
    private ConcurrentLinkedDeque<Future> futures; // do we need concurrent?

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
        t = new TerminateCallback();
        futures = new ConcurrentLinkedDeque<>(); //in Microservice
    }

    @Override
    protected void initialize() {
        this.subscribeBroadcast(TerminateBroadcast.class, t);
        for(Attack elem : attacks){
            futures.add(this.sendEvent(new AttackEvent(elem.getDuration(),elem.getSerials())));
        }
        while(!futures.isEmpty()){ //do we need blockingqueue?
            Future f = futures.poll();
            f.get(); //is it good? do we need to consider the option in which the first event takes longer to finish than the others?
            //prioritize by durations in attacks and by serials.
        }
        futures.add(this.sendEvent(new DeactivationEvent()));
        futures.poll().get();//is it good?
        futures.add(this.sendEvent(new BombDestroyerEvent()));
        futures.poll().get();//is it good?
        this.sendBroadcast(new TerminateBroadcast());
    }
}

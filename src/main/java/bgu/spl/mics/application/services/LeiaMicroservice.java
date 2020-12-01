package bgu.spl.mics.application.services;
import java.util.ArrayList;
import java.util.List;
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
    private Attack[] attacks;
    private Ewoks E;

    public LeiaMicroservice(Attack[] attacks, Ewoks _E) {
        super("Leia");
        this.attacks = attacks;
        E = _E;
    }

    @Override
    protected void initialize() {
        this.subscribeBroadcast(TerminateBroadcast.class, new TerminateCallback());
        //sort of attacks
        for(Attack elem : attacks){
            try{
                this.sendEvent(new AttackEvent(elem.getDuration(),elem.getSerials(),E)).get();}
            catch(InterruptedException i){}
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

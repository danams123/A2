package bgu.spl.mics.application.services;
import java.util.Arrays;
import java.util.Comparator;

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
    private Diary d;

    public LeiaMicroservice(Attack[] attacks, Ewoks _E, Diary _d) {
        super("Leia");
        this.attacks = attacks;
        E = _E;
        d = _d;
    }

    @Override
    protected void initialize() {
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            System.out.println("TerminateCall was called for " + this.getName());
            d.setLeiaTerminate(System.currentTimeMillis() - d.getStartTime());
            this.terminate();
        });
        //sort of attacks
        System.out.println("attacks array before the sort is " + attacks);
        Arrays.sort(attacks, Comparator.comparingInt(o -> (o.getDuration() + o.getSerials().size())));
        System.out.println("attacks array after the sort is " + attacks);
       //check this sort fucker
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

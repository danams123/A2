package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.List;

/**
 * A class implementing {@link Event}. When a microservice recieves an object of this type, it calls for a callback
 * representing an Attack by acquiring the appropriate {@link Ewoks}, after that calling sleep() for the input
 * duration and than complete().
 */
public class AttackEvent implements Event<Boolean> {
    final private long duration;
    final private List<Integer> serials;
    final private Ewoks E;
    final private boolean result;

    public AttackEvent(long _duration, List<Integer> _serials, Ewoks ewoks){
        duration = _duration;
        serials = _serials;
        E = Ewoks.getInstance();
        result = true;
    }

    public long getDuration(){return duration;}

    public List<Integer> getSerials(){return serials;}

    public Ewoks getEwoks(){return E;}

    public boolean getResult(){ return result;}

}

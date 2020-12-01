package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.List;

public class AttackEvent implements Event<Boolean> {
    final private long duration;
    final private List<Integer> serials;
    final private Ewoks E;

    public AttackEvent(long _duration, List<Integer> _serials, Ewoks _E){
        duration = _duration;
        serials = _serials;
        E = _E;
    }

    public long getDuration(){return duration;}

    public List<Integer> getSerials(){return serials;}

    public Ewoks getEwoks(){return E;}
}

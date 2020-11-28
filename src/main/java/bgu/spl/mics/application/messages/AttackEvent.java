package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

import java.util.List;

public class AttackEvent implements Event<Boolean> {
    final private long duration;
    final List<Integer> serials;

    public AttackEvent(long _duration, List<Integer> _serials){
        duration = _duration;
        serials = _serials;
    }

    public long getDuration(){return duration;}

    public List<Integer> getSerials(){return serials;}
}

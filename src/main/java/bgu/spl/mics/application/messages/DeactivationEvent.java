package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class DeactivationEvent implements Event<Boolean> {
    private Boolean result;
    private long duration;

    public DeactivationEvent(long _duration){
        result = true;
        duration = _duration;
    };

}

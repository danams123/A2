package bgu.spl.mics.application.messages;

import bgu.spl.mics.Callback;

public class DeactivationCallback implements Callback {
    private long duration;

    public DeactivationCallback (long _duration){
        duration = _duration;

    };
    public void call(Object c) {//the deactivation of the shield generator is simulated by a sleep of R2D2 for a given amount of time

    }
}

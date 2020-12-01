package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class DeactivationEvent implements Event<Boolean> {
    private Boolean result;

    public DeactivationEvent(){
        result = true;
    }

    public boolean getResult(){ return result;}

}

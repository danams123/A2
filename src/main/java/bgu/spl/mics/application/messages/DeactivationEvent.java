package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class DeactivationEvent implements Event<Boolean> {
    private final Boolean result;
    private final String name;

    public DeactivationEvent(){
        name = "DeactivationEvent";
        result = true;
    }

    public boolean getResult(){ return result;}

    public String getName(){return name;}
}

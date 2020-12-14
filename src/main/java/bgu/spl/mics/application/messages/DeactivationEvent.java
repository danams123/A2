package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;


/**
 * A class implementing {@link Event}. When a microservice recieves an object of this type, it calls for a callback
 * representing a Deactivation of the force shield by calling sleep() for the input duration and than complete().
 */
public class DeactivationEvent implements Event<Boolean> {
    private final Boolean result;

    public DeactivationEvent(){
        result = true;
    }

    public boolean getResult(){ return result;}
}

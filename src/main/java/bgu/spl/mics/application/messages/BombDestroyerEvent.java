package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

/**
 * A class implementing {@link Event}. When a microservice recieves an object of this type, it calls for a callback
 * representing a BombDestroyer of the Death tar by calling sleep() for the input duration and than complete().
 */
public class BombDestroyerEvent implements Event<Boolean> {
    private final boolean result;

    public BombDestroyerEvent(){
        result = true;
    }
    public boolean getResult(){ return result;}
}

package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class BombDestroyerEvent implements Event<Boolean> {
    private final boolean result;

    public BombDestroyerEvent(){
        result = true;
    }
    public boolean getResult(){ return result;}
}

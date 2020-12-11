package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class BombDestroyerEvent implements Event<Boolean> {
    private final boolean result;
//    private final String name;

    public BombDestroyerEvent(){
        result = true;
//        name = "BombDestroyerEvent";
    }
    public boolean getResult(){ return result;}

//    public String getName(){return name;}
}

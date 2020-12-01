package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.MicroService;

public class TerminateBroadcast implements Broadcast {

    MicroService m;

    public TerminateBroadcast(MicroService _m){
        m =_m;
    }
}

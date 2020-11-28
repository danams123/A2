package bgu.spl.mics;

import bgu.spl.mics.Event;

public class ExampleEvent implements Event{

    private String senderName;

    public ExampleEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}
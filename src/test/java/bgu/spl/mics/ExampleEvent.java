package bgu.spl.mics;

public class ExampleEvent implements Event {

    private String senderName;

    public ExampleEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}

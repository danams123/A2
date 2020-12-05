package bgu.spl.mics;

public class ExampleBroadcast implements Broadcast {

    private String senderName;

    public ExampleBroadcast(String senderName) {
        this.senderName = senderName;
    }

    public String getName() {
        return senderName;
    }
}

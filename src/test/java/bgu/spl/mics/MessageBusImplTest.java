package bgu.spl.mics;

import bgu.spl.mics.example.messages.ExampleEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {

    private MessageBusImpl mb;
    private MicroService m1;
    private MicroService m2;
    private ExampleEvent e;
    private Broadcast b;
    private Callback c;

    @BeforeEach
    void setUp() {
        mb = MessageBusImpl.getInstance();
        m1 = new MicroService("") {
            @Override
            protected void initialize() {}
        };
        m2 = new MicroService("") {
            @Override
            protected void initialize() {}
        };
        e = new ExampleEvent("");
        b = () -> {return "";};
        c = (Object o) -> {o = 1;};
    }

    @Test
    void subscribeEvent() {
        mb.register(m1);
        mb.subscribeEvent(e.getClass(),m1);
        mb.sendEvent(e);
        try{
            assertTrue(mb.awaitMessage(m1).equals(e));}//how to handle if it didnt got the message??
        catch(InterruptedException i){
            System.out.println("interrupted");
        }
        mb.unregister(m1);
    }

    @Test
    void subscribeBroadcast() {
        mb.register(m1);
        mb.subscribeBroadcast(b.getClass(),m1);
        mb.sendBroadcast(b);
        try{
            assertTrue(mb.awaitMessage(m1).equals(b));}//how to handle if it didnt got the message??
        catch(InterruptedException i){
            System.out.println("interrupted");
        }
        mb.unregister(m1);
    }

    @Test
    void complete() { //awaitMessage for Darth Yair
        mb.register(m1);
        mb.register(m2);
        m1.subscribeEvent(e.getClass(),c);
        Future f = m2.sendEvent(e);
        assertFalse(f.isDone());
        try{
            mb.awaitMessage(m1);}//how to handle if it didnt got the message??
        catch(InterruptedException i){
            System.out.println("interrupted");
        }
        mb.complete(e,null);//what should i write in the result?
        assertTrue(f.isDone());
        mb.unregister(m1);
        mb.unregister(m2);
    }

    @Test
    void sendBroadcast() {
        //whats the difference between this and subsB?
    }

    @Test
    void sendEvent() {  //whats the difference between this and subsE?
    }

    @Test
    void register() {
    }

    @Test
    void unregister() {

    }

    @Test
    void awaitMessage() { // should i test the blocking part? and how?
        mb.register(m1);
        m1.subscribeEvent(e.getClass(),c);
        mb.sendEvent(e);
        try{
            assertEquals(mb.awaitMessage(m1),e);}
        catch(InterruptedException i){
            System.out.println("interrupted");
        }
        mb.unregister(m1);
    }
}
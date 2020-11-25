package bgu.spl.mics;

import bgu.spl.mics.example.messages.ExampleEvent;
import jdk.internal.vm.compiler.collections.EconomicMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {

    private MessageBusImpl mb;
    private MicroService m1;
    private MicroService m2;
    private Event<Object> e;
    private Broadcast b;
    private Callback c;

    @BeforeEach
    void setUp() {
        mb = MessageBusImpl.getInstance();
        m1 = new MicroService("") {
            @Override
            protected void initialize() {System.out.println("");}
        };
        m2 = new MicroService("") {
            @Override
            protected void initialize() {System.out.println("");}
        };
        e = () -> {System.out.println("");};
        b = () -> {System.out.println("");};
        c = (Object c1) -> {System.out.println("");};
    }

    @AfterEach
    void tearDown() {mb = null; m1 = null; m2 = null; e = null; b = null; c = null;}

    @Test
    void subscribeEvent() {
        Class<Event<Object>> t = null; //is it ok?
        mb.subscribeEvent(t,m1);
        m2.sendEvent(e);
        try{
            assertTrue(mb.awaitMessage(m1).equals(e));}//how to handle if it didnt got the message??
        catch(InterruptedException i){
            System.out.println("interrupted");
        }
    }

    @Test
    void subscribeBroadcast() {
        Class<Broadcast> t = null;
        mb.subscribeBroadcast(t,m1);
        m2.sendBroadcast(b);
        try{
            assertTrue(mb.awaitMessage(m1).equals(b));}//how to handle if it didnt got the message??
        catch(InterruptedException i){
            System.out.println("interrupted");
        }

    }

    @Test
    void complete() {
        Class<Event> t = null;
        m1.subscribeEvent(t,c);
        Future f = m2.sendEvent(e);
        assertFalse(f.isDone());
        try{
            mb.awaitMessage(m1);}//how to handle if it didnt got the message??
        catch(InterruptedException i){
            System.out.println("interrupted");
        }
        mb.complete(e,null);//what should i write in the result?
        assertTrue(f.isDone());
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
        Class<Event> t = null;
        m1.subscribeEvent(t,c);
        m2.sendEvent(e);
        try{
            assertTrue(mb.awaitMessage(m1).equals(e));}//how to handle if it didnt got the message??
        catch(InterruptedException i){
            System.out.println("interrupted");
        }
    }
}
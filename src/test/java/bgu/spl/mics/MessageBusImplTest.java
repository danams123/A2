package bgu.spl.mics;

import bgu.spl.mics.ExampleEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {

    private MessageBusImpl mb;
    private MicroService m1;
    private MicroService m2;
    private Event e;
    private Broadcast b;
    private Callback c;

    @BeforeEach
    void setUp() {
        mb = MessageBusImpl.getInstance();
        m1 = new MicroService("") {
            @Override
            protected void initialize() {}
        };
//        e = new ExampleEvent("");
        e = () -> {return "";};
        b = () -> {return "";};
        c = (Object o) -> {o = 1;};
    }

    @Test
    void subscribeEvent() {
        mb.register(m1);
        ExampleEvent E = new ExampleEvent("");
        mb.subscribeEvent(E.getClass(), m1);
        mb.sendEvent(E);
        try{
            assertTrue(mb.awaitMessage(m1).equals(E));}//how to handle if it didnt got the message??
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
    void complete() throws InterruptedException {
        mb.register(m1);
        m1.subscribeEvent(e.getClass(),c);
        Future<Integer> f = mb.sendEvent(e);
        assertFalse(f.isDone());
        Event E = null;
        try{
            E = (Event) mb.awaitMessage(m1);} //how to handle if it didnt got the message??
        catch(InterruptedException i){
            System.out.println("interrupted");
        }
        mb.complete(E,5);//what should i write in the result?
        assertEquals(f.get(),5);
        mb.unregister(m1);
    }

    @Test
    void sendBroadcast() {
        mb.register(m1);
        m1.subscribeBroadcast(b.getClass() , c);
        mb.sendBroadcast(b);
        try{
            assertEquals(mb.awaitMessage(m1),b);}
        catch(InterruptedException j){
            System.out.println("interrupted");
        }
        mb.unregister(m1);
    }

    @Test
    void sendEvent() {
        mb.register(m1);
        m1.subscribeEvent(e.getClass() , c);
        mb.sendEvent(e);
        try{
            assertEquals(mb.awaitMessage(m1),e);}
        catch(InterruptedException j){
            System.out.println("interrupted");
        }
        mb.unregister(m1);
    }

    @Test
    void register() {
        mb.register(m1);
        m1.subscribeEvent(e.getClass() , c);
        mb.sendEvent(e);
        try{
            assertEquals(mb.awaitMessage(m1),e);}
        catch(InterruptedException j){
            System.out.println("interrupted");
        }
        mb.unregister(m1);
    }

    @Test
    void awaitMessage() { // should i test the blocking part? and how?
        mb.register(m1);
        m1.subscribeEvent(e.getClass(),c);
//        try{
//            assertNull(mb.awaitMessage(m1));}
//        catch(InterruptedException i){
//            System.out.println("interrupted");
//        }
        mb.sendEvent(e);
        try{
            assertEquals(mb.awaitMessage(m1),e);}
        catch(InterruptedException i){
            System.out.println("interrupted");
        }
        mb.unregister(m1);
    }
}
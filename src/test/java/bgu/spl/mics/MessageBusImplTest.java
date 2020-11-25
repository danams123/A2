package bgu.spl.mics;

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
//     mb = MessageBusImpl.getInstance();
     m1 = new MicroService("") {
      @Override
      protected void initialize() {System.out.println("");}
     };
     m2 = new MicroService("") {
      @Override
      protected void initialize() {System.out.println("");}
     };
//     e = () -> {System.out.println("");};
//     b = () -> {System.out.println("");};
     c = (Object c1) -> {System.out.println("");};
    }

    @AfterEach
    void tearDown() {
     mb = null; m1 = null; m2 = null; e = null; b = null; c = null;
    }

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
     Class<Broadcast> type = null; // check
     mb.subscribeBroadcast(type,m1);
     mb.subscribeBroadcast(type,m2);
     mb.sendBroadcast(b);

     //get m1 and m2 queue and check if event type is there

    }

    @Test
    void sendEvent() {

    }

    @Test
    void register() {
     Class<Event> t = null;
     m1.subscribeEvent(t,c);
     mb.sendEvent(e);
     m1.run();

    }

    @Test
    void unregister() {

    }

    @Test
    void awaitMessage() {
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
}
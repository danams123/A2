package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {

    private MessageBusImpl mb;
    private MicroService m1;
    private ExampleEvent e;
    private ExampleBroadcast b;
    private Callback c;

    @BeforeEach
    void setUp() {
        mb = MessageBusImpl.getInstance();
        m1 = new MicroService("") {
            @Override
            protected void initialize() {}
        };
        e = new ExampleEvent("");
        b = new ExampleBroadcast("");
        c = (Object o) -> {o = 1;};
    }

    @Test
    void subscribeEvent() {
//        mb.register(m1);
//        AttackEvent E = new AttackEvent();
//        mb.subscribeEvent(E.getClass(), m1);
//        mb.sendEvent(E);
//        try{
//            assertTrue(mb.awaitMessage(m1).equals(E));}
//        catch(InterruptedException i){
//            System.out.println("interrupted");
//        }
//        mb.unregister(m1);
    }

    @Test
    void subscribeBroadcast() {
        mb.register(m1);
        mb.subscribeBroadcast(b.getClass(),m1);
        mb.sendBroadcast(b);
        try{
            assertTrue(mb.awaitMessage(m1).equals(b));}
        catch(InterruptedException i){
            System.out.println("interrupted");
        }
        mb.unregister(m1);
    }

    @Test
    void complete(){
        ExampleEvent e1 = new ExampleEvent("");
        mb.register(m1);
        m1.subscribeEvent(e1.getClass(),c);
        Future<Integer> f = mb.sendEvent(e1);
        assertFalse(f.isDone());
        try{
            mb.awaitMessage(m1);}
        catch(InterruptedException i){
            System.out.println("interrupted");
        }
        /*
        We dont need to create a new event from awaitmessage() output because we assume it works properly,
        therefore e is the same event as the one awaitmessage() will return, so putting e as an argument in complete()
         or the event we get from awaitmessage() won't change the outcome of complete(), which we are testing here.
         */
        mb.complete(e1,5);
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
        Future f = mb.sendEvent(e);
        assertNull(f);
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
    void awaitMessage(){
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
package bgu.spl.mics;
import java.util.HashMap;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */

//Singleton
public class MessageBusImpl implements MessageBus {

	private HashMap<MicroService,Message> services;
	private HashMap<Class<? extends Message>,MicroService> messages;
	private HashMap<Event,Future> futures;
	//maybe change the fields to some other structures? queue? vector? think about it.

	private static class MessageBusHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private MessageBusImpl(){
	//what do we need here?
		services = new HashMap<>();
		messages = new HashMap<>();
		futures = new HashMap<>();
	}

	public static MessageBusImpl getInstance() {
		return MessageBusHolder.instance;
	}


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        messages.put(type,m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        messages.put(type,m);
	}

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
        futures.get(e).resolve(result);
        futures.remove(e);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
	    MicroService swap = messages.get(b.getClass());
        services.put(swap,b);
        messages.remove(b.getClass(),swap);
        messages.put(b.getClass(),swap);
	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
        Future<T> output = new Future<>();
        MicroService swap = messages.get(e.getClass());
        services.put(swap,e);
        messages.remove(e.getClass(),swap);
        messages.put(e.getClass(),swap);
        //need to add interrupt here - do it carefully, or notifyall?
        futures.put(e,output);
		return output;
	}

	@Override
	public void register(MicroService m) {
        services.put(m,null);
	}

	@Override
	public void unregister(MicroService m) {
        services.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
        while(services.get(m) == null){
            Thread.currentThread().wait(); //do i need sleep here? notify gets interruptedexception?
        }
		Message output = services.get(m);
        services.remove(m,output);
        return output;
	}
}

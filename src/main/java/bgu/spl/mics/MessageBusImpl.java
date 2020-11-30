package bgu.spl.mics;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */

//Singleton
public class MessageBusImpl implements MessageBus {

	private ConcurrentHashMap<MicroService, LinkedBlockingDeque<Message>> services;
	private ConcurrentHashMap<Class<? extends Message>,LinkedBlockingDeque<MicroService>> messages;
	private ConcurrentHashMap<Event,Future> futures;
	private Object lock1, lock2, lock3, lock4;
	//maybe change the fields to some other structures? queue? vector? think about it.

	private static class MessageBusHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private MessageBusImpl(){
		services = new ConcurrentHashMap<>();
		messages = new ConcurrentHashMap<>();
		futures = new ConcurrentHashMap<>();
		lock1 = new Object();
		lock2 = new Object();
		lock3 = new Object();
		lock4 = new Object();
	}

	public static MessageBusImpl getInstance() {
		return MessageBusHolder.instance;
	}


	@Override //TS?
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if(services.containsKey(m)) { //so there won't be multiple instances in the queue of messages
			synchronized (lock1) {//so we wont create two queues
				if (messages.containsKey(type)) { //so it will initialize properly in the first time
					messages.put(type, new LinkedBlockingDeque<>());
				}
			}
			messages.get(type).add(m);
			}
		}

	@Override //TS?
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if(services.containsKey(m)) { //so there won't be multiple instances in the queue of messages
			synchronized (lock2) {//so we wont create two queues
				if (messages.containsKey(type)) { //so it will initialize properly in the first time
					messages.put(type, new LinkedBlockingDeque<>());
				}
			}
			messages.get(type).add(m);
			}
		}

	@Override @SuppressWarnings("unchecked") //TS
	public <T> void complete(Event<T> e, T result) {
        futures.get(e).resolve(result);
		futures.remove(e);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
	    LinkedBlockingDeque<MicroService> toSend = messages.get(b.getClass());
	    for(MicroService elem: toSend){
	    	synchronized (lock3) { // so it wont unregister while we add
				if (services.get(elem) != null) {
					services.get(elem).add(b);
				}
			}
		}
	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
        Future<T> output = null;
        	MicroService swap = messages.get(e.getClass()).poll();
        	synchronized (lock4){
        	if (services.get(swap) != null) {
        		services.get(swap).add(e);
        		messages.get(e.getClass()).add(swap);
        		output = new Future<>();
        		futures.put(e, output);
			}
		}
        return output;
	}

	@Override //TS
	public void register(MicroService m) {
        services.put(m,new LinkedBlockingDeque<>());
	}

	@Override //TS
	public void unregister(MicroService m) {
		synchronized (lock3) { //to save sendB
			synchronized (lock4) { //to save sendE
				services.remove(m);
				for(Class<? extends Message> key: messages.keySet()){
					messages.get(key).remove(m);
				}
			}
		}
	}

	@Override //TS
	public Message awaitMessage(MicroService m) throws InterruptedException {
		return services.get(m).take(); //check if we need to use try catch or throws is enough?
	}
}

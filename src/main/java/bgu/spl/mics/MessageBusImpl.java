package bgu.spl.mics;
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

	private static class SingletonHolder {
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
		System.out.println("The MessageBus has been created!");
	}

	public static MessageBusImpl getInstance() {
		return SingletonHolder.instance;
	}


	@Override //TS?
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		System.out.println(m.getName() + " called subscribeEvent");
		if(services.containsKey(m)) { //so there won't be multiple instances in the queue of messages
			synchronized (lock1) {//so we wont create two queues
				if (!messages.containsKey(type)) { //so it will initialize properly in the first time
					messages.put(type, new LinkedBlockingDeque<>());
					System.out.println("A new queue has been created in messages for " + type);
				}
				messages.get(type).add(m);
				System.out.println(m + " was added to messages in the queue for " + type);
				System.out.println("the current keySet of messages is " + messages.keySet());
			}
		}
		}

	@Override //TS?
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		System.out.println(m.getName() + " called subscribeBroadcast");
		if(services.containsKey(m)) { //so there won't be multiple instances in the queue of messages
			synchronized (lock2) {//so we wont create two queues
				if (!messages.containsKey(type)) { //so it will initialize properly in the first time
					messages.put(type, new LinkedBlockingDeque<>());
					System.out.println("A new queue has been created in messages for " + type);
				}
				messages.get(type).add(m);
				System.out.println(m + " was added to messages in the queue for " + type);
				System.out.println("the current keySet of messages is " + messages.keySet());

			}
		}
	}

	@Override @SuppressWarnings("unchecked") //TS
	public <T> void complete(Event<T> e, T result) {
        futures.get(e).resolve(result);
		System.out.println(futures.get(e) + " has been resolved with " + result);
		futures.remove(e);
		System.out.println("the current keySet of futures is " + futures.keySet());

	}

	@Override
	public void sendBroadcast(Broadcast b) {
	    LinkedBlockingDeque<MicroService> toSend = messages.get(b.getClass());
		System.out.println("MessageBus needs to send the Broadcast " + b);
	    for(MicroService elem: toSend){
	    	synchronized (lock3) { // so it wont unregister while we add
				System.out.println("the value of elem is: " + elem);
				if (services.get(elem) != null) {
					services.get(elem).add(b);
					System.out.println(b + " was added to " + elem + " in services: " + services.get(elem));
				}
			}
		}
	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
        Future<T> output = null;
        MicroService swap = messages.get(e.getClass()).poll();
		System.out.println(swap + " is out of messages for " + e.getClass() + " the queue is: " + messages.get(e.getClass()));
		System.out.println("MessageBus needs to send the Event " + e + " to " + swap);
        	synchronized (lock4){
        	if (services.get(swap) != null) {
        		services.get(swap).add(e);
				System.out.println(e + " was added to " + swap + "in services: " + services.get(swap));
        		messages.get(e.getClass()).add(swap);
				System.out.println(swap + " is back in the end of the queue: " + messages.get(e.getClass()));
        		output = new Future<>();
				System.out.println("the future output is: " + output);
        		futures.put(e, output);
				System.out.println(output + " is in futures: " + futures.keySet());
			}
		}
        return output;
	}

	@Override //TS
	public void register(MicroService m) {
        services.put(m,new LinkedBlockingDeque<>());
		System.out.println(m.getName() + " is now in services: " + services.keySet());
	}

	@Override //TS
	public void unregister(MicroService m) {
		synchronized (lock3) { //to save sendB
			synchronized (lock4) { //to save sendE
				services.remove(m);
				System.out.println(m.getName() + " is out of services: " + services.keySet());
				for(Class<? extends Message> key: messages.keySet()){
					messages.get(key).remove(m);
					System.out.println(m.getName() + " is out of messages for " + key + " check: " + messages.get(key));
				}
			}
		}
	}

	@Override //TS
	public Message awaitMessage(MicroService m) throws InterruptedException {
		Message forDebug = services.get(m).take();
		System.out.println(forDebug + " is out of services for " + m.getName() + " check: " + services.get(m));
		return forDebug;
//		return services.get(m).take(); //check if we need to use try catch or throws is enough?
	}
}

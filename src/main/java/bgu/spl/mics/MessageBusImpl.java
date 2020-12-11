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
	private ConcurrentHashMap<Class<? extends Event>,LinkedBlockingDeque<MicroService>> events;
	private ConcurrentHashMap<Class<? extends Broadcast>,LinkedBlockingDeque<MicroService>> broadcasts;
	private ConcurrentHashMap<Event,Future> futures;
	private Object lock1, lock2;
	//maybe change the fields to some other structures? queue? vector? think about it.

	private static class SingletonHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private MessageBusImpl(){
		services = new ConcurrentHashMap<MicroService, LinkedBlockingDeque<Message>>();
		events = new ConcurrentHashMap<Class<? extends Event>,LinkedBlockingDeque<MicroService>>();
		broadcasts = new ConcurrentHashMap<Class<? extends Broadcast>,LinkedBlockingDeque<MicroService>>();
		futures = new ConcurrentHashMap<Event,Future>();
		lock1 = new Object();
		lock2 = new Object();
//		System.out.println("The MessageBus has been created!");
	}

	public static MessageBusImpl getInstance() {
		return SingletonHolder.instance;
	}


	@Override //TS?
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
//		System.out.println(m.getName() + " called subscribeEvent in MessageBus");
//		if(services.containsKey(m)) { //so there won't be multiple instances in the queue of messages
//			synchronized (lock1) {//so we wont create two queues
//				if (!messages.containsKey(type)) { //so it will initialize properly in the first time
//					messages.put(type, new LinkedBlockingDeque<>());
////					System.out.println(m.getName() + ": A new queue has been created in messages for " + type.getName());
//				}
//				messages.get(type).add(m);
////				System.out.println(m.getName() + " was added to messages in the queue for " + type.getName());
////				System.out.println(m.getName() + ": the current keySet of messages is " + messages.keySet());
//			}
			events.putIfAbsent(type, new LinkedBlockingDeque<>());
			events.get(type).add(m);
		}
//		}

	@Override //TS?
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
//		System.out.println(m.getName() + " called subscribeBroadcast in MessageBus");
//		if(services.containsKey(m)) { //so there won't be multiple instances in the queue of messages
//			synchronized (lock2) {//so we wont create two queues
//				if (!messages.containsKey(type)) { //so it will initialize properly in the first time
//					messages.put(type, new LinkedBlockingDeque<>());
//					System.out.println(m.getName() + ": A new queue has been created in messages for " + type.getName());
//				}
//				messages.get(type).add(m);
//				System.out.println(m.getName() + " was added to messages in the queue for " + type.getName());
//				System.out.println(m.getName() + ": the current keySet of messages is " + messages.keySet());
//
//			}
//		}
		broadcasts.putIfAbsent(type, new LinkedBlockingDeque<>());
		broadcasts.get(type).add(m);
	}

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		synchronized (lock2) {
//			System.out.println("the current keySet of futures is " + futures.keySet());
			if (futures.get(e) != null) {
				futures.get(e).resolve(result);
//				System.out.println(futures.get(e) + " has been resolved with " + result);
//				futures.remove(e);
//				System.out.println("the current keySet of futures is " + futures.keySet());
//				System.out.println(futures.size() + " is the size of futures");
			}
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		synchronized (lock1) { // so it wont unregister while we add
			LinkedBlockingDeque<MicroService> toSend = broadcasts.get(b.getClass());
//			System.out.println("MessageBus needs to send the Broadcast " );
			for(MicroService elem: toSend){
//				System.out.println("the value of elem is: " + elem.getName());
				if (services.get(elem) != null) {
					services.get(elem).add(b);
//					System.out.println(b + " was added to " + elem.getName() + " in services: " + services.get(elem));
				}
			}
		}
	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
			Future<T> output = null;
//			System.out.println("in sendevent of messagebus");
		synchronized (lock2) {
			if (events.get(e.getClass()) != null) {
//				System.out.println("passed the first barrier");
//				output = new Future<>();
//				futures.put(e, output);
				MicroService swap = events.get(e.getClass()).poll();
					if (swap != null && services.get(swap) != null) {
//					System.out.println(swap.getName() + " is out of messages for " + e.getClass().getName() + " the queue is: ");
//					System.out.println("MessageBus needs to send the Event " + " to " + swap.getName());
						services.get(swap).add(e);
//					System.out.println(e.getName() + " was added to " + swap.getName() + " in services: " + services.get(swap));
						events.get(e.getClass()).add(swap);
//					System.out.println(swap.getName() + " is back in the end of the queue: " + messages.get(e.getClass()));
						output = new Future<>();
//					System.out.println("the future output is: " + output);
						futures.put(e, output);
//					System.out.println(output + " the current keySet of futures is: " + futures.keySet());
//					System.out.println(futures.size() + " is the size of futures");
					}
				}
			}
			return output;
		}

	@Override //TS
	public void register(MicroService m) {
        services.put(m,new LinkedBlockingDeque<>());
//		System.out.println(m.getName() + " is now in services: " + services.keySet());
	}

	@Override //TS
	public void unregister(MicroService m) {
		synchronized (m) {
			services.remove(m);
		}
		synchronized (lock2) { //to save sendE
//				System.out.println(m.getName() + " is out of services: " + services.keySet());
			for (LinkedBlockingDeque<MicroService> elem : events.values()) {
//					events.get(key).remove(m);
//					if(events.get(key).size() == 1){
//						events.remove(key);
//					}
				elem.remove(m);
//					System.out.println(m.getName() + " is out of messages for " + key.getName() + " check: " + events.get(key));
			}
		}
		synchronized (lock1) { //to save sendB
			for (LinkedBlockingDeque<MicroService> elem : broadcasts.values()) {
//					events.get(key).remove(m);
//					if(events.get(key).size() == 1){
//						events.remove(key);
//					}
				elem.remove(m);
			}
		}
//		while (!futuresToResolve.isEmpty()) {
//			Message message = futuresToResolve.poll();
//			Future<?> future = futures.get(message);
//			if (future != null)
//				future.resolve(null);
//		}
	}

	@Override //TS
	public Message awaitMessage(MicroService m) throws InterruptedException {
//		System.out.println(m.getName() + " is in awaitmessage");
//		System.out.println(services.get(m) + " is this for " + m.getName());
		return services.get(m).take();
//		System.out.println(forDebug.getName() + " is out of services for " + m.getName() + " check: " + services.get(m));
//		return forDebug;
//		return services.get(m).take(); //check if we need to use try catch or throws is enough?
	}
}

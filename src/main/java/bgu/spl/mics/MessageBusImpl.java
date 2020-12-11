package bgu.spl.mics;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */

public class MessageBusImpl implements MessageBus {

	private ConcurrentHashMap<MicroService, LinkedBlockingDeque<Message>> services;
	private ConcurrentHashMap<Class<? extends Event>,LinkedBlockingDeque<MicroService>> events;
	private ConcurrentHashMap<Class<? extends Broadcast>,LinkedBlockingDeque<MicroService>> broadcasts;
	private ConcurrentHashMap<Event,Future> futures;
	private Object lock1, lock2;

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
	}

	public static MessageBusImpl getInstance() {
		return SingletonHolder.instance;
	}


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
			events.putIfAbsent(type, new LinkedBlockingDeque<>());
			events.get(type).add(m);
		}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		broadcasts.putIfAbsent(type, new LinkedBlockingDeque<>());
		broadcasts.get(type).add(m);
	}

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		synchronized (lock2) {
			if (futures.get(e) != null) {
				futures.get(e).resolve(result);
			}
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		synchronized (lock1) { // so it wont unregister while we add
			LinkedBlockingDeque<MicroService> toSend = broadcasts.get(b.getClass());
			for(MicroService elem: toSend){
				if (services.get(elem) != null) {
					services.get(elem).add(b);
				}
			}
		}
	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
			Future<T> output = null;
		synchronized (lock2) {
			if (events.get(e.getClass()) != null) {
				MicroService swap = events.get(e.getClass()).poll();
					if (swap != null && services.get(swap) != null) {
						services.get(swap).add(e);
						events.get(e.getClass()).add(swap);
						output = new Future<>();
						futures.put(e, output);
					}
				}
			}
			return output;
		}

	@Override
	public void register(MicroService m) {
        services.put(m,new LinkedBlockingDeque<>());
	}

	@Override
	public void unregister(MicroService m) {
		synchronized (m) {
			services.remove(m);
		}
		synchronized (lock2) { //to save sendE
			for (LinkedBlockingDeque<MicroService> elem : events.values()) {
				elem.remove(m);
			}
		}
		synchronized (lock1) { //to save sendB
			for (LinkedBlockingDeque<MicroService> elem : broadcasts.values()) {
				elem.remove(m);
			}
		}
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		return services.get(m).take();
	}
}

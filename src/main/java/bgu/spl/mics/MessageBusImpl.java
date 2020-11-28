package bgu.spl.mics;
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

	private static class MessageBusHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private MessageBusImpl(){
	//what do we need here?
	}

	public static MessageBusImpl getInstance() {
		return MessageBusHolder.instance;
	}

//why here its event<T> and on ms it isnt
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {

	}

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {

	}

	@Override
	public void sendBroadcast(Broadcast b) {

	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {

		return null;
	}

	@Override
	public void register(MicroService m) {

	}

	@Override
	public void unregister(MicroService m) {

	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {

		return null;
	}
}

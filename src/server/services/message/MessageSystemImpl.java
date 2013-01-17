package server.services.message;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import server.services.message.address.Address;
import server.services.message.address.AddressService;
import server.services.message.address.AddressServiceImpl;

public class MessageSystemImpl implements MessageSystem {
	final private int MAX_QUEUE_SIZE= 1000;
	private static MessageSystemImpl messageSystem = null;
	private AddressService addressService;
	
	private Map<Address, ArrayBlockingQueue<Msg>> messages = 
			new HashMap<Address, ArrayBlockingQueue<Msg>>();
	
	public void registrateAbonent(String abonentName, Abonent abonent) {
		messages.put(abonent.getAddress(), new ArrayBlockingQueue<Msg>(MAX_QUEUE_SIZE));
		addressService.registrateAbonent(abonentName, abonent);
	}
		
	public void sendMessage(Msg message){
		Queue<Msg> messageQueue = messages.get(message.getTo());
		messageQueue.add(message);
	}
	
	public void execForAbonent(Abonent abonent) {
		Queue<Msg> messageQueue = messages.get(abonent.getAddress());
		while (!messageQueue.isEmpty()) {
			Msg message = messageQueue.poll();
			message.exec(abonent);
		}
	}
	
	public AddressService getAddressService() {
		return this.addressService;		
	}
	
	public static MessageSystem getInstance() {
		if (messageSystem == null)
			messageSystem = new MessageSystemImpl();
		return (MessageSystem) messageSystem;
	}
	
	private MessageSystemImpl() {
		addressService = AddressServiceImpl.getInstance();
	}
}
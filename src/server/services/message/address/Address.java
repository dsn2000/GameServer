package server.services.message.address;

import java.util.concurrent.atomic.AtomicInteger;

public class Address {
	private static AtomicInteger abonentCreator = new AtomicInteger();
	public final int abonentId;
	
	public Address() {
		this.abonentId = abonentCreator.getAndIncrement();
	}
	
	public int getAbonentId() {
		return this.abonentId;
	}
}

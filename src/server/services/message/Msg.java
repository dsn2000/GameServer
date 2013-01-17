package server.services.message;

import server.services.message.address.Address;

public abstract class Msg {
	private final Address from;
	private final Address to;
	
	public Msg(Address from, Address to) {
		this.from = from;
		this.to = to;
	}
	
	public Address getFrom() {
		return this.from;
	}
	
	public Address getTo() {
		return this.to;
	}
	
	public abstract void exec(Abonent abonent);
}

package server.services.account;

import server.services.message.Abonent;
import server.services.message.Msg;
import server.services.message.address.Address;

public abstract class MsgToAS extends Msg {
	public MsgToAS(Address from, Address to) {
		super(from, to);
	}
	
	public void exec(Abonent abonent) {
		if (abonent instanceof AccountService) {
			exec((AccountService) abonent);
		}
	}
	
	public abstract void exec(AccountService accountService);
}

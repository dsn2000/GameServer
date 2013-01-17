package server.services.account;

import server.services.message.Abonent;
import server.services.message.address.Address;

public interface AccountService extends Abonent, Runnable{
	public Address getAddress();
	public void getUserId(int sessionId, String userName);
}
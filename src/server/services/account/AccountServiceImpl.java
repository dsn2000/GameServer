package server.services.account;

import server.frontend.MsgUpdateUserId;
import server.services.database.Users;
import server.services.message.MessageSystem;
import server.services.message.MessageSystemImpl;
import server.services.message.Msg;
import server.services.message.address.Address;
import server.services.message.address.AddressService;
import server.services.message.address.AddressServiceImpl;

public class AccountServiceImpl implements AccountService {
	private MessageSystem messageSystem;
	private AddressService addressService;
	private int TICK_TIME = 1000;
	private Address address;
	private Users users;
	
	public AccountServiceImpl(Users users) {
		this.address = new Address();
		this.addressService = AddressServiceImpl.getInstance();
		this.users = users;
		initializeMessageSystem();
	}
	
	private void initializeMessageSystem() {
		this.messageSystem = MessageSystemImpl.getInstance();
		this.messageSystem.registrateAbonent("AccountService", this);
	}
	
	public Address getAddress() {
		return address;
	}
	
	public void run() {
		while (true) {
			messageSystem.execForAbonent(this);
			try {
				Thread.sleep(TICK_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}	
	
	public void getUserId(int sessionId, String userName) {
		Integer userId = users.get(userName);
		if (userId != null) {
			Address addressFrom = this.getAddress();
			Address addressTo = addressService.getAbonentAddress("Frontend");
			Msg message = new MsgUpdateUserId(addressFrom, addressTo, sessionId, userId, userName);
			messageSystem.sendMessage(message);
		}
	}
}

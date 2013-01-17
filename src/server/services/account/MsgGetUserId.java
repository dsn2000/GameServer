package server.services.account;

import server.services.message.address.Address;

public class MsgGetUserId extends MsgToAS {
	private final int sessionId;
	private final String userName;
	
	public MsgGetUserId(Address from, Address to, int sessionId, String userName) {
		super(from, to);
		this.sessionId = sessionId;
		this.userName = userName;
	}

	public void exec(AccountService accountService) {
		accountService.getUserId(sessionId, userName);
	}	
}


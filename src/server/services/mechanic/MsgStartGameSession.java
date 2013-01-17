package server.services.mechanic;

import server.services.message.address.Address;

public class MsgStartGameSession extends MsgToGM {
	private int userId1, userId2;
	
	public MsgStartGameSession(Address from, Address to, int userId1, int userId2) {
		super(from, to);
		this.userId1 = userId1;
		this.userId2 = userId2;
	}

	public void exec(GameMechanics gameMechanics) {
		gameMechanics.startGameSession(userId1, userId2);
	}

}

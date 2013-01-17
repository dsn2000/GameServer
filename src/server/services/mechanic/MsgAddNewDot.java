package server.services.mechanic;

import server.services.message.address.Address;

public class MsgAddNewDot extends MsgToGM {
	private final int sessionId; 
	private final int i;
	private final int j;
	
	public MsgAddNewDot(Address from, Address to, int sessionId, int i, int j) {
		super(from, to);
		this.sessionId = sessionId;
		this.i = i;
		this.j = j;		
	}
	
	public int getSessionId() {
		return this.sessionId;
	}
	
	public int getI() {
		return this.i;
	}
	
	public int getJ() {
		return this.j;
	}

	public void exec(GameMechanics gameMechanics) {
		gameMechanics.addDot(sessionId, i, j);		
	}

}

package server.services.mechanic;

import server.services.message.Abonent;
import server.services.message.Msg;
import server.services.message.address.Address;

public abstract class MsgToGM extends Msg {

	public MsgToGM(Address from, Address to) {
		super(from, to);
	}

	public void exec(Abonent abonent) {
		if (abonent instanceof GameMechanics) {
			exec((GameMechanics) abonent);
		}
	}
	
	public abstract void exec(GameMechanics gameMechanics);
}

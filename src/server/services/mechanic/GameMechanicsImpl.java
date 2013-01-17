package server.services.mechanic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import server.frontend.Frontend;
import server.services.account.UserSession;
import server.services.message.MessageSystem;
import server.services.message.MessageSystemImpl;
import server.services.message.address.Address;
import server.utils.TimeHelper;

 
public class GameMechanicsImpl implements GameMechanics{
	private int SLEEP_TIME = 100;
	private Address address;
	private Frontend frontend;
	private MessageSystem messageSystem;
	final private Map<Integer, GameSession> userIdToGameSessions = 
		new HashMap<Integer, GameSession>();

	public GameMechanicsImpl(Frontend frontend) {
		this.address = new Address();
		messageSystem = MessageSystemImpl.getInstance();
		messageSystem.registrateAbonent("GameMechanics", this);
		this.frontend = frontend;
	}
	
	public void run(){
		while(true) {
			processMessages();
			doGMStep();
			replicateGamesToFrontend();
			TimeHelper.sleep(SLEEP_TIME);
		}
	}
		
	private void processMessages() {
		messageSystem.execForAbonent(this);
	}

	private void doGMStep() {
		// stub
	}

	public void replicateGamesToFrontend() {
		Iterator<Integer> keyIter = userIdToGameSessions.keySet().iterator();
		while (keyIter.hasNext()) {
			Integer userId = keyIter.next();
			GameSession gameSession = userIdToGameSessions.get(userId);
			UserSession userSession = frontend.getUserSessionByUserId(userId);
			replicateGameSessionToUserSession(gameSession, userSession);			
		}
	}
	
	private void replicateGameSessionToUserSession(GameSession gameSession, UserSession userSession) {
		if (userSession.getUser().getId() == gameSession.getCurrentStepUserId()) {
			userSession.setMyTurn(true);
		} else {
			userSession.setMyTurn(false);
		}
	}
	
	public void startGameSession(int userId1, int userId2) {
		GameSession gameSession = new GameSession(userId1, userId2);
		
		userIdToGameSessions.put(userId1, gameSession);
		UserSession session1 = frontend.getUserSessionByUserId(userId1);
		session1.setColor("blue");
		session1.setField(gameSession.getField());
		
		userIdToGameSessions.put(userId2, gameSession);
		UserSession session2 = frontend.getUserSessionByUserId(userId2);
		session2.setColor("red");
		session2.setField(gameSession.getField());
		
		gameSession.start();
	}
	
	public void addDot(int userId, int i, int j) {
		GameSession gameSession = userIdToGameSessions.get(userId);
		if (gameSession != null && gameSession.addDot(userId, i, j)) {
			gameSession.nextStep();
			if (isGameComplited(gameSession)){
				handleGameEnd(gameSession);
			}
		}		
	}
	
	public boolean isGameComplited(GameSession gameSession) {
		// stub
		return false;
	}
	
	public void handleGameEnd(GameSession gameSession) {
		// stub
	}
	
	public Address getAddress() {
		return address;
	}
	
	public GameSession getGameSession(int userId) {
		return this.userIdToGameSessions.get(userId);
	}
}

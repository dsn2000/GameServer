package server.services.account;

import java.util.concurrent.atomic.AtomicInteger;

import server.services.mechanic.Field;


public class UserSession {
	private static AtomicInteger sessionCreator = new AtomicInteger();
	private int sessionId;
	private User user;
	private boolean myTurn = false;
	private String color = "blue";
	private Field field;
	
	public UserSession() {
		this.sessionId = sessionCreator.getAndIncrement();
		this.user = null;
	}
	
	public int getSessionId() {
		return this.sessionId;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public boolean isMyTurn() {
		return myTurn;
	}
	
	public void setMyTurn(boolean myTurn) {
		this.myTurn = myTurn;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getColor() {
		return this.color;
	}
	
	public void setField(Field field) {
		this.field = field;
	}
	
	public Field getField() {
		return this.field;
	}
}

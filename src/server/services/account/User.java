package server.services.account;

public class User {
	private final String name;
	private final int id;
	
	public User(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Integer getId() {
		return this.id;
	}
}

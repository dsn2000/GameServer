package server.services.database;

import java.util.HashMap;
import java.util.Map;



public class MockUsers implements Users {
	private Map<String, Integer> userIds;
	
	public MockUsers() {
		this.userIds = new HashMap<String, Integer>();
		userIds.put("Alexey", 1);
		userIds.put("Elena", 2);
		userIds.put("Alexander", 3);
	}
	
	public Integer get(String name) {
		return userIds.get(name);
	}
}

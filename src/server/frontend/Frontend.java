package server.frontend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import server.services.account.UserSession;
import server.services.message.Abonent;
import server.services.message.address.Address;


public interface Frontend extends Abonent, Runnable {
	public void updateUser(int sessionId, int userId, String userName);
	public Address getAddress();
	public void handleRequest(Request baseRequest, HttpServletRequest request, HttpServletResponse response); 
	public UserSession getUserSession(int sessionId);
	public Integer getSessionIdByUserId(int userId);
	public UserSession getUserSessionByUserId(int userId);
}

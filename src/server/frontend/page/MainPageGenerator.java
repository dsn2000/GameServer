package server.frontend.page;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Request;

import server.services.account.User;

public class MainPageGenerator extends PageGenerator {
	public String getPage(Request baseRequest, HttpServletRequest request, Object... args) {
		Integer sessionId = (Integer) args[0];
		User user = (User) args[1];
		StringBuilder buff = new StringBuilder();
		buff.append("<body>\n");
		buff.append("<form id=\"main_form\" method=\"POST\">\n");
		buff.append("	<h4>\n"); 
		buff.append("		Hello " + user.getName() + ", " +
					"		your Id is " + user.getId() + "\n");
		buff.append("	</h4>\n");
		buff.append("	<input type=\"hidden\" name=\"sessionId\" value=" + sessionId + ">\n");
		buff.append("</form>\n");
		buff.append("</body>\n");
		return buff.toString();	
	}
}

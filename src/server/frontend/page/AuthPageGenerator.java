package server.frontend.page;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Request;

public class AuthPageGenerator extends PageGenerator {

	public String getPage(Request baseRequest, HttpServletRequest request, Object... args) {
		Integer sessionId = (Integer) args[0];
		StringBuilder buff = new StringBuilder();
		buff.append("<body>\n");
		buff.append("<form id=\"main_form\" method=\"POST\">\n");
		buff.append("	<h4>\n"); 
		buff.append("		Please, enter your name\n");
		buff.append("	</h4>\n");
		buff.append("	<input type=\"text\" name=\"user_name\">\n");
		buff.append("	<input type=\"hidden\" name=\"sessionId\" value=" + sessionId + ">\n");
		buff.append("	<input type=\"submit\" value=\"ok\"\n");
		buff.append("</form>\n");
		buff.append("</body>\n");
		return buff.toString();	
	}

	
}

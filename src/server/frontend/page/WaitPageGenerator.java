package server.frontend.page;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Request;

public class WaitPageGenerator extends PageGenerator {
	public String getPage(Request baseRequest, HttpServletRequest request, Object... args) {
		Integer sessionId = (Integer) args[0];
		StringBuilder buff = new StringBuilder();
		buff.append("<script>\n");
		buff.append("function refresh() {\n");
		buff.append("		document.forms['main_form'].submit();\n");
		buff.append("	}\n");
		buff.append("</script>\n");
		buff.append("<body onload=\"setTimeout(function(){refresh();}, 1000)\">\n");
		buff.append("<form id=\"main_form\" method=\"POST\">\n");
		buff.append("	<h4>\n"); 
		buff.append("		Please, wait\n");
		buff.append("	</h4>\n");
		buff.append("	<input type=\"hidden\" name=\"sessionId\" value=" + sessionId + ">\n");
		buff.append("</form>\n");
		buff.append("</body>\n");
		return buff.toString();	
	}
}

package server.frontend.page;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Request;

public abstract class PageGenerator {
	
	public abstract String getPage(Request baseRequest, HttpServletRequest request, Object... args);
}

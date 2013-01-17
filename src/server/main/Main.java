package server.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import server.frontend.Frontend;
import server.frontend.FrontendImpl;
import server.services.account.AccountServiceImpl;
import server.services.database.MockUsers;
import server.services.mechanic.GameMechanicsImpl;

public class Main extends AbstractHandler {
	private Frontend frontend;
	
	public Main(Frontend frontend) {
		this.frontend = frontend;
	}
	
	public void handle(String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response) 
	throws IOException, ServletException
	{
		this.frontend.handleRequest(baseRequest, request, response);
	}
	
	public static void main(String[] args) throws Exception
	{
		try {
			Thread accountServerThread = new Thread(new AccountServiceImpl(new MockUsers()));
			accountServerThread.start();
					
			Frontend frontend = new FrontendImpl();
			Thread frontendThread = new Thread(frontend);
			frontendThread.start();
			
			Thread gameMechanicsThread = new Thread(new GameMechanicsImpl(frontend));
			gameMechanicsThread.start();
					
			Server server = new Server(8081);
			server.setHandler(new Main(frontend));					
			server.start();
			server.join();
		} catch (Exception err) {
			System.out.println(err);
		}
	}
}
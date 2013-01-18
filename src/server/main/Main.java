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
import server.services.account.AccountService;
import server.services.account.AccountServiceImpl;
import server.services.database.MockUsers;
import server.services.mechanic.GameMechanics;
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
	
	
	static int port = 8081;
	
	public static void main(String[] args) throws Exception
	{
		try {
			
			runAccountServer();
			Frontend frontend = runFrontend();
			runGameMechanics(frontend);
								
			Server server = initServer(frontend,port);				
			server.start();
			server.join();
			
		} catch (Exception err) {
			System.out.println(err);
		}
	}
	
	private static AccountService runAccountServer()
	{
		AccountService service  = new AccountServiceImpl(new MockUsers());
		Thread accountServerThread = new Thread(service);
		accountServerThread.start();
		return service;	
	}
	
	private static Frontend runFrontend()
	{
		Frontend service  = new FrontendImpl();
		Thread frontendThread = new Thread(service);
		frontendThread.start();
		return service;	
	}
	
	private static GameMechanics runGameMechanics(Frontend frontend)
	{
		GameMechanics service  = new GameMechanicsImpl(frontend);
		Thread gameMechanicsThread = new Thread(service);
		gameMechanicsThread.start();
		return service;	
	}
	
	private static Server initServer(Frontend frontend, int port)
	{
		Server server = new Server(port);
		server.setHandler(new Main(frontend));	
		return server;
	}
}
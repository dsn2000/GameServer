package server.frontend;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import server.frontend.page.AuthPageGenerator;
import server.frontend.page.GamePageGenerator;
import server.frontend.page.WaitPageGenerator;
import server.services.account.MsgGetUserId;
import server.services.account.User;
import server.services.account.UserSession;
import server.services.mechanic.MsgAddNewDot;
import server.services.mechanic.MsgStartGameSession;
import server.services.message.MessageSystem;
import server.services.message.MessageSystemImpl;
import server.services.message.Msg;
import server.services.message.address.Address;
import server.services.message.address.AddressService;
import server.services.message.address.AddressServiceImpl;
import server.utils.TimeHelper;

public class FrontendImpl implements Frontend {
	
	private MessageSystem messageSystem;
	private AddressService addressService;
	private Address address;
	private Map<Integer, UserSession> sessionIdToUserSession;
	private Map<Integer, Integer> userIdToSessionId;
	private List<Integer> freeUserSessions;
	
	private AuthPageGenerator authPageGenerator;
	private WaitPageGenerator waitPageGenerator;
	private GamePageGenerator gamePageGenerator;
	
	private int TICK_TIME = 1000;
	
	public FrontendImpl() {
		this.address = new Address();
		this.addressService = AddressServiceImpl.getInstance();
		this.messageSystem = MessageSystemImpl.getInstance();
		this.messageSystem.registrateAbonent("Frontend", this);
		this.sessionIdToUserSession = new HashMap<Integer, UserSession>();
		this.userIdToSessionId = new HashMap<Integer, Integer>(); 
		this.freeUserSessions = new LinkedList<Integer>();
		initializePageGenerators();
	}
		
	private void initializePageGenerators() {
		this.authPageGenerator = new AuthPageGenerator();
		this.waitPageGenerator = new WaitPageGenerator();
		this.gamePageGenerator = new GamePageGenerator();
	}
	
	public Address getAddress() {
		return this.address;
	}
	
	public void run() {
		while (true) {
			messageSystem.execForAbonent(this);
			TimeHelper.sleep(TICK_TIME);
		}
	}
	
	public void handleRequest(Request baseRequest, HttpServletRequest request,
			HttpServletResponse response) {	
		
		if (isStaticContentRequest(baseRequest, request)) {
			handleStaticContentRequest(baseRequest, request, response);
		} else if (isRefreshGameRequest(baseRequest, request)) {
			handleRefreshGameRequest(baseRequest, request, response);
		} else {
			handleGameEventsRequest(baseRequest, request, response);
		}	
	}
	
	private boolean isStaticContentRequest(Request baseRequest, HttpServletRequest request) {
		if (request.getMethod().equals("GET") && request.getRequestURI().startsWith("/static"))
			return true;
		return false;
	}
	
	private void handleStaticContentRequest(Request baseRequest, HttpServletRequest request,
			HttpServletResponse response) {
		int filePathStartIndex = request.getRequestURI().indexOf("/static");
		if (filePathStartIndex >= 0) {			
			try {
				String filePath = request.getRequestURI().substring(filePathStartIndex + 1);
				File file = new File(filePath);
				byte[] outBuff = getBytesFromFile(file);
				response.getOutputStream().write(outBuff, 0, outBuff.length);
				response.setContentType("image/png;charset=utf-8");
				response.setStatus(HttpServletResponse.SC_OK);				
			} catch (IOException e) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			}			
		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
		baseRequest.setHandled(true);
	}
	
	@SuppressWarnings("resource")
	public byte[] getBytesFromFile(File file) throws IOException {
	    InputStream is = new FileInputStream(file);
	    long length = file.length();
	    if (length > Integer.MAX_VALUE) {
	        return null;
	    }
	 
	    byte[] bytes = new byte[(int)length];
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }
	 
	    if (offset < bytes.length) {
	        throw new IOException("Could not completely read file " + file.getName());
	    }
	 
	    is.close();
	    return bytes;
	}
	
	private boolean isRefreshGameRequest(Request baseRequest, HttpServletRequest request) {
		if (request.getMethod().equals("GET") && request.getRequestURI().startsWith("/refresh"))
			return true;
		return false;
	}
	
	private void handleRefreshGameRequest(Request baseRequest, HttpServletRequest request,
			HttpServletResponse response) {
		String sessionId = request.getParameter("sessionId");
		System.out.println("sessionId = " + sessionId);
		if (sessionId != null) {
			String clickedDotX = request.getParameter("clickedDotX");
			String clickedDotY = request.getParameter("clickedDotY");
			System.out.println("Clicked dot: " + clickedDotX + ", " + clickedDotY);
			if (clickedDotX != null && clickedDotY != null) {
				sendClickedPointToGameMechanics(new Integer(clickedDotX), new Integer(clickedDotY), new Integer(sessionId));				
			}						
			buildRefreshGameResponse(response, new Integer(sessionId));				
		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);	
		}
		baseRequest.setHandled(true);
	}
	
	private void sendClickedPointToGameMechanics(int clickedDotX, int clickedDotY, int sessionId) {		
		MsgAddNewDot msg = new MsgAddNewDot(
				this.getAddress(), addressService.getAbonentAddress("GameMechanics"),
				getUserSession(sessionId).getUser().getId(), clickedDotX, clickedDotY);
		messageSystem.sendMessage(msg);
	}
	
	private void buildRefreshGameResponse(HttpServletResponse response, int sessionId) {			
		UserSession userSession = sessionIdToUserSession.get(sessionId);
		if (userSession != null) {
			try {
				response.setContentType("application/json;charset=utf-8");
				PrintWriter out = response.getWriter();
				Boolean myTurn = userSession.isMyTurn();
				String field = userSession.getField().toJSON();
				String s =  "handle({\"myTurn\": " + myTurn + ", \"hasChanges\": " + "true" + ", \"field\": " + field + "});";
				out.println(s);		
				response.setStatus(HttpServletResponse.SC_OK);
			} catch(IOException err) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);			
			}
		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}		
	}
	
	private void handleGameEventsRequest(Request baseRequest, HttpServletRequest request,
			HttpServletResponse response) {
		String result = "";		
		Integer sessionId = getSessionId(baseRequest);
		if (sessionId == null) {
			sessionId = registrateUserSession();
			result = authPageGenerator.getPage(baseRequest, request, sessionId);
		} else {
			Integer userId = getUserId(sessionId);
			if (userId == null) {
				String userName = baseRequest.getParameter("user_name");
				if (userName != null) {
					sendMessageToAS(sessionId, userName);					
				} 
				result = waitPageGenerator.getPage(baseRequest, request, sessionId);
			} else {
				if (!freeUserSessions.contains(sessionId)){ 
					UserSession userSession = sessionIdToUserSession.get(sessionId);
					if (userSession != null) {
						result = gamePageGenerator.getPage(baseRequest, request, userSession);
					} else {
						result = waitPageGenerator.getPage(baseRequest, request, sessionId);
					}
				} else {
					if (freeUserSessions.size() >= 2) {
						startNewGameSession(sessionId);
					}
					result = waitPageGenerator.getPage(baseRequest, request, sessionId);
				}				
			}		
		}
		
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().println(result);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (IOException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}	
		baseRequest.setHandled(true);
	}
	
	private Integer registrateUserSession() {
		UserSession session = new UserSession();
		int sessionId = session.getSessionId();
		sessionIdToUserSession.put(sessionId, session);
		return sessionId;
	}
	
	private Integer getSessionId(Request baseRequest) {
		String sessionId = baseRequest.getParameter("sessionId");
		if (sessionId == null) {
			return null;
		}
		return new Integer(sessionId);		
	}
	
	private Integer getUserId(Integer sessionId) {
		UserSession session = sessionIdToUserSession.get(sessionId);
		User user = session.getUser();
		if (user != null && user.getId() != null)
			return user.getId();
		return null;		
	}
	
	private void sendMessageToAS(Integer sessionId, String userName) {
		Msg message = new MsgGetUserId(this.getAddress(),
				addressService.getAbonentAddress("AccountService"), 
				sessionId, userName);
		messageSystem.sendMessage(message);
	}
	
	private void startNewGameSession(Integer sessionId1) {
		freeUserSessions.remove(sessionId1);
		Integer sessionId2 = freeUserSessions.get(0);
		freeUserSessions.remove(sessionId2);
		MsgStartGameSession msg = new MsgStartGameSession(
				this.getAddress(), addressService.getAbonentAddress("GameMechanics"), 
				getUserIdBySessionId(sessionId1), getUserIdBySessionId(sessionId2));
		messageSystem.sendMessage(msg);
	}
		
	public void updateUser(int sessionId, int userId, String userName) {
		UserSession session = sessionIdToUserSession.get(sessionId);
		if (session.getUser() == null) {
			session.setUser(new User(userName, userId));
			userIdToSessionId.put(userId, sessionId);
		}
		freeUserSessions.add(sessionId);
	}
	
	public UserSession getUserSession(int sessionId) {
		return sessionIdToUserSession.get(sessionId);
	}
	
	public Integer getSessionIdByUserId(int userId) {
		return userIdToSessionId.get(userId);
	}
	
	public UserSession getUserSessionByUserId(int userId) {
		Integer sessionId = getSessionIdByUserId(userId);
		if (sessionId == null) {
			return null;
		}
		return getUserSession(sessionId);
	}
	
	public Integer getUserIdBySessionId(Integer sessionId) {
		return getUserSession(sessionId).getUser().getId();		
	}
	

}

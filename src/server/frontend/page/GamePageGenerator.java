package server.frontend.page;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Request;

import server.services.account.User;
import server.services.account.UserSession;

public class GamePageGenerator extends PageGenerator {

	public String getPage(Request baseRequest, HttpServletRequest request, 	Object... args) {
		UserSession userSession = (UserSession) args[0];  
		Integer sessionId = userSession.getSessionId();
		User user = userSession.getUser();
		String userColor = userSession.getColor();
		Boolean myTurn = userSession.isMyTurn();
		StringBuilder buff = new StringBuilder();
		buff.append("<body>\n");
		
		buff.append("<p id=\"turn\">Turn</p>\n");
		
		buff.append("<canvas id=\"example\" height=\"500\" width=\"500\" align=\"center\" style=\"border:1px solid blue;\">\n");
		buff.append("	Html5 canvas is not supported\n");
		buff.append("</canvas>\n");
		
		buff.append("<div style=\"position: absolute; left: -100px;\">\n");
		buff.append("<img id=\"dot-red\" src=\"static/dot-red.png\">\n");
		buff.append("<img id=\"dot-blue\" src=\"static/dot-blue.png\">\n");
		buff.append("<img id=\"dot-red-transparent\" src=\"static/dot-red-transparent.png\">\n");
		buff.append("<img id=\"dot-blue-transparent\" src=\"static/dot-blue-transparent.png\">\n");
		buff.append("</div>\n");
		
		buff.append("<form id=\"main_form\" method=\"POST\">\n");
		buff.append("	<input type=\"hidden\" name=\"userId\" value=" + user.getId() + ">\n");
		buff.append("	<input type=\"hidden\" name=\"sessionId\" value=" + sessionId + ">\n");
		buff.append("</form>\n");
		
		buff.append("<script language=\"JavaScript\" type=\"text/javascript\" src=\"http://code.jquery.com/jquery-1.8.2.js\"></script>\n");
		buff.append("<script>\n");
		
		buff.append("	var userColor = \"" + userColor + "\";\n");
		buff.append("	var clickedDot = [];\n");
		buff.append("	var myTurn = " + myTurn + "\n");
		buff.append("	var sessionId = " + sessionId + "\n");
		
		buff.append("	function drawDot(i, j, color) {\n");
		buff.append("		var canvas = document.getElementById('example');\n");
		buff.append("		var ctx = canvas.getContext('2d');\n");
		buff.append("		var dotImg = document.getElementById('dot-' + color);\n");
		buff.append("		ctx.drawImage(dotImg, i * 25 - dotImg.width/2, j * 25 - dotImg.height/2);\n");
		buff.append("	}\n");

		buff.append("	function loadDots(stringData) {\n");
		buff.append("		console.log(\"load dots: \" + stringData);\n");
		buff.append("		if (stringData != \"\") { \n");
		buff.append("			var jsonData = JSON.parse(stringData);\n");
		buff.append("			console.log(\"json data: \" + jsonData);\n");
		buff.append("			for(dotIdx in jsonData)	{\n");
		buff.append("				console.log(\"dotIdx: \" + dotIdx);\n");
		buff.append("				drawDot(jsonData[dotIdx][0], jsonData[dotIdx][1], jsonData[dotIdx][2]);\n");
		buff.append("				dotsArray.push(jsonData[dotIdx]);\n");
		buff.append("			}\n");
		buff.append("		};\n");
		buff.append("	}\n");

		buff.append("	function clickedAt(x, y) {\n");
		buff.append("		if (!myTurn) {\n");
		buff.append("			return;\n");
		buff.append("		}\n");
		buff.append("		var dotImg;\n");
		buff.append("		if (userColor == 'blue') {\n");
		buff.append("			dotImg = document.getElementById('dot-blue');\n");
		buff.append("		} else {\n");
		buff.append("			userColor = 'red';\n");	
		buff.append("			dotImg = document.getElementById('dot-red');\n");
		buff.append("		}\n");		
		buff.append("		var i = (aligned(x))/25;\n");
		buff.append("		var j = (aligned(y))/25;\n");
		buff.append("		for(dotIdx in dotsArray) {\n");
		buff.append("			if(dotsArray[dotIdx][0] == i && dotsArray[dotIdx][1] == j) {\n");
		buff.append("				return;\n");
		buff.append("			}\n");
		buff.append("		}\n");
		buff.append("		clickedDot[0] = i;\n");
		buff.append("		clickedDot[1] = j;\n");
		buff.append("		drawDot(i, j, userColor);\n");
		buff.append("		length = dotsArray.push([i, j, userColor]);\n");
		buff.append("		var d = JSON.stringify(dotsArray);\n");
		buff.append("		$(\"#pid\").html(d);\n");
		buff.append("	}\n");

		buff.append("	function movedAt(x, y) {\n");
		buff.append("		var canvas = document.getElementById('example');\n");
		buff.append("		var x_aligned = aligned(x);\n");
		buff.append("		var y_aligned = aligned(y);\n");
		buff.append("		var dotImg;\n");
		buff.append("		if (userColor == 'blue') {");
		buff.append("			dotImg = document.getElementById('dot-blue');\n");
		buff.append("			$(\"#dot-blue-transparent\").offset({\n");
		buff.append("				top: y_aligned + canvas.offsetTop + 1 - dotImg.height/2,\n");
		buff.append("				left: x_aligned + canvas.offsetLeft + 1 - dotImg.width/2\n");
		buff.append("			});\n");
		buff.append("		} else {");
		buff.append("			dotImg = document.getElementById('dot-red');\n");
		buff.append("			$(\"#dot-red-transparent\").offset({\n");
		buff.append("				top: y_aligned + canvas.offsetTop + 1 - dotImg.height/2,\n");
		buff.append("				left: x_aligned + canvas.offsetLeft + 1 - dotImg.width/2\n");
		buff.append("			});\n");
		buff.append("		}");	
		buff.append("	}\n");

		buff.append("	function aligned(z) {\n");
		buff.append("		var z_aligned;\n");
		buff.append("		var field_z = z;\n");
		buff.append("		var z_idx = parseInt(field_z / 25, 10);\n");
		buff.append("		var low_z =  (z_idx) * 25;\n");
		buff.append("		var high_z =  (z_idx + 1) * 25;\n");
		buff.append("		if(z < low_z + (high_z - low_z)/2) {\n");
		buff.append("			z_aligned = low_z;\n");
		buff.append("		} else {\n");
		buff.append("			z_aligned = high_z;\n");
		buff.append("		}\n");
		buff.append("		return z_aligned;\n");
		buff.append("	}\n");

		buff.append("	function refreshField() {\n");
		buff.append("		if (clickedDot != []) {\n");
		buff.append("			clickedDotX = clickedDot[0];\n");		
		buff.append("			clickedDotY = clickedDot[1];\n");
		buff.append("			clickedDot = [];\n");
		buff.append("		} else {\n");
		buff.append("			clickedDotX = null;\n");
		buff.append("			clickedDotY = null;\n");
		buff.append("		}\n");
		buff.append("		$.ajax({\n");
		buff.append("			url: \"http://127.0.0.1:8081/refresh\",\n");
		buff.append("			dataType: \"jsonp\",\n");
		buff.append("			data: {\n");
		buff.append("				'clickedDotX': clickedDotX,\n");
		buff.append("				'clickedDotY': clickedDotY,\n");
		buff.append("				'sessionId': sessionId,\n");
		buff.append("			},\n");
        buff.append("			jsonp: 'jsonp_callback',\n");
		buff.append("			success: \n");
		buff.append("				function(data) {\n");
		buff.append("					alert('success...');\n");
		buff.append("				}\n");
		buff.append("		});\n");
		buff.append("	}\n");
		
		buff.append("	function handle(data) {\n");
		buff.append("		console.log(\"handle\");\n");
		buff.append("		console.log(\"sessionId=\"+sessionId);\n");
		buff.append("		myTurn = data.myTurn;");
		buff.append("		if (data.myTurn) {\n");
		buff.append("			$(\"#turn\").html(\"Your turn\");\n");
		buff.append("			console.log(\"your turn\");\n");
		buff.append("			$(\"#turn\").html(\"Your turn\");\n");
		buff.append("		} else {\n");
		buff.append("			$(\"#turn\").html(\"Your opponent turn\");\n");
		buff.append("			console.log(\"your opponent turn\");\n");
		buff.append("		}\n");
							// TODO: this is bad!!!!!
		buff.append("		if (data.hasChanges) {\n");
		buff.append("			console.log(\"dots: \" + data.field);\n");
		buff.append("			loadDots(data.field);\n");
		buff.append("		}\n");
		buff.append("	}\n");
		
		buff.append("	function drawField() {\n");
		buff.append("		var canvas = document.getElementById('example');\n");
		buff.append("		var ctx = canvas.getContext('2d');\n");
		buff.append("		ctx.lineWidth = 0.5;\n");
		buff.append("		var a = 25;\n");
		buff.append("		var n = canvas.width / a;\n");
		buff.append("		var m = canvas.height / a;\n");
		buff.append("		ctx.beginPath();\n");
		buff.append("		for(i = 1; i < m; i++) {\n");
		buff.append("			ctx.moveTo(0, a*i);\n"); 
		buff.append("			ctx.lineTo(canvas.width, a*i);\n");
		buff.append("		}\n");
		buff.append("		for(j = 1; j < n; j++) {\n");
		buff.append("			ctx.moveTo(a*j, 0);\n"); 
		buff.append("			ctx.lineTo(a*j, canvas.height);\n");
		buff.append("		}\n");
		buff.append("		ctx.closePath();\n");
		buff.append("		ctx.stroke();\n");	
		buff.append("	}\n");

		buff.append("	function bindEvents() {\n");
		buff.append("		$(\"#example\").click(function (e) {\n");
		buff.append("			var canvas = document.getElementById('example');\n");
		buff.append("			clickedAt(e.pageX - canvas.offsetLeft, e.pageY - canvas.offsetTop);\n");
		buff.append("		});\n");
		buff.append("		$(\"#example\").mousemove(function (e) {\n");
		buff.append("			var canvas = document.getElementById('example');\n");
		buff.append("			movedAt(e.pageX - canvas.offsetLeft, e.pageY - canvas.offsetTop);\n");
		buff.append("		});\n");
		buff.append("		$(\"#dot-blue-transparent\").click(function (e) {\n");
		buff.append("			var canvas = document.getElementById('example');\n");
		buff.append("			clickedAt(e.pageX - canvas.offsetLeft, e.pageY - canvas.offsetTop);\n");
		buff.append("		});\n");
		buff.append("		$(\"#dot-blue-transparent\").mousemove(function (e) {\n");
		buff.append("			var canvas = document.getElementById('example');\n");
		buff.append("			movedAt(e.pageX - canvas.offsetLeft, e.pageY - canvas.offsetTop);\n");
		buff.append("		});\n");
		buff.append("		$(\"#dot-red-transparent\").click(function (e) {\n");
		buff.append("			var canvas = document.getElementById('example');\n");
		buff.append("			clickedAt(e.pageX - canvas.offsetLeft, e.pageY - canvas.offsetTop);\n");
		buff.append("		});\n");
		buff.append("		$(\"#dot-red-transparent\").mousemove(function (e) {\n");
		buff.append("			var canvas = document.getElementById('example');\n");
		buff.append("			movedAt(e.pageX - canvas.offsetLeft, e.pageY - canvas.offsetTop);\n");
		buff.append("		});\n");
		buff.append("	}\n");

		buff.append("	setInterval(refreshField, 100);\n");
		buff.append("	drawField();\n");
		buff.append("	bindEvents();\n");

		/*buff.append("	loadDots(d);\n");*/
		buff.append("	var dotsArray = [];\n");
		
		buff.append("</script>\n");
				
		buff.append("</body>\n");
		return buff.toString();
	}
}



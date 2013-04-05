<%@ page language="java" import="cs5530.*" %>
<html>
<head>

</head>
<body>

<%
	String loginValue = request.getParameter( "loginValue" );
	String passwordValue = request.getParameter( "passwordValue" );
	cs5530.Connector connector = new Connector();
	cs5530.User user = new User(connector, connector.stmt);
	
	if( user.loginUser(loginValue, passwordValue) ){
		session.setAttribute("user", user);
		session.setAttribute("loggedIn", "true");
		response.sendRedirect("mainmenu.jsp");
	} else {
		response.sendRedirect("login.jsp");
		connector.closeStatement();
		connector.closeConnection();
	}  // We are ending the braces for else here
%>
</body>

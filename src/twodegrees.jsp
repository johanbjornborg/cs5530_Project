<%@ page language="java" import="cs5530.*, java.util.*" %>
<html>
<head>
<script LANGUAGE="javascript">

function check_all_fields(form_obj){
        alert(form_obj.searchAttribute.value+"='"+form_obj.attributeValue.value+"'");
        if( form_obj.attributeValue.value == ""){
                alert("Search field should be nonempty");
                return false;
        }
        return true;
}

</script>
</head>
<body>

<%
String searchAttribute = request.getParameter("searchAttribute");
if( searchAttribute == null ){
%>

		Two Degrees of Separation:<br>
		Enter the first actor name, and the actor you wish to compare to. <br>
		If there exists a two degree separation, the results will appear below.<br>
        <form name="twodegrees" method=get onsubmit="return check_all_fields(this)" action="twodegrees.jsp"><br>
				Actor 1: <input type=text name="one" ><br>
				Actor 2: <input type=text name="two" ><br>
				<input type=hidden name="searchAttribute" value="twoDegrees">
				<input type=submit>
        </form>

<%

} else {
		
		String one = request.getParameter("one");
		String two = request.getParameter("two");
		String output = "NOTHING!";
		
		cs5530.Connector connector = new Connector();
		cs5530.CastAndCrew cc = new CastAndCrew(connector, connector.stmt);
		
		if(one.length() > 0 && two.length() > 0){
			output = cc.twoDegrees(one, two); 
		}

%>

  <p><b>Listing feedback in JSP: </b><BR><BR>
  All actors exactly two degrees away from <%=one%> and <%=two%> are:<BR><BR>
  <%=output%> <BR><BR>

  <%
 connector.closeStatement();
 connector.closeConnection();
}  // We are ending the braces for else here
%>

<BR><a href="twodegrees.jsp"> Try a new 2 degree search</a></p>

</body>

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
		
		cs5530.Connector connector = new Connector();
		cs5530.User user = (cs5530.User) session.getAttribute("user");

%>

  <p><b>Full Report: </b><BR><BR>
  <%=user.getUserRecord()%> <BR><BR>

  <%
 connector.closeStatement();
 connector.closeConnection();
%>

<BR><a href="feedback.jsp"> Search for other feedback</a></p>

</body>

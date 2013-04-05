<%@ page language="java" import="cs5530.*" %>
<html>
<head>
<script LANGUAGE="javascript">

function check_all_fields(form_obj){
        alert(form_obj.loggedIn.value+"='"+form_obj.attributeValue.value+"'");
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
cs5530.User user = (cs5530.User) session.getAttribute("user");
if(user == null){
	response.sendRedirect("login.jsp");
} else {
%>
<BR><a href="orders.jsp"> Order a Movie </a></p>
<BR><a href="orderHistory.jsp"> See Order History </a></p>
<BR><a href="feedback.jsp"> View/Leave Feedback </a></p>
<BR><a href="relationships.jsp"> Order a Movie </a></p>
<BR><a href="reports.jsp"> User Relationships </a></p>
<BR><a href="twodegrees.jsp"> Two degrees of awesome</a></p>

<%
}  // We are ending the braces for else here
%>

<BR><a href="orders.jsp"> New query </a></p>

</body>

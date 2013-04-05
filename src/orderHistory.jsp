<%@ page language="java" import="cs5530.*" %>
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
        cs5530.QueryOrder order = new QueryOrder(connector, connector.stmt, user);

%>

  <p><b>Listing orders in JSP: </b><BR><BR>

  The order history for <%= user.fullName %> as follows:<BR><BR>
  <%=order.getOrderHistory()%> <BR><BR>

<BR><a href="mainmenu.jsp"> Main Menu</a></p>

</body>

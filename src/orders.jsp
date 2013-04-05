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

        Order by ISBN:
        <form name="isbn_order" method=get onsubmit="return check_all_fields(this)" action="orders.jsp"><br>
				ISBN: <input type=text name="isbnValue" length=10><br>
				Quantity: <input type=qty name="qty" length=10><br>
				<input type=hidden name="searchAttribute" value="isbn">
				<input type=hidden name="titleValue" value="">
                <input type=submit>
        </form>
		OR
        <BR><BR>
        Order by Movie Title:
        <form name="title_order" method=get onsubmit="return check_all_fields(this)" action="orders.jsp"><br>
				Title: <input type=text name="titleValue" length=10><br>
				Quantity: <input type=text name="qty" length=10><br>
				<input type=hidden name="searchAttribute" value="title">
				<input type=hidden name="isbnValue" value="">
				<input type=submit>
        </form>

<%

} else {

        String titleValue = request.getParameter("titleValue");
		String isbnValue = request.getParameter("isbnValue");
		String qty = request.getParameter("qty");
		cs5530.Connector connector = new Connector();
		cs5530.User user = new User(connector, connector.stmt);
		user.loginUser("jwells","qwerty");
        cs5530.QueryOrder order = new QueryOrder(connector, connector.stmt, user);

%>

  <p><b>Listing orders in JSP: </b><BR><BR>

  Order status for: <b><%=searchAttribute%>='<%=isbnValue%>'</b> is as follows:<BR><BR>
  <%=order.orderVideos(isbnValue, titleValue, Integer.parseInt(qty), new Date() )%> <BR><BR>

  <%
 connector.closeStatement();
 connector.closeConnection();
}  // We are ending the braces for else here
%>

<BR><a href="orders.jsp"> New Order </a></p>

</body>

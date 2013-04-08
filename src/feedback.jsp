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

		View feedback for movie:
        <form name="movie_feedback" method=get onsubmit="return check_all_fields(this)" action="feedback.jsp"><br>
				ISBN: <input type=text name="isbn" ><br>
				Top n = <input type=text name="n" > Results <br>
				<input type=hidden name="searchAttribute" value="viewFeedback">
				<input type=submit>
        </form>
		
		Leave feedback for movie:
		<form name="leave_new_feedback" method=get onsubmit="return check_all_fields(this)" action="feedback.jsp"><br>
				ISBN: <input type=text name="isbn" ><br>
				Score (1-10): <input type=text name="score" > Results <br>
				Comments: <br>
				<textarea rows="5" cols="80" name="comments">Enter your comments here...</textarea>
				<input type=hidden name="searchAttribute" value="newFeedback">
				<input type=submit>
        </form>

		<form name="rate_feedback" method=get onsubmit="return check_all_fields(this)" action="feedback.jsp"><br>
				Feedback ID: <input type=text name="feedbackID" ><br>
				Usefulness (0-2): <input type=text name="usefulness" > Results <br><br>
				<input type=hidden name="searchAttribute" value="newFeedback">
				<input type=submit>
        </form>
<%

} else {
		
		String isbn = request.getParameter("isbn");
		String n = request.getParameter("n");
		String score = request.getParameter("score");
		String comments = request.getParameter("comments");
		String feedbackID = request.getParameter("feedbackID");
		String usefulness = request.getParameter("usefulness");
		
		String output = "";
		
		cs5530.Connector connector = new Connector();
		cs5530.User user = (cs5530.User) session.getAttribute("user");
        cs5530.QueryFeedback feedback = new QueryFeedback(connector, connector.stmt, user);
		
		if(isbn != null && comments == null){
			if(n != null){
			output = feedback.getUsefulFeedback(Integer.parseInt(n), Integer.parseInt(isbn));
			}
			else{
			output = feedback.getFeedback(Integer.parseInt(isbn));
			}
		}
		else if(comments != null) {
			output = feedback.leaveNewFeedback(Integer.parseInt(isbn), comments ,Integer.parseInt(n));
		}
		else if (feedback != null) 
		{
			output = "<br>" + feedback.rateFeedback(Integer.parseInt(feedbackID), Integer.parseInt(usefulness));
		}

%>

  <p><b>Listing feedback in JSP: </b><BR><BR>
  Videos matching parameters are as follows:<BR><BR>
  <%=output%> <BR><BR>

  <%
 connector.closeStatement();
 connector.closeConnection();
}  // We are ending the braces for else here
%>

<BR><a href="feedback.jsp"> Search for other feedback</a></p>

</body>

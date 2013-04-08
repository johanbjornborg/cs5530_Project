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

        Browse Selection<br>
		Enter search parameters in fields below, separate with a semicolon ';'.<br>
        <form name="browse_movies" method=get onsubmit="return check_all_fields(this)" action="browse.jsp"><br>
				Title: <input type=text name="title" ><br>
				Cast Member: <input type=qty name="cast" ><br>
				Directed by: <input type=qty name="director" ><br>
				Rating (G, PG, PG-13, R): <input type=qty name="rating" ><br>
				Genre: <input type=qty name="genre" ><br>
				<input type=hidden name="searchAttribute" value="browse">
				<input type=submit>
        </form>


<%

} else {
		HashMap<String, ArrayList<String>> params = new HashMap<String, ArrayList<String>>();
		params.put("title", new ArrayList<String>());
		params.put("cast", new ArrayList<String>());
		params.put("director", new ArrayList<String>());
		params.put("rating", new ArrayList<String>());
		params.put("genre", new ArrayList<String>());
		params.put("keyword", new ArrayList<String>());
		
        String title = request.getParameter("title");
		String cast = request.getParameter("cast");
		String director = request.getParameter("director");
		String rating = request.getParameter("rating");
		String genre = request.getParameter("genre");
		
		if(title.length() > 1)
		params.get("title").addAll(Arrays.asList(title.split("; ")));
		if(cast.length() > 1)
		params.get("cast").addAll(Arrays.asList(cast.split("; ")));
		if(director.length() > 1)
		params.get("director").addAll(Arrays.asList(director.split("; ")));
		if(rating.length() > 1)
		params.get("rating").addAll(Arrays.asList(rating.split("; ")));
		if(genre.length() > 1) 
		params.get("genre").addAll(Arrays.asList(genre.split("; ")));
		
		cs5530.Connector connector = new Connector();
        cs5530.QueryVideos videos = new QueryVideos(connector, connector.stmt);

%>

  <p><b>Listing orders in JSP: </b><BR><BR>
	parameters:<%=params.get("title").toString()%>
  Videos matching parameters are as follows:<BR><BR>
  <%=videos.browseTitles(params, "release_year")%> <BR><BR>

  <%
 connector.closeStatement();
 connector.closeConnection();
}  // We are ending the braces for else here
%>

<BR><a href="browse.jsp"> New Search </a></p>

</body>

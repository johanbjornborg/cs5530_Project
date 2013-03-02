package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Feedback {
	public Feedback() {
	}

	public String getOrders(String attrName, String attrValue, Statement stmt) throws Exception {
		String query;
		String resultstr = "";
		ResultSet results;
		query = "Select * from orders where " + attrName + "='" + attrValue + "'";
		try {
			results = stmt.executeQuery(query);
		} catch (Exception e) {
			System.err.println("Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			throw (e);
		}
		System.out.println("Order:getOrders query=" + query + "\n");
		while (results.next()) {
			resultstr += "<b>" + results.getString("login") + "</b> purchased " + results.getInt("quantity") + " copies of &nbsp'<i>" + results.getString("title") + "'</i><BR>\n";
		}
		return resultstr;
	}
	
	public String newFeedback(String user, int isbn){
		throw new NotImplementedException();	
	}
	
	public String getUsefulFeedback(int n){
		throw new NotImplementedException();
	}
	
	public String rateFeedback(int score){
		throw new NotImplementedException();
	}
	
	public String leaveNewFeedback(String user, int isbn, String comments, int score){
		throw new NotImplementedException();
	}
}

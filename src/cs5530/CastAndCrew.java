package cs5530;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CastAndCrew {
	Connector con;
	Statement stmt;

	public CastAndCrew(Connector con, Statement stmt) {
		this.con = con;
		this.stmt = stmt;
	}

	public String getOrders(String attrName, String attrValue, Statement stmt) {
		String query;
		String resultstr = "";
		ResultSet results;
		query = "Select * from orders where " + attrName + "='" + attrValue + "'";
		try {
			results = stmt.executeQuery(query);
			while (results.next()) {
				resultstr += "<b>" + results.getString("login") + "</b> purchased " + results.getInt("quantity") + " copies of &nbsp'<i>" + results.getString("title")
						+ "'</i><BR>\n";
			}
		} catch (SQLException e) {
			System.err.println("Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			return "<br>getOrders: Unable to execute query:" + query + "<br>";
		}
		System.out.println("Order:getOrders query=" + query + "\n");

		return resultstr;
	}

	/**
	 * Two degrees of separation implementation goes here.
	 * 
	 * @return
	 */
	public String twoDegrees(String actorA, String actorB) throws Exception {

		String resultStr = "";
		ResultSet results;
		String query = String.format("SELECT * From CastAndCrew Z WHERE Z.id IN "
				+ "(SELECT X.id AS movie FROM CastLookup AS X INNER JOIN CastLookup AS Y ON X.id = Y.id GROUP BY movie)", actorA, actorB);

		try {
			results = stmt.executeQuery(query);
			while (results.next()) {
				resultStr += "<br>Name: " + results.getString("full_name") + " Role: " + results.getString("role") + "<br>";
			}
		} catch (SQLException e) {
			System.err.println("Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			return "<br>CastAndCrew: Unable to execute query:" + query + "<br>" + e.getMessage();
		}
		System.out.println("CastAndCrew: TwoDegrees query=" + query + "<br>");

		return resultStr;
	}

	// public String newCastMember(String name, int[] movies) {
	// throw new NotImplementedException();
	// }

	// public String newCrewMember(String name, int[] movies) {
	// throw new NotImplementedException();
	// }
}

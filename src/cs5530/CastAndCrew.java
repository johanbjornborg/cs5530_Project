package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CastAndCrew {
	public CastAndCrew() {
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

	/**
	 * Two degrees of separation implementation goes here.
	 * 
	 * @return
	 */
	public String twoDegrees(String actorA, String actorB) {
		String query = String
				.format("SELECT * From CastAndCrew Z WHERE Z.id IN " +
						"(SELECT X.id AS movie FROM CastLookup AS X INNER JOIN CastLookup AS Y ON X.id = Y.id GROUP BY movie)" +
						" AND Z.full_name = '%s' OR Z.full_name = '%s'", actorA, actorB);
		throw new NotImplementedException();
	}

	public String newCastMember(String name, int[] movies) {
		throw new NotImplementedException();
	}

	public String newCrewMember(String name, int[] movies) {
		throw new NotImplementedException();
	}
}

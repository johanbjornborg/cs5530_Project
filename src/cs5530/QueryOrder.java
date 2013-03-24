package cs5530;

import java.sql.*;
import java.util.Date;
//import javax.servlet.http.*;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class QueryOrder {

	private User user;
	@SuppressWarnings("unused")
	private Connector con;
	private Statement stmt;

	public QueryOrder(Connector con, Statement stmt, User user) {
		this.con = con;
		this.stmt = stmt;
		this.user = user;
	}

	private ResultSet checkAvailability(String isbn, String title, int quantity) throws Exception {
		String query = "";
		ResultSet results;

		if (!isbn.equals("")) {
			query = String.format("SELECT * FROM VideoData v natural join " + "Quantities q where v.isbn = '%s'" + " having q.available_qty > %d", isbn, quantity);
		}
		// Otherwise if title is provided:
		else if (!title.equals("")) {
			query = String.format("SELECT * FROM VideoData v natural join Quantities" + " q where v.title = '%s' group by v.isbn having q.available_qty > %d", title, quantity);
		}
		try {
			results = stmt.executeQuery(query);
		} catch (Exception e) {
			System.err.println("Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			throw (e);
		}

		System.out.println("Order:checkAvailability query=" + query + "\n");

		return results;
	}

	/**
	 * 
	 * @param isbn
	 * @param title
	 * @param quantity
	 * @param rentDate
	 * @return
	 * @throws Exception
	 */
	public String orderVideos(String isbn, String title, int quantity, Date rentDate) throws Exception {
		// TODO: Adjust quantity available after order.

		System.out.println("Order Movie: ");
		java.sql.Date sqlDate = new java.sql.Date(rentDate.getTime());
		String query = "";
		String result = "Failed to create Order";
		// First check if movie exists, and it is available.
		ResultSet results = checkAvailability(isbn, title, quantity);
		// If so, order the movie(s)
		if (!results.next()) {
			System.out.println("Results empty. Likely that quantity desired is invalid");
			return result;
		}
		while (results.next()) {
			isbn = results.getString("isbn");
		}
		query = String.format("INSERT INTO OrderHistory VALUES ('%s', '%s', '%s', '%s', '%s')", Variables.getLogin(), isbn, sqlDate, sqlDate,
				"Test rental stuff. Nothing to see here.");
		try {
			// if (Variables.getStatement().executeUpdate(query) == 1) {

			stmt.executeUpdate(query);
			result = "Successfully Ordered Video " + title + ".";

		} catch (SQLException e) {
			System.err.println("Order: OrderVideos: Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			throw (e);
		}
		System.out.println("Order:OrderVideos query=" + query + "\n");
		System.out.println(result);

		return result;
	}

	public String getOrderHistory() throws SQLException {

		ResultSet results;
		String resultstr = user.fullName + "<br>";

		String query = "SELECT * FROM OrderHistory WHERE userLogin = '" + this.user.login + "'";
		results = stmt.executeQuery(query);

		while (results.next()) {
			resultstr += "<b>" + results.getString("isbn") + "</b><br>" + results.getString("") + " Rent Date: " + results.getString("Rent_date")
					+ " <br>Additional Info: &nbsp'<i>" + results.getString("Additional_info") + "'</i><BR>\n";
		}
		return resultstr;
	}

	public String buyingSuggestions(String user, int isbn) {
		throw new NotImplementedException();
	}

	public String userAwards() {
		throw new NotImplementedException();
	}
}

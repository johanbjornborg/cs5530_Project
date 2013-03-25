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

	/**
	 * Given an isbn|title, determine if the movie is available to rent by
	 * ensuring that there is enough in stock to order.
	 * 
	 * @param isbn
	 * @param title
	 * @param quantity
	 * @return
	 * @throws SQLException
	 */
	private ResultSet checkAvailability(String isbn, String title, int quantity) throws SQLException {
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
		} catch (SQLException e) {
			System.err.println("Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			throw (e);
		}

		System.out.println("Order:checkAvailability query=" + query + "\n");

		return results;
	}

	/**
	 * Orders a video from the video store, and records it in OrderHistory
	 * Either isbn or title are required.
	 * 
	 * @param isbn
	 *            - Movie isbn
	 * @param title
	 *            - Movie Title
	 * @param quantity
	 *            - Number desired
	 * @param rentDate
	 *            - Date rental was requested
	 * @return String declaring success or failure.
	 * @throws Exception
	 */
	public String orderVideos(String isbn, String title, int quantity, Date rentDate) throws SQLException {
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
		query = String.format("INSERT INTO OrderHistory VALUES ('%s', '%s', '%s', '%s', '%s')", user.login, isbn, sqlDate, sqlDate, "Test rental stuff. Nothing to see here.");
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

	/**
	 * Returns the complete order history for the currently logged in user.
	 * 
	 * @return String containing a comprehensive list of user-relevant data.
	 * @throws SQLException
	 */
	public String getOrderHistory() throws SQLException {

		ResultSet results;
		String resultstr = user.fullName + "<br>";

		String query = "SELECT * FROM OrderHistory WHERE userLogin = '" + this.user.login + "'";
		results = stmt.executeQuery(query);

		while (results.next()) {
			resultstr += "<b>" + results.getString("isbn") + "</b><br> Rent Date: " + results.getString("Rent_date") + " <br>Additional Info: &nbsp'<i>"
					+ results.getString("Additional_info") + "'</i><BR>\n";
		}
		return resultstr;
	}

	/**
	 * Given a movie ID, return suggestions based on users that also purchased
	 * that movie.
	 * 
	 * @param isbn
	 *            : ID of movie user just purchased.
	 * @return String containing a list of movies that are suitable for the
	 *         user.
	 * @throws SQLException
	 */
	public String buyingSuggestions(int isbn) throws SQLException {
		ResultSet results;
		String resultStr = "";
		String query = String.format("SELECT * FROM VideoData v " + "WHERE v.isbn = (SELECT OH1.isbn FROM OrderHistory OH1 WHERE OH1.isbn IN "
				+ "(SELECT OH.isbn FROM OrderHistory OH WHERE OH.userLogin = '%s' AND OH.isbn = '%s') AND OH1.userLogin <> '%s') LIMIT 10", isbn, user.login, user.login);

		try {
			results = stmt.executeQuery(query);
			while (results.next()) {
				resultStr += "<b>" + results.getString("isbn") + "</b><br>Title: " + results.getString("title") + " Release Year: " + results.getString("release_year")
						+ " <br>Formats: &nbsp'<i>" + results.getString("format") + "'</i><BR>\n" + " <br>Price: &nbsp'<i>" + results.getString("price") + "'</i><BR>\n"
						+ " <br>Genre: &nbsp'<i>" + results.getString("genre") + "'</i><BR>\n";
			}

		} catch (SQLException e) {
			System.err.println("Videos:browseMovies: Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			throw (e);
		}
		return resultStr;
	}

	public String userAwards() {
		throw new NotImplementedException();
	}
}

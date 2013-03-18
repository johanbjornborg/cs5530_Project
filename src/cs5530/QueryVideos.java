package cs5530;

import java.sql.*;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class QueryVideos {

	private Connector con;
	private Statement stmt;

	public QueryVideos(Connector con, Statement stmt) {
		this.stmt = stmt;
		this.con = con;
	}

	public String newMovie(String... attrs) throws SQLException {
		// String vname, int isbn, String year, String genre, String cast,
		// String rating, String format, String price, int qty
		System.out.println("New Movie: ");
		String query = "";
		String query2 = "";
		String resultstr = "";
		String result = "Failed to add movie!";

		// First check if movie exists, and it is available.

		query = String.format("INSERT INTO VideoData VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", (Object[]) attrs);
		query2 = String.format("INSERT INTO Quantities VALUES ('%s', '%s', '%s')", attrs[0], attrs[8], attrs[8]);

		try {

			stmt.executeUpdate(query);
			stmt.executeUpdate(query2);
			result = "Successfully Added New Video: " + attrs[1] + ".";

		} catch (SQLException e) {
			System.err.println("Videos:newMovie: Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			throw (e);
		}
		System.out.println("Videos:NewMovie query=" + query + "\n");
		System.out.println(result);
		return result;
	}

	/**
	 * 
	 * @param isbn
	 * @param qty
	 * @throws SQLException
	 **/
	public String updateQuantity(int isbn, int qty) throws SQLException {
		// UPDATE VideoData SET Card_id = 225 WHERE `Login`='jwells';
		String query = String.format("UPDATE Quantities SET available_qty = available_qty + '%d', total_qty = total_qty  + '%d' WHERE isbn = '%d'", qty, qty, isbn);
		String result = "Failure.";

		try {
			stmt.executeUpdate(query);
			System.out.println("Successfully updated quantiy!");
			result = "<b> SUCCESS! ";
		} catch (SQLException e) {
			System.err.println("Videos:newMovie: Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			throw (e);
		}
		System.out.println("Video:updateteMovie query=" + query + "\n");
		return result;
	}

	/**
	 * 
	 * @param attrs
	 * @return
	 */
	public String browseTitles(String... attrs) {

		String sort;
		throw new NotImplementedException();
	}

}

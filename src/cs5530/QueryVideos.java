package cs5530;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

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
			System.err.println("Videos:updateQuantity: Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			throw (e);
		}
		System.out.println("Video:updateQuantity query=" + query + "\n");
		return result;
	}

	/**
	 * 
	 * @param attrs
	 * @return
	 * @throws SQLException
	 */
	public String browseTitles(HashMap<String, ArrayList<String>> params, String sortBy) throws SQLException {

		StringBuilder sb = new StringBuilder();
		ResultSet results;
		String resultStr = "";
		String separator = " OR ";
		String query = "";
		sb.append("SELECT * FROM VideoData v WHERE ");
		for (String s : params.get("title")) {
			sb.append(String.format("v.title LIKE '%%%s%%'", s)).append(separator);
		}
		for (String s : params.get("cast")) {
			sb.append(String.format("v.cast_and_crew LIKE '%%%s%%'", s)).append(separator);
		}
		for (String s : params.get("director")) {
			sb.append(String.format("v.cast_and_crew LIKE '%%%s%%'", s)).append(separator);
		}
		for (String s : params.get("rating")) {
			sb.append(String.format("v.rating LIKE '%%%s%%'", s)).append(separator);
		}
		for (String s : params.get("genre")) {
			sb.append(String.format("v.genre LIKE '%%%s%%'", s)).append(separator);
		}
		sb.delete(sb.lastIndexOf(separator), sb.length() - 1);
		// TODO: Implement keyword searches.
		// for (String s : params.get("keyword")) {
		// query += String.format("v.title LIKE '%s'", s);
		// }
		try {
			query = sb.toString();
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
}

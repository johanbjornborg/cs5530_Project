package cs5530;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class QueryFeedback {
	Connector con;
	Statement stmt;
	User user;

	public QueryFeedback(Connector con, Statement stmt, User user) {
		this.con = con;
		this.stmt = stmt;
		this.user = user;
	}

	public String getFeedback(int isbn) throws Exception {

		String query = "";
		String resultstr = "<b>" + isbn + "</b><br>";
		ResultSet results;

		query = String.format("SELECT * FROM Feedback f WHERE f.isbn = '%s'", isbn);

		try {
			results = stmt.executeQuery(query);
		} catch (Exception e) {
			System.err.println("Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			throw (e);
		}

		System.out.println("Feedback:GetFeedback query=" + query + "\n");

		while (results.next()) {
			resultstr += results.getString("commenterLogin") + " Score: " + results.getString("Score") + " <br>Comments: &nbsp'<i>" + results.getString("Short_Text")
					+ "'</i><BR>\n";
		}
		return resultstr;
	}

	// /**
	// * Ensures that a user cannot leave multiple feedback for any video.
	// *
	// * @param isbn
	// * @param title
	// * @param quantity
	// * @return
	// * @throws Exception
	// */
	// private ResultSet checkIfValid(String isbn) throws Exception {
	// String query = "";
	// String resultstr = "";
	// ResultSet results;
	// int available_qty, total_qty;
	// double price;
	//
	// query =
	// String.format("SELECT * FROM Feedback f WHERE commenterLogin = '%s' AND ",
	// user.login, isbn);
	//
	// try {
	// results = stmt.executeQuery(query);
	// } catch (Exception e) {
	// System.err.println("Unable to execute query:" + query + "\n");
	// System.err.println(e.getMessage());
	// throw (e);
	// }
	//
	// System.out.println("Feedback:checkIfValid query=" + query + "\n");
	//
	// return results;
	// }

	public String getUsefulFeedback(int n, int isbn) throws Exception {

		String query = "";
		String resultstr = "<b>Useful Feedback for Movie" + isbn + "</b><br>";
		ResultSet results;

		query = String.format("SELECT * FROM FeedbackRatings fr NATURAL JOIN Feedback f WHERE f.isbn = '%s'", isbn);

		try {
			results = stmt.executeQuery(query);
		} catch (Exception e) {
			System.err.println("Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			throw (e);
		}

		System.out.println("Feedback:GetFeedback query=" + query + "\n");

		while (results.next()) {
			resultstr += results.getString("commenterLogin") + " Score: " + results.getString("Score") + " <br>Comments: &nbsp'<i>" + results.getString("Short_Text")
					+ "'</i><BR>\n";
		}
		return resultstr;
	}

	public String rateFeedback(int id, int score) throws SQLException {
		System.out.println("Rate Feedback: ");
		String query = "";
		String result = "Failed to Rate Feedback";

		if (score > 2) {
			score = 2;
		}
		if (score < 0) {
			score = 0;
		}
		query = String.format("INSERT INTO FeedbackRatings VALUES ('%s', '%s', '%s')", id, user.login, score);
		try {
			stmt.executeUpdate(query);
			result = "Successfully Rated Feedback " + id + "!";

		} catch (SQLException e) {
			System.err.println("Feedback: RateFeedback: Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			throw (e);
		}
		System.out.println("Feedback:RateFeedback query=" + query + "\n");
		System.out.println(result);

		return result;
	}

	public String leaveNewFeedback(int isbn, String comments, int score) throws SQLException {
		System.out.println("Leave Feedback: ");
		String query = "";
		String result = "Failed to Leave Feedback";
		query = String.format("INSERT INTO Feedback VALUES ('%s', '%s', '%s', '%s')", user.login, isbn, score, comments);
		try {
			stmt.executeUpdate(query);
			result = "Successfully Left Feedback for ISBN " + isbn + "!";

		} catch (SQLException e) {
			System.err.println("Feedback: LeaveNewFeedback: Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			throw (e);
		}
		System.out.println("Feedback:LeaveNewFeedback query=" + query + "\n");
		System.out.println(result);

		return result;
	}
}

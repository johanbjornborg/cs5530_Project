package cs5530;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.mysql.jdbc.Connection;

import sun.org.mozilla.javascript.internal.xml.XMLLib.Factory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class User {

	protected Connector conn;
	protected Statement stmt;

	private boolean loggedIn;
	public String login;
	public String fullName, firstName, lastName;
	private String passwd;
	private String address;
	private String phoneNumber;
	private int cardNumber;

	public User(Connector conn, Statement stmt) {
		this.conn = conn;
		this.stmt = stmt;
		this.loggedIn = false;
	}

	public boolean loginUser(String login, String passwd) throws SQLException {
		ResultSet results;
		String query;
		String resultstr = "";
		String result = "Failed to create an account. See log for details.";

		query = String.format("SELECT * FROM UserData WHERE Login = '%s' AND Password = '%s'", login, passwd);
		try {

			results = stmt.executeQuery(query);
			while (results.next()) {
				// Check column names for correctness.
				this.login = results.getString("Login");
				this.passwd = results.getString("Password");
				this.fullName = results.getString("Full_name");
				this.firstName = this.fullName.split(" ")[0];
				this.lastName = this.fullName.split(" ")[1];
				this.address = results.getString("Address");
				this.phoneNumber = results.getString("Phone");
				this.cardNumber = results.getInt("Card_id");
				this.loggedIn = true;
			}
			return true;
		} catch (SQLException e) {
			System.err.println("Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			this.loggedIn = false;
			return false;
		}

	}

	public String getUserRecord() throws Exception {

		String query = "";
		String resultStr = "<b>USER REPORT</b><br>";
		ResultSet results;

		// Get all personal info
		// Select all from user where user = user...
		query = String.format("SELECT * FROM UserData WHERE Login = '%s'", this.login);
		try {

			results = stmt.executeQuery(query);
			while (results.next()) {
				// Check column names for correctness.
				resultStr += "<b> Login: " + results.getString("Login") + " Name: " + results.getString("Full_name") + " <br>Address: &nbsp'<i>" + results.getString("Address")
						+ " <br>Phone: &nbsp'<i>" + results.getString("Phone") + " <br>Credit Card: &nbsp'<i>" + results.getString("Card_id") + "'</i><BR>\n";

			}

		} catch (SQLException e) {
			System.err.println("Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			this.loggedIn = false;

		}
		// Get all orders (movie name, copies ordered, date)
		// Select all from order history where user = user and select movie name
		// where order.isbn = movie.isbn
		resultStr += "<b>Order History</b><br>";
		query = String.format("SELECT * FROM OrderHistory o NATURAL JOIN VideoData v WHERE o.userLogin = '%s' AND o.isbn = v.isbn", this.login);
		try {

			results = stmt.executeQuery(query);
			while (results.next()) {
				// Check column names for correctness.
				resultStr += " ISBN: " + results.getString("isbn") + " <br>Order Date: &nbsp'<i>" + results.getString("Rent_date") + "Additional Info: "
						+ results.getString("Additional_info") + "'</i><BR>\n";

			}

		} catch (SQLException e) {
			System.err.println("Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			this.loggedIn = false;

		}
		// Get all feedback left
		// Select all from feedback where user = user.
		resultStr += "<b>Feedback left:</b><br>";
		query = String.format("SELECT * FROM Feedback WHERE commenterLogin = '%s'", this.login);
		try {

			results = stmt.executeQuery(query);
			while (results.next()) {
				// Check column names for correctness.
				resultStr += results.getString("commenterLogin") + " Score: " + results.getString("Score") + " <br>Comments: &nbsp'<i>" + results.getString("Short_Text")
						+ "'</i><BR>\n";

			}

		} catch (SQLException e) {
			System.err.println("Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			this.loggedIn = false;

		}
		// Get all feedback rated
		// select all feedback where ... crap.
		resultStr += "<b>Feedback rated by User:</b><br>";
		query = String.format("SELECT * FROM FeedbackRatings fr WHERE fr.user = '%s'", this.login);
		try {

			results = stmt.executeQuery(query);
			while (results.next()) {
				// Check column names for correctness.
				resultStr += results.getString("feedback_id") + " Rating (0-2): " + results.getString("usefulness") + "'</i><BR>\n";

			}

		} catch (SQLException e) {
			System.err.println("Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			this.loggedIn = false;

		}

		// Get all trusted users, incl. date trusted.
		resultStr += "<b>Other users trusted by User:</b><br>";
		query = String.format("SELECT * FROM Trusts WHERE Truster = '%s'", this.login);
		try {

			results = stmt.executeQuery(query);
			while (results.next()) {
				// Check column names for correctness.
				resultStr += "Trustee (User trusts this person)" + results.getString("Trustee") + " isTrusted (0|1): " + results.getString("date_trusted") + "'</i><BR>\n";
			}

		} catch (SQLException e) {
			System.err.println("Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			this.loggedIn = false;

		}
		return resultStr;
	}

	public String getOrderHistory() {
		QueryOrder order = new QueryOrder(conn, stmt, this);
		try {
			return order.getOrderHistory();
		} catch (SQLException e) {
			e.printStackTrace();
			return "Unable to retrieve order history.";
		}
	}

	public String registerNewUser(String... attrs) throws Exception {
		// Per the schema, the values of attrs are as follows:
		// full name, login, password, address, phone, credit card.
		// insert into UserData values
		// ('jwells', '*****', 'John Doe', '123 St.', 'phone',
		// '12345678910111213')
		String query;
		String resultstr = "";
		String result = "Failed to create an account. See log for details.";

		query = String.format("INSERT INTO UserData VALUES ('%s', '%s', '%s', '%s', '%s', '0')", (Object[]) attrs);
		try {
			// if(Variables.getStatement().executeUpdate(query) == 1){
			if (stmt.executeUpdate(query) == 1) {
				result = "Successfully created account for " + attrs[0] + ".";
			}

		} catch (SQLException e) {
			System.err.println("Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			throw (e);
		}
		System.out.println("User:RegisterNewUsers query=" + query + "\n");
		System.out.println(result);
		return result;
	}

	public String trustRelationship(String trustee, int isTrusted) throws SQLException {
		Date date = new Date();
		System.out.println("Trust User: ");
		String query = "";
		String result = "Failed to Leave Feedback";
		query = String.format("INSERT INTO Trusts VALUES ('%s', '%s', '%s', 0, '%s')", this.login, trustee, isTrusted, date);
		try {
			stmt.executeUpdate(query);
			result = "Successfully set Trust level for " + trustee + "!";

		} catch (SQLException e) {
			System.err.println("User: TrustRelationship: Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			throw (e);
		}
		System.out.println("User: TrustRelationship query=" + query + "\n");
		System.out.println(result);

		return result;

	}

	public String userAwards(int m) {
		throw new NotImplementedException();
	}

	public String getStatistics(int m) {
		throw new NotImplementedException();
	}
}

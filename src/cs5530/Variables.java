package cs5530;

import java.sql.*;

public class Variables {
	private static String FirstName;
	private static String LastName;
	private static String uLogin;
	private static int magicNumber;
	private static int studentID;
	private static Connection con;
	private static Statement stmt;

	public static void setConnection() throws Exception {
		try {
			String userName = "cs5530u63";
			String password = "6fllo6op";
			String url = "jdbc:mysql://georgia.eng.utah.edu/cs5530db63";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url, userName, password);

			// DriverManager.registerDriver (new
			// oracle.jdbc.driver.OracleDriver());
			// stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
			// ResultSet.CONCUR_UPDATABLE);
			stmt = con.createStatement();
			// stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
			// ResultSet.CONCUR_UPDATABLE);
		} catch (Exception e) {
			System.err.println("Unable to open mysql jdbc connection. The error is as follows,\n");
			System.err.println(e.getMessage());
			throw (e);
		}
	}

	public static Connection getConnection() {
		return con;
	}

	public static Statement getStatement(){
		return stmt;
	}
	
	public static void setLogin(String UserLogin) {
		uLogin = UserLogin;
	}

	public static String getLogin() {
		return uLogin;
	}



}

package cs5530;

import java.sql.*;
import java.util.Vector;

public class Connector {

	public Connection con;
	public Statement stmt;
	
	private String userName = "cs5530u63";
	private String password = "6fllo6op";
	private String url = "jdbc:mysql://georgia.eng.utah.edu/cs5530db63";
	Vector<Connection> connectionPool = new Vector<Connection>();

	public Connector() throws Exception {
		initialize();
	}

	private void initialize() throws Exception {
		initializeConnectionPool();

		try {

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

	private void initializeConnectionPool() {
		// TODO Auto-generated method stub
		while (!checkIfConnectionPoolIsFull()) {
			System.out.println("Connection Pool is NOT full. Proceeding with adding new connections");
			// Adding new connection instance until the pool is full
			connectionPool.addElement(createNewConnectionForPool());
		}
		System.out.println("Connection Pool is full.");
	}

	private synchronized boolean checkIfConnectionPoolIsFull() {
		final int MAX_POOL_SIZE = 5;

		// Check if the pool size
		if (connectionPool.size() < 5) {
			return false;
		}

		return true;
	}

	// Creating a connection
	private Connection createNewConnectionForPool() {
		Connection connection = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url, userName, password);
			System.out.println("Connection: " + connection);
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle);
			return null;
		} catch (ClassNotFoundException cnfe) {
			System.err.println("ClassNotFoundException: " + cnfe);
			return null;
		}

		return connection;
	}

	public synchronized Connection getConnectionFromPool() {
		Connection connection = null;

		// Check if there is a connection available. There are times when all
		// the connections in the pool may be used up
		if (connectionPool.size() > 0) {
			connection = (Connection) connectionPool.firstElement();
			connectionPool.removeElementAt(0);
		}
		// Giving away the connection from the connection pool
		return connection;
	}

	public synchronized void returnConnectionToPool(Connection connection) {
		// Adding the connection from the client back to the connection pool
		connectionPool.addElement(connection);
	}

	public void closeConnection() throws Exception {
		con.close();
	}
}

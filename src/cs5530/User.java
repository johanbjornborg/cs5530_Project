package cs5530;

import java.sql.ResultSet;
import java.sql.Statement;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class User {
	public User() {
	}

	public String getUser(String name){
		throw new NotImplementedException();
	}
	
	public String getUserRecord(String attrName, String attrValue, Statement stmt) throws Exception {
		throw new NotImplementedException();
	}
	
	public String registerNewUser(String ... attrs){
		throw new NotImplementedException();
	}
	
	public String trustRelationship(String user1, String user2, Boolean isTrusted){
		// User1 [not]trusts user2.
		throw new NotImplementedException();
	}
}

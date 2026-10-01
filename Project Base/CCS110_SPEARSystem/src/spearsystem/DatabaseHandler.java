package spearsystem;

import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandler {
	
	// REGION - CONNECTION GETTER
	//Handles the initialisation/confirmation of connections status to the Database.
	private String connectionStatus;
	public boolean checkConnection() {
		
		try(Connection authC = ConnectionHandler.getAsAuthenticator();
			Connection custoC = ConnectionHandler.getAsCustodian()) {
			
			connectionStatus = "Database Connection successfully established.";
			return true;
		} catch (SQLException e) {
			
			e.printStackTrace();
			connectionStatus = "Connection Timeout.";
			return false;
		}
		
	}
	
	public String getConnStatus() {
		return connectionStatus;
	}
	
	// END - CONNECTION GETTER
	
	
	// REGION - LOGIN HANDLER
	// Handles the authentication of user credentials.
	private String authStatus;
	public boolean authenticate(String user, Reader pass) {
		
		try(Connection auth = ConnectionHandler.getAsAuthenticator();
			PreparedStatement st = auth.prepareStatement("select * from userAccounts WHERE username = ? AND pass = password(?) LIMIT 1")) {
			
			st.setString(1, user);
			st.setCharacterStream(2, pass);
			
			ResultSet results = st.executeQuery();
			
			if(results.next()) {
				
				return true;
			}
			else {
				
				authStatus = "Invalid Credentials.";
				return false;
			}
			
		} catch(SQLException e) {
			
			e.printStackTrace();
			authStatus = "Connection / Database Error.";
			return false;
		}
		
	}
	
	public String getAuthStatus() {
		return authStatus;
	}
	
	// END - LOGIN HANDLER
	
	
	
	
}

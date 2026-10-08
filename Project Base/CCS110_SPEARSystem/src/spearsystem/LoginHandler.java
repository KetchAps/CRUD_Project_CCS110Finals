package spearsystem;

import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
public class LoginHandler {

	public record AuthStatus(
			int ID,
			String user,
			boolean isAdmin
			) {}
	
	// REGION - CONNECTION GETTER
	//Handles the initialisation/confirmation of connections status to the Database.
	private String connectionStatus;
	public boolean checkConnection() {
		
		try(Connection authC = ConnectionHandler.tryConnect()) {
			
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
	
	
	public AuthStatus authenticate(String user, Reader pass) throws SPEARException {
		
		try(Connection auth = ConnectionHandler.tryConnect();
			PreparedStatement st = auth.prepareStatement("select userID, username, isAdmin from userAccounts WHERE username = ? AND password = password(?) LIMIT 1")) {
			AuthStatus authForm;
			
			st.setString(1, user);
			st.setCharacterStream(2, pass);
			
			ResultSet results = st.executeQuery();
			
			if(results.next()) {
				authForm = new AuthStatus(results.getInt(1), results.getString(2), results.getBoolean(3));
				return authForm;
			}
			else throw new SPEARException("Authentication Failed: " + user + " is not recognised.");
		} catch(SQLException e) {
			e.printStackTrace();
			throw new SPEARException("Authentication Failed: Connection Timeout.");
		}
		
	}
	
	
}

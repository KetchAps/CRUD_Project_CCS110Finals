package sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.time.LocalTime;

public class DatabaseHandler {
	
	String exceptionMessage;
	
	
	public boolean initialiseConnections()  {
		
		try(Connection tryAuth = ConnectPool.getAuthenticator();
		Connection tryMain = ConnectPool.getEmployeeAccess()) {
			
			exceptionMessage = "Connections to Database Established Successfully.";
			return true;
		} catch (SQLException e) {
			
			exceptionMessage = "Connection Timeout.";
			return false;
		}
		
		
	}
	
	public boolean authenticate(String user, String pass) {
		
		boolean authResult;
		
		try(Connection connect = ConnectPool.getAuthenticator()) {
			
			PreparedStatement st = connect.prepareStatement("select * from employeeList where employeeName = ? AND pass = ? LIMIT 1");
	
			st.setString(1, user);
			st.setString(2, pass);
			
			ResultSet result = st.executeQuery();
			
			if (result.next())
				authResult = true;
			else
			{
				exceptionMessage = "Invalid Credentials.";
				authResult = false;
			}
			st.close();
			result.close();
		} catch (SQLException e) {
			
			exceptionMessage = "Connection Timeout.";
			e.printStackTrace();
			return false;
		}
		
		
		return authResult;
	}
	
	public boolean writeData(String ID, String equipment, String date) {
		String query = "insert into BorrowRecord values (recordID, ?, ?, ?, false)";
		try (Connection connect = ConnectPool.getEmployeeAccess();
				PreparedStatement st = connect.prepareStatement(query)) {
				
				st.setString(1, ID);
				st.setString(2, equipment);
				st.setString(3, date);
				
				st.executeUpdate();
				return true;
				
			} catch (SQLException e) {
				
				e.printStackTrace();
				return false;
			}
			
		
	}
	
	
	String getExceptionCause() {
		return exceptionMessage;
	}
	
}

package sql;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectPool {
		private static boolean authConnectionOnline;
		private static boolean employeeConnectionOnline;
	    private static DataSource authenticatorConnectionSource;
	    private static DataSource employeeConnectionSource;
	   
	    static {
	    	
	    	authenticatorConnectionSource = AuthenticatorSource();
	    	employeeConnectionSource = EmployeeSource();
	    }

	    public static Connection getAuthenticator() throws SQLException {
	        return authenticatorConnectionSource.getConnection();
	    
	    }
	    
	    public static Connection getEmployeeAccess() throws SQLException {
	    	return employeeConnectionSource.getConnection();
	    }

	    private static DataSource AuthenticatorSource() {
	        ComboPooledDataSource cpds = new ComboPooledDataSource();
	        try {
	            cpds.setDriverClass("com.mysql.cj.jdbc.Driver");
	        } catch (PropertyVetoException e) {
	            e.printStackTrace();
	        }
	        
	        try {
				cpds.setLoginTimeout(3);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/ProofOfConcept");
	        cpds.setUser("authenticator");
	        cpds.setPassword("root");
	        cpds.setMinPoolSize(3);
	        cpds.setAcquireIncrement(5);
	        cpds.setMaxPoolSize(20);
	        cpds.setCheckoutTimeout(2000);
	        cpds.setIdleConnectionTestPeriod(60);
	        cpds.setTestConnectionOnCheckin(true);
	        return cpds;
	    }
	    
	    private static DataSource EmployeeSource() {
	    	ComboPooledDataSource cpds = new ComboPooledDataSource();
	        try {
	            cpds.setDriverClass("com.mysql.cj.jdbc.Driver");
	        } catch (PropertyVetoException e) {
	            e.printStackTrace();
	        }
	        try {
				cpds.setLoginTimeout(3);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/ProofOfConcept");
	        cpds.setUser("employee");
	        cpds.setPassword("employee");
	        cpds.setMinPoolSize(3);
	        cpds.setAcquireIncrement(5);
	        cpds.setMaxPoolSize(20);
	        cpds.setCheckoutTimeout(2000);
	        cpds.setIdleConnectionTestPeriod(60);
	        cpds.setTestConnectionOnCheckin(true);
	        return cpds;
	    	
	    	
	    }
	    
	    
	    public static void setAuthConnectionState(boolean state) {
	    	authConnectionOnline = state;
	    	
	    }
	    
	    public static boolean getAuthConnectionState() {
	    	return authConnectionOnline;
	    }
	    
	}
	    


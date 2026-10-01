package spearsystem;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionHandler {
	
	// CONNECTION POOL HANDLER (c3p0) - DO NOT TOUCH.
	
    private static DataSource authenticatorConnectionSource;
    private static DataSource employeeConnectionSource;
   
    static {
    	
    	authenticatorConnectionSource = AuthenticatorSource();
    	employeeConnectionSource = EmployeeSource();
    }

    public static Connection getAsAuthenticator() throws SQLException {
        return authenticatorConnectionSource.getConnection();
    
    }
    
    public static Connection getAsCustodian() throws SQLException {
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
        
        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/spearDatabase");
        cpds.setUser("authenticator");
        cpds.setPassword("userAuthProfile");
        cpds.setMinPoolSize(3);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(3);
        cpds.setCheckoutTimeout(9000);
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
        
        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/spearDatabase");
        cpds.setUser("custodian");
        cpds.setPassword("custodianAccess");
        cpds.setMinPoolSize(3);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);
        cpds.setCheckoutTimeout(9000);
        cpds.setIdleConnectionTestPeriod(60);
        cpds.setTestConnectionOnCheckin(true);
        return cpds;
    	
    	
    }
	}
	    


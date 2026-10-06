package spearsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;

public class DatabaseHandler {
	
	public record BorrowRecord(
			int borrowerID,
			int equipmentID,
			Date dateBorrowed,
			Date returbByDate,
			Date dateReturned,
			int custodian,
			String conditionOnBorrow,
			String conditionOnReturn
			) {}
	
	public record MaintenanceReport(
			int equipmentID,
			Date reportedDate,
			int custodian,
			String issueReport,
			String actionsTaken,
			double cost,
			String status
			) {}
	
	/*  X- transactionID INT AUTO_INCREMENT PRIMARY KEY,
    	1- borrowerID INT NOT NULL,
    	2- equipmentID INT NOT NULL,
    	3- borrowDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    	4- expectedReturnDate DATE NOT NULL,
    	5- actualReturnDate DATE,
    	6- receivedBy INT,
		7- itemConditionUponBorrow VARCHAR(50) DEFAULT 'Good',
    	8- itemConditionUponReturn VARCHAR(50),   -- NULL = not yet returned
    	X- status VARCHAR(50) DEFAULT 'Borrowed',
    	X- remarks VARCHAR(255),
    */
	public boolean createBorrowRecord(BorrowRecord r) {
		
		try(Connection conn = ConnectionHandler.tryConnect();
			PreparedStatement brwRecord = conn.prepareStatement("insert into borrow_transaction values (transactionID, ?, ?, ?, ?, ?, ?, ?, ?, status, null)")) {
			
			brwRecord.setInt(1, r.borrowerID);
			brwRecord.setInt(2, r.equipmentID);
			brwRecord.setDate(3, (r.dateBorrowed));
			brwRecord.setDate(4, (r.returbByDate));
			brwRecord.setDate(5, (r.dateReturned));
			brwRecord.setInt(6, r.custodian);
			brwRecord.setString(7, r.conditionOnBorrow);
			brwRecord.setString(8, r.conditionOnReturn);
			
			brwRecord.executeUpdate();
			
			//brwRecord.setD(4, );
			return true;
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
		
	}
	
	/*
	 * 		1- int equipmentID,
			2- Date reportedDate,
			3- int custodian,
			4- String issueReport,
			5- String actionsTaken,
			6- double cost,
			7- String status
	 */
	
	public boolean createMaintenanceReport(MaintenanceReport m) {
		try(Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement report = conn.prepareStatement("insert into maintenance_records values (maintenanceID, ?, ?, ?, ?, ?, ?, ?)")) {
				
				report.setInt(1, m.equipmentID);
				report.setDate(2, m.reportedDate);
				report.setInt(3, m.custodian);
				report.setString(4, m.issueReport);
				report.setString(5, m.actionsTaken);
				report.setDouble(6, m.cost);
				report.setString(7, m.status);
				report.executeUpdate();
				
				//brwRecord.setD(4, );
				return true;
			} catch (SQLException e) {
				
				e.printStackTrace();
				return false;
			}
	}
	
	public boolean updateEquipmentAvailability(int value, int id) {
			String status;
			switch (value) {
			default: 
			case 0: status = "Available";	break;
			case 1: status = "In Use"; 		break;
			case 2: status = "Maintenance"; break;
			
			}
			
		try(Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement st = conn.prepareStatement("update inventory SET availability = ? WHERE equipmentID = ?")) {
				
				st.setString(1, status);
				st.setInt(2, id);
				st.executeUpdate();
				
				//brwRecord.setD(4, );
				return true;
			} catch (SQLException e) {
				
				e.printStackTrace();
				return false;
			}
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------
	// QUERIES / READ
	//---------------------------------------------------------------------------------------------------------------------------------
		
	
	public String[][] getEquipmentList() {
		
		
		try(Connection conn = ConnectionHandler.tryConnect();
			PreparedStatement st = conn.prepareStatement("SELECT * from inventory")) {
			ArrayList<String[]> list = new ArrayList<>();
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				String[] temp = new String[13];
				
				for(int i = 1; i <= 13; i++) {
				temp[i - 1] = rs.getString(i);
			
				}
				
				list.add(temp);
				
			}
			String[][] array = list.toArray(new String[0][]);
			return array;
			
		} catch (SQLException e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	public String[] getInventoryFilters() {
		
		try(Connection conn = ConnectionHandler.tryConnect();
			PreparedStatement cat = conn.prepareStatement("select distinct categoryName from inventory");
			PreparedStatement catC = conn.prepareStatement("select count(distinct categoryName) from inventory")) {
			
			String[] values;
			int categorySize = 1;
			
			ResultSet cs = catC.executeQuery();
			if(cs.next()) categorySize = cs.getInt(1);
			
			values = new String[categorySize];
			ResultSet rs = cat.executeQuery();
			
			for (int i = 0; i < values.length; i++) {
				if(rs.next()) 
				values[i] = rs.getString(1);
				
				else break;
			}
			
			cs.close();
			rs.close();
			return values;
			
		} catch(SQLException e) {
			
			e.printStackTrace();
			return null;
			
		}
		
	}
	
	public boolean checkItemValidity(int id) {
		
		try (Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement st = conn.prepareStatement("select equipmentID from inventory WHERE equipmentID = ? LIMIT 1")) {
				
				st.setInt(1, id);
				
				ResultSet rs = st.executeQuery();
				
				if(rs.next()) return true;
				else return false;
			}
		 catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
	}
	
	public String[] fetchEquipmentDetails(int id) {
		
		try (Connection conn = ConnectionHandler.tryConnect();
			PreparedStatement st = conn.prepareStatement("select * from inventory WHERE equipmentID = ? LIMIT 1")) {
			
			st.setInt(1, id);
			
			ResultSet rs = st.executeQuery();
			
			ArrayList<String> list = new ArrayList<>();
			if(rs.next()) {
				
				for ( int i = 1; i <= 13; i++)
					list.add(rs.getString(i));			
			}
			
			return list.toArray(new String[0]);
			
		}
	 catch (SQLException e) {
		
		e.printStackTrace();
		return null;
	}
		
	}
	
	public String[] getBorrowerList() {
		
		try (Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement st = conn.prepareStatement("select CONCAT('(', borrowerID, ') ', firstName, ' ', lastname) from borrowers")) {
				
				ResultSet rs = st.executeQuery();
				
				ArrayList<String> list = new ArrayList<>();
				while(rs.next()) {
					
						list.add(rs.getString(1));
						System.out.println(rs.getString(1));
				}
				
				return list.toArray(new String[0]);
				
			}
		 catch (SQLException e) {
			
			e.printStackTrace();
			return null;
		}
		
	}
	
	public Integer[] getBorrowerIDList() {
		
		try (Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement st = conn.prepareStatement("select borrowerID from borrowers")) {
				
				ResultSet rs = st.executeQuery();
				
				ArrayList<Integer> list = new ArrayList<>();
				while(rs.next()) {
					
						list.add(rs.getInt(1));
				}
				
				return list.toArray(new Integer[0]);
				
			}
		 catch (SQLException e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
}

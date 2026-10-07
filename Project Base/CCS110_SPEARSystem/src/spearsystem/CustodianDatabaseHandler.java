package spearsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;

public class CustodianDatabaseHandler {
	
	/**
	 * @param
	 */
	public record BorrowRecord(
			int ID,
			int equipmentID,
			Date dateBorrowed,
			Date returbByDate,
			Date dateReturned,
			int custodian,
			String conditionOnBorrow,
			String conditionOnReturn,
			String status
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
	
	public record EquipmentDetails(
			int equipmentID,
		    String propertyTag,
		    String equipmentName,
		    String categoryName,
		    String brandModel,
		    String serialNumber,
		    String description,
		    int roomID,
		    Date purchaseDate,
		    double purchaseCost,
		    String condition,
		    String availability,
		    Date date_added
		    ) {}
	
	public record BorrowerList(String[] borrower, Integer[] borrowerID) {}
	
	
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
			PreparedStatement brwRecord = conn.prepareStatement("insert into borrow_transaction values (transactionID, ?, ?, ?, ?, ?, ?, ?, ?, ?, null)")) {
			
			brwRecord.setInt(1, r.ID());
			brwRecord.setInt(2, r.equipmentID());
			brwRecord.setDate(3, (r.dateBorrowed()));
			brwRecord.setDate(4, (r.returbByDate()));
			brwRecord.setDate(5, (r.dateReturned()));
			brwRecord.setInt(6, r.custodian());
			brwRecord.setString(7, r.conditionOnBorrow());
			brwRecord.setString(8, r.conditionOnReturn());
			brwRecord.setString(9, r.status());
			
			brwRecord.executeUpdate();
			
			//brwRecord.setD(4, );
			return true;
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	public boolean updateBorrowRecord(BorrowRecord r) {
		
		try(Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement brwRecord = conn.prepareStatement("update borrow_transaction SET expectedReturnDate = ?, actualReturnDate = ?, itemConditionUponReturn = ?, status = ? WHERE transactionID = ?")) {
				
				brwRecord.setDate(1, r.returbByDate());
				brwRecord.setDate(2, r.dateReturned());
				brwRecord.setString(3, r.conditionOnReturn());
				brwRecord.setString(4, r.status());
				
				brwRecord.setInt(5, r.ID());
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
				
				report.setInt(1, m.equipmentID());
				report.setDate(2, m.reportedDate());
				report.setInt(3, m.custodian());
				report.setString(4, m.issueReport());
				report.setString(5, m.actionsTaken());
				report.setDouble(6, m.cost());
				report.setString(7, m.status());
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
			case 1: status = "Borrowed"; 	break;
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
	
	public boolean updateEquipmentCondition(int value, int id) {
		String status;
			switch (value) {
			default: 
			case 0: status = "Good";	 break;
			case 1: status = "Worn";  	 break;
			case 2: status = "Damageed"; break;
			
			}
			
		try(Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement st = conn.prepareStatement("update inventory SET condition = ? WHERE equipmentID = ?")) {
				
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
	
	public boolean updateEquipmentCondition(String status, int id) {
			
		try(Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement st = conn.prepareStatement("update inventory SET condition_status = ? WHERE equipmentID = ?")) {
				
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
			
			try
			(ResultSet rs = st.executeQuery())
					{
			while(rs.next()) {
				String[] temp = new String[13];
				
				for(int i = 1; i <= 13; i++) {
				temp[i - 1] = rs.getString(i);
			
				}
				
				list.add(temp);
			}
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
	
	public EquipmentDetails fetchEquipmentDetails(int id) {
		
		try (Connection conn = ConnectionHandler.tryConnect();
			PreparedStatement st = conn.prepareStatement("select * from inventory WHERE equipmentID = ? LIMIT 1")) {
			EquipmentDetails deets = null;
			
			st.setInt(1, id);
			
			try
			(ResultSet rs = st.executeQuery();)
			{
			
			if(rs.next()) {
				deets = new EquipmentDetails(
				rs.getInt(1),
			    rs.getString(2),
			    rs.getString(3),
			    rs.getString(4),
			    rs.getString(5),
			    rs.getString(6),
			    rs.getString(7),
			    rs.getInt(8),
			    rs.getDate(9),
			    rs.getDouble(10),
			    rs.getString(11),
			    rs.getString(12),
			    rs.getDate(13)		
						);
			}}
			
			return deets;
			
		}
	 catch (SQLException e) {
		
		e.printStackTrace();
		return null;
	}
		
	}
	
	public String checkEquipmentAvailability(int id) {
		
		try(Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement c = conn.prepareStatement("select availability from inventory WHERE equipmentID = ? LIMIT 1")) {
				
				c.setInt(1, id);
			
				String value = null;
				
				try (ResultSet rs = c.executeQuery()) {
					if(rs.next()) value = rs.getString(1);
				
				} 
				
				return value;
				
				
			} catch(SQLException e) {
				
				e.printStackTrace();
				return null;
				
			}
		
		
	}
	
	
	
	public boolean checkBorrowerExistance(int id) {
		
		try (Connection conn = ConnectionHandler.tryConnect();
			PreparedStatement st = conn.prepareStatement("select borrowerID from borrowers WHERE borrowerID = ?")) {
				
			st.setInt(1, id);
			
				try (ResultSet rs = st.executeQuery();) {
					if(rs.next())
					
						return true;
				   else return false;
				}
		
			
	}catch (SQLException e) {
		
		e.printStackTrace();
		return false;
	}
	
	}
	
	public String[] getBorrowerList() {
		
		try (Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement st = conn.prepareStatement("select CONCAT('(', borrowerID, ') ', firstName, ' ', lastname) from borrowers")) {
				
			
				try (ResultSet rs = st.executeQuery();) {
				
				ArrayList<String> list = new ArrayList<>();
				while(rs.next()) {
					
						list.add(rs.getString(1));
						System.out.println(rs.getString(1));
				}
				
				return list.toArray(new String[0]);
				
			}
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
	
	public BorrowRecord fetchBorrowRecord(int id) {
		
		try (Connection conn = ConnectionHandler.tryConnect();
			PreparedStatement st = conn.prepareStatement("select * from borrow_transaction WHERE transactionID = ? LIMIT 1")) {
			BorrowRecord record = null;
			
			st.setInt(1, id);
			
			try
			(ResultSet rs = st.executeQuery();)
			{
			
			if(rs.next()) record = new BorrowRecord(
					rs.getInt(2),
					rs.getInt(3),
					rs.getDate(4),
					rs.getDate(5),
					rs.getDate(6),
					rs.getInt(7),
					rs.getString(8),
					rs.getString(9),
					rs.getString(10)
					);
			}
			
			return record;
			
		}
	 catch (SQLException e) {
		
		e.printStackTrace();
		return null;
	}
		
	}
	
	public boolean checkRecordValidity(int id) {
		
		try (Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement st = conn.prepareStatement("select transactionID from borrow_transaction WHERE transactionID = ? LIMIT 1")) {
				
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
	

	public String[][] getBorrowRecordList() throws SPEARException {
		try(Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement st = conn.prepareStatement("SELECT * from borrow_transaction")) {
				ArrayList<String[]> list = new ArrayList<>();
				
				try
				(ResultSet rs = st.executeQuery())
						{
				while(rs.next()) {
					String[] temp = new String[11];
					
					for(int i = 1; i <= 11; i++) {
					temp[i - 1] = rs.getString(i);
					System.out.println(" " + rs.getString(i) + " ");
					}
					
					list.add(temp);
				}
				}
				String[][] array = list.toArray(new String[0][]);
				
				return array;
				
			} catch (SQLException e) {
				
				throw new SPEARException("E");
				
			}
		
	}

}

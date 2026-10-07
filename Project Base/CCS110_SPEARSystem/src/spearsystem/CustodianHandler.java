package spearsystem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;

import spearsystem.CustodianDatabaseHandler.BorrowRecord;
import spearsystem.CustodianDatabaseHandler.BorrowerList;
import spearsystem.CustodianDatabaseHandler.EquipmentDetails;
import spearsystem.CustodianDatabaseHandler.MaintenanceReport;

public class CustodianHandler {
	
	private CustodianDatabaseHandler db = new CustodianDatabaseHandler();
	
	
	// EQUIPMENT MANAGEMENT
	
	public String[][] getEquipmentList() throws SPEARException {
		String[][] value = db.getEquipmentList();
		if(value == null || value.length <= 0) throw new SPEARException("Something went wrong while trying to fetch Equipment Data. Check your connection and try again.");
		return value;
	}
	
	public String[][] getBorrowedRecordsList() throws SPEARException {
		String[][] value = db.getBorrowRecordList();
		
		if(value == null) throw new SPEARException("Something went wrong while trying to fetch Equipment Data. Check your connection and try again.");
		return value;
	}
	
	
	public String[] getInventoryFilters() throws SPEARException {
		String[] value = db.getInventoryFilters();
		
		if(value == null) throw new SPEARException("Something went wrong while trying to fetch Equipment categories. Check your connection and try again.");
		return value;
		
	}
	

	/**
	 * Returns a given row from the Database's Inventory.
	 * 	Values are returned in order:
	 * 	(0) equipmentID 
    	(1) propertyTag 
		(2) equipmentName 
    	(3) categoryName   
    	(4) brandModel  
    	(5) serialNumber  
    	(6) description 
    	(7) roomID  
    	(8) purchaseDate  
    	(9) purchaseCost  
		(10) condition_status  
    	(11) availability  
		(12) date_added  
	 * 
	 * @param id
	 * @return String[]
	 */
	
	public EquipmentDetails fetchEquipmentDetails(int id) throws SPEARException {
		boolean validate = db.checkItemValidity(id);
		
		if(!validate) throw new SPEARException("Selected Equipment does not exist.");
		
		EquipmentDetails result = db.fetchEquipmentDetails(id);
		if(result == null) throw new SPEARException("Something went wrong while trying to fetch Equipment Data. Check your connection and try again.");
		
		return result;
	}
	
	public BorrowRecord fetchBorrowRecord(int id) throws SPEARException {
		boolean validate = db.checkRecordValidity(id);
		
		if(!validate) throw new SPEARException("Selected Record does not exist.");
		
		BorrowRecord result = db.fetchBorrowRecord(id);
		if(result == null) throw new SPEARException("Something went wrong while trying to fetch Borrow Record. Check your connection and try again.");
		
		return result;
	}
	
	/**
	 * e
	 * @return
	 * @throws SPEARException
	 */
	public BorrowerList fetchBorrowers() throws SPEARException {
		String[] name = db.getBorrowerList();
		Integer[] id = db.getBorrowerIDList();
		
		if(name == null || id == null) throw new SPEARException("Something went wrong while trying to fetch Equipment Data. Check your connection and try again.");
		return new BorrowerList(name, id);
	}
	
	
	public boolean checkItemValidity(int id) {
		return db.checkItemValidity(id);
	}
	
	public boolean isItemAvailable(int id) {
		String check = db.checkEquipmentAvailability(id);
		if(check == null) return false;
		if(check.equals("Available")) return true;
		else return false;
	}
	
	// RECORD CREATION

	public boolean CreateReport(String equipID, String dateReported, String custodian, String issue, String action, String cost, String status) throws SPEARException {
		MaintenanceReport report = ValidateReport(equipID, dateReported, custodian, issue, action, cost, status);
		
		boolean create = db.createMaintenanceReport(report); if(!create) throw new SPEARException("Failed to publish Report to the Database. Check your connection.");
		if(!report.status().equals("Resolved")) {
		boolean update = db.updateEquipmentAvailability(2, report.equipmentID()); if(!update) throw new SPEARException("Failed to Update Equipment Status. Check your connection.");
		}
		
		return true;
	}
	
	
	private MaintenanceReport ValidateReport(String equipID, String dateReported, String custodian, String issue, String action, String cost, String status) throws SPEARException {
		int ID; boolean existCheck, isAvailable;
		Date reportDate;
		int custodianID;
		String reportedIssue;
		String actionTaken;
		double maintenanceCost;
		String currentStatus;
		
		// Validate Equipment ID
		try { ID = Integer.parseInt(equipID); } catch (NumberFormatException e) { throw new SPEARException("Equipment ID field is empty."); }
		existCheck = checkItemValidity(ID);
		if(!existCheck) throw new SPEARException("Selected Equipment does not exist."); 
		
		isAvailable = isItemAvailable(ID);
		if(!isAvailable) throw new SPEARException("Selected Equipment already is undergoing Maintenance or is currently being Borrowed.");
		
		// Validate Reported Date - Translate into sql.Date object.
		reportDate = Date.valueOf(dateReported);
		
		// Validate custodianID
		custodianID = Integer.parseInt(custodian);
		
		// Validate reportedIssue ( MUST NOT BE NULL )
		if(issue.isBlank() || issue.isEmpty()) throw new SPEARException("Report must list an Issue with the Equipment.");
		reportedIssue = issue;
		
		// Validate Action Taken
		actionTaken = action;
		
		// Validate Maintenance Cost
		try { maintenanceCost = simplifyCost(Double.parseDouble(cost)); } catch (NumberFormatException e) { throw new SPEARException("Maintenance Cost is Invalid"); }
		
		// Validate Current Status
		if(status.isBlank() || status.isEmpty()) throw new SPEARException("Maintenance Report Status was not set.");
		currentStatus = status;
		
		MaintenanceReport report = new MaintenanceReport(ID, reportDate, custodianID, reportedIssue, actionTaken, maintenanceCost, currentStatus);
		return report;
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------
	// BORROWING RECORDS C.U.D
	//---------------------------------------------------------------------------------------------------------------------------------
		
	
	public boolean CreateRecord(String borrowerID, String equipID, String dateBorrowed, String returnByDate,
							    String dateReturned, String custodian, String conditionOnBorrow, String conditionOnReturn, String status) throws SPEARException {
		BorrowRecord record = ValidateRecord(borrowerID, equipID, dateBorrowed, returnByDate, dateReturned, custodian, conditionOnBorrow, conditionOnReturn, status);
		
		boolean create = db.createBorrowRecord(record); if(!create) throw new SPEARException("Failed to publish Report to the Database. Check your connection.");
		if(!record.status().equals("Returned")) {
		boolean update = db.updateEquipmentAvailability(1, record.equipmentID()); if(!update) throw new SPEARException("Failed to Update Equipment Status. Check your connection.");
		}
		return true;	
	}
	
	public boolean UpdateRecord(String transactionID, String returnByDate, String dateReturned, String conditionOnReturn, String status, String updateRecords) throws SPEARException {
		BorrowRecord record = ValidateRecord(transactionID, returnByDate, dateReturned, conditionOnReturn, status);
		
		boolean update = db.updateBorrowRecord(record); if (!update) throw new SPEARException("Failed to publish changes to the Database. Check your connection.");
		if(Boolean.parseBoolean(updateRecords)) {
			int eID = fetchBorrowRecord(record.ID()).equipmentID();
		if(record.status().equals("Returned")) {
			boolean updAvailability = db.updateEquipmentAvailability(0, eID); if(!updAvailability) throw new SPEARException("Failed to Update Equipment Availability. Check your connection.");
			boolean updCondition = db.updateEquipmentCondition(conditionOnReturn, eID); if(!updCondition) throw new SPEARException("Failed to Update Equipment Condition. Check your connection.");
		} else {
			boolean updAvailability = db.updateEquipmentAvailability(1, eID); if(!updAvailability) throw new SPEARException("Failed to Update Equipment Availability. Check your connection.");
			
		}
		}
		return true;
	}
	
	private BorrowRecord ValidateRecord(String borrowerID, String equipID, String dateBorrowed, String returnByDate,
										String dateReturned, String custodian, String conditionOnBorrow, String conditionOnReturn, String status) throws SPEARException {
		int borrower; boolean existCheck;
		int equipmentID; boolean isAvailable;
		Date borrowDate;
		Date returnDate;
		Date dateReturn;
		int custodianID;
		String conOnBorrow;
		String conOnReturn;
		String currentStatus;
		// Validate Borrower ID
		try { borrower = Integer.parseInt(borrowerID); } catch (NumberFormatException e) { throw new SPEARException("No Borrower Selected."); }
		existCheck = db.checkBorrowerExistance(borrower);
		if(!existCheck) throw new SPEARException("Selected Borrower does not exist."); 
		
		// Validate Equipment
		try { equipmentID = Integer.parseInt(equipID); } catch (NumberFormatException e) { throw new SPEARException("Equipment ID field is empty."); }
		existCheck = checkItemValidity(equipmentID);
		if(!existCheck) throw new SPEARException("Selected Equipment does not exist."); 
		
		isAvailable = isItemAvailable(equipmentID);
		if(!isAvailable) throw new SPEARException("Selected Equipment already is undergoing Maintenance or is currently being Borrowed.");
		
		// Validate Borrow Date
		try { borrowDate = Date.valueOf(dateBorrowed); } catch (Exception e) { throw new SPEARException("Improper Date format for [Borrow Date]"); }
		
		if(returnByDate.isBlank() || returnByDate.isEmpty()) returnDate = null; else
		try { returnDate = Date.valueOf(returnByDate); } catch (Exception e) { throw new SPEARException("Improper Date format for [Return By Date]"); }
		
		if(dateReturned.isBlank() || dateReturned.isEmpty()) dateReturn = null; else
		try { dateReturn = Date.valueOf(dateReturned); } catch (Exception e) { throw new SPEARException("Improper Date format for [Date Returned]"); }
		
		// Validate custodianID
		custodianID = Integer.parseInt(custodian);
		
		// Validate Condition
		conOnBorrow = (fetchEquipmentDetails(borrower)).condition(); 
		
		// Validate Condition upon Return
		if(dateReturn != null) {
			if(conditionOnReturn.isBlank() || conditionOnReturn.isEmpty()) throw new SPEARException("Condition on Return is not defined for a Returned Equipment.");
			conOnReturn = conditionOnReturn;
		} else conOnReturn = null;
		
		
		if(status.isBlank() || status.isEmpty()) throw new SPEARException("Borrow Status was not set.");
		currentStatus = status;
		
		
		BorrowRecord record = new BorrowRecord(borrower, equipmentID, borrowDate, returnDate, dateReturn, custodianID, conOnBorrow, conOnReturn, currentStatus);
		return record;
	}
	
	
	private BorrowRecord ValidateRecord(String transactionID, String returnByDate, String dateReturned, String conditionOnReturn, String status) throws SPEARException {
		int ID; boolean existCheck;
		Date returnDate;
		Date dateReturn;
		String conOnReturn = "";
		String currentStatus;
		
		try { ID = Integer.parseInt(transactionID); } catch (NumberFormatException e) { throw new SPEARException("No Transaction Selected."); }
		existCheck = db.checkBorrowerExistance(ID);
		if(!existCheck) throw new SPEARException("Selected Transaction does not exist."); 
		
		if(status.isBlank() || status.isEmpty()) throw new SPEARException("Borrow Status was not set.");
		currentStatus = status;
		
		if(returnByDate.isBlank() || returnByDate.isEmpty()) returnDate = null; else
		try { returnDate = Date.valueOf(returnByDate); } catch (Exception e) { throw new SPEARException("Improper Date format for [Return By Date]"); }
		
		if(dateReturned.isBlank() || dateReturned.isEmpty()) dateReturn = null; else
		try { dateReturn = Date.valueOf(dateReturned); } catch (Exception e) { throw new SPEARException("Improper Date format for [Date Returned]"); }
		
		// Validate Condition upon Return
		if(dateReturn != null) {
			if(conditionOnReturn.isBlank() || conditionOnReturn.isEmpty()) throw new SPEARException("Condition on Return is not defined for a Returned Equipment.");
			
			if(currentStatus.equals("Returned"))
			conOnReturn = conditionOnReturn;
		} else conOnReturn = null;
		
		
		
		if(status.equals("Returned") && dateReturn == null) throw new SPEARException("Return Date is missing for a Returned Equipment.");
		
		
		BorrowRecord record = new BorrowRecord(ID, -1, null, returnDate, dateReturn, -1, null, conOnReturn, currentStatus);
		return record;
	}
	
	
	
	
	double simplifyCost(double val) {
		
		BigDecimal bd = BigDecimal.valueOf(val);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
}

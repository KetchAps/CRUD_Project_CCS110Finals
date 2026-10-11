package spearsystem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;

import normie.util.SPEARException;
import spearsystem.CustodianDatabaseHandler.BorrowRecord;
import spearsystem.CustodianDatabaseHandler.BorrowerList;
import spearsystem.CustodianDatabaseHandler.BorrowerProfile;
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
		
		if(value == null) throw new SPEARException("Something went wrong while trying to fetch Records Data. Check your connection and try again.");
		return value;
	}
	
	public String[][] getMaintenanceReportsList() throws SPEARException {
		String[][] value = db.getMaintenanceReportList();
		
		if(value == null) throw new SPEARException("Something went wrong while trying to fetch Report Data. Check your connection and try again.");
		return value;
	}
	
	public String[][] getRegisteredBorrowersList() throws SPEARException {
		String[][] value = db.getBorrowerDetailsList();
		
		if(value == null) throw new SPEARException("Something went wrong while trying to fetch Borrower List. Check your connection.");
		return value;
	}
	
	public String[][] getRegisteredUserPreview() throws SPEARException {
		String[][] value = db.getBorrowerPreview();
		
		if(value == null) throw new SPEARException("Something went wrong while trying to fetch Borrower Data. Check your connection and try again.");
		return value;
	}
	
	public String[][] getBorrowPreview() throws SPEARException {
		String[][] value = db.getBorrowPreview();
		
		if(value == null) throw new SPEARException("Something went wrong while trying to fetch Record Data. Check your connection and try again.");
		return value;
	}
		
	public String[][] getMaintenancePreview() throws SPEARException {
		String[][] value = db.getMaintenancePreview();
		
		if(value == null) throw new SPEARException("Something went wrong while trying to fetch Report Data. Check your connection and try again.");
		return value;
	}
	
	public String[] getInventoryFilters() throws SPEARException {
		String[] value = db.getInventoryFilters();
		
		if(value == null) throw new SPEARException("Something went wrong while trying to fetch Equipment categories. Check your connection and try again.");
		return value;
		
	}

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
	
	public MaintenanceReport fetchMaintenanceReport(int id) throws SPEARException {
		boolean validate = db.checkReportValidity(id);
		
		if(!validate) throw new SPEARException("Selected Report does not exist.");
		
		MaintenanceReport result = db.fetchMaintenanceReport(id);
		if(result == null) throw new SPEARException("Something went wrong while trying to fetch Maintenance Report. Check your connection and try again.");
		
		
		return result;
	}
	
	public BorrowerProfile fetchBorrowerProfile(int id) throws SPEARException {
		boolean validate = db.checkBorrowerExistance(id);
		
		if(!validate) throw new SPEARException("Selected Profile does not exist.");
		
		BorrowerProfile result = db.fetchBorrowerProfile(id);
		if(result == null) throw new SPEARException("Something went wrong while trying to fetch Borrower Profile. Check your connection and try again.");
		return result;
	}
	
	public BorrowerList fetchBorrowers() throws SPEARException {
		String[] name = db.getBorrowerList();
		Integer[] id = db.getBorrowerIDList();
		
		if(name == null || id == null) throw new SPEARException("Something went wrong while trying to fetch Equipment Data. Check your connection and try again.");
		return new BorrowerList(name, id);
	}
	
	
	public boolean checkItemValidity(int id) {
		boolean result = db.checkItemValidity(id);
		System.out.print("Is Item " + id + " valid: " + result);
		return result;
	}
	
	public boolean isItemAvailable(int id) {
		String check = db.checkEquipmentAvailability(id);
		if(check == null) return false;
		if(check.equals("Available")) return true;
		else return false;
	}
	
	public boolean isItemBorrowable(int id) {
		String check = db.checkEquipmentAvailability(id);
		if(check == null) return false;
		if(!check.equals("Maintenance")) return true;
		else return false;
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------
	// WRITE
	//---------------------------------------------------------------------------------------------------------------------------------
	
	public boolean deleteBorrowerProfile(int id) throws SPEARException {
		boolean check = db.checkBorrowerExistance(id);
		if(!check) throw new SPEARException("Selected Borrower does not exist.");
		
		boolean update = db.annihilateBorrower(id); if(!update) throw new SPEARException("Something went wrong while trying to delete this Borrower's profile. Check your connection.");
		
		return true;
	}
	
			
	public boolean UpdateBorrowerProfile(String[] details) throws SPEARException {
		BorrowerProfile prof = ValidateRegistration(details);
		
		boolean updateProfile = db.updateBorrowerProfile(prof); if (!updateProfile) throw new SPEARException("");
		
		return true;
	}
	
	public boolean RegisterBorrower(String[] details) throws SPEARException {
		BorrowerProfile prof = ValidateRegistration(details);
		
		boolean createProfile = db.registerNewBorrower(prof); if (!createProfile) throw new SPEARException("Something went wrong while trying to create the user Profile. Check your connection.");
		
		
		
		return true;
	}
	
	public BorrowerProfile ValidateRegistration(String[] details) throws SPEARException {
		
		for(int i = 0; i < details.length; i++) {
			System.out.print(details[i] + " ");
			if(details == null || details[i].isBlank() || details[i].isEmpty())
				throw new SPEARException("All fields must be field in.");
		}
		
		BorrowerProfile profile = new BorrowerProfile(-1, details[0], details[1], details[2], details[3], details[4], details[5], details[6]);
		return profile;
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
	
	public boolean UpdateReport(String equipID, String dateReported, String custodian, String issue, String action, String cost, String status, int maintenanceID) throws SPEARException {
		MaintenanceReport report = ValidateReport(equipID, dateReported, custodian, issue, action, cost, status);
		
		
		
		boolean create = db.updateMaintenanceReport(report, maintenanceID); if(!create) throw new SPEARException("Failed to publish Report to the Database. Check your connection.");
		
			int eID = report.equipmentID();
		if(report.status().equals("Resolved")) {
			boolean updAvailability = db.updateEquipmentAvailability(0, eID); if(!updAvailability) throw new SPEARException("Failed to Update Equipment Availability. Check your connection.");
			//boolean updCondition = db.updateEquipmentCondition(conditionOnReturn, eID); if(!updCondition) throw new SPEARException("Failed to Update Equipment Condition. Check your connection.");
		}
		
		return true;	
	}
	
	private MaintenanceReport ValidateReport(String equipID, String dateReported, String custodian, String issue, String action, String cost, String status) throws SPEARException {
		int ID; boolean existCheck, isAvailable;
		Date reportDate;
		int custodianID;
		String reportedIssue;
		String actionTaken = "";
		double maintenanceCost;
		String currentStatus;
		
		if(status.isBlank() || status.isEmpty()) throw new SPEARException("Maintenance Report Status was not set.");
		currentStatus = status;
		
		// Validate Equipment ID
		try { ID = Integer.parseInt(equipID); } catch (NumberFormatException e) { throw new SPEARException("Equipment ID field is empty."); }
		existCheck = checkItemValidity(ID);
		if(!existCheck) throw new SPEARException("Selected Equipment does not exist."); 
		
		if(actionTaken.equals("In Progress")) {
		isAvailable = isItemAvailable(ID);
		if(!isAvailable) throw new SPEARException("Selected Equipment already is undergoing Maintenance or is currently being Borrowed.");
		}
		
		// Validate Reported Date - Translate into sql.Date object.
		reportDate = Date.valueOf(dateReported);
		
		// Validate custodianID
		custodianID = Integer.parseInt(custodian);
		
		// Validate reportedIssue ( MUST NOT BE NULL )
		if(issue.isBlank() || issue.isEmpty()) throw new SPEARException("Report must list an Issue with the Equipment.");
		reportedIssue = issue;
		
		// Validate Action Taken
		if(currentStatus.equals("Resolved")) {
		if(action == null || action.isBlank() || action.isEmpty()) throw new SPEARException("[Action Taken] must be filled out to Resolve this Maintenance Report.");
		actionTaken = action;
		
		}
		// Validate Maintenance Cost
		if(cost != null)
		try { maintenanceCost = simplifyCost(Double.parseDouble(cost)); } catch (NumberFormatException e) { throw new SPEARException("Maintenance Cost is Invalid"); }
		else maintenanceCost = 0;
		// Validate Current Status
		
		
		MaintenanceReport report = new MaintenanceReport(ID, reportDate, custodianID, reportedIssue, actionTaken, maintenanceCost, currentStatus, -1);
		return report;
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------
	// BORROWING RECORDS C.U.D
	//---------------------------------------------------------------------------------------------------------------------------------
		
	
	public boolean CreateRecord(String borrowerID, String equipID, String dateBorrowed, String returnByDate,
							    String dateReturned, String custodian, String conditionOnBorrow, String conditionOnReturn, String status) throws SPEARException {
		
		BorrowRecord record = ValidateRecord(borrowerID, equipID, dateBorrowed, returnByDate, dateReturned, custodian, conditionOnBorrow, conditionOnReturn, status);
		
		
		boolean create = db.createBorrowRecord(record); if(!create) throw new SPEARException("Failed to publish Report to the Database. Check your connection.");
	
		boolean update = db.updateEquipmentAvailability(1, record.equipmentID()); if(!update) throw new SPEARException("Failed to Update Equipment Status. Check your connection.");
		if(record.status().equals("Returned")) {
		boolean condition = db.updateEquipmentCondition(record.conditionOnReturn(), record.equipmentID()); if(!condition) throw new SPEARException("Failed to Update Equipment Status. Check your connection.");
		}
		return true;	
	}
	
	public boolean UpdateRecord(String borrowerID, String equipID, String dateBorrowed, String returnByDate,
		    String dateReturned, String custodian, String conditionOnBorrow, String conditionOnReturn, String status, int transactionID) throws SPEARException {
		BorrowRecord record = ValidateRecord(borrowerID, equipID, dateBorrowed, returnByDate, dateReturned, custodian, conditionOnBorrow, conditionOnReturn, status);
		
		
		
		boolean create = db.updateBorrowRecord(record, transactionID); if(!create) throw new SPEARException("Failed to publish Report to the Database. Check your connection.");
		
			int eID = record.equipmentID();
		if(record.status().equals("Returned")) {
			boolean updAvailability = db.updateEquipmentAvailability(0, eID); if(!updAvailability) throw new SPEARException("Failed to Update Equipment Availability. Check your connection.");
			boolean updCondition = db.updateEquipmentCondition(conditionOnReturn, eID); if(!updCondition) throw new SPEARException("Failed to Update Equipment Condition. Check your connection.");
		} else {
			boolean updAvailability = db.updateEquipmentAvailability(1, eID); if(!updAvailability) throw new SPEARException("Failed to Update Equipment Availability. Check your connection.");
			
		}
		
		return true;	
	}
	
	private BorrowRecord ValidateRecord(String borrowerID, String equipID, String dateBorrowed, String returnByDate,
										String dateReturned, String custodian, String conditionOnBorrow, String conditionOnReturn, String actionTaken ) throws SPEARException {
		int borrower = -1; boolean existCheck;
		int equipmentID; boolean isAvailable;
		Date borrowDate;
		Date returnDate;
		Date dateReturn = null;
		int custodianID;
		String conOnBorrow = null;
		String conOnReturn;
		String currentStatus;
		
		if(actionTaken.isBlank() || actionTaken.isEmpty()) throw new SPEARException("No action selected.");
		currentStatus = actionTaken;
		
		// Validate Borrow Date first : Required to check if the Re
		try { borrowDate = Date.valueOf(dateBorrowed); } catch (Exception e) { throw new SPEARException("Improper Date format for [Borrow Date]"); }
				
		// Validate Borrower ID
		try { borrower = Integer.parseInt(borrowerID); } catch (NumberFormatException e) { throw new SPEARException("No Borrower Selected."); }
		existCheck = db.checkBorrowerExistance(borrower);
		if(!existCheck) throw new SPEARException("Selected Borrower does not exist."); 
		
		// Validate Equipment
		try { equipmentID = Integer.parseInt(equipID); } catch (NumberFormatException e) { throw new SPEARException("Equipment ID field is empty."); }
		existCheck = checkItemValidity(equipmentID);
		System.out.println(existCheck);
		if(!existCheck) throw new SPEARException("Selected Equipment does not exist."); 
		
		if(actionTaken.equals("Borrowed")) {
		isAvailable = isItemBorrowable(equipmentID);
		if(!isAvailable) throw new SPEARException("Selected Equipment is undergoing Maintenance or is currently being Borrowed.");
		}
		
		
		//if(returnByDate.isBlank() || returnByDate.isEmpty()) returnDate = null; else
		try { returnDate = Date.valueOf(returnByDate); } catch (Exception e) { throw new SPEARException("Improper Date format for [Return By Date]"); }
		if(returnDate.before(borrowDate)) throw new SPEARException("[Return By Date] cannot be set to a date before [Borrow Date]");
		
		if(actionTaken.equals("Returned")) {
		if(dateReturned.isBlank() || dateReturned.isEmpty()) throw new SPEARException("[Return Date] cannot be blank for a returned equipment.");
		try { dateReturn = Date.valueOf(dateReturned); } catch (Exception e) { throw new SPEARException("Improper Date format for [Date Returned]"); }
		
		if(dateReturn.before(returnDate)) throw new SPEARException("[Date Returned] cannot be set to a date before [Return By Date]");
		}
		
		// Validate custodianID
		custodianID = Integer.parseInt(custodian);
		
		// Validate Condition
		if(currentStatus.equals("Borrowed"))
		conOnBorrow = (fetchEquipmentDetails(equipmentID)).condition();
		else conOnBorrow = conditionOnBorrow;
		
		// Validate Condition upon Return
		if(dateReturn != null) {
			if(conditionOnReturn.isBlank() || conditionOnReturn.isEmpty()) throw new SPEARException("Condition on Return is not defined for a Returned Equipment.");
			conOnReturn = conditionOnReturn;
		} else conOnReturn = null;
		
		
		
		
		BorrowRecord record = new BorrowRecord(borrower, equipmentID, borrowDate, returnDate, dateReturn, custodianID, conOnBorrow, conOnReturn, currentStatus, -1);
		return record;
	}
	
	
	
	double simplifyCost(double val) {
		
		BigDecimal bd = BigDecimal.valueOf(val);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
}

package spearsystem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;

import spearsystem.CustodianDatabaseHandler.BorrowRecord;
import spearsystem.CustodianDatabaseHandler.BorrowerList;
import spearsystem.CustodianDatabaseHandler.MaintenanceReport;

public class CustodianHandler {
	
	private CustodianDatabaseHandler db = new CustodianDatabaseHandler();
	
	
	// EQUIPMENT MANAGEMENT
	
	public String[][] getEquipmentList() throws SPEARException {
		String[][] value = db.getEquipmentList();
		
		if(value == null) throw new SPEARException("Something went wrong while trying to ferch Equipment Data. Check your connection and try again.");
		return value;
	}
	
	
	
	public String[] getInventoryFilters() throws SPEARException {
		String[] value = db.getInventoryFilters();
		
		if(value == null) throw new SPEARException("Something went wrong while trying to fetch Equipment categories. Check your connection and try again.");
		return value;
		
	}
	
	
	public String[] fetchEquipmentDetails(int id) throws SPEARException {
		boolean validate = db.checkItemValidity(id);
		
		if(!validate) throw new SPEARException("Selected Equipment does not exist.");
		
		String[] result = db.fetchEquipmentDetails(id);
		if(result == null) throw new SPEARException("Something went wrong while trying to fetch Equipment Data. Check your connection and try again.");
		
		return result;
	}
	
	
	public BorrowerList fetchBorrowers() throws SPEARException {
		String[] name = db.getBorrowerList();
		Integer[] id = db.getBorrowerIDList();
		
		if(name == null || id == null) throw new SPEARException("Something went wrong while trying to fetch Equipment Data. Check your connection and try again.");
		return new BorrowerList(name, id);
	}
	
	
	public boolean checkItemValidity(int id) {
		return db.checkItemValidity(id);
		
		
	}
	
	// RECORD CREATION

	public void CreateReport(String equipID, String dateReported, String custodian, String issue, String action, String cost, String status) throws SPEARException {
		MaintenanceReport report = ValidateReport(equipID, dateReported, custodian, issue, action, cost, status);
		
		boolean create = db.createMaintenanceReport(report); if(!create) throw new SPEARException("Failed to publish Report to the Database. Check your connection.");
		boolean update = db.updateEquipmentAvailability(2, report.equipmentID()); if(!update) throw new SPEARException("Failed to Update Equipment Status. Check your connection.");
		
	}
	
	
	private MaintenanceReport ValidateReport(String equipID, String dateReported, String custodian, String issue, String action, String cost, String status) throws SPEARException {
		int ID; boolean existCheck;
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
		currentStatus = status;
		
		MaintenanceReport report = new MaintenanceReport(ID, reportDate, custodianID, reportedIssue, actionTaken, maintenanceCost, currentStatus);
		return report;
	}
	
	public void ValidateRecord(MaintenanceReport m) {}
	
	
	double simplifyCost(double val) {
		
		BigDecimal bd = BigDecimal.valueOf(val);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
}

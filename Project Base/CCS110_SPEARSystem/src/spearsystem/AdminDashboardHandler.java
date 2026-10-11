package spearsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminDashboardHandler {

	// the four numbers on the dashboard cards
	public record Summary(int totalEquipment, int currentlyBorrowed, int pendingReturns, int totalUsers) {
	}

	// GET summary
	// totalEquipment = every item in the inventory
	// currentlyBorrowed = borrow transactions that have no return date yet
	// pendingReturns = borrowed items that are due today or already overdue
	// totalUsers = login accounts (administrators + staff)
	public static Summary getSummary() throws SQLException {

		String sql = "SELECT (SELECT COUNT(*) FROM inventory), "
				+ "(SELECT COUNT(*) FROM borrow_transaction WHERE actualReturnDate IS NULL), "
				+ "(SELECT COUNT(*) FROM borrow_transaction WHERE actualReturnDate IS NULL "
				+ "AND expectedReturnDate IS NOT NULL AND expectedReturnDate <= CURDATE()), "
				+ "(SELECT COUNT(*) FROM userAccounts)";

		try (Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (!rs.next())
				return new Summary(0, 0, 0, 0);
			return new Summary(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
		}
	}

	// GET recent equipment
	public static String[][] getRecentEquipment(int limit) throws SQLException {

		String sql = "SELECT equipmentName, categoryName, availability FROM inventory "
				+ "ORDER BY date_added DESC, equipmentID DESC LIMIT ?";

		try (Connection conn = ConnectionHandler.tryConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, limit);
			try (ResultSet rs = ps.executeQuery()) {
				ArrayList<String[]> list = new ArrayList<>();
				while (rs.next())
					list.add(new String[] { rs.getString(1), rs.getString(2), rs.getString(3) });
				return list.toArray(new String[0][]);
			}
		}
	}

	// GET recent borrowings
	public static String[][] getRecentBorrowings(int limit) throws SQLException {
		String sql = "SELECT i.equipmentName, CONCAT(b.firstName, ' ', b.lastName), DATE(t.borrowDate), t.status "
				+ "FROM borrow_transaction t " + "JOIN inventory i ON i.equipmentID = t.equipmentID "
				+ "JOIN borrowers b ON b.borrowerID = t.borrowerID "
				+ "ORDER BY t.borrowDate DESC, t.transactionID DESC LIMIT ?";
		try (Connection conn = ConnectionHandler.tryConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, limit);
			try (ResultSet rs = ps.executeQuery()) {
				ArrayList<String[]> list = new ArrayList<>();
				while (rs.next())
					list.add(new String[] { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4) });
				return list.toArray(new String[0][]);
			}
		}
	}

	// GET display name
	public static String getDisplayName(int custodianID) throws SQLException {
		if (custodianID <= 0)
			return null;
		try (Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement ps = conn
						.prepareStatement("SELECT firstName FROM custodians WHERE custodianID = ? LIMIT 1")) {
			ps.setInt(1, custodianID);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? rs.getString(1) : null;
			}
		}
	}
}
package spearsystem;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.io.PushbackReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class AdminHandler {

	// returns results to the form
	public static final String SUCCESS = "success";
	public static final String REQUIRED = "required";
	public static final String DUPLICATE = "duplicate"; // same first name + last name + contact number already exists
	public static final String USERNAME_TAKEN = "usernametaken"; // the username already exists
	public static final String TOO_LONG = "toolong";
	public static final String NOT_FOUND = "notfound";
	public static final String PROTECTED = "protected"; // admin accounts / the logged in account cannot be deleted
	public static final String INVALID_DATE = "invaliddate"; // equipment: purchase date is not yyyy-mm-dd
	public static final String INVALID_COST = "invalidcost"; // equipment: purchase cost is not a valid amount
	public static final String HAS_RECORDS = "hasrecords"; // equipment: has borrow / maintenance records, cannot delete

	// column sizes custodians and userAccounts tables
	private static final int NAME_MAX = 50;
	private static final int POSITION_MAX = 100;
	private static final int DEPARTMENT_MAX = 100;
	private static final int CONTACT_MAX = 20;
	private static final int USERNAME_MAX = 100;

	// column sizes taken from the inventory table
	private static final int TAG_MAX = 30;
	private static final int EQUIP_NAME_MAX = 150;
	private static final int BRAND_MAX = 150;
	private static final int SERIAL_MAX = 100;
	private static final int DESC_MAX = 255;

	public record CustodianProfile(int custodianID, String firstName, String lastName, String position,
			String department, String contactNumber, String username) {
	}

	// registers a new custodian with a user account, they are both saved in one
	// transaction so the custodian always has an account
	public static String registerCustodian(String firstName, String lastName, String position, String department,
			String contactNumber, String username, Reader password) throws SQLException {

		// cleans the data
		firstName = clean(firstName);
		lastName = clean(lastName);
		position = clean(position);
		department = clean(department);
		contactNumber = clean(contactNumber);
		username = clean(username);

		// checks for empty text fields
		if (firstName.isEmpty() || lastName.isEmpty() || position.isEmpty() || department.isEmpty()
				|| contactNumber.isEmpty() || username.isEmpty() || password == null) {
			return REQUIRED;
		}
		if (tooLong(firstName, lastName, position, department, contactNumber) || username.length() > USERNAME_MAX) {
			return TOO_LONG;
		}

		// checks for an empty password (puts the first character back so it can still
		// be saved)
		PushbackReader pass = new PushbackReader(password);
		try {
			int first = pass.read();
			if (first == -1)
				return REQUIRED;
			pass.unread(first);
		} catch (IOException e) {
			throw new SQLException("Could not read the password.", e);
		}

		try (Connection conn = ConnectionHandler.tryConnect()) {

			boolean oldAutoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);

			try {

				// duplication checks
				// returns DUPLICATE and USERNAME_TAKEN
				if (isDuplicate(conn, firstName, lastName, contactNumber, 0))
					return DUPLICATE;

				try (PreparedStatement ps = conn
						.prepareStatement("SELECT userID FROM userAccounts WHERE username = ? LIMIT 1")) {
					ps.setString(1, username);
					try (ResultSet rs = ps.executeQuery()) {
						if (rs.next())
							return USERNAME_TAKEN;
					}
				}

				// insert the custodian and keep the generated ID
				int custodianID;
				String insertCustodian = "INSERT INTO custodians (firstName, lastName, position, department, contactNumber) "
						+ "VALUES (?, ?, ?, ?, ?)";
				try (PreparedStatement ps = conn.prepareStatement(insertCustodian, Statement.RETURN_GENERATED_KEYS)) {
					ps.setString(1, firstName);
					ps.setString(2, lastName);
					ps.setString(3, position);
					ps.setString(4, department);
					ps.setString(5, contactNumber);
					ps.executeUpdate();

					try (ResultSet keys = ps.getGeneratedKeys()) {
						if (!keys.next())
							throw new SQLException("Could not get the new Custodian ID.");
						custodianID = keys.getInt(1);
					}
				}
				// the account must point to the custodian (userID) and the password must be
				// hashed
				// with password(?) because that is how LoginHandler checks it.
				String insertUser = "INSERT INTO userAccounts (userID, username, password, isAdmin) VALUES (?, ?, password(?), false)";
				try (PreparedStatement ps = conn.prepareStatement(insertUser)) {
					ps.setInt(1, custodianID);
					ps.setString(2, username);
					ps.setCharacterStream(3, pass);
					ps.executeUpdate();
				}

				conn.commit();
				return SUCCESS;

			} catch (SQLException e) {
				conn.rollback();
				throw e;
			} finally {
				conn.setAutoCommit(oldAutoCommit);
			}
		}
	}

	// GET custodian
	public static String[][] getCustodianList() throws SQLException {
		String sql = "SELECT c.custodianID, c.firstName, c.lastName, c.position, c.department, c.contactNumber, u.username "
				+ "FROM custodians c LEFT JOIN userAccounts u ON u.userID = c.custodianID " + "ORDER BY c.custodianID";
		try (Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			ArrayList<String[]> list = new ArrayList<>();
			while (rs.next()) {
				String[] row = new String[7];
				for (int i = 1; i <= 7; i++)
					row[i - 1] = rs.getString(i);
				list.add(row);
			}
			return list.toArray(new String[0][]);
		}
	}

	// FETCH custodian

	// returns the custodian, or null if the ID does not exist
	public static CustodianProfile fetchCustodian(int id) throws SQLException {
		String sql = "SELECT c.custodianID, c.firstName, c.lastName, c.position, c.department, c.contactNumber, u.username "
				+ "FROM custodians c LEFT JOIN userAccounts u ON u.userID = c.custodianID "
				+ "WHERE c.custodianID = ? LIMIT 1";
		try (Connection conn = ConnectionHandler.tryConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next())
					return null;
				return new CustodianProfile(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7));
			}
		}
	}

	// UPDATE custodian
	public static String updateCustodian(int custodianID, String firstName, String lastName, String position,
			String department, String contactNumber) throws SQLException {

		firstName = clean(firstName);
		lastName = clean(lastName);
		position = clean(position);
		department = clean(department);
		contactNumber = clean(contactNumber);

		if (firstName.isEmpty() || lastName.isEmpty() || position.isEmpty() || department.isEmpty()
				|| contactNumber.isEmpty()) {
			return REQUIRED;
		}
		if (tooLong(firstName, lastName, position, department, contactNumber))
			return TOO_LONG;

		try (Connection conn = ConnectionHandler.tryConnect()) {

			if (!exists(conn, custodianID))
				return NOT_FOUND;
			if (isDuplicate(conn, firstName, lastName, contactNumber, custodianID))
				return DUPLICATE;

			String update = "UPDATE custodians SET firstName = ?, lastName = ?, position = ?, department = ?, contactNumber = ? "
					+ "WHERE custodianID = ?";
			try (PreparedStatement ps = conn.prepareStatement(update)) {
				ps.setString(1, firstName);
				ps.setString(2, lastName);
				ps.setString(3, position);
				ps.setString(4, department);
				ps.setString(5, contactNumber);
				ps.setInt(6, custodianID);
				ps.executeUpdate();
			}
		}
		return SUCCESS;
	}

	// DELETE custodian
	public static String deleteCustodian(int custodianID, int sessionID) throws SQLException {
		if (custodianID == sessionID)
			return PROTECTED;
		try (Connection conn = ConnectionHandler.tryConnect()) {

			boolean oldAutoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			try {
				if (!exists(conn, custodianID))
					return NOT_FOUND;

				try (PreparedStatement ps = conn
						.prepareStatement("SELECT 1 FROM userAccounts WHERE userID = ? AND isAdmin = true LIMIT 1")) {
					ps.setInt(1, custodianID);
					try (ResultSet rs = ps.executeQuery()) {
						if (rs.next())
							return PROTECTED;
					}
				}
				try (PreparedStatement ps = conn.prepareStatement("DELETE FROM userAccounts WHERE userID = ?")) {
					ps.setInt(1, custodianID);
					ps.executeUpdate();
				}
				try (PreparedStatement ps = conn.prepareStatement("DELETE FROM custodians WHERE custodianID = ?")) {
					ps.setInt(1, custodianID);
					ps.executeUpdate();
				}
				conn.commit();
				return SUCCESS;

			} catch (SQLException e) {
				conn.rollback();
				throw e;
			} finally {
				conn.setAutoCommit(oldAutoCommit);
			}
		}
	}

	// HELPER METHODS
	private static String clean(String s) {
		return s == null ? "" : s.trim();
	}

	private static boolean tooLong(String first, String last, String position, String department, String contact) {
		return first.length() > NAME_MAX || last.length() > NAME_MAX || position.length() > POSITION_MAX
				|| department.length() > DEPARTMENT_MAX || contact.length() > CONTACT_MAX;
	}

	private static boolean exists(Connection conn, int custodianID) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM custodians WHERE custodianID = ? LIMIT 1")) {
			ps.setInt(1, custodianID);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	private static boolean isDuplicate(Connection conn, String first, String last, String contact, int excludeID)
			throws SQLException {
		String check = "SELECT custodianID FROM custodians "
				+ "WHERE firstName = ? AND lastName = ? AND contactNumber = ? AND custodianID <> ? LIMIT 1";
		try (PreparedStatement ps = conn.prepareStatement(check)) {
			ps.setString(1, first);
			ps.setString(2, last);
			ps.setString(3, contact);
			ps.setInt(4, excludeID);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	// ===================================================================
	// EQUIPMENT / INVENTORY
	// (SUCCESS, REQUIRED, DUPLICATE, TOO_LONG and NOT_FOUND are shared with the
	// custodian methods above. DUPLICATE here means the property tag is taken)
	// ===================================================================
	// a room for the Room / Location combo box (toString is what the combo shows)
	public record RoomOption(int roomID, String label) {
		@Override
		public String toString() {
			return label;
		}
	}

	public static final RoomOption NO_ROOM = new RoomOption(0, "(No room)");

	public record Equipment(int equipmentID, String propertyTag, String equipmentName, String categoryName,
			String brandModel, String serialNumber, String description, int roomID, String roomName,
			String purchaseDate, String purchaseCost, String condition, String availability) {
	}

	// LISTS FOR THE COMBO BOXES
	public static String[] getCategories() throws SQLException {
		try (Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement ps = conn
						.prepareStatement("SELECT categoryName FROM equipment_categories ORDER BY categoryName");
				ResultSet rs = ps.executeQuery()) {
			ArrayList<String> list = new ArrayList<>();
			while (rs.next())
				list.add(rs.getString(1));
			return list.toArray(new String[0]);
		}
	}

	public static RoomOption[] getRooms() throws SQLException {
		try (Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement ps = conn.prepareStatement(
						"SELECT roomID, roomName, building FROM rooms_offices ORDER BY building, roomName");
				ResultSet rs = ps.executeQuery()) {
			ArrayList<RoomOption> list = new ArrayList<>();
			while (rs.next())
				list.add(new RoomOption(rs.getInt(1), rs.getString(2) + " (" + rs.getString(3) + ")"));
			return list.toArray(new RoomOption[0]);
		}
	}

	// GET equipment
	public static String[][] getEquipmentList(String search, String category, String condition, String availability)
			throws SQLException {
		StringBuilder sql = new StringBuilder(
				"SELECT i.equipmentID, i.propertyTag, i.equipmentName, i.categoryName, i.brandModel, i.serialNumber, "
						+ "r.roomName, i.purchaseDate, i.purchaseCost, i.condition_status, i.availability "
						+ "FROM inventory i LEFT JOIN rooms_offices r ON r.roomID = i.roomID WHERE 1 = 1");
		ArrayList<String> params = new ArrayList<>();

		search = clean(search);
		if (!search.isEmpty()) {
			sql.append(" AND (i.propertyTag LIKE ? OR i.equipmentName LIKE ? OR i.brandModel LIKE ? "
					+ "OR i.serialNumber LIKE ?)");
			for (int i = 0; i < 4; i++)
				params.add("%" + search + "%");
		}
		if (!clean(category).isEmpty()) {
			sql.append(" AND i.categoryName = ?");
			params.add(clean(category));
		}
		if (!clean(condition).isEmpty()) {
			sql.append(" AND i.condition_status = ?");
			params.add(clean(condition));
		}
		if (!clean(availability).isEmpty()) {
			sql.append(" AND i.availability = ?");
			params.add(clean(availability));
		}
		sql.append(" ORDER BY i.equipmentID");

		try (Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement ps = conn.prepareStatement(sql.toString())) {

			for (int i = 0; i < params.size(); i++)
				ps.setString(i + 1, params.get(i));

			try (ResultSet rs = ps.executeQuery()) {
				ArrayList<String[]> list = new ArrayList<>();
				while (rs.next()) {
					String[] row = new String[11];
					for (int i = 1; i <= 11; i++)
						row[i - 1] = rs.getString(i);
					list.add(row);
				}
				return list.toArray(new String[0][]);
			}
		}
	}

	// returns { total, available, under maintenance, borrowed }
	public static int[] getCounts() throws SQLException {
		String sql = "SELECT COUNT(*), " + "COALESCE(SUM(availability = 'Available'), 0), "
				+ "COALESCE(SUM(availability = 'Maintenance'), 0), "
				+ "COALESCE(SUM(availability IN ('Borrowed', 'In Use')), 0) FROM inventory";

		try (Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			int[] counts = new int[4];
			if (rs.next())
				for (int i = 0; i < 4; i++)
					counts[i] = rs.getInt(i + 1);
			return counts;
		}
	}

	// FETCH equipment
	public static Equipment fetchEquipment(int id) throws SQLException {
		String sql = "SELECT i.equipmentID, i.propertyTag, i.equipmentName, i.categoryName, i.brandModel, "
				+ "i.serialNumber, i.description, i.roomID, r.roomName, i.purchaseDate, i.purchaseCost, "
				+ "i.condition_status, i.availability "
				+ "FROM inventory i LEFT JOIN rooms_offices r ON r.roomID = i.roomID "
				+ "WHERE i.equipmentID = ? LIMIT 1";

		try (Connection conn = ConnectionHandler.tryConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next())
					return null;
				return new Equipment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7), rs.getInt(8), rs.getString(9), rs.getString(10),
						rs.getString(11), rs.getString(12), rs.getString(13));
			}
		}
	}

	// ADD equipment
	public static String addEquipment(String tag, String name, String category, String brand, String serial,
			String description, int roomID, String purchaseDate, String purchaseCost, String condition,
			String availability) throws SQLException {

		tag = clean(tag);
		name = clean(name);
		category = clean(category);
		brand = clean(brand);
		serial = clean(serial);
		description = clean(description);
		purchaseDate = clean(purchaseDate);
		purchaseCost = clean(purchaseCost);
		condition = clean(condition);
		availability = clean(availability);

		String problem = checkEquipment(tag, name, category, brand, serial, description, purchaseDate, purchaseCost);
		if (problem != null)
			return problem;
		if (condition.isEmpty())
			condition = "Good";
		if (availability.isEmpty())
			availability = "Available";

		try (Connection conn = ConnectionHandler.tryConnect()) {

			if (isTagTaken(conn, tag, 0))
				return DUPLICATE;

			String insert = "INSERT INTO inventory (propertyTag, equipmentName, categoryName, brandModel, serialNumber, "
					+ "description, roomID, purchaseDate, purchaseCost, condition_status, availability) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement ps = conn.prepareStatement(insert)) {
				bindEquipment(ps, tag, name, category, brand, serial, description, roomID, purchaseDate, purchaseCost,
						condition, availability);
				ps.executeUpdate();
			}
		}
		return SUCCESS;
	}

	// UPDATE equipment
	public static String updateEquipment(int equipmentID, String tag, String name, String category, String brand,
			String serial, String description, int roomID, String purchaseDate, String purchaseCost, String condition,
			String availability) throws SQLException {

		tag = clean(tag);
		name = clean(name);
		category = clean(category);
		brand = clean(brand);
		serial = clean(serial);
		description = clean(description);
		purchaseDate = clean(purchaseDate);
		purchaseCost = clean(purchaseCost);
		condition = clean(condition);
		availability = clean(availability);

		String problem = checkEquipment(tag, name, category, brand, serial, description, purchaseDate, purchaseCost);
		if (problem != null)
			return problem;
		if (condition.isEmpty())
			condition = "Good";
		if (availability.isEmpty())
			availability = "Available";

		try (Connection conn = ConnectionHandler.tryConnect()) {

			if (!equipmentExists(conn, equipmentID))
				return NOT_FOUND;
			if (isTagTaken(conn, tag, equipmentID))
				return DUPLICATE;

			String update = "UPDATE inventory SET propertyTag = ?, equipmentName = ?, categoryName = ?, "
					+ "brandModel = ?, serialNumber = ?, description = ?, roomID = ?, purchaseDate = ?, "
					+ "purchaseCost = ?, condition_status = ?, availability = ? WHERE equipmentID = ?";
			try (PreparedStatement ps = conn.prepareStatement(update)) {
				bindEquipment(ps, tag, name, category, brand, serial, description, roomID, purchaseDate, purchaseCost,
						condition, availability);
				ps.setInt(12, equipmentID);
				ps.executeUpdate();
			}
		}
		return SUCCESS;
	}

	// DELETE equipment
	public static String deleteEquipment(int equipmentID) throws SQLException {
		try (Connection conn = ConnectionHandler.tryConnect();
				PreparedStatement ps = conn.prepareStatement("DELETE FROM inventory WHERE equipmentID = ?")) {
			ps.setInt(1, equipmentID);
			return ps.executeUpdate() == 0 ? NOT_FOUND : SUCCESS;
		} catch (SQLIntegrityConstraintViolationException e) {
			return HAS_RECORDS;
		}
	}

	// EQUIPMENT HELPER METHODS
	private static String checkEquipment(String tag, String name, String category, String brand, String serial,
			String description, String date, String cost) {
		if (tag.isEmpty() || name.isEmpty() || category.isEmpty())
			return REQUIRED;
		if (tag.length() > TAG_MAX || name.length() > EQUIP_NAME_MAX || brand.length() > BRAND_MAX
				|| serial.length() > SERIAL_MAX || description.length() > DESC_MAX)
			return TOO_LONG;
		if (!date.isEmpty() && parseDate(date) == null)
			return INVALID_DATE;
		if (!cost.isEmpty() && parseCost(cost) == null)
			return INVALID_COST;
		return null;
	}

	private static Date parseDate(String s) {
		try {
			return Date.valueOf(LocalDate.parse(s));
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	private static BigDecimal parseCost(String s) {
		try {
			BigDecimal c = new BigDecimal(s.replace(",", "")).setScale(2, RoundingMode.HALF_UP);
			if (c.signum() < 0 || c.precision() > 12)
				return null;
			return c;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private static void bindEquipment(PreparedStatement ps, String tag, String name, String category, String brand,
			String serial, String description, int roomID, String date, String cost, String condition,
			String availability) throws SQLException {
		ps.setString(1, tag);
		ps.setString(2, name);
		ps.setString(3, category);
		ps.setString(4, brand.isEmpty() ? null : brand);
		ps.setString(5, serial.isEmpty() ? null : serial);
		ps.setString(6, description);

		if (roomID > 0)
			ps.setInt(7, roomID);
		else
			ps.setNull(7, Types.INTEGER);

		Date d = date.isEmpty() ? null : parseDate(date);
		if (d != null)
			ps.setDate(8, d);
		else
			ps.setNull(8, Types.DATE);

		BigDecimal c = cost.isEmpty() ? null : parseCost(cost);
		if (c != null)
			ps.setBigDecimal(9, c);
		else
			ps.setNull(9, Types.DECIMAL);

		ps.setString(10, condition);
		ps.setString(11, availability);
	}

	private static boolean equipmentExists(Connection conn, int equipmentID) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM inventory WHERE equipmentID = ? LIMIT 1")) {
			ps.setInt(1, equipmentID);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	private static boolean isTagTaken(Connection conn, String tag, int excludeID) throws SQLException {
		try (PreparedStatement ps = conn.prepareStatement(
				"SELECT equipmentID FROM inventory WHERE propertyTag = ? AND equipmentID <> ? LIMIT 1")) {
			ps.setString(1, tag);
			ps.setInt(2, excludeID);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}
}
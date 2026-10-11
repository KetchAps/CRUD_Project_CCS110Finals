package spearsystem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class AdminInventoryHandler {

	// results returned to the form
	public static final String SUCCESS = "success";
	public static final String REQUIRED = "required"; // property tag, name or category is empty
	public static final String DUPLICATE = "duplicate"; // the property tag already exists
	public static final String TOO_LONG = "toolong";
	public static final String NOT_FOUND = "notfound";
	public static final String INVALID_DATE = "invaliddate"; // purchase date is not yyyy-mm-dd
	public static final String INVALID_COST = "invalidcost"; // purchase cost is not a valid amount
	public static final String HAS_RECORDS = "hasrecords"; // item has borrow / maintenance records, cannot delete

	// column sizes taken from the inventory table
	private static final int TAG_MAX = 30;
	private static final int NAME_MAX = 150;
	private static final int BRAND_MAX = 150;
	private static final int SERIAL_MAX = 100;
	private static final int DESC_MAX = 255;

	// a room for the Room / Location combo box (toString is what the combo shows)
	public record RoomOption(int roomID, String label) {
		@Override
		public String toString() {
			return label;
		}
	}

	// roomID 0 means "no room" (it is saved as NULL)
	public static final RoomOption NO_ROOM = new RoomOption(0, "(No room)");

	public record Equipment(int equipmentID, String propertyTag, String equipmentName, String categoryName,
			String brandModel, String serialNumber, String description, int roomID, String roomName,
			String purchaseDate, String purchaseCost, String condition, String availability) {
	}

	// --------------------------------------------------------
	// LISTS FOR THE COMBO BOXES
	// --------------------------------------------------------
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
	// --------------------------------------------------------
	// GET equipment
	// --------------------------------------------------------
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

	// --------------------------------------------------------
	// FETCH equipment
	// --------------------------------------------------------
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

	// --------------------------------------------------------
	// ADD equipment
	// --------------------------------------------------------
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

		String problem = check(tag, name, category, brand, serial, description, purchaseDate, purchaseCost);
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
				bind(ps, tag, name, category, brand, serial, description, roomID, purchaseDate, purchaseCost, condition,
						availability);
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

		String problem = check(tag, name, category, brand, serial, description, purchaseDate, purchaseCost);
		if (problem != null)
			return problem;
		if (condition.isEmpty())
			condition = "Good";
		if (availability.isEmpty())
			availability = "Available";

		try (Connection conn = ConnectionHandler.tryConnect()) {

			if (!exists(conn, equipmentID))
				return NOT_FOUND;
			if (isTagTaken(conn, tag, equipmentID))
				return DUPLICATE;

			String update = "UPDATE inventory SET propertyTag = ?, equipmentName = ?, categoryName = ?, "
					+ "brandModel = ?, serialNumber = ?, description = ?, roomID = ?, purchaseDate = ?, "
					+ "purchaseCost = ?, condition_status = ?, availability = ? WHERE equipmentID = ?";
			try (PreparedStatement ps = conn.prepareStatement(update)) {
				bind(ps, tag, name, category, brand, serial, description, roomID, purchaseDate, purchaseCost, condition,
						availability);
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

	// HELPER METHODS
	private static String clean(String s) {
		return s == null ? "" : s.trim();
	}

	// returns an error code, or null when everything is fine
	private static String check(String tag, String name, String category, String brand, String serial,
			String description, String date, String cost) {
		if (tag.isEmpty() || name.isEmpty() || category.isEmpty())
			return REQUIRED;
		if (tag.length() > TAG_MAX || name.length() > NAME_MAX || brand.length() > BRAND_MAX
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

	// null if it is not a valid amount for DECIMAL(12, 2)
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

	private static void bind(PreparedStatement ps, String tag, String name, String category, String brand,
			String serial, String description, int roomID, String date, String cost, String condition,
			String availability) throws SQLException {
		ps.setString(1, tag);
		ps.setString(2, name);
		ps.setString(3, category);
		ps.setString(4, brand.isEmpty() ? null : brand);
		ps.setString(5, serial.isEmpty() ? null : serial);
		ps.setString(6, description); // the column is NOT NULL, so an empty text is saved as ''

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

	private static boolean exists(Connection conn, int equipmentID) throws SQLException {
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
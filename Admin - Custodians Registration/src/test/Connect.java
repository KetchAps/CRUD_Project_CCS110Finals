package test;

import java.sql.*;

public class Connect {
	
	// returns results to the form
    public static String SUCCESS = "success";
    public static String REQUIRED = "required";
    public static String DUPLICATE = "duplicate";
    
    // establishes a connection to mysql
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/admin", "root", "");
    }
    
    public static String register(String firstName, String lastName, String position,
                                  String department, String contactNumber, int roomID) throws SQLException {
        // cleans the data
        firstName = firstName.trim();
        lastName = lastName.trim();
        position = position.trim();
        department = department.trim();
        contactNumber = contactNumber.trim();
        
        // checks for empty text fields
        if (firstName.isEmpty() || lastName.isEmpty() || position.isEmpty()
                || department.isEmpty() || contactNumber.isEmpty() || roomID <= 0) {
            return REQUIRED;
        }
        
        try (Connection conn = getConnection()) {
            // duplication check
            String check = "SELECT custodianID FROM tblcustodians "
                         + "WHERE firstName = ? AND lastName = ? AND contactNumber = ? LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(check)) {
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, contactNumber);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return DUPLICATE;
                }
            }
            
            // 4. insert, roomID must exist in rooms_offices (foreign key)
            String insert = "INSERT INTO tblcustodians (firstName, lastName, position, department, contactNumber, roomID) "
                          + "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, position);
                ps.setString(4, department);
                ps.setString(5, contactNumber);
                ps.setInt(6, roomID);
                ps.executeUpdate();
            }
        }
        return SUCCESS;
}
}
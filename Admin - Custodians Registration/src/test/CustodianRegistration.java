package test;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;	
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class CustodianRegistration extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtFieldFirstName;
	private JTextField txtFieldLastName;
	private JTextField txtFieldPosition;
	private JTextField txtFieldDepartment;
	private JTextField txtFieldContactNumber;
	private JComboBox<Room> cbxRoom;
	private JTable tblRegisteredCustodians;
	private DefaultTableModel tableModel;
	
	// dropdown item, the roomID is saved and the label is shown
	static class Room {
		final int id; 
		final String label;	
		Room(int id, String label) { this.id = id; this.label = label; }
		@Override public String toString() { return label; }
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CustodianRegistration frame = new CustodianRegistration();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CustodianRegistration() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblFirstName = new JLabel("First Name :");
		lblFirstName.setFont(new Font("Serif", Font.PLAIN, 13));
		lblFirstName.setBounds(10, 30, 67, 18);
		contentPane.add(lblFirstName);
		
		JLabel lblLastName = new JLabel("Last Name  :");
		lblLastName.setFont(new Font("Serif", Font.PLAIN, 13));
		lblLastName.setBounds(200, 30, 76, 18);
		contentPane.add(lblLastName);
		
		JLabel lblPosition = new JLabel("Position      :");
		lblPosition.setFont(new Font("Serif", Font.PLAIN, 13));
		lblPosition.setBounds(10, 68, 67, 18);
		contentPane.add(lblPosition);
		
		JLabel lblDepartment = new JLabel("Department :");
		lblDepartment.setFont(new Font("Serif", Font.PLAIN, 13));
		lblDepartment.setBounds(200, 68, 76, 18);
		contentPane.add(lblDepartment);
		
		JLabel lblContactNumber = new JLabel("Contact No. ");
		lblContactNumber.setFont(new Font("Serif", Font.PLAIN, 13));
		lblContactNumber.setBounds(10, 106, 67, 18);
		contentPane.add(lblContactNumber);
		
		cbxRoom = new JComboBox<Room>();
		cbxRoom.setBounds(244, 106, 129, 20);
		contentPane.add(cbxRoom);
		
		JLabel lblRoom = new JLabel("Room :");
		lblRoom.setFont(new Font("Serif", Font.PLAIN, 13));
		lblRoom.setBounds(200, 106, 46, 18);
		contentPane.add(lblRoom);
		
		txtFieldFirstName = new JTextField();
		txtFieldFirstName.setBounds(77, 30, 96, 18);
		contentPane.add(txtFieldFirstName);
		txtFieldFirstName.setColumns(10);
		
		txtFieldLastName = new JTextField();
		txtFieldLastName.setBounds(277, 31, 96, 18);
		contentPane.add(txtFieldLastName);
		txtFieldLastName.setColumns(10);
		
		txtFieldPosition = new JTextField();
		txtFieldPosition.setBounds(77, 69, 96, 18);
		contentPane.add(txtFieldPosition);
		txtFieldPosition.setColumns(10);
		
		txtFieldDepartment = new JTextField();
		txtFieldDepartment.setBounds(277, 69, 96, 18);
		contentPane.add(txtFieldDepartment);
		txtFieldDepartment.setColumns(10);
		
		txtFieldContactNumber = new JTextField();
		txtFieldContactNumber.setBounds(77, 107, 96, 18);
		contentPane.add(txtFieldContactNumber);
		txtFieldContactNumber.setColumns(10);
		
		JButton btnRegisterSubmit = new JButton("Submit");
		btnRegisterSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registerCustodian();
			}
		});
		btnRegisterSubmit.setBounds(77, 155, 96, 20);
		contentPane.add(btnRegisterSubmit);
		
		JButton btnRegisterClear = new JButton("Clear");
		btnRegisterClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearForm();
			}
		});
		btnRegisterClear.setBounds(200, 155, 96, 20);
		contentPane.add(btnRegisterClear);
		
		JButton btnDeleteCustodian = new JButton("Delete");
		btnDeleteCustodian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteCustodian();
			}
		});
		btnDeleteCustodian.setBounds(383, 353, 84, 20);
		contentPane.add(btnDeleteCustodian);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 205, 363, 168);
		contentPane.add(scrollPane);
		
		// rows are loaded from the database
		tableModel = new DefaultTableModel(
		    new Object[][] {},
		    new String[] {
		        "ID", "First Name", "Last Name", "Position", "Department", "Contact Number", "Room ID"
		    }
		) {
		    private static final long serialVersionUID = 1L;
		    @Override public boolean isCellEditable(int row, int column) { return false; }
		};
		tblRegisteredCustodians = new JTable();
		tblRegisteredCustodians.setModel(tableModel);
		scrollPane.setViewportView(tblRegisteredCustodians);
		tblRegisteredCustodians.setFont(new Font("Serif", Font.PLAIN, 11));
		// load data when the window opens
		loadRooms();
		loadCustodians();
	}
	
	// fill the room dropdown item from rooms_offices (column 1 = roomID, column 2 = a readable name)
	private void loadRooms() {
		cbxRoom.removeAllItems();
		try (Connection conn = Connect.getConnection();
		     Statement st = conn.createStatement();
		     ResultSet rs = st.executeQuery("SELECT * FROM rooms_offices")) {
			while (rs.next()) {
				cbxRoom.addItem(new Room(rs.getInt(1), rs.getString(2)));
			}
		} catch (SQLException ex) {
			showError(ex);
		}
	}
	
	// loads the table with all custodians sorted by custodianID (lowest to highest)
	private void loadCustodians() {
		tableModel.setRowCount(0);
		String sql = "SELECT custodianID, firstName, lastName, position, department, contactNumber, roomID "
		           + "FROM tblcustodians ORDER BY custodianID";
		try (Connection conn = Connect.getConnection();
		     Statement st = conn.createStatement();
		     ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				tableModel.addRow(new Object[] {
					rs.getInt("custodianID"), rs.getString("firstName"), rs.getString("lastName"),
					rs.getString("position"), rs.getString("department"),
					rs.getString("contactNumber"), rs.getInt("roomID")
				});
			}
		} catch (SQLException ex) {
			showError(ex);
		}
	}
	
	// submit button, send the form data to Connect.register
	private void registerCustodian() {
		Room room = (Room) cbxRoom.getSelectedItem();
		int roomID = (room == null) ? 0 : room.id;
		try {
			String result = Connect.register(
				txtFieldFirstName.getText(), txtFieldLastName.getText(), txtFieldPosition.getText(),
				txtFieldDepartment.getText(), txtFieldContactNumber.getText(), roomID);

			if (result.equals(Connect.REQUIRED)) {
				JOptionPane.showMessageDialog(this, "All fields are required.", "Missing data", JOptionPane.WARNING_MESSAGE);
			} else if (result.equals(Connect.DUPLICATE)) {
				JOptionPane.showMessageDialog(this, "Already registered.", "Duplicate", JOptionPane.WARNING_MESSAGE);
			} else {
				clearForm();
				loadCustodians();
				JOptionPane.showMessageDialog(this, "Custodian registered successfully.");
			}
		} catch (SQLException ex) {
			showError(ex);
		}
	}
	
	// clear button, empty the text fields
	private void clearForm() {
		txtFieldFirstName.setText("");
		txtFieldLastName.setText("");
		txtFieldPosition.setText("");
		txtFieldDepartment.setText("");
		txtFieldContactNumber.setText("");
		if (cbxRoom.getItemCount() > 0) cbxRoom.setSelectedIndex(0);
	}
	
	// delete button, deletes the selected table row after clicking yes in the confirmation pop up
	private void deleteCustodian() {
		int row = tblRegisteredCustodians.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Select a custodian first.");
			return;
		}
		int id = (int) tableModel.getValueAt(row, 0);
		int confirm = JOptionPane.showConfirmDialog(this, "Delete this custodian?", "Confirm", JOptionPane.YES_NO_OPTION);
		if (confirm != JOptionPane.YES_OPTION) return;
		
		try (Connection conn = Connect.getConnection();
		     PreparedStatement ps = conn.prepareStatement("DELETE FROM tblcustodians WHERE custodianID = ?")) {
			ps.setInt(1, id);
			ps.executeUpdate();
			loadCustodians();
		} catch (SQLException ex) {
			showError(ex);
		}
	}
	
	private void showError(SQLException ex) {
		JOptionPane.showMessageDialog(this, ex.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
	}
}
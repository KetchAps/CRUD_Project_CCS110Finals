package spearsystem;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.CharArrayReader;
import java.io.Reader;
import java.time.LocalTime;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.RowFilter.Entry;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JComboBox;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Client {

	private JFrame frame;
	private DatabaseHandler backend;
	private JTextField authUsername;
	private JPasswordField authPassword;

	
	private CardLayout card = new CardLayout(0,0);
	private JPanel userArea;
	private JTextArea areaLog;
	private JLabel lblLoginState;
	
	private String sessionUser;
	
	private JMenuItem menuOptLogout;
	private JTable searchResult;
	private DefaultTableModel query;
	
	
	private JComboBox<String> cmbCondition;
	private JComboBox<String> cmbCategory;
	private JComboBox<String> cmbAvailability;

	private DefaultComboBoxModel<String> cmbCategoryModel;
	
	private TableRowSorter<DefaultTableModel> inventorySorter = new TableRowSorter<>(query);
	
	private RowFilter<DefaultTableModel, Integer> inventoryRF = new RowFilter<>() {
		@Override
		@SuppressWarnings("rawtypes")
		public boolean include( Entry entry) {
			String categoryFilter = cmbCategory.getSelectedItem().toString();
			String conditionFilter = cmbCondition.getSelectedItem().toString();
			String availabilityFilter = cmbAvailability.getSelectedItem().toString();
			
			if(!categoryFilter.equals("[Category]") && !entry.getStringValue(3).equals(categoryFilter)) return false;
			
			if(!conditionFilter.equals("[Condition]") && !entry.getStringValue(11).equals(conditionFilter)) return false;
			
			if(!availabilityFilter.equals("[Availability]") && !entry.getStringValue(12).equals(availabilityFilter)) return false;
			return true;
		}
		
		
	};
	private JTextField textField;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Client() {
		initialize();
		startupProcess();
	}

	
	private void initialize() {
		
		backend = new DatabaseHandler();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1450, 920);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel mainPane = new JPanel();
		mainPane.setBounds(10, 21, 1414, 849);
		frame.getContentPane().add(mainPane);
		mainPane.setLayout(null);
		
		userArea = new JPanel();
		userArea.setBounds(10, 11, 1394, 601);
		mainPane.add(userArea);
		userArea.setLayout(card);
		
		JPanel loginScreen = new JPanel();
		userArea.add(loginScreen, "login");
		loginScreen.setLayout(null);
		
		JLabel labelLogin = new JLabel("S.P.E.A.R System");
		labelLogin.setFont(new Font("Trebuchet MS", Font.PLAIN, 34));
		labelLogin.setHorizontalAlignment(SwingConstants.CENTER);
		labelLogin.setBounds(500, 11, 302, 117);
		loginScreen.add(labelLogin);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setBounds(510, 139, 85, 14);
		loginScreen.add(lblUsername);
		
		authUsername = new JTextField();
		authUsername.setBounds(605, 136, 197, 20);
		loginScreen.add(authUsername);
		authUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(510, 167, 85, 14);
		loginScreen.add(lblPassword);
		
		authPassword = new JPasswordField();
		authPassword.setBounds(605, 164, 197, 20);
		loginScreen.add(authPassword);
		
		JButton btnAuthenticate = new JButton("Log In");
		btnAuthenticate.addActionListener(e -> { tryAuthenticate(); });
		btnAuthenticate.setBounds(569, 225, 135, 23);
		loginScreen.add(btnAuthenticate);
		
		lblLoginState = new JLabel("-");
		lblLoginState.setBounds(10, 576, 1004, 14);
		loginScreen.add(lblLoginState);
		
		JPanel custodianDashboard = new JPanel();
		userArea.add(custodianDashboard, "custodianDash");
		custodianDashboard.setLayout(null);
		
		JTabbedPane custodianWorkArea = new JTabbedPane(JTabbedPane.TOP);
		custodianWorkArea.setBounds(10, 54, 502, 540);
		custodianDashboard.add(custodianWorkArea);
		
		JPanel BorrowSystem = new JPanel();
		custodianWorkArea.addTab("Equipment Borrowing", null, BorrowSystem, null);
		BorrowSystem.setLayout(null);
		
		JPanel MaintenanceSystem = new JPanel();
		custodianWorkArea.addTab("Equipment Maintenance", null, MaintenanceSystem, null);
		
		JLabel lblCustodian = new JLabel("Custodian");
		lblCustodian.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCustodian.setHorizontalAlignment(SwingConstants.LEFT);
		lblCustodian.setBounds(10, 11, 343, 32);
		custodianDashboard.add(lblCustodian);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(522, 79, 862, 515);
		custodianDashboard.add(scrollPane);
		
		query = new DefaultTableModel();
		inventorySorter.setRowFilter(null);
		searchResult = new JTable();
		searchResult.setModel(query);
		searchResult.setRowSorter(inventorySorter);
		
		scrollPane.setViewportView(searchResult);
		
		JPanel tablePanels = new JPanel();
		tablePanels.setBounds(510, 54, 884, 547);
		custodianDashboard.add(tablePanels);
		tablePanels.setLayout(new CardLayout(0, 0));
		
		JPanel inventoryTable = new JPanel();
		tablePanels.add(inventoryTable, "inventory");
		inventoryTable.setLayout(null);
		
		cmbCondition = new JComboBox<String>();
		cmbCondition.setModel(new DefaultComboBoxModel<String>(new String[] {"[Condition]", "Good", "Damaged", "Worn"}));
		cmbCondition.setBounds(321, 0, 97, 22);
		inventoryTable.add(cmbCondition);
		
		cmbCategory = new JComboBox<String>();
		
		cmbCategoryModel = new DefaultComboBoxModel<String>();
		cmbCategoryModel.addElement("[Category]");
		cmbCategory.setModel(cmbCategoryModel);
		cmbCategory.setBounds(214, 0, 97, 22);
		inventoryTable.add(cmbCategory);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(10, 1, 115, 20);
		inventoryTable.add(textField);
		
		JButton btnRefresh = new JButton("⟳");
		btnRefresh.addActionListener(e -> {displayValues();});
		btnRefresh.setBounds(812, 0, 62, 23);
		inventoryTable.add(btnRefresh);
		
		cmbAvailability = new JComboBox<String>();
		cmbAvailability.setModel(new DefaultComboBoxModel<>(new String[] {"[Availability]", "Available", "In Use", "In Maintenance"}));
		cmbAvailability.setBounds(428, 0, 97, 22);
		inventoryTable.add(cmbAvailability);
		
		JButton btnFilter = new JButton("⌕");
		btnFilter.addActionListener(e -> { inventorySorter.setRowFilter(inventoryRF); });
		btnFilter.setBounds(535, 0, 62, 23);
		inventoryTable.add(btnFilter);
		
		JButton btnClearFilter = new JButton("Clear");
		btnClearFilter.addActionListener(e -> { inventorySorter.setRowFilter(null); });
		btnClearFilter.setBounds(607, 0, 62, 23);
		inventoryTable.add(btnClearFilter);
		
		JPanel adminDashboard = new JPanel();
		adminDashboard.setLayout(null);
		userArea.add(adminDashboard, "adminDash");
		
		JTabbedPane adminWorkArea = new JTabbedPane(JTabbedPane.TOP);
		adminWorkArea.setBounds(10, 50, 1374, 540);
		adminDashboard.add(adminWorkArea);
		
		JPanel equipManagement = new JPanel();
		adminWorkArea.addTab("Inventory Management", null, equipManagement, null);
		
		JPanel userManagement = new JPanel();
		adminWorkArea.addTab("User Management", null, userManagement, null);
		
		JLabel lblAdmin = new JLabel("Administrator");
		lblAdmin.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblAdmin.setHorizontalAlignment(SwingConstants.LEFT);
		lblAdmin.setBounds(10, 11, 343, 32);
		adminDashboard.add(lblAdmin);
		
		areaLog = new JTextArea();
		areaLog.setEditable(false);
		areaLog.setBounds(10, 623, 1394, 225);
		mainPane.add(areaLog);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1434, 22);
		frame.getContentPane().add(menuBar);
		
		JMenu menuMain = new JMenu("SETTINGS");
		menuBar.add(menuMain);
		
		menuOptLogout = new JMenuItem("Logout");
		menuOptLogout.addActionListener(e -> { Logout(); });
		menuOptLogout.setEnabled(false);
		menuMain.add(menuOptLogout);
	}
	
	void startupProcess() {
		
		boolean result = backend.checkConnection();
		String connStatMsg = backend.getConnStatus();
		
		loginScreenText(result, connStatMsg);
		
	}
	
	void tryAuthenticate() {
		
		String user = authUsername.getText();
		Reader pass = new CharArrayReader(authPassword.getPassword());
		
		boolean result = backend.authenticate(user, pass);
		
		if(result) {
			sessionUser = user;
			menuOptLogout.setEnabled(true);
			
			if(sessionUser.equals("admin")) {
				card.show(userArea, "adminDash");
				areaLog.append("\n[" + LocalTime.now() + "] Administrator Override: Login Successful.");
				
			}
			else {
				card.show(userArea, "custodianDash");
				areaLog.append("\n[" + LocalTime.now() + "] " + sessionUser + " Login Successful.");
			}
		}
		else {
			
			loginScreenText(result, backend.getAuthStatus());
			areaLog.append("\n[" + LocalTime.now() + "] Connection Error.");
			
		}
		
		authUsername.setText("");
		authPassword.setText("");
	}

	void Logout() {
		if(sessionUser == null) return;
		
		card.show(userArea, "login");
		areaLog.append("\n[" + LocalTime.now() + "]" + sessionUser +" Logged out.");
		sessionUser = null;
		menuOptLogout.setEnabled(false);
		startupProcess();
	}
	
	// Helper Methods
	void loginScreenText(boolean success, String msg) {
		
		if(success) {
			
			lblLoginState.setForeground(new Color(0, 128, 0));
			lblLoginState.setText("Authentication Success: " + msg);
		} else {
			
			lblLoginState.setForeground(new Color(128, 0, 0));
			lblLoginState.setText("Authentication Failed: " + msg);
			
		}
		
	}

	void displayValues() {
		UpdateFilterBox();
		
		String[][] list;
			list = backend.getEquipmentList();
		
		query = new DefaultTableModel(list.length, 13);
		for(int r = 0; r < list.length; r++)
			for (int c = 0; c < list[r].length; c++)
				query.setValueAt(list[r][c], r, c);
		
		searchResult.setModel(query);
		inventorySorter.setModel(query);
		searchResult.setRowSorter(inventorySorter);
	}
	
	
	void UpdateFilterBox() {
		
		String[] values = backend.getInventoryFilters();
		
		cmbCategoryModel.removeAllElements();
		cmbCategoryModel.addElement("[Category]");
		for (int i = 0; i < values.length; i++)
		{
			cmbCategoryModel.addElement(values[i]);

			System.out.print(values[i] + " ");
		}
		
	}
}

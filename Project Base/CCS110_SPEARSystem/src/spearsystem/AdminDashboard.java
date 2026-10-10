package spearsystem;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;


import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;


import kenUI.ButtonOne;
import kenUI.GradientPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent; 
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTable;

public class AdminDashboard {

	final String[] RECORDSTATUS = { "Returned", "Borrowed" };

	final String[] EQUIPCONDITIONS = { "Good", "Worn", "Damaged" };
	final String[] EQUIPAVAILABILITY = { "Available", "Borrowed", "Maintenance" };

	private JPanel adminDashboard;

	private CardLayout card = new CardLayout(0, 0);

	private JPanel cardContainer;

	private DefaultComboBoxModel<String> cmbCategoryModel;
	private JTextField txtFirstName;
	private JTextField txtLastName;
	private JTextField txtContactNumber;
	private JTextField txtUsername;
	private JTextField txtPassword;
	private JTextField txtSearch;
	private JTable table;
	private JTextField txtPropertyTag;
	private JTextField txtEquipmentName;
	private JTextField txtSerialNumber;
	private JTextField txtBrandModel;
	private JTextField txtPurchaseCost;
	private JTextField txtPurchaseDate;
	private JTextField txtDescription;
	private JTextField txtSearch2;

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
//		frame = new JFrame();
//		frame.setBounds(10, 11, 1423, 654);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		adminDashboard = new JPanel();
		adminDashboard.setLayout(null);
		adminDashboard.setBounds(10, 54, 1283, 700);

		cardContainer = new JPanel();
		cardContainer.setBackground(new Color(255, 255, 255));
		cardContainer.setBounds(0, 72, 1284, 628);
		adminDashboard.add(cardContainer);
		cardContainer.setLayout(card);

		cmbCategoryModel = new DefaultComboBoxModel<String>();
		cmbCategoryModel.addElement("[Category]");

		JPanel main = new JPanel();
		main.setBackground(new Color(255, 255, 255));

		main.setLayout(null);

		// ==========================================
		// Gradient test
		// ==========================================
		Color topColor = new Color(212, 255, 253);
		Color bottomColor = new Color(247, 214, 190);
		GradientPanel sidebarMain = new GradientPanel(topColor, bottomColor);

		sidebarMain.setBounds(0, 0, 297, 639);
		sidebarMain.setLayout(null);
		main.add(sidebarMain);

		sidebarMain.setBounds(0, 0, 277, 639);
		sidebarMain.setLayout(null);
		main.add(sidebarMain);

		// Custom Button 1
		ButtonOne btnViewCustodian = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnViewCustodian.setBounds(24, 145, 225, 55);
		sidebarMain.add(btnViewCustodian);
		btnViewCustodian.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnViewCustodian.setText("View Custodian");
		// -

		// Custom Button 3
		ButtonOne btnViewInventory = new ButtonOne(new Color(230, 126, 34), new Color(211, 84, 0));

		btnViewInventory.setBounds(24, 266, 225, 55);
		sidebarMain.add(btnViewInventory);
		btnViewInventory.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnViewInventory.setText("View Inventory");

		ImageIcon originalIcon = new ImageIcon(LoginScreen.class.getResource("/ui/spearKen.png"));
		Image resizedImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);

		JLabel spearLogo = new JLabel("S.P.E.A.R");
		spearLogo.setForeground(new Color(30, 144, 255));
		spearLogo.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		spearLogo.setIcon(new ImageIcon(resizedImage));
		spearLogo.setBounds(0, 11, 249, 64);
		sidebarMain.add(spearLogo);

		JLabel lblCustodianManagement = new JLabel("Custodian Management");
		lblCustodianManagement.setForeground(new Color(30, 144, 255));
		lblCustodianManagement.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblCustodianManagement.setBounds(24, 107, 225, 27);
		sidebarMain.add(lblCustodianManagement);

		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(new Color(30, 144, 255));
		separator_2.setBounds(24, 94, 225, 2);
		sidebarMain.add(separator_2);

		JSeparator separator3 = new JSeparator();
		separator3.setForeground(new Color(30, 144, 255));
		separator3.setBounds(24, 211, 225, 2);
		sidebarMain.add(separator3);

		ImageIcon originalIcon2 = new ImageIcon(LoginScreen.class.getResource("/ui/custodian.png"));
		Image resizedImage2 = originalIcon2.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

		JLabel lblInventoryManagement = new JLabel("Inventory Management");
		lblInventoryManagement.setForeground(new Color(30, 144, 255));
		lblInventoryManagement.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblInventoryManagement.setBounds(24, 224, 225, 27);
		sidebarMain.add(lblInventoryManagement);

		JSeparator separator4 = new JSeparator();
		separator4.setForeground(new Color(30, 144, 255));
		separator4.setBounds(25, 332, 225, 2);
		sidebarMain.add(separator4);
		
		JSeparator separator5 = new JSeparator();
		separator5.setForeground(new Color(30, 144, 255));
		separator5.setBounds(25, 455, 225, 2);
		sidebarMain.add(separator5);
		
		ButtonOne btnBorrowManager = new ButtonOne(new Color(230, 126, 34), new Color(211, 84, 0));
		btnBorrowManager.setText("Borrow Manager");
		btnBorrowManager.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnBorrowManager.setBounds(24, 389, 225, 55);
		sidebarMain.add(btnBorrowManager);
		
		JLabel lblBorrowManagement = new JLabel("Borrow Manager");
		lblBorrowManagement.setForeground(new Color(30, 144, 255));
		lblBorrowManagement.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblBorrowManagement.setBounds(59, 347, 158, 27);
		sidebarMain.add(lblBorrowManagement);
		
		JSeparator separator6 = new JSeparator();
		separator6.setForeground(new Color(30, 144, 255));
		separator6.setBounds(25, 576, 225, 2);
		sidebarMain.add(separator6);
		
		ButtonOne btnMaintenanceManager = new ButtonOne(new Color(230, 126, 34), new Color(211, 84, 0));
		btnMaintenanceManager.setText("Maintenance Manager");
		btnMaintenanceManager.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnMaintenanceManager.setBounds(24, 510, 225, 55);
		sidebarMain.add(btnMaintenanceManager);
		
		JLabel lblMaintenanceManagement = new JLabel("Maintenance Manager");
		lblMaintenanceManagement.setForeground(new Color(30, 144, 255));
		lblMaintenanceManagement.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblMaintenanceManagement.setBounds(33, 468, 211, 27);
		sidebarMain.add(lblMaintenanceManagement);

		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(30, 144, 255));
		separator.setBounds(10, 62, 1380, 2);
		adminDashboard.add(separator);

		cardContainer.add(main, "dashboard");

		JPanel greet = new JPanel();
		greet.setBounds(287, 11, 987, 117);
		main.add(greet);
		greet.setLayout(null);

		JLabel lblUsernameDashboard = new JLabel("Good Morning, Losamia!");
		lblUsernameDashboard.setForeground(new Color(30, 144, 255));
		lblUsernameDashboard.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		lblUsernameDashboard.setBounds(10, 11, 554, 62);
		greet.add(lblUsernameDashboard);

		JLabel lblDescription = new JLabel("Here's an overview of the school's property and equipemnt.");
		lblDescription.setForeground(new Color(30, 144, 255));
		lblDescription.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
		lblDescription.setBounds(10, 65, 554, 27);
		greet.add(lblDescription);

		JPanel totalEquipmentPanel = new JPanel();
		totalEquipmentPanel.setBounds(287, 139, 238, 141);
		main.add(totalEquipmentPanel);
		totalEquipmentPanel.setLayout(null);

		JLabel lblTotalEquipment = new JLabel("Total Equipment");
		lblTotalEquipment.setForeground(new Color(30, 144, 255));
		lblTotalEquipment.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblTotalEquipment.setBounds(55, 11, 127, 27);
		totalEquipmentPanel.add(lblTotalEquipment);

		JLabel lblTotalEquipmentCounter = new JLabel("00");
		lblTotalEquipmentCounter.setForeground(new Color(30, 144, 255));
		lblTotalEquipmentCounter.setFont(new Font("Segoe UI Black", Font.PLAIN, 60));
		lblTotalEquipmentCounter.setBounds(77, 39, 72, 62);
		totalEquipmentPanel.add(lblTotalEquipmentCounter);

		JPanel totalUsersPanel = new JPanel();
		totalUsersPanel.setBounds(1027, 139, 247, 141);
		main.add(totalUsersPanel);
		totalUsersPanel.setLayout(null);

		JLabel lblTotalUsers = new JLabel("Total Users");
		lblTotalUsers.setForeground(new Color(30, 144, 255));
		lblTotalUsers.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblTotalUsers.setBounds(79, 11, 88, 27);
		totalUsersPanel.add(lblTotalUsers);
		
		JLabel lblTotalUsersCounter = new JLabel("00");
		lblTotalUsersCounter.setForeground(new Color(30, 144, 255));
		lblTotalUsersCounter.setFont(new Font("Segoe UI Black", Font.PLAIN, 60));
		lblTotalUsersCounter.setBounds(87, 39, 72, 62);
		totalUsersPanel.add(lblTotalUsersCounter);

		JPanel pendingReturnsPanel = new JPanel();
		pendingReturnsPanel.setBounds(776, 139, 240, 141);
		main.add(pendingReturnsPanel);
		pendingReturnsPanel.setLayout(null);

		JLabel lblPendingReturns = new JLabel("Pending Returns");
		lblPendingReturns.setForeground(new Color(30, 144, 255));
		lblPendingReturns.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblPendingReturns.setBounds(59, 11, 121, 27);
		pendingReturnsPanel.add(lblPendingReturns);
		
		JLabel lblPendingReturnsCounter = new JLabel("00");
		lblPendingReturnsCounter.setForeground(new Color(30, 144, 255));
		lblPendingReturnsCounter.setFont(new Font("Segoe UI Black", Font.PLAIN, 60));
		lblPendingReturnsCounter.setBounds(84, 39, 72, 62);
		pendingReturnsPanel.add(lblPendingReturnsCounter);

		JPanel currentlyBorrowedPanel = new JPanel();
		currentlyBorrowedPanel.setBounds(532, 139, 234, 141);
		main.add(currentlyBorrowedPanel);
		currentlyBorrowedPanel.setLayout(null);

		JLabel lblCurrentlyBorrowed = new JLabel("Currently Borrowed");
		lblCurrentlyBorrowed.setForeground(new Color(30, 144, 255));
		lblCurrentlyBorrowed.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblCurrentlyBorrowed.setBounds(43, 11, 148, 27);
		currentlyBorrowedPanel.add(lblCurrentlyBorrowed);
		
		JLabel lblCurrentlyBorrowedCounter = new JLabel("00");
		lblCurrentlyBorrowedCounter.setForeground(new Color(30, 144, 255));
		lblCurrentlyBorrowedCounter.setFont(new Font("Segoe UI Black", Font.PLAIN, 60));
		lblCurrentlyBorrowedCounter.setBounds(81, 39, 72, 62);
		currentlyBorrowedPanel.add(lblCurrentlyBorrowedCounter);

		JPanel recentEquipmentPanel = new JPanel();
		recentEquipmentPanel.setBounds(285, 291, 481, 337);
		main.add(recentEquipmentPanel);
		recentEquipmentPanel.setLayout(null);

		JPanel reTablePanel = new JPanel();
		reTablePanel.setBounds(0, 53, 481, 284);
		recentEquipmentPanel.add(reTablePanel);

		Object[][] testData = { { "Laser Cutter", "ICT equipment", "Active" },
				{ "3D Printer", "AV Equipment", "Maintenance" }, { "CNC Router", "Photography", "Idle" } };
		String[] columnNames = { "ItemName", "Category", "Status" };
		reTablePanel.setLayout(null);

		org.jdesktop.swingx.JXTable testTable = new org.jdesktop.swingx.JXTable(testData, columnNames);
		testTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		testTable.setColumnControlVisible(true);
		testTable.setHighlighters(org.jdesktop.swingx.decorator.HighlighterFactory.createSimpleStriping());
		testTable.setRowHeight(30);

		testTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
		testTable.setFillsViewportHeight(true);

		testTable.setIntercellSpacing(new java.awt.Dimension(2, 2));
		testTable.setGridColor(java.awt.Color.LIGHT_GRAY);

		JScrollPane scrollPane1 = new JScrollPane(testTable);
		scrollPane1.setBounds(0, 5, 481, 279);
		reTablePanel.add(scrollPane1);

		JLabel lblRecentEquipment = new JLabel("Recent Equipment");
		lblRecentEquipment.setForeground(new Color(30, 144, 255));
		lblRecentEquipment.setFont(new Font("Segoe UI Black", Font.PLAIN, 25));
		lblRecentEquipment.setBounds(128, 11, 224, 27);
		recentEquipmentPanel.add(lblRecentEquipment);

		JPanel recentBorrowingsPanel = new JPanel();
		recentBorrowingsPanel.setBounds(775, 291, 499, 337);
		main.add(recentBorrowingsPanel);
		recentBorrowingsPanel.setLayout(null);

		JPanel ebBorrowingTablePanel = new JPanel();
		ebBorrowingTablePanel.setBounds(0, 53, 499, 284);
		recentBorrowingsPanel.add(ebBorrowingTablePanel);
		ebBorrowingTablePanel.setLayout(null);

		Object[][] borrowingData = { { "Laser Cutter", "Felonia", "2026-10-08", "Pending" },
				{ "3D Printer", "Losamia", "2026-10-07", "Approved" },
				{ "CNC Router", "Paz", "2026-10-05", "Returned" } };
		String[] borrowingColumns = { "Item", "Borrower", "Date", "Status" };

		org.jdesktop.swingx.JXTable borrowingTable = new org.jdesktop.swingx.JXTable(borrowingData, borrowingColumns);
		borrowingTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		borrowingTable.setColumnControlVisible(true);
		borrowingTable.setHighlighters(org.jdesktop.swingx.decorator.HighlighterFactory.createSimpleStriping());
		borrowingTable.setRowHeight(30);

		borrowingTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
		borrowingTable.setFillsViewportHeight(true);

		borrowingTable.setIntercellSpacing(new java.awt.Dimension(2, 2));
		borrowingTable.setGridColor(java.awt.Color.LIGHT_GRAY);
		borrowingTable.setShowHorizontalLines(true);
		borrowingTable.setShowVerticalLines(true);

		JScrollPane borrowingScrollPane = new JScrollPane(borrowingTable);
		borrowingScrollPane.setSize(499, 284);
		ebBorrowingTablePanel.add(borrowingScrollPane, java.awt.BorderLayout.CENTER);

		JLabel lblRecentBorrowings = new JLabel("Recent Borrowings");
		lblRecentBorrowings.setForeground(new Color(30, 144, 255));
		lblRecentBorrowings.setFont(new Font("Segoe UI Black", Font.PLAIN, 25));
		lblRecentBorrowings.setBounds(132, 11, 234, 27);
		recentBorrowingsPanel.add(lblRecentBorrowings);

		card.show(cardContainer, "dashboard");

		JPanel custodianManagementPanel = new JPanel();
		cardContainer.add(custodianManagementPanel, "custodianManagement");

		// ==========================================
		// Gradient test
		// ==========================================
		Color topColor2 = new Color(212, 255, 253);
		Color bottomColor2 = new Color(247, 214, 190);
		custodianManagementPanel.setLayout(null);
		GradientPanel cmSidebar = new GradientPanel(topColor2, bottomColor2);
		cmSidebar.setBounds(0, 0, 277, 639);
		cmSidebar.setLayout(null);
		custodianManagementPanel.add(cmSidebar);
		custodianManagementPanel.setBounds(0, 0, 277, 639);

		JLabel spearLogo2 = new JLabel("S.P.E.A.R");
		spearLogo2.setForeground(new Color(30, 144, 255));
		spearLogo2.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		spearLogo2.setBounds(0, 11, 250, 64);
		spearLogo2.setIcon(new ImageIcon(resizedImage));
		cmSidebar.add(spearLogo2);

		JLabel lblCustodianManager = new JLabel("Custodian Manager");
		lblCustodianManager.setForeground(new Color(30, 144, 255));
		lblCustodianManager.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblCustodianManager.setBounds(47, 107, 183, 27);
		cmSidebar.add(lblCustodianManager);

		JSeparator separator7 = new JSeparator();
		separator7.setForeground(new Color(30, 144, 255));
		separator7.setBounds(26, 94, 225, 2);
		cmSidebar.add(separator7);
		
		JPanel totalCustomerPanel = new JPanel();
		totalCustomerPanel.setBounds(25, 147, 225, 110);
		cmSidebar.add(totalCustomerPanel);
		totalCustomerPanel.setLayout(null);
		
		JLabel lblTotalCustomer = new JLabel("Total Customers");
		lblTotalCustomer.setForeground(new Color(30, 144, 255));
		lblTotalCustomer.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblTotalCustomer.setBounds(36, 11, 153, 27);
		totalCustomerPanel.add(lblTotalCustomer);
		
		JLabel lblTotalCustomerCounter = new JLabel("00");
		lblTotalCustomerCounter.setForeground(new Color(30, 144, 255));
		lblTotalCustomerCounter.setFont(new Font("Segoe UI Black", Font.PLAIN, 55));
		lblTotalCustomerCounter.setBounds(79, 38, 66, 61);
		totalCustomerPanel.add(lblTotalCustomerCounter);
		
		JPanel administratorPanel = new JPanel();
		administratorPanel.setBounds(26, 268, 225, 110);
		cmSidebar.add(administratorPanel);
		administratorPanel.setLayout(null);
		
		JLabel lblAdministrator = new JLabel("Administrators");
		lblAdministrator.setForeground(new Color(30, 144, 255));
		lblAdministrator.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblAdministrator.setBounds(36, 11, 153, 27);
		administratorPanel.add(lblAdministrator);
		
		JLabel lblAdministratorCounter = new JLabel("00");
		lblAdministratorCounter.setForeground(new Color(30, 144, 255));
		lblAdministratorCounter.setFont(new Font("Segoe UI Black", Font.PLAIN, 55));
		lblAdministratorCounter.setBounds(79, 38, 66, 61);
		administratorPanel.add(lblAdministratorCounter);
		
		JPanel staffPanel = new JPanel();
		staffPanel.setBounds(25, 389, 225, 110);
		cmSidebar.add(staffPanel);
		staffPanel.setLayout(null);
		
		JLabel lblStaff = new JLabel("Staff");
		lblStaff.setForeground(new Color(30, 144, 255));
		lblStaff.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblStaff.setBounds(89, 11, 46, 27);
		staffPanel.add(lblStaff);
		
		JLabel lblStaffCounter = new JLabel("00");
		lblStaffCounter.setForeground(new Color(30, 144, 255));
		lblStaffCounter.setFont(new Font("Segoe UI Black", Font.PLAIN, 55));
		lblStaffCounter.setBounds(79, 38, 66, 61);
		staffPanel.add(lblStaffCounter);
		
		ButtonOne btnGoBack = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		
		btnGoBack.setText("Go Back");
		btnGoBack.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnGoBack.setBounds(26, 561, 224, 45);
		cmSidebar.add(btnGoBack);
		
		ButtonOne btnViewInventory2 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		
		btnViewInventory2.setText("View Inventory");
		btnViewInventory2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnViewInventory2.setBounds(26, 510, 224, 45);
		cmSidebar.add(btnViewInventory2);
		
		JPanel addCustodianPanel = new JPanel();
		addCustodianPanel.setBounds(287, 0, 237, 617);
		addCustodianPanel.setBackground(new Color(255, 255, 255));
		custodianManagementPanel.add(addCustodianPanel);
		addCustodianPanel.setLayout(null);
		
		JPanel addCustodianPane = new JPanel();
		addCustodianPane.setBounds(0, 0, 237, 47);
		addCustodianPane.setBackground(new Color(240, 248, 255));
		addCustodianPanel.add(addCustodianPane);
		addCustodianPane.setLayout(null);
		
		JLabel lblNewLabel_1_2 = new JLabel("Add Custodian");
		lblNewLabel_1_2.setBackground(new Color(230, 230, 250));
		lblNewLabel_1_2.setBounds(10, 0, 227, 47);
		lblNewLabel_1_2.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_2.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		addCustodianPane.add(lblNewLabel_1_2);
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblFirstName.setBounds(10, 58, 107, 25);
		addCustodianPanel.add(lblFirstName);
		
		txtFirstName = new JTextField();
		txtFirstName.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtFirstName.setBounds(10, 86, 215, 25);
		addCustodianPanel.add(txtFirstName);
		txtFirstName.setColumns(10);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblLastName.setBounds(10, 122, 107, 25);
		addCustodianPanel.add(lblLastName);
		
		txtLastName = new JTextField();
		txtLastName.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtLastName.setColumns(10);
		txtLastName.setBounds(10, 150, 215, 25);
		addCustodianPanel.add(txtLastName);
		
		JLabel lblPosition = new JLabel("Position");
		lblPosition.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblPosition.setBounds(10, 186, 107, 25);
		addCustodianPanel.add(lblPosition);
		
		// Do not remove
		JComboBox positionCbo = new JComboBox();
		positionCbo.setBounds(10, 211, 215, 25);
		addCustodianPanel.add(positionCbo);
		
		JLabel lblDepartment = new JLabel("Department");
		lblDepartment.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblDepartment.setBounds(10, 247, 107, 25);
		addCustodianPanel.add(lblDepartment);
		
		// Do not remove
		JComboBox departmentCbo = new JComboBox();
		departmentCbo.setBounds(10, 272, 215, 25);
		addCustodianPanel.add(departmentCbo);
		
		JLabel lblContactnumber = new JLabel("ContactNumber");
		lblContactnumber.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblContactnumber.setBounds(10, 308, 107, 25);
		addCustodianPanel.add(lblContactnumber);
		
		txtContactNumber = new JTextField();
		txtContactNumber.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtContactNumber.setColumns(10);
		txtContactNumber.setBounds(10, 336, 215, 25);
		addCustodianPanel.add(txtContactNumber);
		
		JLabel lblCreateLoginAccount = new JLabel("Create Login Account");
		lblCreateLoginAccount.setForeground(new Color(30, 144, 255));
		lblCreateLoginAccount.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblCreateLoginAccount.setBounds(51, 368, 135, 27);
		addCustodianPanel.add(lblCreateLoginAccount);
		
		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtUsername.setColumns(10);
		txtUsername.setBounds(10, 420, 215, 25);
		addCustodianPanel.add(txtUsername);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblUsername.setBounds(10, 392, 107, 25);
		addCustodianPanel.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblPassword.setBounds(9, 448, 107, 25);
		addCustodianPanel.add(lblPassword);
		
		txtPassword = new JTextField();
		txtPassword.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtPassword.setColumns(10);
		txtPassword.setBounds(9, 476, 215, 25);
		addCustodianPanel.add(txtPassword);
		
		ButtonOne btnAdd = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnAdd.setText("Add");
		btnAdd.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnAdd.setBounds(9, 510, 105, 45);
		addCustodianPanel.add(btnAdd);
		
		ButtonOne btnEdit = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnEdit.setText("Edit");
		btnEdit.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnEdit.setBounds(10, 561, 105, 45);
		addCustodianPanel.add(btnEdit);
		
		ButtonOne btnDelete = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnDelete.setText("Delete");
		btnDelete.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnDelete.setBounds(118, 512, 105, 45);
		addCustodianPanel.add(btnDelete);
		
		ButtonOne btnClear = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnClear.setText("Clear");
		btnClear.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnClear.setBounds(118, 561, 105, 45);
		addCustodianPanel.add(btnClear);
		
		JSeparator separator_2_2_2 = new JSeparator();
		separator_2_2_2.setForeground(new Color(30, 144, 255));
		separator_2_2_2.setBounds(11, 387, 215, 2);
		
		JPanel custodianRecordsPanel = new JPanel();
		custodianRecordsPanel.setBackground(new Color(255, 255, 255));
		custodianRecordsPanel.setBounds(534, 0, 740, 617);
		custodianManagementPanel.add(custodianRecordsPanel);
		custodianRecordsPanel.setLayout(null);
		
		JPanel custodianRecordsHero = new JPanel();
		custodianRecordsHero.setLayout(null);
		custodianRecordsHero.setBackground(new Color(240, 248, 255));
		custodianRecordsHero.setBounds(0, 0, 740, 47);
		custodianRecordsPanel.add(custodianRecordsHero);
		
		JLabel lblCustodiansRecords = new JLabel("Custodian Records");
		lblCustodiansRecords.setForeground(new Color(30, 144, 255));
		lblCustodiansRecords.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		lblCustodiansRecords.setBackground(new Color(230, 230, 250));
		lblCustodiansRecords.setBounds(10, 0, 306, 47);
		custodianRecordsHero.add(lblCustodiansRecords);
		
		JPanel quickAccessPanel = new JPanel();
		quickAccessPanel.setLayout(null);
		quickAccessPanel.setBackground(new Color(255, 255, 255));
		quickAccessPanel.setBounds(0, 45, 740, 72);
		custodianRecordsPanel.add(quickAccessPanel);
		
		txtSearch = new JTextField();
		txtSearch.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtSearch.setColumns(10);
		txtSearch.setBounds(10, 36, 227, 25);
		quickAccessPanel.add(txtSearch);
		
		JButton btnSearch = new JButton("Q");
		btnSearch.setBounds(247, 36, 51, 25);
		quickAccessPanel.add(btnSearch);
		
		JComboBox allPositionsCbo = new JComboBox();
		allPositionsCbo.setBounds(545, 37, 185, 25);
		quickAccessPanel.add(allPositionsCbo);
		
		JLabel lblSearch = new JLabel("Search");
		lblSearch.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblSearch.setBounds(10, 11, 107, 25);
		quickAccessPanel.add(lblSearch);
		
		JLabel lblAllPositions = new JLabel("All Positions");
		lblAllPositions.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblAllPositions.setBounds(545, 11, 107, 25);
		quickAccessPanel.add(lblAllPositions);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(613, 224, 661, 393);
		custodianManagementPanel.add(scrollPane);
		
		table = new JTable();
		table.setBounds(613, 224, 661, 393);
		custodianManagementPanel.add(table);
		
		JPanel inventoryManagementPanel = new JPanel();
		inventoryManagementPanel.setLayout(null);
		cardContainer.add(inventoryManagementPanel, "inventoryManagementPanel");
		
		GradientPanel imSidebar = new GradientPanel(new Color(212, 255, 253), new Color(247, 214, 190));
		imSidebar.setLayout(null);
		imSidebar.setBounds(0, 0, 277, 639);
		inventoryManagementPanel.add(imSidebar);
		
		JLabel spearLogo3 = new JLabel("S.P.E.A.R");
		spearLogo3.setForeground(new Color(30, 144, 255));
		spearLogo3.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		spearLogo3.setBounds(0, 11, 250, 64);
		spearLogo3.setIcon(new ImageIcon(resizedImage));
		imSidebar.add(spearLogo3);
		
		JLabel lblInventoryManager = new JLabel("Inventory Manager");
		lblInventoryManager.setForeground(new Color(30, 144, 255));
		lblInventoryManager.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblInventoryManager.setBounds(47, 107, 183, 27);
		imSidebar.add(lblInventoryManager);
		
		JSeparator separator8 = new JSeparator();
		separator8.setForeground(new Color(30, 144, 255));
		separator8.setBounds(26, 94, 225, 2);
		imSidebar.add(separator8);
		
		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBounds(25, 147, 225, 80);
		imSidebar.add(panel_4);
		
		JLabel lblTotalEquipment2 = new JLabel("Total Equipment");
		lblTotalEquipment2.setForeground(new Color(30, 144, 255));
		lblTotalEquipment2.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblTotalEquipment2.setBounds(51, 0, 122, 34);
		panel_4.add(lblTotalEquipment2);
		
		JLabel lblTotalEquipment2Counter = new JLabel("00");
		lblTotalEquipment2Counter.setForeground(new Color(30, 144, 255));
		lblTotalEquipment2Counter.setFont(new Font("Segoe UI Black", Font.PLAIN, 45));
		lblTotalEquipment2Counter.setBounds(85, 25, 54, 50);
		panel_4.add(lblTotalEquipment2Counter);
		
		ButtonOne btnGoBack_1 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		
		btnGoBack_1.setText("Go Back");
		btnGoBack_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnGoBack_1.setBounds(26, 561, 224, 45);
		imSidebar.add(btnGoBack_1);
		
		ButtonOne btnViewInventory_1_1 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		
		btnViewInventory_1_1.setText("View Custodian");
		btnViewInventory_1_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnViewInventory_1_1.setBounds(26, 510, 224, 45);
		imSidebar.add(btnViewInventory_1_1);
		
		JPanel availablePanel = new JPanel();
		availablePanel.setLayout(null);
		availablePanel.setBounds(26, 238, 225, 80);
		imSidebar.add(availablePanel);
		
		JLabel lblAvailable = new JLabel("Available");
		lblAvailable.setForeground(new Color(30, 144, 255));
		lblAvailable.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblAvailable.setBounds(78, 0, 69, 34);
		availablePanel.add(lblAvailable);
		
		JLabel lblAvailableCounter = new JLabel("00");
		lblAvailableCounter.setForeground(new Color(30, 144, 255));
		lblAvailableCounter.setFont(new Font("Segoe UI Black", Font.PLAIN, 45));
		lblAvailableCounter.setBounds(85, 25, 54, 50);
		availablePanel.add(lblAvailableCounter);
		
		JPanel underMaintenancePanel = new JPanel();
		underMaintenancePanel.setLayout(null);
		underMaintenancePanel.setBounds(26, 329, 225, 80);
		imSidebar.add(underMaintenancePanel);
		
		JLabel lblUnderMaintenance = new JLabel("Under Maintenance");
		lblUnderMaintenance.setForeground(new Color(30, 144, 255));
		lblUnderMaintenance.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblUnderMaintenance.setBounds(42, 0, 141, 34);
		underMaintenancePanel.add(lblUnderMaintenance);
		
		JLabel lblUnderMaintenanceCounter = new JLabel("00");
		lblUnderMaintenanceCounter.setForeground(new Color(30, 144, 255));
		lblUnderMaintenanceCounter.setFont(new Font("Segoe UI Black", Font.PLAIN, 45));
		lblUnderMaintenanceCounter.setBounds(85, 25, 54, 50);
		underMaintenancePanel.add(lblUnderMaintenanceCounter);
		
		JPanel borrowedPanel = new JPanel();
		borrowedPanel.setLayout(null);
		borrowedPanel.setBounds(26, 420, 225, 80);
		imSidebar.add(borrowedPanel);
		
		JLabel lblBorrowed = new JLabel("Borrowed");
		lblBorrowed.setForeground(new Color(30, 144, 255));
		lblBorrowed.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblBorrowed.setBounds(75, 0, 75, 34);
		borrowedPanel.add(lblBorrowed);
		
		JLabel lblBorrowedCounter = new JLabel("00");
		lblBorrowedCounter.setForeground(new Color(30, 144, 255));
		lblBorrowedCounter.setFont(new Font("Segoe UI Black", Font.PLAIN, 45));
		lblBorrowedCounter.setBounds(85, 25, 54, 50);
		borrowedPanel.add(lblBorrowedCounter);
		
		JPanel addCustodianPanel_1 = new JPanel();
		addCustodianPanel_1.setLayout(null);
		addCustodianPanel_1.setBackground(Color.WHITE);
		addCustodianPanel_1.setBounds(287, 347, 987, 270);
		inventoryManagementPanel.add(addCustodianPanel_1);
		
		JPanel hero2 = new JPanel();
		hero2.setLayout(null);
		hero2.setBackground(new Color(240, 248, 255));
		hero2.setBounds(0, 0, 987, 31);
		addCustodianPanel_1.add(hero2);
		
		JLabel lblAddOrEditEquipment = new JLabel("Add / Edit Equipment");
		lblAddOrEditEquipment.setForeground(new Color(30, 144, 255));
		lblAddOrEditEquipment.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblAddOrEditEquipment.setBackground(new Color(230, 230, 250));
		lblAddOrEditEquipment.setBounds(10, 0, 158, 31);
		hero2.add(lblAddOrEditEquipment);
		
		JLabel lblPropertyTag = new JLabel("Property Tag*");
		lblPropertyTag.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblPropertyTag.setBounds(10, 42, 261, 25);
		addCustodianPanel_1.add(lblPropertyTag);
		
		txtPropertyTag = new JTextField();
		txtPropertyTag.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtPropertyTag.setColumns(10);
		txtPropertyTag.setBounds(10, 70, 261, 25);
		addCustodianPanel_1.add(txtPropertyTag);
		
		JLabel lblEquipmentName = new JLabel("Equipment Name*");
		lblEquipmentName.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblEquipmentName.setBounds(10, 98, 261, 25);
		addCustodianPanel_1.add(lblEquipmentName);
		
		txtEquipmentName = new JTextField();
		txtEquipmentName.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtEquipmentName.setColumns(10);
		txtEquipmentName.setBounds(10, 126, 261, 25);
		addCustodianPanel_1.add(txtEquipmentName);
		
		JLabel lblCategory = new JLabel("Category*");
		lblCategory.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblCategory.setBounds(10, 157, 261, 25);
		addCustodianPanel_1.add(lblCategory);
		
		// Do not remove
		JComboBox cboCategory = new JComboBox();
		cboCategory.setBounds(10, 185, 261, 25);
		addCustodianPanel_1.add(cboCategory);
		
		ButtonOne btnClear_1 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnClear_1.setText("Save Equipment");
		btnClear_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnClear_1.setBounds(647, 221, 215, 45);
		addCustodianPanel_1.add(btnClear_1);
		
		ButtonOne btnClear_2_1 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnClear_2_1.setText("Reset");
		btnClear_2_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnClear_2_1.setBounds(872, 221, 105, 45);
		addCustodianPanel_1.add(btnClear_2_1);
		
		JLabel lblRoomLocation = new JLabel("Room / Location");
		lblRoomLocation.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblRoomLocation.setBounds(281, 157, 250, 25);
		addCustodianPanel_1.add(lblRoomLocation);
		
		// Do not remove
		JComboBox cboRoomLocation = new JComboBox();
		cboRoomLocation.setBounds(281, 185, 250, 25);
		addCustodianPanel_1.add(cboRoomLocation);
		
		txtSerialNumber = new JTextField();
		txtSerialNumber.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtSerialNumber.setColumns(10);
		txtSerialNumber.setBounds(281, 126, 250, 25);
		addCustodianPanel_1.add(txtSerialNumber);
		
		JLabel lblSerialNumber = new JLabel("Serial Number");
		lblSerialNumber.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblSerialNumber.setBounds(281, 98, 250, 25);
		addCustodianPanel_1.add(lblSerialNumber);
		
		txtBrandModel = new JTextField();
		txtBrandModel.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtBrandModel.setColumns(10);
		txtBrandModel.setBounds(281, 70, 250, 25);
		addCustodianPanel_1.add(txtBrandModel);
		
		JLabel lblBrandModel = new JLabel("Brand / Model");
		lblBrandModel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblBrandModel.setBounds(281, 42, 250, 25);
		addCustodianPanel_1.add(lblBrandModel);
		
		JLabel lblCondition = new JLabel("Condition");
		lblCondition.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblCondition.setBounds(541, 157, 213, 25);
		addCustodianPanel_1.add(lblCondition);
		
		// Do not remove
		JComboBox cboCondition = new JComboBox();
		cboCondition.setBounds(541, 185, 213, 25);
		addCustodianPanel_1.add(cboCondition);
		
		txtPurchaseCost = new JTextField();
		txtPurchaseCost.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtPurchaseCost.setColumns(10);
		txtPurchaseCost.setBounds(541, 126, 213, 25);
		addCustodianPanel_1.add(txtPurchaseCost);
		
		JLabel lblPurchaseCost = new JLabel("Purchase Cost");
		lblPurchaseCost.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblPurchaseCost.setBounds(541, 98, 213, 25);
		addCustodianPanel_1.add(lblPurchaseCost);
		
		txtPurchaseDate = new JTextField();
		txtPurchaseDate.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtPurchaseDate.setColumns(10);
		txtPurchaseDate.setBounds(541, 70, 213, 25);
		addCustodianPanel_1.add(txtPurchaseDate);
		
		JLabel lblPurchaseDate = new JLabel("Purchase Date");
		lblPurchaseDate.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblPurchaseDate.setBounds(541, 42, 213, 25);
		addCustodianPanel_1.add(lblPurchaseDate);
		
		JLabel lblAvailability = new JLabel("Availability");
		lblAvailability.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblAvailability.setBounds(764, 42, 213, 25);
		addCustodianPanel_1.add(lblAvailability);
		
		JComboBox cboAvailability = new JComboBox();
		cboAvailability.setBounds(764, 70, 213, 25);
		addCustodianPanel_1.add(cboAvailability);
		
		JLabel lblDescription2 = new JLabel("Description");
		lblDescription2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblDescription2.setBounds(764, 106, 213, 25);
		addCustodianPanel_1.add(lblDescription2);
		
		txtDescription = new JTextField();
		txtDescription.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtDescription.setColumns(10);
		txtDescription.setBounds(764, 129, 213, 81);
		addCustodianPanel_1.add(txtDescription);
		
		ButtonOne btnAdd2 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnAdd2.setText("Add");
		btnAdd2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnAdd2.setBounds(10, 221, 89, 45);
		addCustodianPanel_1.add(btnAdd2);
		
		ButtonOne btnDelete2 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnDelete2.setText("Delete");
		btnDelete2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnDelete2.setBounds(109, 221, 89, 45);
		addCustodianPanel_1.add(btnDelete2);
		
		ButtonOne btnView2 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnView2.setText("View");
		btnView2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnView2.setBounds(208, 221, 89, 45);
		addCustodianPanel_1.add(btnView2);
		
		ButtonOne btnEdit2 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnEdit2.setText("Edit");
		btnEdit2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnEdit2.setBounds(307, 221, 89, 45);
		addCustodianPanel_1.add(btnEdit2);
		
		JPanel custodianRecordsPane2 = new JPanel();
		custodianRecordsPane2.setLayout(null);
		custodianRecordsPane2.setBackground(Color.WHITE);
		custodianRecordsPane2.setBounds(287, 44, 987, 292);
		inventoryManagementPanel.add(custodianRecordsPane2);
		
		JPanel hero3 = new JPanel();
		hero3.setLayout(null);
		hero3.setBackground(new Color(240, 248, 255));
		hero3.setBounds(0, 0, 987, 47);
		custodianRecordsPane2.add(hero3);
		
		JLabel lblCustodianRecords = new JLabel("Custodian Records");
		lblCustodianRecords.setForeground(new Color(30, 144, 255));
		lblCustodianRecords.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		lblCustodianRecords.setBackground(new Color(230, 230, 250));
		lblCustodianRecords.setBounds(10, 0, 306, 47);
		hero3.add(lblCustodianRecords);
		
		JPanel quickActions2 = new JPanel();
		quickActions2.setBackground(new Color(255, 255, 255));
		quickActions2.setBounds(287, 0, 987, 36);
		inventoryManagementPanel.add(quickActions2);
		quickActions2.setLayout(null);
		
		txtSearch2 = new JTextField();
		txtSearch2.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtSearch2.setColumns(10);
		txtSearch2.setBounds(57, 5, 227, 25);
		quickActions2.add(txtSearch2);
		
		JButton btnSearch2 = new JButton("Q");
		btnSearch2.setBounds(289, 6, 51, 23);
		quickActions2.add(btnSearch2);
		
		JLabel lblSearch2 = new JLabel("Search");
		lblSearch2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblSearch2.setBounds(10, 5, 51, 25);
		quickActions2.add(lblSearch2);
		
		JLabel lblAllPositions_1 = new JLabel("Category");
		lblAllPositions_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblAllPositions_1.setBounds(350, 5, 56, 25);
		quickActions2.add(lblAllPositions_1);
		
		JComboBox cboCategory2 = new JComboBox();
		cboCategory2.setBounds(416, 5, 137, 25);
		quickActions2.add(cboCategory2);
		
		JComboBox cboCondition2 = new JComboBox();
		cboCondition2.setBounds(629, 7, 117, 25);
		quickActions2.add(cboCondition2);
		
		JLabel lblCondition2 = new JLabel("Condition");
		lblCondition2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblCondition2.setBounds(563, 5, 68, 25);
		quickActions2.add(lblCondition2);
		
		JLabel lblAvailability2 = new JLabel("Availability");
		lblAvailability2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblAvailability2.setBounds(756, 5, 76, 25);
		quickActions2.add(lblAvailability2);
		
		JComboBox cboAvailability2 = new JComboBox();
		cboAvailability2.setBounds(842, 5, 135, 25);
		quickActions2.add(cboAvailability2);

		JLabel lblAdmin = new JLabel("Admin Dashboard");
		lblAdmin.setIcon(new ImageIcon(resizedImage2));
		lblAdmin.setForeground(new Color(30, 144, 255));
		lblAdmin.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		lblAdmin.setBounds(10, 0, 396, 64);
		adminDashboard.add(lblAdmin);

		// ===========================================
		// BUTTON ACTION LISTENER
		// ===========================================
		btnViewCustodian.addActionListener(e -> {
			card.show(cardContainer, "custodianManagement");
			lblAdmin.setText("Custodian Manager");
		});

		btnViewInventory.addActionListener(e -> {
			card.show(cardContainer, "inventoryManagementPanel");
			lblAdmin.setText("Inventory Manager");
		});
		
		btnGoBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				card.show(cardContainer, "dashboard");
				lblAdmin.setText("Admin Dashboard");
			}
		});
		
		btnGoBack_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				card.show(cardContainer, "dashboard");
				lblAdmin.setText("Admin Dashboard");
			}
		});
		
		btnViewInventory2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				card.show(cardContainer, "inventoryManagementPanel");
				lblAdmin.setText("Inventory Manager");
			}
		});
		
		btnViewInventory_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				card.show(cardContainer, "custodianManagement");
				lblAdmin.setText("Custodian Manager");
			}
		});

		// frame.setVisible(true);

	}
	// Change the first parameter from Object to MainClient, and call initialize()
	public AdminDashboard(MainClient mainClient, CustodianHandler custodianHandler) {
		initialize();
	}
	public Component getPanel() {
		// TODO Auto-generated method stub
		return adminDashboard;
	}
}

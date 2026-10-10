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
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTable table;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_12;
	private JTextField textField_13;
	private JTextField textField_11;

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
		GradientPanel sidebar = new GradientPanel(topColor, bottomColor);

		sidebar.setBounds(0, 0, 297, 639);
		sidebar.setLayout(null);
		main.add(sidebar);

		sidebar.setBounds(0, 0, 277, 639);
		sidebar.setLayout(null);
		main.add(sidebar);

		// Custom Button 1
		ButtonOne btnViewCustodian = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnViewCustodian.setBounds(24, 145, 225, 55);
		sidebar.add(btnViewCustodian);
		btnViewCustodian.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnViewCustodian.setText("View Custodian");
		// -

		// Custom Button 3
		ButtonOne btnViewInventory = new ButtonOne(new Color(230, 126, 34), new Color(211, 84, 0));

		btnViewInventory.setBounds(24, 266, 225, 55);
		sidebar.add(btnViewInventory);
		btnViewInventory.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnViewInventory.setText("View Inventory");

		ImageIcon originalIcon = new ImageIcon(LoginScreen.class.getResource("/ui/spearKen.png"));
		Image resizedImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);

		JLabel spearLogo = new JLabel("S.P.E.A.R");
		spearLogo.setForeground(new Color(30, 144, 255));
		spearLogo.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		spearLogo.setIcon(new ImageIcon(resizedImage));
		spearLogo.setBounds(0, 11, 249, 64);
		sidebar.add(spearLogo);

		JLabel lblCustodianManagement = new JLabel("Custodian Management");
		lblCustodianManagement.setForeground(new Color(30, 144, 255));
		lblCustodianManagement.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblCustodianManagement.setBounds(24, 107, 225, 27);
		sidebar.add(lblCustodianManagement);

		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(new Color(30, 144, 255));
		separator_2.setBounds(24, 94, 225, 2);
		sidebar.add(separator_2);

		JSeparator separator_2_1 = new JSeparator();
		separator_2_1.setForeground(new Color(30, 144, 255));
		separator_2_1.setBounds(24, 211, 225, 2);
		sidebar.add(separator_2_1);

		ImageIcon originalIcon2 = new ImageIcon(LoginScreen.class.getResource("/ui/custodian.png"));
		Image resizedImage2 = originalIcon2.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

		JLabel lblInventoryManagement = new JLabel("Inventory Management");
		lblInventoryManagement.setForeground(new Color(30, 144, 255));
		lblInventoryManagement.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblInventoryManagement.setBounds(24, 224, 225, 27);
		sidebar.add(lblInventoryManagement);

		JSeparator separator_2_1_1 = new JSeparator();
		separator_2_1_1.setForeground(new Color(30, 144, 255));
		separator_2_1_1.setBounds(25, 332, 225, 2);
		sidebar.add(separator_2_1_1);
		
		JSeparator separator_2_1_1_1 = new JSeparator();
		separator_2_1_1_1.setForeground(new Color(30, 144, 255));
		separator_2_1_1_1.setBounds(25, 455, 225, 2);
		sidebar.add(separator_2_1_1_1);
		
		ButtonOne btnBorrowManager = new ButtonOne(new Color(230, 126, 34), new Color(211, 84, 0));
		btnBorrowManager.setText("Borrow Manager");
		btnBorrowManager.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnBorrowManager.setBounds(24, 389, 225, 55);
		sidebar.add(btnBorrowManager);
		
		JLabel lblBorrowManagement = new JLabel("Borrow Manager");
		lblBorrowManagement.setForeground(new Color(30, 144, 255));
		lblBorrowManagement.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblBorrowManagement.setBounds(59, 347, 158, 27);
		sidebar.add(lblBorrowManagement);
		
		JSeparator separator_2_1_1_2 = new JSeparator();
		separator_2_1_1_2.setForeground(new Color(30, 144, 255));
		separator_2_1_1_2.setBounds(25, 576, 225, 2);
		sidebar.add(separator_2_1_1_2);
		
		ButtonOne btnMaintenanceManager = new ButtonOne(new Color(230, 126, 34), new Color(211, 84, 0));
		btnMaintenanceManager.setText("Maintenance Manager");
		btnMaintenanceManager.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnMaintenanceManager.setBounds(24, 510, 225, 55);
		sidebar.add(btnMaintenanceManager);
		
		JLabel lblMaintenanceManagement = new JLabel("Maintenance Manager");
		lblMaintenanceManagement.setForeground(new Color(30, 144, 255));
		lblMaintenanceManagement.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblMaintenanceManagement.setBounds(33, 468, 211, 27);
		sidebar.add(lblMaintenanceManagement);

		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(30, 144, 255));
		separator.setBounds(10, 62, 1380, 2);
		adminDashboard.add(separator);

		cardContainer.add(main, "dashboard");

		JPanel greet = new JPanel();
		greet.setBounds(287, 11, 987, 117);
		main.add(greet);
		greet.setLayout(null);

		JLabel lblTotalEquipment_1_2 = new JLabel("Good Morning, Losamia!");
		lblTotalEquipment_1_2.setForeground(new Color(30, 144, 255));
		lblTotalEquipment_1_2.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		lblTotalEquipment_1_2.setBounds(10, 11, 554, 62);
		greet.add(lblTotalEquipment_1_2);

		JLabel lblTotalEquipment_1_2_1 = new JLabel("Here's an overview of the school's property and equipemnt.");
		lblTotalEquipment_1_2_1.setForeground(new Color(30, 144, 255));
		lblTotalEquipment_1_2_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
		lblTotalEquipment_1_2_1.setBounds(10, 65, 554, 27);
		greet.add(lblTotalEquipment_1_2_1);

		JPanel totalEquipmentPanel = new JPanel();
		totalEquipmentPanel.setBounds(287, 139, 238, 141);
		main.add(totalEquipmentPanel);
		totalEquipmentPanel.setLayout(null);

		JLabel lblTotalEquipment = new JLabel("Total Equipment");
		lblTotalEquipment.setForeground(new Color(30, 144, 255));
		lblTotalEquipment.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblTotalEquipment.setBounds(55, 11, 127, 27);
		totalEquipmentPanel.add(lblTotalEquipment);

		JLabel lblTotalEquipment_1 = new JLabel("00");
		lblTotalEquipment_1.setForeground(new Color(30, 144, 255));
		lblTotalEquipment_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 60));
		lblTotalEquipment_1.setBounds(77, 39, 72, 62);
		totalEquipmentPanel.add(lblTotalEquipment_1);

		JPanel totalUsersPanel = new JPanel();
		totalUsersPanel.setBounds(1027, 139, 247, 141);
		main.add(totalUsersPanel);
		totalUsersPanel.setLayout(null);

		JLabel lblTotalUsers = new JLabel("Total Users");
		lblTotalUsers.setForeground(new Color(30, 144, 255));
		lblTotalUsers.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblTotalUsers.setBounds(79, 11, 88, 27);
		totalUsersPanel.add(lblTotalUsers);
		
		JLabel lblTotalEquipment_1_4 = new JLabel("00");
		lblTotalEquipment_1_4.setForeground(new Color(30, 144, 255));
		lblTotalEquipment_1_4.setFont(new Font("Segoe UI Black", Font.PLAIN, 60));
		lblTotalEquipment_1_4.setBounds(87, 39, 72, 62);
		totalUsersPanel.add(lblTotalEquipment_1_4);

		JPanel pendingReturnsPanel = new JPanel();
		pendingReturnsPanel.setBounds(776, 139, 240, 141);
		main.add(pendingReturnsPanel);
		pendingReturnsPanel.setLayout(null);

		JLabel lblPendingReturns = new JLabel("Pending Returns");
		lblPendingReturns.setForeground(new Color(30, 144, 255));
		lblPendingReturns.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblPendingReturns.setBounds(59, 11, 121, 27);
		pendingReturnsPanel.add(lblPendingReturns);
		
		JLabel lblTotalEquipment_1_3 = new JLabel("00");
		lblTotalEquipment_1_3.setForeground(new Color(30, 144, 255));
		lblTotalEquipment_1_3.setFont(new Font("Segoe UI Black", Font.PLAIN, 60));
		lblTotalEquipment_1_3.setBounds(84, 39, 72, 62);
		pendingReturnsPanel.add(lblTotalEquipment_1_3);

		JPanel currentlyBorrowedPanel = new JPanel();
		currentlyBorrowedPanel.setBounds(532, 139, 234, 141);
		main.add(currentlyBorrowedPanel);
		currentlyBorrowedPanel.setLayout(null);

		JLabel lblCurrentlyBorrowed = new JLabel("Currently Borrowed");
		lblCurrentlyBorrowed.setForeground(new Color(30, 144, 255));
		lblCurrentlyBorrowed.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblCurrentlyBorrowed.setBounds(43, 11, 148, 27);
		currentlyBorrowedPanel.add(lblCurrentlyBorrowed);
		
		JLabel lblTotalEquipment_1_1 = new JLabel("00");
		lblTotalEquipment_1_1.setForeground(new Color(30, 144, 255));
		lblTotalEquipment_1_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 60));
		lblTotalEquipment_1_1.setBounds(81, 39, 72, 62);
		currentlyBorrowedPanel.add(lblTotalEquipment_1_1);

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
		GradientPanel sidebar2 = new GradientPanel(topColor2, bottomColor2);
		sidebar2.setBounds(0, 0, 277, 639);
		sidebar2.setLayout(null);
		custodianManagementPanel.add(sidebar2);
		custodianManagementPanel.setBounds(0, 0, 277, 639);

		JLabel spearLogo_1 = new JLabel("S.P.E.A.R");
		spearLogo_1.setForeground(new Color(30, 144, 255));
		spearLogo_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		spearLogo_1.setBounds(0, 11, 250, 64);
		spearLogo_1.setIcon(new ImageIcon(resizedImage));
		sidebar2.add(spearLogo_1);

		JLabel lblNewLabel_1 = new JLabel("Custodian Manager");
		lblNewLabel_1.setForeground(new Color(30, 144, 255));
		lblNewLabel_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblNewLabel_1.setBounds(47, 107, 183, 27);
		sidebar2.add(lblNewLabel_1);

		JSeparator separator_2_2 = new JSeparator();
		separator_2_2.setForeground(new Color(30, 144, 255));
		separator_2_2.setBounds(26, 94, 225, 2);
		sidebar2.add(separator_2_2);
		
		JPanel panel = new JPanel();
		panel.setBounds(25, 147, 225, 110);
		sidebar2.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1_4 = new JLabel("Total Customers");
		lblNewLabel_1_4.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_4.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblNewLabel_1_4.setBounds(36, 11, 153, 27);
		panel.add(lblNewLabel_1_4);
		
		JLabel lblNewLabel_1_4_2 = new JLabel("00");
		lblNewLabel_1_4_2.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_4_2.setFont(new Font("Segoe UI Black", Font.PLAIN, 55));
		lblNewLabel_1_4_2.setBounds(79, 38, 66, 61);
		panel.add(lblNewLabel_1_4_2);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(26, 268, 225, 110);
		sidebar2.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblNewLabel_1_4_1 = new JLabel("Administrators");
		lblNewLabel_1_4_1.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_4_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblNewLabel_1_4_1.setBounds(36, 11, 153, 27);
		panel_2.add(lblNewLabel_1_4_1);
		
		JLabel lblNewLabel_1_4_2_1 = new JLabel("00");
		lblNewLabel_1_4_2_1.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_4_2_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 55));
		lblNewLabel_1_4_2_1.setBounds(79, 38, 66, 61);
		panel_2.add(lblNewLabel_1_4_2_1);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(25, 389, 225, 110);
		sidebar2.add(panel_3);
		panel_3.setLayout(null);
		
		JLabel lblNewLabel_1_4_1_1 = new JLabel("Staff");
		lblNewLabel_1_4_1_1.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_4_1_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblNewLabel_1_4_1_1.setBounds(89, 11, 46, 27);
		panel_3.add(lblNewLabel_1_4_1_1);
		
		JLabel lblNewLabel_1_4_2_2 = new JLabel("00");
		lblNewLabel_1_4_2_2.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_4_2_2.setFont(new Font("Segoe UI Black", Font.PLAIN, 55));
		lblNewLabel_1_4_2_2.setBounds(79, 38, 66, 61);
		panel_3.add(lblNewLabel_1_4_2_2);
		
		ButtonOne btnGoBack = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		
		btnGoBack.setText("Go Back");
		btnGoBack.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnGoBack.setBounds(26, 561, 224, 45);
		sidebar2.add(btnGoBack);
		
		ButtonOne btnViewInventory_1 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		
		btnViewInventory_1.setText("View Inventory");
		btnViewInventory_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnViewInventory_1.setBounds(26, 510, 224, 45);
		sidebar2.add(btnViewInventory_1);
		
		JPanel addCustodianPanel = new JPanel();
		addCustodianPanel.setBounds(287, 0, 237, 617);
		addCustodianPanel.setBackground(new Color(255, 255, 255));
		custodianManagementPanel.add(addCustodianPanel);
		addCustodianPanel.setLayout(null);
		
		JPanel hPanel1 = new JPanel();
		hPanel1.setBounds(0, 0, 316, 47);
		hPanel1.setBackground(new Color(240, 248, 255));
		addCustodianPanel.add(hPanel1);
		hPanel1.setLayout(null);
		
		JLabel lblNewLabel_1_2 = new JLabel("Add Custodian");
		lblNewLabel_1_2.setBackground(new Color(230, 230, 250));
		lblNewLabel_1_2.setBounds(10, 0, 306, 47);
		lblNewLabel_1_2.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_2.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		hPanel1.add(lblNewLabel_1_2);
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblFirstName.setBounds(10, 58, 107, 25);
		addCustodianPanel.add(lblFirstName);
		
		textField = new JTextField();
		textField.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		textField.setBounds(10, 86, 215, 25);
		addCustodianPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblLastName.setBounds(10, 122, 107, 25);
		addCustodianPanel.add(lblLastName);
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		textField_1.setColumns(10);
		textField_1.setBounds(10, 150, 215, 25);
		addCustodianPanel.add(textField_1);
		
		JLabel lblPosition = new JLabel("Position");
		lblPosition.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblPosition.setBounds(10, 186, 107, 25);
		addCustodianPanel.add(lblPosition);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(10, 211, 215, 25);
		addCustodianPanel.add(comboBox);
		
		JLabel lblDepartment = new JLabel("Department");
		lblDepartment.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblDepartment.setBounds(10, 247, 107, 25);
		addCustodianPanel.add(lblDepartment);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(10, 272, 215, 25);
		addCustodianPanel.add(comboBox_1);
		
		JLabel lblContactnumber = new JLabel("ContactNumber");
		lblContactnumber.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblContactnumber.setBounds(10, 308, 107, 25);
		addCustodianPanel.add(lblContactnumber);
		
		textField_2 = new JTextField();
		textField_2.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		textField_2.setColumns(10);
		textField_2.setBounds(10, 336, 215, 25);
		addCustodianPanel.add(textField_2);
		
		JLabel lblNewLabel_1_3 = new JLabel("Create Login Account");
		lblNewLabel_1_3.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_3.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblNewLabel_1_3.setBounds(51, 368, 135, 27);
		addCustodianPanel.add(lblNewLabel_1_3);
		
		textField_3 = new JTextField();
		textField_3.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		textField_3.setColumns(10);
		textField_3.setBounds(10, 420, 215, 25);
		addCustodianPanel.add(textField_3);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblUsername.setBounds(10, 392, 107, 25);
		addCustodianPanel.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblPassword.setBounds(9, 448, 107, 25);
		addCustodianPanel.add(lblPassword);
		
		textField_4 = new JTextField();
		textField_4.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		textField_4.setColumns(10);
		textField_4.setBounds(9, 476, 215, 25);
		addCustodianPanel.add(textField_4);
		
		ButtonOne btnAddCustodian = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnAddCustodian.setText("Add");
		btnAddCustodian.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnAddCustodian.setBounds(9, 510, 105, 45);
		addCustodianPanel.add(btnAddCustodian);
		
		ButtonOne btnClear = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnClear.setText("Edit");
		btnClear.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnClear.setBounds(10, 561, 105, 45);
		addCustodianPanel.add(btnClear);
		
		ButtonOne btnDeleteCustodian = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnDeleteCustodian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnDeleteCustodian.setText("Delete");
		btnDeleteCustodian.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnDeleteCustodian.setBounds(118, 512, 105, 45);
		addCustodianPanel.add(btnDeleteCustodian);
		
		ButtonOne btnClear_2 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnClear_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnClear_2.setText("Clear");
		btnClear_2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnClear_2.setBounds(118, 561, 105, 45);
		addCustodianPanel.add(btnClear_2);
		
		JSeparator separator_2_2_2 = new JSeparator();
		separator_2_2_2.setForeground(new Color(30, 144, 255));
		separator_2_2_2.setBounds(11, 387, 215, 2);
		
		JPanel custodianRecordsPanel = new JPanel();
		custodianRecordsPanel.setBackground(new Color(255, 255, 255));
		custodianRecordsPanel.setBounds(534, 0, 740, 617);
		custodianManagementPanel.add(custodianRecordsPanel);
		custodianRecordsPanel.setLayout(null);
		
		JPanel hPanel1_1 = new JPanel();
		hPanel1_1.setLayout(null);
		hPanel1_1.setBackground(new Color(240, 248, 255));
		hPanel1_1.setBounds(0, 0, 740, 47);
		custodianRecordsPanel.add(hPanel1_1);
		
		JLabel lblNewLabel_1_2_1 = new JLabel("Custodian Records");
		lblNewLabel_1_2_1.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_2_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		lblNewLabel_1_2_1.setBackground(new Color(230, 230, 250));
		lblNewLabel_1_2_1.setBounds(10, 0, 306, 47);
		hPanel1_1.add(lblNewLabel_1_2_1);
		
		JPanel hPanel1_1_1 = new JPanel();
		hPanel1_1_1.setLayout(null);
		hPanel1_1_1.setBackground(new Color(255, 255, 255));
		hPanel1_1_1.setBounds(0, 45, 740, 72);
		custodianRecordsPanel.add(hPanel1_1_1);
		
		textField_5 = new JTextField();
		textField_5.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		textField_5.setColumns(10);
		textField_5.setBounds(10, 36, 227, 25);
		hPanel1_1_1.add(textField_5);
		
		JButton btnNewButton = new JButton("Q");
		btnNewButton.setBounds(247, 38, 51, 23);
		hPanel1_1_1.add(btnNewButton);
		
		JComboBox comboBox_1_1 = new JComboBox();
		comboBox_1_1.setBounds(545, 37, 185, 25);
		hPanel1_1_1.add(comboBox_1_1);
		
		JLabel lblSearch = new JLabel("Search");
		lblSearch.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblSearch.setBounds(10, 11, 107, 25);
		hPanel1_1_1.add(lblSearch);
		
		JLabel lblAllPositions = new JLabel("All Positions");
		lblAllPositions.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblAllPositions.setBounds(545, 11, 107, 25);
		hPanel1_1_1.add(lblAllPositions);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(613, 224, 661, 393);
		custodianManagementPanel.add(scrollPane);
		
		table = new JTable();
		table.setBounds(613, 224, 661, 393);
		custodianManagementPanel.add(table);
		
		JPanel inventoryManagementPanel = new JPanel();
		inventoryManagementPanel.setLayout(null);
		cardContainer.add(inventoryManagementPanel, "inventoryManagementPanel");
		
		GradientPanel sidebar2_2 = new GradientPanel(new Color(212, 255, 253), new Color(247, 214, 190));
		sidebar2_2.setLayout(null);
		sidebar2_2.setBounds(0, 0, 277, 639);
		inventoryManagementPanel.add(sidebar2_2);
		
		JLabel spearLogo_1_2 = new JLabel("S.P.E.A.R");
		spearLogo_1_2.setForeground(new Color(30, 144, 255));
		spearLogo_1_2.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		spearLogo_1_2.setBounds(0, 11, 250, 64);
		spearLogo_1_2.setIcon(new ImageIcon(resizedImage));
		sidebar2_2.add(spearLogo_1_2);
		
		JLabel lblNewLabel_1_5 = new JLabel("Custodian Manager");
		lblNewLabel_1_5.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_5.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblNewLabel_1_5.setBounds(47, 107, 183, 27);
		sidebar2_2.add(lblNewLabel_1_5);
		
		JSeparator separator_2_2_3 = new JSeparator();
		separator_2_2_3.setForeground(new Color(30, 144, 255));
		separator_2_2_3.setBounds(26, 94, 225, 2);
		sidebar2_2.add(separator_2_2_3);
		
		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBounds(25, 147, 225, 80);
		sidebar2_2.add(panel_4);
		
		JLabel lblNewLabel_1_4_3 = new JLabel("Total Equipment");
		lblNewLabel_1_4_3.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_4_3.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblNewLabel_1_4_3.setBounds(51, 0, 122, 34);
		panel_4.add(lblNewLabel_1_4_3);
		
		JLabel lblNewLabel_1_4_2_3 = new JLabel("00");
		lblNewLabel_1_4_2_3.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_4_2_3.setFont(new Font("Segoe UI Black", Font.PLAIN, 45));
		lblNewLabel_1_4_2_3.setBounds(85, 25, 54, 50);
		panel_4.add(lblNewLabel_1_4_2_3);
		
		ButtonOne btnGoBack_1 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		
		btnGoBack_1.setText("Go Back");
		btnGoBack_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnGoBack_1.setBounds(26, 561, 224, 45);
		sidebar2_2.add(btnGoBack_1);
		
		ButtonOne btnViewInventory_1_1 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		
		btnViewInventory_1_1.setText("View Custodian");
		btnViewInventory_1_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnViewInventory_1_1.setBounds(26, 510, 224, 45);
		sidebar2_2.add(btnViewInventory_1_1);
		
		JPanel panel_4_1 = new JPanel();
		panel_4_1.setLayout(null);
		panel_4_1.setBounds(26, 238, 225, 80);
		sidebar2_2.add(panel_4_1);
		
		JLabel lblNewLabel_1_4_3_1 = new JLabel("Available");
		lblNewLabel_1_4_3_1.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_4_3_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblNewLabel_1_4_3_1.setBounds(78, 0, 69, 34);
		panel_4_1.add(lblNewLabel_1_4_3_1);
		
		JLabel lblNewLabel_1_4_2_3_1 = new JLabel("00");
		lblNewLabel_1_4_2_3_1.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_4_2_3_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 45));
		lblNewLabel_1_4_2_3_1.setBounds(85, 25, 54, 50);
		panel_4_1.add(lblNewLabel_1_4_2_3_1);
		
		JPanel panel_4_2 = new JPanel();
		panel_4_2.setLayout(null);
		panel_4_2.setBounds(26, 329, 225, 80);
		sidebar2_2.add(panel_4_2);
		
		JLabel lblNewLabel_1_4_3_2 = new JLabel("Under Maintenance");
		lblNewLabel_1_4_3_2.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_4_3_2.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblNewLabel_1_4_3_2.setBounds(42, 0, 141, 34);
		panel_4_2.add(lblNewLabel_1_4_3_2);
		
		JLabel lblNewLabel_1_4_2_3_2 = new JLabel("00");
		lblNewLabel_1_4_2_3_2.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_4_2_3_2.setFont(new Font("Segoe UI Black", Font.PLAIN, 45));
		lblNewLabel_1_4_2_3_2.setBounds(85, 25, 54, 50);
		panel_4_2.add(lblNewLabel_1_4_2_3_2);
		
		JPanel panel_4_3 = new JPanel();
		panel_4_3.setLayout(null);
		panel_4_3.setBounds(26, 420, 225, 80);
		sidebar2_2.add(panel_4_3);
		
		JLabel lblNewLabel_1_4_3_3 = new JLabel("Borrowed");
		lblNewLabel_1_4_3_3.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_4_3_3.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblNewLabel_1_4_3_3.setBounds(75, 0, 75, 34);
		panel_4_3.add(lblNewLabel_1_4_3_3);
		
		JLabel lblNewLabel_1_4_2_3_3 = new JLabel("00");
		lblNewLabel_1_4_2_3_3.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_4_2_3_3.setFont(new Font("Segoe UI Black", Font.PLAIN, 45));
		lblNewLabel_1_4_2_3_3.setBounds(85, 25, 54, 50);
		panel_4_3.add(lblNewLabel_1_4_2_3_3);
		
		JPanel addCustodianPanel_1 = new JPanel();
		addCustodianPanel_1.setLayout(null);
		addCustodianPanel_1.setBackground(Color.WHITE);
		addCustodianPanel_1.setBounds(287, 347, 987, 270);
		inventoryManagementPanel.add(addCustodianPanel_1);
		
		JPanel hPanel1_2 = new JPanel();
		hPanel1_2.setLayout(null);
		hPanel1_2.setBackground(new Color(240, 248, 255));
		hPanel1_2.setBounds(0, 0, 987, 31);
		addCustodianPanel_1.add(hPanel1_2);
		
		JLabel lblNewLabel_1_2_2 = new JLabel("Add / Edit Equipment");
		lblNewLabel_1_2_2.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_2_2.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblNewLabel_1_2_2.setBackground(new Color(230, 230, 250));
		lblNewLabel_1_2_2.setBounds(10, 0, 158, 31);
		hPanel1_2.add(lblNewLabel_1_2_2);
		
		JLabel lblFirstName_1 = new JLabel("Property Tag*");
		lblFirstName_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblFirstName_1.setBounds(10, 42, 261, 25);
		addCustodianPanel_1.add(lblFirstName_1);
		
		textField_6 = new JTextField();
		textField_6.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		textField_6.setColumns(10);
		textField_6.setBounds(10, 70, 261, 25);
		addCustodianPanel_1.add(textField_6);
		
		JLabel lblLastName_1 = new JLabel("Equipment Name*");
		lblLastName_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblLastName_1.setBounds(10, 98, 261, 25);
		addCustodianPanel_1.add(lblLastName_1);
		
		textField_7 = new JTextField();
		textField_7.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		textField_7.setColumns(10);
		textField_7.setBounds(10, 126, 261, 25);
		addCustodianPanel_1.add(textField_7);
		
		JLabel lblPosition_1 = new JLabel("Category*");
		lblPosition_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblPosition_1.setBounds(10, 157, 261, 25);
		addCustodianPanel_1.add(lblPosition_1);
		
		JComboBox comboBox_1_2 = new JComboBox();
		comboBox_1_2.setBounds(10, 185, 261, 25);
		addCustodianPanel_1.add(comboBox_1_2);
		
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
		
		JLabel lblPosition_1_1 = new JLabel("Room / Location");
		lblPosition_1_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblPosition_1_1.setBounds(281, 157, 250, 25);
		addCustodianPanel_1.add(lblPosition_1_1);
		
		JComboBox comboBox_1_2_1 = new JComboBox();
		comboBox_1_2_1.setBounds(281, 185, 250, 25);
		addCustodianPanel_1.add(comboBox_1_2_1);
		
		textField_8 = new JTextField();
		textField_8.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		textField_8.setColumns(10);
		textField_8.setBounds(281, 126, 250, 25);
		addCustodianPanel_1.add(textField_8);
		
		JLabel lblLastName_1_1 = new JLabel("Serial Number");
		lblLastName_1_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblLastName_1_1.setBounds(281, 98, 250, 25);
		addCustodianPanel_1.add(lblLastName_1_1);
		
		textField_9 = new JTextField();
		textField_9.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		textField_9.setColumns(10);
		textField_9.setBounds(281, 70, 250, 25);
		addCustodianPanel_1.add(textField_9);
		
		JLabel lblFirstName_1_1 = new JLabel("Brand / Model");
		lblFirstName_1_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblFirstName_1_1.setBounds(281, 42, 250, 25);
		addCustodianPanel_1.add(lblFirstName_1_1);
		
		JLabel lblPosition_1_1_1 = new JLabel("Condition");
		lblPosition_1_1_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblPosition_1_1_1.setBounds(541, 157, 213, 25);
		addCustodianPanel_1.add(lblPosition_1_1_1);
		
		JComboBox comboBox_1_2_1_1 = new JComboBox();
		comboBox_1_2_1_1.setBounds(541, 185, 213, 25);
		addCustodianPanel_1.add(comboBox_1_2_1_1);
		
		textField_10 = new JTextField();
		textField_10.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		textField_10.setColumns(10);
		textField_10.setBounds(541, 126, 213, 25);
		addCustodianPanel_1.add(textField_10);
		
		JLabel lblLastName_1_1_1 = new JLabel("Purchase Cost");
		lblLastName_1_1_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblLastName_1_1_1.setBounds(541, 98, 213, 25);
		addCustodianPanel_1.add(lblLastName_1_1_1);
		
		textField_12 = new JTextField();
		textField_12.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		textField_12.setColumns(10);
		textField_12.setBounds(541, 70, 213, 25);
		addCustodianPanel_1.add(textField_12);
		
		JLabel lblFirstName_1_1_1 = new JLabel("Purchase Date");
		lblFirstName_1_1_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblFirstName_1_1_1.setBounds(541, 42, 213, 25);
		addCustodianPanel_1.add(lblFirstName_1_1_1);
		
		JLabel lblPosition_1_1_1_1 = new JLabel("Availability");
		lblPosition_1_1_1_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblPosition_1_1_1_1.setBounds(764, 42, 213, 25);
		addCustodianPanel_1.add(lblPosition_1_1_1_1);
		
		JComboBox comboBox_1_2_1_1_1 = new JComboBox();
		comboBox_1_2_1_1_1.setBounds(764, 70, 213, 25);
		addCustodianPanel_1.add(comboBox_1_2_1_1_1);
		
		JLabel lblFirstName_1_1_1_1 = new JLabel("Description");
		lblFirstName_1_1_1_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblFirstName_1_1_1_1.setBounds(764, 106, 213, 25);
		addCustodianPanel_1.add(lblFirstName_1_1_1_1);
		
		textField_13 = new JTextField();
		textField_13.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		textField_13.setColumns(10);
		textField_13.setBounds(764, 129, 213, 81);
		addCustodianPanel_1.add(textField_13);
		
		ButtonOne btnClear_1_1 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnClear_1_1.setText("Add");
		btnClear_1_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnClear_1_1.setBounds(10, 221, 89, 45);
		addCustodianPanel_1.add(btnClear_1_1);
		
		ButtonOne btnClear_1_1_1 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnClear_1_1_1.setText("Delete");
		btnClear_1_1_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnClear_1_1_1.setBounds(109, 221, 89, 45);
		addCustodianPanel_1.add(btnClear_1_1_1);
		
		ButtonOne btnClear_1_1_1_1 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnClear_1_1_1_1.setText("View");
		btnClear_1_1_1_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnClear_1_1_1_1.setBounds(208, 221, 89, 45);
		addCustodianPanel_1.add(btnClear_1_1_1_1);
		
		ButtonOne btnClear_1_1_1_1_1 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnClear_1_1_1_1_1.setText("Edit");
		btnClear_1_1_1_1_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnClear_1_1_1_1_1.setBounds(307, 221, 89, 45);
		addCustodianPanel_1.add(btnClear_1_1_1_1_1);
		
		JPanel custodianRecordsPanel_1 = new JPanel();
		custodianRecordsPanel_1.setLayout(null);
		custodianRecordsPanel_1.setBackground(Color.WHITE);
		custodianRecordsPanel_1.setBounds(287, 44, 987, 292);
		inventoryManagementPanel.add(custodianRecordsPanel_1);
		
		JPanel hPanel1_1_2 = new JPanel();
		hPanel1_1_2.setLayout(null);
		hPanel1_1_2.setBackground(new Color(240, 248, 255));
		hPanel1_1_2.setBounds(0, 0, 987, 47);
		custodianRecordsPanel_1.add(hPanel1_1_2);
		
		JLabel lblNewLabel_1_2_1_1 = new JLabel("Custodian Records");
		lblNewLabel_1_2_1_1.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_2_1_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		lblNewLabel_1_2_1_1.setBackground(new Color(230, 230, 250));
		lblNewLabel_1_2_1_1.setBounds(10, 0, 306, 47);
		hPanel1_1_2.add(lblNewLabel_1_2_1_1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setBounds(287, 0, 987, 36);
		inventoryManagementPanel.add(panel_1);
		panel_1.setLayout(null);
		
		textField_11 = new JTextField();
		textField_11.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		textField_11.setColumns(10);
		textField_11.setBounds(57, 5, 227, 25);
		panel_1.add(textField_11);
		
		JButton btnNewButton_1 = new JButton("Q");
		btnNewButton_1.setBounds(289, 6, 51, 23);
		panel_1.add(btnNewButton_1);
		
		JLabel lblSearch_1 = new JLabel("Search");
		lblSearch_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblSearch_1.setBounds(10, 5, 107, 25);
		panel_1.add(lblSearch_1);
		
		JLabel lblAllPositions_1 = new JLabel("Category");
		lblAllPositions_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblAllPositions_1.setBounds(350, 5, 56, 25);
		panel_1.add(lblAllPositions_1);
		
		JComboBox comboBox_1_1_1 = new JComboBox();
		comboBox_1_1_1.setBounds(416, 5, 137, 25);
		panel_1.add(comboBox_1_1_1);
		
		JComboBox comboBox_1_1_1_1 = new JComboBox();
		comboBox_1_1_1_1.setBounds(629, 7, 117, 25);
		panel_1.add(comboBox_1_1_1_1);
		
		JLabel lblAllPositions_1_1 = new JLabel("Condition");
		lblAllPositions_1_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblAllPositions_1_1.setBounds(563, 5, 68, 25);
		panel_1.add(lblAllPositions_1_1);
		
		JLabel lblAllPositions_1_1_1 = new JLabel("Availability");
		lblAllPositions_1_1_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblAllPositions_1_1_1.setBounds(756, 5, 76, 25);
		panel_1.add(lblAllPositions_1_1_1);
		
		JComboBox comboBox_1_1_1_1_1 = new JComboBox();
		comboBox_1_1_1_1_1.setBounds(842, 5, 135, 25);
		panel_1.add(comboBox_1_1_1_1_1);

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
		
		btnViewInventory_1.addActionListener(new ActionListener() {
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

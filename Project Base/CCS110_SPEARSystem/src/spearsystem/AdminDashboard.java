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

public class AdminDashboard {

	final String[] RECORDSTATUS = { "Returned", "Borrowed" };

	final String[] EQUIPCONDITIONS = { "Good", "Worn", "Damaged" };
	final String[] EQUIPAVAILABILITY = { "Available", "Borrowed", "Maintenance" };

	private JPanel adminDashboard;

	private CardLayout card = new CardLayout(0, 0);

	private JPanel cardContainer;

	private DefaultComboBoxModel<String> cmbCategoryModel;

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
		ButtonOne button = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		button.setBounds(24, 145, 225, 55);
		sidebar.add(button);
		button.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		button.setText("View Custodian");
		// -

		// Custom Button 3
		ButtonOne button3 = new ButtonOne(new Color(230, 126, 34), new Color(211, 84, 0));

		button3.setBounds(24, 266, 225, 55);
		sidebar.add(button3);
		button3.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		button3.setText("View Inventory");

		ImageIcon originalIcon = new ImageIcon(LoginScreen.class.getResource("/ui/spearKen.png"));
		Image resizedImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);

		JLabel spearLogo = new JLabel("S.P.E.A.R");
		spearLogo.setForeground(new Color(30, 144, 255));
		spearLogo.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		spearLogo.setIcon(new ImageIcon(resizedImage));
		spearLogo.setBounds(0, 11, 249, 64);
		sidebar.add(spearLogo);

		JLabel lblNewLabel = new JLabel("Custodian Management");
		lblNewLabel.setForeground(new Color(30, 144, 255));
		lblNewLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblNewLabel.setBounds(24, 107, 225, 27);
		sidebar.add(lblNewLabel);

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

		JLabel lblWhatHere = new JLabel("Inventory Management");
		lblWhatHere.setForeground(new Color(30, 144, 255));
		lblWhatHere.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblWhatHere.setBounds(24, 224, 225, 27);
		sidebar.add(lblWhatHere);

		ButtonOne whiteOutline = new ButtonOne(new Color(18, 145, 96), new Color(0, 0, 0));
		whiteOutline.setEnabled(false);
		whiteOutline.setText("New Borrow Record");
		whiteOutline.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		whiteOutline.setBounds(20, 141, 233, 64);
		sidebar.add(whiteOutline);

		ButtonOne whiteOutline_1 = new ButtonOne(new Color(180, 88, 15), Color.BLACK);
		whiteOutline_1.setText("New Borrow Record");
		whiteOutline_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		whiteOutline_1.setEnabled(false);
		whiteOutline_1.setBounds(20, 262, 233, 64);
		sidebar.add(whiteOutline_1);

		JSeparator separator_2_1_1 = new JSeparator();
		separator_2_1_1.setForeground(new Color(30, 144, 255));
		separator_2_1_1.setBounds(25, 332, 225, 2);
		sidebar.add(separator_2_1_1);

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

		JLabel lblTotalEquipment_1 = new JLabel("000");
		lblTotalEquipment_1.setForeground(new Color(30, 144, 255));
		lblTotalEquipment_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 50));
		lblTotalEquipment_1.setBounds(75, 48, 127, 62);
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

		JLabel lblTotalEquipment_1_1_1_1 = new JLabel("000");
		lblTotalEquipment_1_1_1_1.setForeground(new Color(30, 144, 255));
		lblTotalEquipment_1_1_1_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 50));
		lblTotalEquipment_1_1_1_1.setBounds(75, 49, 127, 62);
		totalUsersPanel.add(lblTotalEquipment_1_1_1_1);

		JPanel pendingReturnsPanel = new JPanel();
		pendingReturnsPanel.setBounds(776, 139, 240, 141);
		main.add(pendingReturnsPanel);
		pendingReturnsPanel.setLayout(null);

		JLabel lblPendingReturns = new JLabel("Pending Returns");
		lblPendingReturns.setForeground(new Color(30, 144, 255));
		lblPendingReturns.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblPendingReturns.setBounds(59, 11, 121, 27);
		pendingReturnsPanel.add(lblPendingReturns);

		JLabel lblTotalEquipment_1_1_1 = new JLabel("000");
		lblTotalEquipment_1_1_1.setForeground(new Color(30, 144, 255));
		lblTotalEquipment_1_1_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 50));
		lblTotalEquipment_1_1_1.setBounds(69, 50, 127, 62);
		pendingReturnsPanel.add(lblTotalEquipment_1_1_1);

		JPanel currentlyBorrowedPanel = new JPanel();
		currentlyBorrowedPanel.setBounds(532, 139, 234, 141);
		main.add(currentlyBorrowedPanel);
		currentlyBorrowedPanel.setLayout(null);

		JLabel lblCurrentlyBorrowed = new JLabel("Currently Borrowed");
		lblCurrentlyBorrowed.setForeground(new Color(30, 144, 255));
		lblCurrentlyBorrowed.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		lblCurrentlyBorrowed.setBounds(43, 11, 148, 27);
		currentlyBorrowedPanel.add(lblCurrentlyBorrowed);

		JLabel lblTotalEquipment_1_1 = new JLabel("000");
		lblTotalEquipment_1_1.setForeground(new Color(30, 144, 255));
		lblTotalEquipment_1_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 50));
		lblTotalEquipment_1_1.setBounds(68, 49, 127, 62);
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
		custodianManagementPanel.setLayout(null);

		// ==========================================
		// Gradient test
		// ==========================================
		Color topColor2 = new Color(212, 255, 253);
		Color bottomColor2 = new Color(247, 214, 190);
		GradientPanel sidebar2 = new GradientPanel(topColor2, bottomColor2);

		sidebar2.setBounds(0, 0, 277, 639);
		sidebar2.setLayout(null);
		custodianManagementPanel.add(sidebar2);

		JLabel spearLogo_1 = new JLabel("S.P.E.A.R");
		spearLogo_1.setForeground(new Color(30, 144, 255));
		spearLogo_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		spearLogo_1.setBounds(14, 11, 249, 64);
		spearLogo_1.setIcon(new ImageIcon(resizedImage));
		sidebar2.add(spearLogo_1);

		JLabel lblNewLabel_1 = new JLabel("Custodian Management");
		lblNewLabel_1.setForeground(new Color(30, 144, 255));
		lblNewLabel_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblNewLabel_1.setBounds(26, 107, 225, 27);
		sidebar2.add(lblNewLabel_1);

		JSeparator separator_2_2 = new JSeparator();
		separator_2_2.setForeground(new Color(30, 144, 255));
		separator_2_2.setBounds(26, 94, 225, 2);
		sidebar2.add(separator_2_2);

		ButtonOne add = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		add.setText("Add");
		add.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		add.setBounds(26, 145, 100, 55);
		sidebar2.add(add);

		ButtonOne view = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		view.setText("View");
		view.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		view.setBounds(151, 145, 100, 55);
		sidebar2.add(view);

		ButtonOne btnDelete = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnDelete.setText("Delete");
		btnDelete.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnDelete.setBounds(26, 219, 100, 55);
		sidebar2.add(btnDelete);

		ButtonOne btnEdit = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnEdit.setText("Edit");
		btnEdit.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnEdit.setBounds(151, 219, 100, 55);
		sidebar2.add(btnEdit);

		JPanel panel = new JPanel();
		panel.setBounds(275, 0, 1009, 617);
		custodianManagementPanel.add(panel);

		JPanel InventoryManagementPanel = new JPanel();
		cardContainer.add(InventoryManagementPanel, "InventoryManagementPanel");
		InventoryManagementPanel.setLayout(null);

		GradientPanel sidebar2_1 = new GradientPanel(new Color(212, 255, 253), new Color(247, 214, 190));
		sidebar2_1.setLayout(null);
		sidebar2_1.setBounds(0, 0, 277, 639);
		InventoryManagementPanel.add(sidebar2_1);

		JLabel spearLogo_1_1 = new JLabel("S.P.E.A.R");
		spearLogo_1_1.setForeground(new Color(30, 144, 255));
		spearLogo_1_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		spearLogo_1_1.setBounds(14, 11, 249, 64);
		sidebar2_1.add(spearLogo_1_1);

		JLabel lblNewLabel_1_1 = new JLabel("Inventory Management");
		lblNewLabel_1_1.setForeground(new Color(30, 144, 255));
		lblNewLabel_1_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblNewLabel_1_1.setBounds(26, 107, 225, 27);
		sidebar2_1.add(lblNewLabel_1_1);

		JSeparator separator_2_2_1 = new JSeparator();
		separator_2_2_1.setForeground(new Color(30, 144, 255));
		separator_2_2_1.setBounds(26, 94, 225, 2);
		sidebar2_1.add(separator_2_2_1);

		ButtonOne add_1 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		add_1.setText("Add");
		add_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		add_1.setBounds(26, 145, 100, 55);
		sidebar2_1.add(add_1);

		ButtonOne view_1 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		view_1.setText("View");
		view_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		view_1.setBounds(151, 145, 100, 55);
		sidebar2_1.add(view_1);

		ButtonOne btnDelete_1 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnDelete_1.setText("Delete");
		btnDelete_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnDelete_1.setBounds(26, 219, 100, 55);
		sidebar2_1.add(btnDelete_1);

		ButtonOne btnEdit_1 = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnEdit_1.setText("Edit");
		btnEdit_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnEdit_1.setBounds(151, 219, 100, 55);
		sidebar2_1.add(btnEdit_1);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(275, 0, 1009, 628);
		InventoryManagementPanel.add(panel_1);

		JLabel lblAdmin = new JLabel("Admin Dashboard");
		lblAdmin.setIcon(new ImageIcon(resizedImage2));
		lblAdmin.setForeground(new Color(30, 144, 255));
		lblAdmin.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		lblAdmin.setBounds(10, 0, 396, 64);
		adminDashboard.add(lblAdmin);

		button.addActionListener(e -> {
			card.show(cardContainer, "custodianManagement");
			lblAdmin.setText("Custodian Manager");
		});

		button3.addActionListener(e -> {
			card.show(cardContainer, "InventoryManagementPanel");
			lblAdmin.setText("Inventory Manager");
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

	// ---------------------------------------------------------------------------------------------------------------------------------
	// UNIVERSAL HELPER METHODS
	// ---------------------------------------------------------------------------------------------------------------------------------
}

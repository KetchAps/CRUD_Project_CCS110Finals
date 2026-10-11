package spearsystem;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import kenUI.ButtonOne;
import kenUI.GradientPanel;
import normie.util.*;
import spearsystem.CustodianDatabaseHandler.BorrowRecord;
import spearsystem.CustodianDatabaseHandler.BorrowerList;
import spearsystem.CustodianDatabaseHandler.BorrowerProfile;
import spearsystem.CustodianDatabaseHandler.EquipmentDetails;
import spearsystem.CustodianDatabaseHandler.MaintenanceReport;


public class CustodianDashboard {
	
	final String[] RECORDSTATUS = {"Returned", "Borrowed"};
	final String[] REPORTSTATUS = {"In Progress", "Resolved"};
	
	final String[] BORROWERTYPES = {"Student", "Staff"};
	final String[] EQUIPCONDITIONS = {"Good", "Worn", "Damaged"};
	final String[] EQUIPAVAILABILITY = {"Available", "Borrowed", "Maintenance"}; 
	private Integer[] borrowerIDs;
	
	private EquipmentDetails loadedEquip;
	private BorrowRecord loadedRecord;
	private MaintenanceReport loadedReport;
	private BorrowerProfile loadedProfile;
	
	private EmailValidator mailValidator = new EmailValidator();
	
	private int activeScreen = 0;
	
	
	private JPanel custodianDashboard;
	private MainClient parent;
	private CustodianHandler backend;
	
	private CardLayout card = new CardLayout(0,0);
	private CardLayout formCard = new CardLayout(0,0);
	private CardLayout borrowManagingCard = new CardLayout(0, 0);
	private JComboBox<String> cmbCondition;
	private JComboBox<String> cmbCategory;
	private JComboBox<String> cmbAvailability;
	private JComboBox<String> cmbStatus;	
	private JComboBox<String> cmbBorrowerName;
	private JComboBox<String> cmbCondOnReturn;
	private JComboBox<String> cmbStatusMnt;
	private JComboBox<String> cmbNewBorrowType;
	private JComboBox<String> cmbBorrowType;
	private JComboBox<String> cmbMngBorrowerType;
	
	private JPanel cardContainer;
	private JPanel formCreator; // JPanel of the Forms
	private JPanel borrowerManager;

	private DefaultComboBoxModel<String> cmbCategoryModel;
	private DefaultComboBoxModel<String> cmbBorrowerModel;
	
	private JTextField searchBar;
	private JTable searchResult;
	private DefaultTableModel query;
	
	private JTable tblBorrowPreview;
	private DefaultTableModel borrowPreview;
	
	private JTable tblMaintenancePreview;
	private DefaultTableModel maintenancePreview;
	
	private JTable tblborrowRecord;
	private JTable tblUpdReport;
	
	private JTable tblBorrowers;
	private DefaultTableModel registeredUserList;
	
	
	private DefaultTableModel borrowList;
	private DefaultTableModel reportList;
	
	
	private JTextArea txtAMntIssue;
	private JTextArea txtAMntUpdIssueReported;
	private JTextArea txtAMntUpdActionTaken;
	private JTextField txtBrwEquipID;
	private JTextField txtBrwEquipName;
	private JTextField txtBrwEquipTag;
	private JTextField txtBrwReturnByDate;
	private JTextField txtDateBorrowed;
	private JTextField txtBrwEquipCondition;

	private JTextField txtMntEquipID;
	private JTextField txtMntEquipName;
	private JTextField txtMntEquipCondition;
	private JTextField txtMntEquipTag;
	private JTextField txtMntDateReported;
	private JTextField txtbrwSearchBar;
	
	private JTextField txtTransactionID;
	private JTextField txtUpdBorrowerID;
	private JTextField txtUpdBorrowedEquip;
	private JTextField txtBorrowDate;
	private JTextField txtUpdCustodian;
	private JTextField txtUpdCondOnBorrow;
	private JTextField txtExpectedReturnDate;
	private JTextField txtDateReturned;
	private JTextField txtBrwEquipAvail;
	
	private JTextField txtUpdMntSearch;
	private JTextField txtMntCost;
	private JTextField txtMntUpdTransactionID;
	private JTextField txtMntUpdReport;
	private JTextField txtMntUpdDate;
	private JTextField txtMntUpdEquipID;

	private JTextField textField;
	private JTextField txtNewBorrowNameF;
	private JTextField txtNewBorrowNameL;
	private JTextField txtNewBorrowID;
	private JTextField txtNewBorrowDept;
	private JTextField txtNewBorrowContact;
	private JTextField txtNewBorrowEmail;
	private JTextField txtMngBorrowerEmail;
	private JTextField txtMngBorrowerContactNo;
	private JTextField txtMngBorrowerDept;
	private JTextField txtMngBorrowerID;
	private JTextField txtMngBorrowerNameL;
	private JTextField txtMngBorrowerNameF;
	private JTextField txtMngBorrowerTransaction;
	
	private JLabel lblCreationTitle;
	private JLabel lblBorrowerTitle;
	
	private TableRowSorter<DefaultTableModel> inventorySorter = new TableRowSorter<>(query);
	private RowFilter<DefaultTableModel, Integer> inventoryRF = new RowFilter<>() {
		@Override
		@SuppressWarnings("rawtypes")
		public boolean include( Entry entry) {
			
			String searchFilter = searchBar.getText();
			String categoryFilter = cmbCategory.getSelectedItem().toString();
			String conditionFilter = cmbCondition.getSelectedItem().toString();
			String availabilityFilter = cmbAvailability.getSelectedItem().toString();
			
			// searchBar
			String id = entry.getStringValue(0);
			String tag = entry.getStringValue(1);
			String name = entry.getStringValue(2);
			
			if(!(searchFilter.isBlank() || searchFilter.isEmpty()) &&
				!(id.contains(searchFilter) || tag.contains(searchFilter) || name.contains(searchFilter))) return false;
			
			if(!categoryFilter.equals("[Category]") && !entry.getStringValue(3).equals(categoryFilter)) return false;
			
			if(!conditionFilter.equals("[Condition]") && !entry.getStringValue(11).equals(conditionFilter)) return false;
			
			if(!availabilityFilter.equals("[Availability]") && !entry.getStringValue(12).equals(availabilityFilter)) return false;
			return true;
		}
		
		
	};
	
	private TableRowSorter<DefaultTableModel> borrowRecordSorter = new TableRowSorter<>(borrowList);
	private RowFilter<DefaultTableModel, Integer> borrowRecordRF = new RowFilter<>() {
		@Override
		@SuppressWarnings("rawtypes")
		public boolean include( Entry entry) {
			
			String searchFilter = txtbrwSearchBar.getText();
			String statusFilter = cmbStatus.getSelectedItem().toString();
			
			// searchBar
			String id = entry.getStringValue(0);
//			String tag = entry.getStringValue(1);
//			String name = entry.getStringValue(2);
			
			if(!(searchFilter.isBlank() || searchFilter.isEmpty()) &&
				!(id.contains(searchFilter))) return false;
			
			if(!statusFilter.equals("[Status]") && !entry.getStringValue(9).equals(statusFilter)) return false;
			return true;
		}
		
		
	};
	
	private TableRowSorter<DefaultTableModel> maintenanceSorter = new TableRowSorter<>(reportList);
	private RowFilter<DefaultTableModel, Integer> maintenanceRF = new RowFilter<>() {
		@Override
		@SuppressWarnings("rawtypes")
		public boolean include( Entry entry) {
			
			String searchFilter = txtUpdMntSearch.getText();
			String statusFilter = cmbStatusMnt.getSelectedItem().toString();
			
			// searchBar
			String id = entry.getStringValue(0);
			if(!(searchFilter.isBlank() || searchFilter.isEmpty()) &&
				!(id.contains(searchFilter))) return false;
			
			if(!statusFilter.equals("[Availability]") && !entry.getStringValue(7).equals(statusFilter)) return false;
			return true;
		}
		
		
	};
	
	
	
	KeyAdapter numericalOnly = new KeyAdapter() {
		@Override
		public void keyTyped(KeyEvent e) {
		if(!Character.isDigit(e.getKeyChar())) e.consume();
		}
	};
	
	
	DocumentFilter moneyInput = new DocumentFilter() {
        public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet a) throws BadLocationException {

            String txt = fb.getDocument().getText(0, fb.getDocument().getLength());
            txt += str;
            if ((fb.getDocument().getLength() + str.length() - length) <= 20 && txt.matches("^[0-9]+[.]?[0-9]{0,10}$")) {
                super.replace(fb, offset, length, str, a);
            }
        }

        public void insertString(FilterBypass fb, int offset, String str, AttributeSet a) throws BadLocationException {

            String txt = fb.getDocument().getText(0, fb.getDocument().getLength());
            txt += str;
            if ((fb.getDocument().getLength() + str.length()) <= 20 && txt.matches("^[0-9]+[.]?[0-9]{0,10}$")) {
                super.insertString(fb, offset, str, a);
            }
        }
    };
  
    
	
	
	
	
	/**
	 * Create the application.
	 */
	public CustodianDashboard(MainClient parent, CustodianHandler backend) {
		this.parent = parent;
		this.backend = backend;
		initialize();
	}

	
	private void initialize() {
		
		backend = new CustodianHandler();
		
		Font lblFont = new Font("Segoe UI Semibold", Font.PLAIN, 11);
		Font hintFont = new Font("Segoe UI", Font.PLAIN, 10);
		
		custodianDashboard = new JPanel();
		custodianDashboard.setLayout(null);
		custodianDashboard.setBounds(10, 54, 1283, 700);
		
		// IMAGE
		ImageIcon custodianIcon = new ImageIcon(LoginScreen.class.getResource("/ui/custodian2.png"));
		Image custodianIconResized = custodianIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		
		JLabel lblCustodian = new JLabel("Custodian Dashboard");
		lblCustodian.setIcon(new ImageIcon(custodianIconResized));
		lblCustodian.setForeground(new Color(0, 153, 255));
		lblCustodian.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblCustodian.setHorizontalAlignment(SwingConstants.LEFT);
		lblCustodian.setBounds(10, 0, 437, 56);
		custodianDashboard.add(lblCustodian);
		
		query = new DefaultTableModel();
		inventorySorter.setRowFilter(null);
		
		cardContainer = new JPanel();
		cardContainer.setBounds(0, 61, 1273, 628);
		custodianDashboard.add(cardContainer);
		cardContainer.setLayout(card);
		
		JPanel borrowFormCreation = new JPanel();
		borrowFormCreation.setLayout(null);

		cmbCategoryModel = new DefaultComboBoxModel<String>();
		cmbCategoryModel.addElement("[Category]");

		GradientPanel bfSidebar = new GradientPanel(new Color(212, 255, 253), new Color(247, 214, 190));
		bfSidebar.setLayout(null);
		bfSidebar.setBounds(0, 0, 277, 628);
		borrowFormCreation.add(bfSidebar);

		ImageIcon bfIcon = new ImageIcon(LoginScreen.class.getResource("/ui/spearKen.png"));
		Image bfImage = bfIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);

		JLabel bfLogo = new JLabel("S.P.E.A.R");
		bfLogo.setForeground(new Color(30, 144, 255));
		bfLogo.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		bfLogo.setIcon(new ImageIcon(bfImage));
		bfLogo.setBounds(0, 11, 250, 64);
		bfSidebar.add(bfLogo);

		JSeparator bfSep = new JSeparator();
		bfSep.setForeground(new Color(30, 144, 255));
		bfSep.setBounds(26, 94, 225, 2);
		bfSidebar.add(bfSep);

		lblCreationTitle = new JLabel("Borrow Manager");
		lblCreationTitle.setForeground(new Color(30, 144, 255));
		lblCreationTitle.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblCreationTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblCreationTitle.setBounds(0, 107, 277, 27);
		bfSidebar.add(lblCreationTitle);

		ButtonOne btnBfGoBack = new ButtonOne(new Color(255, 127, 80), new Color(230, 100, 60));
		btnBfGoBack.setText("Go Back");
		btnBfGoBack.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnBfGoBack.addActionListener(e -> { displayDashboard(); });
		btnBfGoBack.setBounds(26, 561, 224, 45);
		bfSidebar.add(btnBfGoBack);

		JPanel inventoryTable = new JPanel();
		inventoryTable.setBackground(Color.WHITE);
		inventoryTable.setBounds(592, 0, 681, 617);
		borrowFormCreation.add(inventoryTable);
		inventoryTable.setLayout(null);

		JPanel invHero = new JPanel();
		invHero.setLayout(null);
		invHero.setBackground(new Color(240, 248, 255));
		invHero.setBounds(0, 0, 681, 47);
		inventoryTable.add(invHero);

		JLabel lblInvRecords = new JLabel("Inventory Records");
		lblInvRecords.setForeground(new Color(30, 144, 255));
		lblInvRecords.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		lblInvRecords.setBounds(10, 0, 306, 47);
		invHero.add(lblInvRecords);

		searchBar = new JTextField();
		searchBar.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		searchBar.setColumns(10);
		searchBar.setBounds(10, 57, 136, 25);
		inventoryTable.add(searchBar);

		JButton btnFilter = new JButton("⌕");
		btnFilter.addActionListener(e -> { inventorySorter.setRowFilter(inventoryRF); });
		btnFilter.setBounds(153, 56, 45, 25);
		inventoryTable.add(btnFilter);

		cmbCategory = new JComboBox<String>();
		cmbCategory.setModel(cmbCategoryModel);
		cmbCategory.setBounds(205, 56, 105, 25);
		inventoryTable.add(cmbCategory);

		cmbCondition = new JComboBox<String>();
		cmbCondition.setModel(new DefaultComboBoxModel<String>(new String[] {"[Condition]", "Good", "Damaged", "Worn"}));
		cmbCondition.setBounds(316, 57, 100, 25);
		inventoryTable.add(cmbCondition);

		cmbAvailability = new JComboBox<String>();
		cmbAvailability.setModel(new DefaultComboBoxModel<>(new String[] {"[Availability]", "Available", "In Use", "In Maintenance"}));
		cmbAvailability.setBounds(423, 57, 115, 25);
		inventoryTable.add(cmbAvailability);

		JButton btnClearFilter = new JButton("Clear");
		btnClearFilter.addActionListener(e -> { inventorySorter.setRowFilter(null); });
		btnClearFilter.setBounds(541, 57, 75, 25);
		inventoryTable.add(btnClearFilter);

		JButton btnRefresh = new JButton("⟳");
		btnRefresh.addActionListener(e -> { displayInventoryValues(); });
		btnRefresh.setBounds(620, 57, 51, 25);
		inventoryTable.add(btnRefresh);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 92, 681, 525);
		inventoryTable.add(scrollPane);

		searchResult = new JTable();
		searchResult.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane.setViewportView(searchResult);

		formCreator = new JPanel();
		formCreator.setBackground(Color.WHITE);
		formCreator.setBounds(287, 0, 295, 617);
		borrowFormCreation.add(formCreator);
		formCreator.setLayout(formCard);
		
		JPanel createBorrow = new JPanel();
		createBorrow.setLayout(null);
		createBorrow.setBackground(Color.WHITE);
		
		JLabel lblBorrowForm = new JLabel("New Borrow Form");
		lblBorrowForm.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		lblBorrowForm.setForeground(new Color(30, 144, 255));
		lblBorrowForm.setOpaque(true);
		lblBorrowForm.setBackground(new Color(240, 248, 255));
		lblBorrowForm.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
		lblBorrowForm.setHorizontalAlignment(SwingConstants.LEFT);
		lblBorrowForm.setBounds(0, 0, 295, 47);
		createBorrow.add(lblBorrowForm);
		
		txtBrwEquipID = new JTextField();
		txtBrwEquipID.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		txtBrwEquipID.addKeyListener(numericalOnly);
		txtBrwEquipID.setBounds(171, 99, 39, 28);
		createBorrow.add(txtBrwEquipID);
		txtBrwEquipID.setColumns(10);
		
		JLabel lblBrowEQuipID = new JLabel("Equipment ID");
		lblBrowEQuipID.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 10));
		lblBrowEQuipID.setHorizontalAlignment(SwingConstants.LEFT);
		lblBrowEQuipID.setBounds(171, 82, 96, 12);
		createBorrow.add(lblBrowEQuipID);
		
		txtBrwEquipName = new JTextField();
		txtBrwEquipName.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		txtBrwEquipName.setEditable(false);
		txtBrwEquipName.setBounds(10, 100, 151, 28);
		createBorrow.add(txtBrwEquipName);
		txtBrwEquipName.setColumns(10);
		
		JLabel lblBrwEquipName = new JLabel("Equipment Name");
		lblBrwEquipName.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 10));
		lblBrwEquipName.setBounds(10, 82, 101, 12);
		createBorrow.add(lblBrwEquipName);
		
		txtBrwEquipTag = new JTextField();
		txtBrwEquipTag.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		txtBrwEquipTag.setEditable(false);
		txtBrwEquipTag.setBounds(200, 158, 82, 22);
		createBorrow.add(txtBrwEquipTag);
		txtBrwEquipTag.setColumns(10);
		
		JLabel lblBrwEquipTag = new JLabel("Property Tag");
		lblBrwEquipTag.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 10));
		lblBrwEquipTag.setBounds(200, 140, 82, 12);
		createBorrow.add(lblBrwEquipTag);
		
		JLabel lblEquipDetails = new JLabel("Equipment Details");
		lblEquipDetails.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
		lblEquipDetails.setBounds(84, 50, 126, 18);
		createBorrow.add(lblEquipDetails);
		
		JSeparator sprtrBrwTop_1 = new JSeparator();
		sprtrBrwTop_1.setBounds(9, 196, 274, 6);
		createBorrow.add(sprtrBrwTop_1);
		
		JLabel lblBorrowerDetails = new JLabel("Borrower Details");
		lblBorrowerDetails.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		lblBorrowerDetails.setBounds(102, 202, 96, 18);
		createBorrow.add(lblBorrowerDetails);
		
		// Custom button
		ButtonOne btnBrwLoadID = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnBrwLoadID.setText("Select");
		btnBrwLoadID.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
		btnBrwLoadID.addActionListener(e -> { loadItem(0); });
		btnBrwLoadID.setBounds(213, 99, 71, 29);
		createBorrow.add(btnBrwLoadID);
		
		cmbBorrowerModel = new DefaultComboBoxModel<String>();
		
		cmbBorrowerName = new JComboBox<String>();
		cmbBorrowerName.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		cmbBorrowerName.setModel(cmbBorrowerModel);
		cmbBorrowerName.setBounds(10, 233, 274, 26);
		createBorrow.add(cmbBorrowerName);
		
		JLabel lblBorrowerName = new JLabel("Borrower");
		lblBorrowerName.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 10));
		lblBorrowerName.setBounds(10, 216, 82, 18);
		createBorrow.add(lblBorrowerName);
		
		txtBrwReturnByDate = new JTextField();
		txtBrwReturnByDate.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		txtBrwReturnByDate.setBounds(171, 285, 113, 20);
		createBorrow.add(txtBrwReturnByDate);
		txtBrwReturnByDate.setColumns(10);
		
		txtDateBorrowed = new JTextField();
		txtDateBorrowed.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		txtDateBorrowed.setText("2000-12-19");
		txtDateBorrowed.setEditable(false);
		txtDateBorrowed.setColumns(10);
		txtDateBorrowed.setBounds(10, 284, 113, 20);
		createBorrow.add(txtDateBorrowed);
		
		JLabel lblDateBorrowed = new JLabel("Date Borrowed:");
		lblDateBorrowed.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 10));
		lblDateBorrowed.setBounds(10, 269, 96, 15);
		createBorrow.add(lblDateBorrowed);
		
		JLabel lblRetunByDate = new JLabel("Return by Date:*");
		lblRetunByDate.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 10));
		lblRetunByDate.setBounds(171, 270, 96, 15);
		createBorrow.add(lblRetunByDate);
		
		txtBrwEquipCondition = new JTextField();
		txtBrwEquipCondition.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		txtBrwEquipCondition.setEditable(false);
		txtBrwEquipCondition.setColumns(10);
		txtBrwEquipCondition.setBounds(10, 157, 85, 24);
		createBorrow.add(txtBrwEquipCondition);
		
		JLabel lblBrwEquipCondition = new JLabel("Condition");
		lblBrwEquipCondition.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 10));
		lblBrwEquipCondition.setBounds(10, 139, 82, 12);
		createBorrow.add(lblBrwEquipCondition);
		
		// Custom Button #2
		ButtonOne btnBrwSubmit = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnBrwSubmit.setText("Create Record");
		btnBrwSubmit.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnBrwSubmit.addActionListener(e -> { createBorrowRecord(); });
		btnBrwSubmit.setBounds(10, 561, 160, 45);
		createBorrow.add(btnBrwSubmit);
		
		// Custom Button #3
		ButtonOne btnCancel = new ButtonOne(new Color(231, 76, 60), new Color(192, 57, 43));
		btnCancel.setText("Cancel");
		btnCancel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnCancel.addActionListener(e -> { displayDashboard(); });
		btnCancel.setBounds(178, 561, 104, 45);
		createBorrow.add(btnCancel);
		
		JLabel lblyyyymmdd = new JLabel("(YYYY-MM-DD)");
		lblyyyymmdd.setHorizontalAlignment(SwingConstants.CENTER);
		lblyyyymmdd.setForeground(new Color(192, 192, 192));
		lblyyyymmdd.setBounds(171, 307, 113, 14);
		createBorrow.add(lblyyyymmdd);
		
		JPanel createMaintenance = new JPanel();
		createMaintenance.setLayout(null);
		createMaintenance.setBackground(Color.WHITE);
		
		JLabel lblMaintenanceForm = new JLabel("New Maintenance Report");
		lblMaintenanceForm.setHorizontalAlignment(SwingConstants.CENTER);
		lblMaintenanceForm.setFont(new Font("Trebuchet MS", Font.BOLD, 21));
		lblMaintenanceForm.setBounds(10, 11, 274, 30);
		createMaintenance.add(lblMaintenanceForm);
		
		JSeparator sprtrBrwTop_2 = new JSeparator();
		sprtrBrwTop_2.setBounds(10, 38, 274, 6);
		createMaintenance.add(sprtrBrwTop_2);
		
		JLabel lblEquipDetails_1 = new JLabel("Equipment Details");
		lblEquipDetails_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEquipDetails_1.setBounds(10, 39, 113, 18);
		lblEquipDetails.setForeground(new Color(30, 144, 255));
		lblBorrowerDetails.setForeground(new Color(30, 144, 255));
		createMaintenance.add(lblEquipDetails_1);
		
		JLabel lblMntEquipID = new JLabel("Equipment ID");
		lblMntEquipID.setHorizontalAlignment(SwingConstants.LEFT);
		lblMntEquipID.setBounds(10, 63, 96, 12);
		createMaintenance.add(lblMntEquipID);
		
		txtMntEquipID = new JTextField();
		txtMntEquipID.addKeyListener(numericalOnly);
		txtMntEquipID.setColumns(10);
		txtMntEquipID.setBounds(132, 60, 60, 18);
		createMaintenance.add(txtMntEquipID);
		
		JLabel lblBrwEquipName_1 = new JLabel("Equipment Name");
		lblBrwEquipName_1.setBounds(10, 88, 101, 12);
		createMaintenance.add(lblBrwEquipName_1);
		
		txtMntEquipName = new JTextField();
		txtMntEquipName.setEditable(false);
		txtMntEquipName.setColumns(10);
		txtMntEquipName.setBounds(10, 106, 182, 18);
		createMaintenance.add(txtMntEquipName);
		
		JLabel lblBrwEquipCondition_1 = new JLabel("Condition");
		lblBrwEquipCondition_1.setBounds(10, 135, 82, 12);
		createMaintenance.add(lblBrwEquipCondition_1);
		
		txtMntEquipCondition = new JTextField();
		txtMntEquipCondition.setEditable(false);
		txtMntEquipCondition.setColumns(10);
		txtMntEquipCondition.setBounds(10, 153, 82, 18);
		createMaintenance.add(txtMntEquipCondition);
		
		JLabel lblBrwEquipTag_1 = new JLabel("Property Tag");
		lblBrwEquipTag_1.setBounds(202, 88, 82, 12);
		createMaintenance.add(lblBrwEquipTag_1);
		
		txtMntEquipTag = new JTextField();
		txtMntEquipTag.setEditable(false);
		txtMntEquipTag.setColumns(10);
		txtMntEquipTag.setBounds(202, 106, 82, 18);
		createMaintenance.add(txtMntEquipTag);
		
		JButton btnMntLoadID = new JButton("Select");
		btnMntLoadID.addActionListener(e -> { loadItem(1); });
		btnMntLoadID.setBounds(200, 59, 84, 20);
		createMaintenance.add(btnMntLoadID);
		
		JSeparator sprtrBrwTop_1_1 = new JSeparator();
		sprtrBrwTop_1_1.setBounds(10, 182, 274, 6);
		createMaintenance.add(sprtrBrwTop_1_1);
		
		JLabel lblMaintenanceDetails = new JLabel("Maintenance Details");
		lblMaintenanceDetails.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMaintenanceDetails.setBounds(10, 182, 113, 18);
		createMaintenance.add(lblMaintenanceDetails);
		
		JLabel lblDateReported = new JLabel("Date Reported:");
		lblDateReported.setBounds(171, 135, 113, 14);
		createMaintenance.add(lblDateReported);
		
		txtMntDateReported = new JTextField();
		txtMntDateReported.setEditable(false);
		txtMntDateReported.setColumns(10);
		txtMntDateReported.setBounds(171, 149, 113, 20);
		createMaintenance.add(txtMntDateReported);
		
		JLabel lblMntIssue = new JLabel("Reported Issue:");
		lblMntIssue.setBounds(10, 205, 113, 14);
		createMaintenance.add(lblMntIssue);
		
		JScrollPane txtMntIssueViewport = new JScrollPane();
		txtMntIssueViewport.setBounds(10, 224, 274, 95);
		createMaintenance.add(txtMntIssueViewport);
		
		txtAMntIssue = new JTextArea();
		txtMntIssueViewport.setViewportView(txtAMntIssue);
		
		
		JButton btnMntSubmit = new JButton("Create Report");
		btnMntSubmit.addActionListener(e -> { createMaintenanceReport(); });
		btnMntSubmit.setBounds(10, 510, 151, 23);
		createMaintenance.add(btnMntSubmit);
		
		JButton btnMntCancel = new JButton("Cancel");
		btnMntCancel.addActionListener(e -> { displayDashboard(); });
		btnMntCancel.setBounds(171, 510, 113, 23);
		createMaintenance.add(btnMntCancel);
		
		JPanel main = new JPanel();
		main.setBackground(new Color(255, 255, 255));
		
		main.setLayout(null);
		
		JPanel borrowTablePreview = new JPanel();
		borrowTablePreview.setBounds(293, 11, 344, 544);
		main.add(borrowTablePreview);
		borrowTablePreview.setLayout(null);
		
		JScrollPane brwPreviewViewport = new JScrollPane();
		brwPreviewViewport.setBounds(10, 10, 324, 523);
		borrowTablePreview.add(brwPreviewViewport);
		
		tblBorrowPreview = new JTable();
		tblBorrowPreview.setFillsViewportHeight(true);
		brwPreviewViewport.setViewportView(tblBorrowPreview);
		
		JPanel sideBar = new JPanel();
		sideBar.setBackground(new Color(240, 255, 240));
		sideBar.setForeground(new Color(0, 0, 255));
		sideBar.setBounds(0, 0, 283, 566);
		main.add(sideBar);
		sideBar.setLayout(null);
		
		ImageIcon originalIcon = new ImageIcon(LoginScreen.class.getResource("/ui/spearKen.png"));
		Image resizedImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		
		JLabel spearLogo = new JLabel("S.P.E.A.R");
		spearLogo.setForeground(new Color(30, 144, 255));
		spearLogo.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		spearLogo.setIcon(new ImageIcon(resizedImage));
		spearLogo.setBounds(0, 10, 259, 64);
		sideBar.add(spearLogo);
		
		ButtonOne btnRegister = new ButtonOne(new Color(0, 82, 255), new Color(0, 65, 204));
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				displayBorrowerRegistration();
			
			}
		});
		btnRegister.setBounds(27, 114, 225, 55);
		sideBar.add(btnRegister);
		btnRegister.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnRegister.setText("Register Borrowers");
		
		ButtonOne btnNewBorrowRecord = new ButtonOne(new Color(0, 223, 137), new Color(0, 184, 113));
		btnNewBorrowRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				displayNewBorrow();
				
			}
		});
		btnNewBorrowRecord.setText("New Borrow Record");
		btnNewBorrowRecord.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnNewBorrowRecord.setBounds(27, 246, 225, 55);
		sideBar.add(btnNewBorrowRecord);
		
		ButtonOne btnUpdateBorrowRecord = new ButtonOne(new Color(255, 90, 0), new Color(214, 75, 0));
		btnUpdateBorrowRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayUpdateBorrow();
				
			}
		});
		btnUpdateBorrowRecord.setText("Update Borrow Record");
		btnUpdateBorrowRecord.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnUpdateBorrowRecord.setBounds(27, 317, 225, 55);
		sideBar.add(btnUpdateBorrowRecord);
		
		ButtonOne btnNewMaintenanceReport = new ButtonOne(new Color(255, 184, 0), new Color(219, 158, 0));
		btnNewMaintenanceReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayNewMaintenance();
			}
		});
		btnNewMaintenanceReport.setText("New Maintenance Report");
		btnNewMaintenanceReport.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnNewMaintenanceReport.setBounds(27, 382, 225, 55);
		sideBar.add(btnNewMaintenanceReport);
		
		ButtonOne btnUpdateMaintenanceReport_1 = new ButtonOne(new Color(255, 0, 85), new Color(214, 0, 71));
		btnUpdateMaintenanceReport_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayUpdateMaintenance();
			}
		});
		btnUpdateMaintenanceReport_1.setText("Update Maintenance Report");
		btnUpdateMaintenanceReport_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnUpdateMaintenanceReport_1.setBounds(27, 447, 225, 55);
		sideBar.add(btnUpdateMaintenanceReport_1);
		
		ButtonOne btnManage = new ButtonOne(new Color(121, 40, 202), new Color(97, 32, 161));
		btnManage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				displayManageBorrower();
			
			}
		});
		btnManage.setText("Manage Borrowers");
		btnManage.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnManage.setBounds(27, 180, 225, 55);
		sideBar.add(btnManage);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBackground(new Color(30, 144, 255));
		separator_2.setBounds(27, 85, 225, 2);
		sideBar.add(separator_2);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 54, 1380, 2);
		custodianDashboard.add(separator);
		
		cardContainer.add(main, "dashboard");
		
		JPanel borrowTablePreview_1 = new JPanel();
		borrowTablePreview_1.setLayout(null);
		borrowTablePreview_1.setBounds(656, 11, 344, 544);
		main.add(borrowTablePreview_1);
		
		JScrollPane brwPreviewViewport_1 = new JScrollPane();
		brwPreviewViewport_1.setBounds(10, 10, 324, 523);
		borrowTablePreview_1.add(brwPreviewViewport_1);
		
		tblMaintenancePreview = new JTable();
		brwPreviewViewport_1.setViewportView(tblMaintenancePreview);
		tblMaintenancePreview.setFillsViewportHeight(true);
		
		JPanel borrowTablePreview_1_1 = new JPanel();
		borrowTablePreview_1_1.setLayout(null);
		borrowTablePreview_1_1.setBounds(1017, 11, 344, 544);
		main.add(borrowTablePreview_1_1);
		
		JScrollPane brwPreviewViewport_1_1 = new JScrollPane();
		brwPreviewViewport_1_1.setBounds(10, 10, 324, 523);
		borrowTablePreview_1_1.add(brwPreviewViewport_1_1);
		cardContainer.add(borrowFormCreation, "creation");
		
		formCreator.add(createBorrow, "borrowForm");
		
		JLabel lblNote = new JLabel("* : If applicable");
		lblNote.setForeground(new Color(128, 128, 128));
		lblNote.setBounds(10, 536, 257, 14);
		createBorrow.add(lblNote);
		
		JLabel lblBorrowStatus = new JLabel("Availability");
		lblBorrowStatus.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 10));
		lblBorrowStatus.setBounds(105, 139, 85, 14);
		createBorrow.add(lblBorrowStatus);
		
		txtBrwEquipAvail = new JTextField();
		txtBrwEquipAvail.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		txtBrwEquipAvail.setEditable(false);
		txtBrwEquipAvail.setColumns(10);
		txtBrwEquipAvail.setBounds(105, 157, 85, 24);
		createBorrow.add(txtBrwEquipAvail);
		formCreator.add(createMaintenance, "maintenanceForm");
		
		card.show(cardContainer, "dashboard");
		
		borrowList = new DefaultTableModel();
		
		JPanel updateBorrow = new JPanel();
		cardContainer.add(updateBorrow, "updateBorrow");
		updateBorrow.setLayout(null);

		GradientPanel ubSidebar = new GradientPanel(new Color(212, 255, 253), new Color(247, 214, 190));
		ubSidebar.setLayout(null);
		ubSidebar.setBounds(0, 0, 277, 628);
		updateBorrow.add(ubSidebar);

		ImageIcon ubIcon = new ImageIcon(LoginScreen.class.getResource("/ui/spearKen.png"));
		Image ubImage = ubIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);

		JLabel ubLogo = new JLabel("S.P.E.A.R");
		ubLogo.setForeground(new Color(30, 144, 255));
		ubLogo.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		ubLogo.setIcon(new ImageIcon(ubImage));
		ubLogo.setBounds(0, 11, 250, 64);
		ubSidebar.add(ubLogo);

		JSeparator ubSep = new JSeparator();
		ubSep.setForeground(new Color(30, 144, 255));
		ubSep.setBounds(26, 94, 225, 2);
		ubSidebar.add(ubSep);

		JLabel ubTitle = new JLabel("Borrow Updater");
		ubTitle.setForeground(new Color(30, 144, 255));
		ubTitle.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		ubTitle.setHorizontalAlignment(SwingConstants.CENTER);
		ubTitle.setBounds(0, 107, 277, 27);
		ubSidebar.add(ubTitle);

		ButtonOne btnUbGoBack = new ButtonOne(new Color(255, 127, 80), new Color(230, 100, 60));
		btnUbGoBack.setText("Go Back");
		btnUbGoBack.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnUbGoBack.addActionListener(e -> { displayDashboard(); });
		btnUbGoBack.setBounds(26, 561, 224, 45);
		ubSidebar.add(btnUbGoBack);

		JPanel ubForm = new JPanel();
		ubForm.setBackground(Color.WHITE);
		ubForm.setLayout(null);
		ubForm.setBounds(287, 0, 295, 617);
		updateBorrow.add(ubForm);

		JLabel lblUpdateBorrow = new JLabel("Update Borrow Form");
		lblUpdateBorrow.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		lblUpdateBorrow.setForeground(new Color(30, 144, 255));
		lblUpdateBorrow.setOpaque(true);
		lblUpdateBorrow.setBackground(new Color(240, 248, 255));
		lblUpdateBorrow.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
		lblUpdateBorrow.setHorizontalAlignment(SwingConstants.LEFT);
		lblUpdateBorrow.setBounds(0, 0, 295, 47);
		ubForm.add(lblUpdateBorrow);

		JLabel lblUbTransaction = new JLabel("Transaction ID");
		lblUbTransaction.setFont(lblFont);
		lblUbTransaction.setBounds(10, 57, 130, 14);
		ubForm.add(lblUbTransaction);

		JLabel lblUbReceivedBy = new JLabel("Received By");
		lblUbReceivedBy.setFont(lblFont);
		lblUbReceivedBy.setBounds(150, 57, 135, 14);
		ubForm.add(lblUbReceivedBy);

		JLabel lblUbBorrowerID = new JLabel("Borrower ID");
		lblUbBorrowerID.setFont(lblFont);
		lblUbBorrowerID.setBounds(10, 106, 130, 14);
		ubForm.add(lblUbBorrowerID);

		JLabel lblUbEquipID = new JLabel("Equipment ID");
		lblUbEquipID.setFont(lblFont);
		lblUbEquipID.setBounds(150, 106, 135, 14);
		ubForm.add(lblUbEquipID);

		JLabel lblUbDateBorrowed = new JLabel("Date Borrowed");
		lblUbDateBorrowed.setFont(lblFont);
		lblUbDateBorrowed.setBounds(10, 155, 130, 14);
		ubForm.add(lblUbDateBorrowed);

		JLabel lblUbCondBorrow = new JLabel("Condition on Borrow");
		lblUbCondBorrow.setFont(lblFont);
		lblUbCondBorrow.setBounds(150, 155, 135, 14);
		ubForm.add(lblUbCondBorrow);

		JLabel lblUbReturnBy = new JLabel("Return by Date:");
		lblUbReturnBy.setFont(lblFont);
		lblUbReturnBy.setBounds(10, 272, 275, 14);
		ubForm.add(lblUbReturnBy);

		JLabel lblUbHint1 = new JLabel("(YYYY-MM-DD)");
		lblUbHint1.setFont(hintFont);
		lblUbHint1.setForeground(new Color(150, 150, 150));
		lblUbHint1.setBounds(10, 316, 275, 14);
		ubForm.add(lblUbHint1);

		JLabel lblUbDateReturned = new JLabel("Date Returned");
		lblUbDateReturned.setFont(lblFont);
		lblUbDateReturned.setBounds(10, 340, 275, 14);
		ubForm.add(lblUbDateReturned);

		JLabel lblUbHint2 = new JLabel("(YYYY-MM-DD)");
		lblUbHint2.setFont(hintFont);
		lblUbHint2.setForeground(new Color(150, 150, 150));
		lblUbHint2.setBounds(10, 384, 275, 14);
		ubForm.add(lblUbHint2);

		JLabel lblUbCondReturn = new JLabel("Condition On Return");
		lblUbCondReturn.setFont(lblFont);
		lblUbCondReturn.setBounds(10, 408, 275, 14);
		ubForm.add(lblUbCondReturn);
		
		txtTransactionID = new JTextField();
		txtTransactionID.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtTransactionID.setBounds(10, 73, 130, 25);
		ubForm.add(txtTransactionID);

		txtUpdCustodian = new JTextField();
		txtUpdCustodian.setEditable(false);
		txtUpdCustodian.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtUpdCustodian.setBounds(150, 73, 135, 25);
		ubForm.add(txtUpdCustodian);

		txtUpdBorrowerID = new JTextField();
		txtUpdBorrowerID.setEditable(false);
		txtUpdBorrowerID.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtUpdBorrowerID.setBounds(10, 122, 130, 25);
		ubForm.add(txtUpdBorrowerID);

		txtUpdBorrowedEquip = new JTextField();
		txtUpdBorrowedEquip.setEditable(false);
		txtUpdBorrowedEquip.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtUpdBorrowedEquip.setBounds(150, 122, 135, 25);
		ubForm.add(txtUpdBorrowedEquip);

		txtBorrowDate = new JTextField();
		txtBorrowDate.setEditable(false);
		txtBorrowDate.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtBorrowDate.setBounds(10, 171, 130, 25);
		ubForm.add(txtBorrowDate);

		txtUpdCondOnBorrow = new JTextField();
		txtUpdCondOnBorrow.setEditable(false);
		txtUpdCondOnBorrow.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtUpdCondOnBorrow.setBounds(150, 171, 135, 25);
		ubForm.add(txtUpdCondOnBorrow);

		ButtonOne btnUpdSelect = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnUpdSelect.setText("Select");
		btnUpdSelect.setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
		btnUpdSelect.addActionListener(e -> { LoadRecord(); });
		btnUpdSelect.setBounds(10, 208, 275, 38);
		ubForm.add(btnUpdSelect);

		JSeparator ubSep2 = new JSeparator();
		ubSep2.setForeground(new Color(30, 144, 255));
		ubSep2.setBounds(10, 260, 275, 2);
		ubForm.add(ubSep2);

		txtExpectedReturnDate = new JTextField();
		txtExpectedReturnDate.setEditable(false);
		txtExpectedReturnDate.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtExpectedReturnDate.setBounds(10, 290, 275, 25);
		ubForm.add(txtExpectedReturnDate);

		txtDateReturned = new JTextField();
		txtDateReturned.setEditable(false);
		txtDateReturned.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtDateReturned.setBounds(10, 358, 275, 25);
		ubForm.add(txtDateReturned);

		cmbCondOnReturn = new JComboBox<>();
		cmbCondOnReturn.setModel(new DefaultComboBoxModel<>(EQUIPCONDITIONS));
		cmbCondOnReturn.setBounds(10, 426, 275, 25);
		ubForm.add(cmbCondOnReturn);

		ButtonOne btnBrwUpdate = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnBrwUpdate.setText("Update Record");
		btnBrwUpdate.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnBrwUpdate.addActionListener(e -> { updateBorrowRecord(); });
		btnBrwUpdate.setBounds(10, 505, 275, 45);
		ubForm.add(btnBrwUpdate);

		ButtonOne btnBrwUpdCancel = new ButtonOne(new Color(231, 76, 60), new Color(192, 57, 43));
		btnBrwUpdCancel.setText("Cancel");
		btnBrwUpdCancel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnBrwUpdCancel.addActionListener(e -> { displayDashboard(); });
		btnBrwUpdCancel.setBounds(10, 558, 275, 45);
		ubForm.add(btnBrwUpdCancel);

		JPanel ubRecords = new JPanel();
		ubRecords.setBackground(Color.WHITE);
		ubRecords.setLayout(null);
		ubRecords.setBounds(592, 0, 681, 617);
		updateBorrow.add(ubRecords);

		JPanel ubHero = new JPanel();
		ubHero.setLayout(null);
		ubHero.setBackground(new Color(240, 248, 255));
		ubHero.setBounds(0, 0, 681, 47);
		ubRecords.add(ubHero);

		JLabel lblBorrowRecords = new JLabel("Borrow Records");
		lblBorrowRecords.setForeground(new Color(30, 144, 255));
		lblBorrowRecords.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		lblBorrowRecords.setBounds(10, 0, 306, 47);
		ubHero.add(lblBorrowRecords);

		txtbrwSearchBar = new JTextField();
		txtbrwSearchBar.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtbrwSearchBar.setColumns(10);
		txtbrwSearchBar.setBounds(10, 57, 150, 25);
		ubRecords.add(txtbrwSearchBar);

		JButton btnBrwUpdFilter = new JButton("⌕");
		btnBrwUpdFilter.addActionListener(e -> { borrowRecordSorter.setRowFilter(borrowRecordRF); });
		btnBrwUpdFilter.setBounds(165, 57, 45, 25);
		ubRecords.add(btnBrwUpdFilter);

		cmbStatus = new JComboBox<String>();
		cmbStatus.setModel(new DefaultComboBoxModel<>(RECORDSTATUS));
		cmbStatus.setBounds(220, 57, 120, 25);
		ubRecords.add(cmbStatus);

		JButton btnClearFilter_1 = new JButton("Clear");
		btnClearFilter_1.addActionListener(e -> { borrowRecordSorter.setRowFilter(null); });
		btnClearFilter_1.setBounds(348, 57, 80, 25);
		ubRecords.add(btnClearFilter_1);

		JButton btnRefresh_1 = new JButton("⟳");
		btnRefresh_1.addActionListener(e -> { displayBorrowRecords(); });
		btnRefresh_1.setBounds(625, 57, 46, 25);
		ubRecords.add(btnRefresh_1);

		JScrollPane updtBorrowViewport = new JScrollPane();
		updtBorrowViewport.setBounds(0, 92, 681, 525);
		ubRecords.add(updtBorrowViewport);

		borrowList = new DefaultTableModel();

		tblborrowRecord = new JTable();
		tblborrowRecord.setFillsViewportHeight(true);
		tblborrowRecord.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		updtBorrowViewport.setViewportView(tblborrowRecord);
		
		JPanel updateMaintenance = new JPanel();
		cardContainer.add(updateMaintenance, "updateMaintenance");
		updateMaintenance.setLayout(null);

		GradientPanel umSidebar = new GradientPanel(new Color(212, 255, 253), new Color(247, 214, 190));
		umSidebar.setLayout(null);
		umSidebar.setBounds(0, 0, 277, 628);
		updateMaintenance.add(umSidebar);

		ImageIcon umIcon = new ImageIcon(LoginScreen.class.getResource("/ui/spearKen.png"));
		Image umImage = umIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);

		JLabel umLogo = new JLabel("S.P.E.A.R");
		umLogo.setForeground(new Color(30, 144, 255));
		umLogo.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		umLogo.setIcon(new ImageIcon(umImage));
		umLogo.setBounds(0, 11, 250, 64);
		umSidebar.add(umLogo);

		JSeparator umSep = new JSeparator();
		umSep.setForeground(new Color(30, 144, 255));
		umSep.setBounds(26, 94, 225, 2);
		umSidebar.add(umSep);

		JLabel umTitle = new JLabel("Maintenance Updater");
		umTitle.setForeground(new Color(30, 144, 255));
		umTitle.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		umTitle.setHorizontalAlignment(SwingConstants.CENTER);
		umTitle.setBounds(0, 107, 277, 27);
		umSidebar.add(umTitle);

		ButtonOne btnUmGoBack = new ButtonOne(new Color(255, 127, 80), new Color(230, 100, 60));
		btnUmGoBack.setText("Go Back");
		btnUmGoBack.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnUmGoBack.addActionListener(e -> { displayDashboard(); });
		btnUmGoBack.setBounds(26, 561, 224, 45);
		umSidebar.add(btnUmGoBack);

		JPanel umForm = new JPanel();
		umForm.setBackground(Color.WHITE);
		umForm.setLayout(null);
		umForm.setBounds(287, 0, 295, 617);
		updateMaintenance.add(umForm);

		JLabel lblMaintenanceForm_1 = new JLabel("Update Maintenance Report");
		lblMaintenanceForm_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 17));
		lblMaintenanceForm_1.setForeground(new Color(30, 144, 255));
		lblMaintenanceForm_1.setOpaque(true);
		lblMaintenanceForm_1.setBackground(new Color(240, 248, 255));
		lblMaintenanceForm_1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
		lblMaintenanceForm_1.setBounds(0, 0, 295, 47);
		umForm.add(lblMaintenanceForm_1);

		JLabel lblUmTransaction = new JLabel("Transaction ID");
		lblUmTransaction.setFont(lblFont);
		lblUmTransaction.setBounds(10, 57, 130, 14);
		umForm.add(lblUmTransaction);

		JLabel lblUmEquipID = new JLabel("Equipment ID");
		lblUmEquipID.setFont(lblFont);
		lblUmEquipID.setBounds(10, 112, 130, 14);
		umForm.add(lblUmEquipID);

		JLabel lblUmDateReported = new JLabel("Date Reported");
		lblUmDateReported.setFont(lblFont);
		lblUmDateReported.setBounds(150, 112, 135, 14);
		umForm.add(lblUmDateReported);

		JLabel lblUmReportedBy = new JLabel("Reported By");
		lblUmReportedBy.setFont(lblFont);
		lblUmReportedBy.setBounds(10, 163, 275, 14);
		umForm.add(lblUmReportedBy);

		JLabel lblUmIssue = new JLabel("Reported Issue");
		lblUmIssue.setFont(lblFont);
		lblUmIssue.setBounds(10, 226, 275, 14);
		umForm.add(lblUmIssue);

		JLabel lblUmActions = new JLabel("Actions Taken");
		lblUmActions.setFont(lblFont);
		lblUmActions.setBounds(10, 337, 275, 14);
		umForm.add(lblUmActions);

		JLabel lblUmCost = new JLabel("Maintenance Cost (₱)");
		lblUmCost.setFont(lblFont);
		lblUmCost.setBounds(10, 448, 275, 14);
		umForm.add(lblUmCost);
		
		txtMntUpdTransactionID = new JTextField();
		txtMntUpdTransactionID.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtMntUpdTransactionID.setBounds(10, 73, 130, 25);
		umForm.add(txtMntUpdTransactionID);

		ButtonOne btnMntUpdSelect = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnMntUpdSelect.setText("Select");
		btnMntUpdSelect.setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
		btnMntUpdSelect.addActionListener(e -> { LoadReport(); });
		btnMntUpdSelect.setBounds(150, 68, 135, 35);
		umForm.add(btnMntUpdSelect);

		txtMntUpdEquipID = new JTextField();
		txtMntUpdEquipID.setEditable(false);
		txtMntUpdEquipID.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtMntUpdEquipID.setBounds(10, 128, 130, 25);
		umForm.add(txtMntUpdEquipID);

		txtMntUpdDate = new JTextField();
		txtMntUpdDate.setEditable(false);
		txtMntUpdDate.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtMntUpdDate.setBounds(150, 128, 135, 25);
		umForm.add(txtMntUpdDate);

		txtMntUpdReport = new JTextField();
		txtMntUpdReport.setEditable(false);
		txtMntUpdReport.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtMntUpdReport.setBounds(10, 179, 275, 25);
		umForm.add(txtMntUpdReport);

		JSeparator umSep2 = new JSeparator();
		umSep2.setForeground(new Color(30, 144, 255));
		umSep2.setBounds(10, 216, 275, 2);
		umForm.add(umSep2);

		JScrollPane srllPaneIssueViewport = new JScrollPane();
		srllPaneIssueViewport.setBounds(10, 242, 275, 85);
		umForm.add(srllPaneIssueViewport);

		txtAMntUpdIssueReported = new JTextArea();
		txtAMntUpdIssueReported.setEditable(false);
		srllPaneIssueViewport.setViewportView(txtAMntUpdIssueReported);

		JScrollPane srllPaneATViewport = new JScrollPane();
		srllPaneATViewport.setBounds(10, 353, 275, 85);
		umForm.add(srllPaneATViewport);

		txtAMntUpdActionTaken = new JTextArea();
		srllPaneATViewport.setViewportView(txtAMntUpdActionTaken);

		txtMntCost = new JTextField();
		txtMntCost.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtMntCost.setBounds(10, 464, 275, 25);
		AbstractDocument mntCostdoc = (AbstractDocument) txtMntCost.getDocument();
		mntCostdoc.setDocumentFilter(moneyInput);
		umForm.add(txtMntCost);

		ButtonOne btnMntUpdate = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnMntUpdate.setText("Update Record");
		btnMntUpdate.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnMntUpdate.addActionListener(e -> { updateMaintenanceReport(); });
		btnMntUpdate.setBounds(10, 505, 275, 45);
		umForm.add(btnMntUpdate);

		ButtonOne btnMntUpdateCancel = new ButtonOne(new Color(231, 76, 60), new Color(192, 57, 43));
		btnMntUpdateCancel.setText("Cancel");
		btnMntUpdateCancel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnMntUpdateCancel.addActionListener(e -> { displayDashboard(); });
		btnMntUpdateCancel.setBounds(10, 558, 275, 45);
		umForm.add(btnMntUpdateCancel);

		JPanel umRecords = new JPanel();
		umRecords.setBackground(Color.WHITE);
		umRecords.setLayout(null);
		umRecords.setBounds(592, 0, 681, 617);
		updateMaintenance.add(umRecords);

		JPanel umHero = new JPanel();
		umHero.setLayout(null);
		umHero.setBackground(new Color(240, 248, 255));
		umHero.setBounds(0, 0, 681, 47);
		umRecords.add(umHero);

		JLabel lblMntReports = new JLabel("Maintenance Reports");
		lblMntReports.setForeground(new Color(30, 144, 255));
		lblMntReports.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		lblMntReports.setBounds(10, 0, 306, 47);
		umHero.add(lblMntReports);

		txtUpdMntSearch = new JTextField();
		txtUpdMntSearch.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtUpdMntSearch.setColumns(10);
		txtUpdMntSearch.setBounds(10, 57, 150, 25);
		umRecords.add(txtUpdMntSearch);

		JButton btnFilter_1_1 = new JButton("⌕");
		btnFilter_1_1.addActionListener(e -> { maintenanceSorter.setRowFilter(maintenanceRF); });
		btnFilter_1_1.setBounds(165, 57, 45, 25);
		umRecords.add(btnFilter_1_1);

		cmbStatusMnt = new JComboBox<String>();
		cmbStatusMnt.setModel(new DefaultComboBoxModel<>(RECORDSTATUS));
		cmbStatusMnt.setBounds(220, 57, 120, 25);
		umRecords.add(cmbStatusMnt);

		JButton btnClearFilter_1_1 = new JButton("Clear");
		btnClearFilter_1_1.addActionListener(e -> { maintenanceSorter.setRowFilter(null); });
		btnClearFilter_1_1.setBounds(348, 57, 83, 25);
		umRecords.add(btnClearFilter_1_1);

		JButton btnUpdMntRefresh = new JButton("⟳");
		btnUpdMntRefresh.addActionListener(e -> { displayMaintenanceReports(); });
		btnUpdMntRefresh.setBounds(625, 57, 46, 25);
		umRecords.add(btnUpdMntRefresh);

		JScrollPane updtMaintenanceViewport = new JScrollPane();
		updtMaintenanceViewport.setBounds(0, 92, 681, 525);
		umRecords.add(updtMaintenanceViewport);

		tblUpdReport = new JTable();
		tblUpdReport.setFillsViewportHeight(true);
		tblUpdReport.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		updtMaintenanceViewport.setViewportView(tblUpdReport);
		
		JPanel addBorrower = new JPanel();
		cardContainer.add(addBorrower, "borrowerRegistration");
		addBorrower.setLayout(null);

		GradientPanel abSidebar = new GradientPanel(new Color(212, 255, 253), new Color(247, 214, 190));
		abSidebar.setLayout(null);
		abSidebar.setBounds(0, 0, 277, 628);
		addBorrower.add(abSidebar);

		ImageIcon abIcon = new ImageIcon(LoginScreen.class.getResource("/ui/spearKen.png"));
		Image abImage = abIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);

		JLabel abLogo = new JLabel("S.P.E.A.R");
		abLogo.setForeground(new Color(30, 144, 255));
		abLogo.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		abLogo.setIcon(new ImageIcon(abImage));
		abLogo.setBounds(0, 11, 250, 64);
		abSidebar.add(abLogo);

		JSeparator abSep = new JSeparator();
		abSep.setForeground(new Color(30, 144, 255));
		abSep.setBounds(26, 94, 225, 2);
		abSidebar.add(abSep);

		lblBorrowerTitle = new JLabel("Borrower Manager");
		lblBorrowerTitle.setForeground(new Color(30, 144, 255));
		lblBorrowerTitle.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblBorrowerTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblBorrowerTitle.setBounds(0, 107, 277, 27);
		abSidebar.add(lblBorrowerTitle);

		ButtonOne btnAbGoBack = new ButtonOne(new Color(255, 127, 80), new Color(230, 100, 60));
		btnAbGoBack.setText("Go Back");
		btnAbGoBack.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnAbGoBack.addActionListener(e -> { displayDashboard(); });
		btnAbGoBack.setBounds(26, 561, 224, 45);
		abSidebar.add(btnAbGoBack);

		borrowerManager = new JPanel();
		borrowerManager.setBackground(Color.WHITE);
		borrowerManager.setBounds(287, 0, 295, 617);
		addBorrower.add(borrowerManager);
		borrowerManager.setLayout(borrowManagingCard);

		JPanel register = new JPanel();
		register.setBackground(Color.WHITE);
		register.setLayout(null);
		borrowerManager.add(register, "registerBorrower");

		JLabel lblBorrowerRegistration = new JLabel("Register New Borrower");
		lblBorrowerRegistration.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblBorrowerRegistration.setForeground(new Color(30, 144, 255));
		lblBorrowerRegistration.setOpaque(true);
		lblBorrowerRegistration.setBackground(new Color(240, 248, 255));
		lblBorrowerRegistration.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
		lblBorrowerRegistration.setBounds(0, 0, 295, 47);
		register.add(lblBorrowerRegistration);

		JLabel lblRegFirst = new JLabel("First Name");
		lblRegFirst.setFont(lblFont);
		lblRegFirst.setBounds(10, 57, 275, 14);
		register.add(lblRegFirst);

		JLabel lblRegLast = new JLabel("Last Name");
		lblRegLast.setFont(lblFont);
		lblRegLast.setBounds(10, 106, 275, 14);
		register.add(lblRegLast);

		JLabel lblRegType = new JLabel("Borrower Type");
		lblRegType.setFont(lblFont);
		lblRegType.setBounds(10, 155, 275, 14);
		register.add(lblRegType);

		JLabel lblRegID = new JLabel("ID Number");
		lblRegID.setFont(lblFont);
		lblRegID.setBounds(10, 204, 275, 14);
		register.add(lblRegID);

		JLabel lblRegDept = new JLabel("Department / Grade");
		lblRegDept.setFont(lblFont);
		lblRegDept.setBounds(10, 253, 275, 14);
		register.add(lblRegDept);

		JLabel lblRegContact = new JLabel("Contact No.");
		lblRegContact.setFont(lblFont);
		lblRegContact.setBounds(10, 302, 275, 14);
		register.add(lblRegContact);

		JLabel lblRegEmail = new JLabel("Email");
		lblRegEmail.setFont(lblFont);
		lblRegEmail.setBounds(10, 351, 275, 14);
		register.add(lblRegEmail);
		
		txtNewBorrowNameF = new JTextField();
		txtNewBorrowNameF.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtNewBorrowNameF.setBounds(10, 73, 275, 25);
		register.add(txtNewBorrowNameF);

		txtNewBorrowNameL = new JTextField();
		txtNewBorrowNameL.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtNewBorrowNameL.setBounds(10, 122, 275, 25);
		register.add(txtNewBorrowNameL);

		cmbNewBorrowType = new JComboBox<>();
		cmbNewBorrowType.setModel(new DefaultComboBoxModel<>(BORROWERTYPES));
		cmbNewBorrowType.setBounds(10, 171, 275, 25);
		register.add(cmbNewBorrowType);

		txtNewBorrowID = new JTextField();
		txtNewBorrowID.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtNewBorrowID.setBounds(10, 220, 275, 25);
		register.add(txtNewBorrowID);

		txtNewBorrowDept = new JTextField();
		txtNewBorrowDept.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtNewBorrowDept.setBounds(10, 269, 275, 25);
		register.add(txtNewBorrowDept);

		txtNewBorrowContact = new JTextField();
		txtNewBorrowContact.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtNewBorrowContact.setBounds(10, 318, 275, 25);
		register.add(txtNewBorrowContact);

		txtNewBorrowEmail = new JTextField();
		txtNewBorrowEmail.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtNewBorrowEmail.setBounds(10, 367, 275, 25);
		register.add(txtNewBorrowEmail);

		ButtonOne btnRegisterBorrower = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnRegisterBorrower.setText("Register Borrower");
		btnRegisterBorrower.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnRegisterBorrower.addActionListener(e -> { registerBorrower(); });
		btnRegisterBorrower.setBounds(10, 505, 275, 45);
		register.add(btnRegisterBorrower);

		ButtonOne btnMntUpdateCancel_1 = new ButtonOne(new Color(231, 76, 60), new Color(192, 57, 43));
		btnMntUpdateCancel_1.setText("Cancel");
		btnMntUpdateCancel_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnMntUpdateCancel_1.addActionListener(e -> { displayDashboard(); });
		btnMntUpdateCancel_1.setBounds(10, 558, 275, 45);
		register.add(btnMntUpdateCancel_1);

		JPanel delete = new JPanel();
		delete.setBackground(Color.WHITE);
		delete.setLayout(null);
		borrowerManager.add(delete, "manageBorrower");

		JLabel lblBorrowerManage = new JLabel("Manage Borrowers");
		lblBorrowerManage.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		lblBorrowerManage.setForeground(new Color(30, 144, 255));
		lblBorrowerManage.setOpaque(true);
		lblBorrowerManage.setBackground(new Color(240, 248, 255));
		lblBorrowerManage.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
		lblBorrowerManage.setBounds(0, 0, 295, 47);
		delete.add(lblBorrowerManage);

		JLabel lblMngBorrowerID = new JLabel("Borrower ID");
		lblMngBorrowerID.setFont(lblFont);
		lblMngBorrowerID.setBounds(10, 57, 130, 14);
		delete.add(lblMngBorrowerID);

		JLabel lblMngFirst = new JLabel("First Name");
		lblMngFirst.setFont(lblFont);
		lblMngFirst.setBounds(10, 122, 275, 14);
		delete.add(lblMngFirst);

		JLabel lblMngLast = new JLabel("Last Name");
		lblMngLast.setFont(lblFont);
		lblMngLast.setBounds(10, 171, 275, 14);
		delete.add(lblMngLast);

		JLabel lblMngType = new JLabel("Borrower Type");
		lblMngType.setFont(lblFont);
		lblMngType.setBounds(10, 220, 275, 14);
		delete.add(lblMngType);

		JLabel lblMngID = new JLabel("ID Number");
		lblMngID.setFont(lblFont);
		lblMngID.setBounds(10, 269, 275, 14);
		delete.add(lblMngID);

		JLabel lblMngDept = new JLabel("Department / Grade");
		lblMngDept.setFont(lblFont);
		lblMngDept.setBounds(10, 318, 275, 14);
		delete.add(lblMngDept);

		JLabel lblMngContact = new JLabel("Contact No.");
		lblMngContact.setFont(lblFont);
		lblMngContact.setBounds(10, 367, 275, 14);
		delete.add(lblMngContact);

		JLabel lblMngEmail = new JLabel("Email");
		lblMngEmail.setFont(lblFont);
		lblMngEmail.setBounds(10, 416, 275, 14);
		delete.add(lblMngEmail);
		
		txtMngBorrowerTransaction = new JTextField();
		txtMngBorrowerTransaction.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtMngBorrowerTransaction.setBounds(10, 73, 130, 25);
		delete.add(txtMngBorrowerTransaction);

		ButtonOne btnNewButton = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnNewButton.setText("Select");
		btnNewButton.setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
		btnNewButton.addActionListener(e -> { loadBorrower(); });
		btnNewButton.setBounds(150, 68, 135, 35);
		delete.add(btnNewButton);

		JSeparator abSep2 = new JSeparator();
		abSep2.setForeground(new Color(30, 144, 255));
		abSep2.setBounds(10, 112, 275, 2);
		delete.add(abSep2);

		txtMngBorrowerNameF = new JTextField();
		txtMngBorrowerNameF.setEditable(false);
		txtMngBorrowerNameF.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtMngBorrowerNameF.setBounds(10, 138, 275, 25);
		delete.add(txtMngBorrowerNameF);

		txtMngBorrowerNameL = new JTextField();
		txtMngBorrowerNameL.setEditable(false);
		txtMngBorrowerNameL.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtMngBorrowerNameL.setBounds(10, 187, 275, 25);
		delete.add(txtMngBorrowerNameL);

		cmbMngBorrowerType = new JComboBox<String>();
		cmbMngBorrowerType.setModel(new DefaultComboBoxModel<>(BORROWERTYPES));
		cmbMngBorrowerType.setBounds(10, 236, 275, 25);
		delete.add(cmbMngBorrowerType);

		txtMngBorrowerID = new JTextField();
		txtMngBorrowerID.setEditable(false);
		txtMngBorrowerID.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtMngBorrowerID.setBounds(10, 285, 275, 25);
		delete.add(txtMngBorrowerID);

		txtMngBorrowerDept = new JTextField();
		txtMngBorrowerDept.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtMngBorrowerDept.setBounds(10, 334, 275, 25);
		delete.add(txtMngBorrowerDept);

		txtMngBorrowerContactNo = new JTextField();
		txtMngBorrowerContactNo.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtMngBorrowerContactNo.setBounds(10, 383, 275, 25);
		delete.add(txtMngBorrowerContactNo);

		txtMngBorrowerEmail = new JTextField();
		txtMngBorrowerEmail.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		txtMngBorrowerEmail.setBounds(10, 432, 275, 25);
		delete.add(txtMngBorrowerEmail);

		ButtonOne btnPublishEdit = new ButtonOne(new Color(46, 204, 113), new Color(39, 174, 96));
		btnPublishEdit.setText("Publish Update");
		btnPublishEdit.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnPublishEdit.setBounds(10, 470, 275, 40);
		delete.add(btnPublishEdit);

		ButtonOne btnDeleteProfile = new ButtonOne(new Color(231, 76, 60), new Color(192, 57, 43));
		btnDeleteProfile.setText("Delete");
		btnDeleteProfile.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnDeleteProfile.addActionListener(e -> { deleteBorrower(); });
		btnDeleteProfile.setBounds(10, 516, 275, 40);
		delete.add(btnDeleteProfile);

		ButtonOne btnMntUpdateCancel_1_1 = new ButtonOne(new Color(255, 127, 80), new Color(230, 100, 60));
		btnMntUpdateCancel_1_1.setText("Cancel");
		btnMntUpdateCancel_1_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		btnMntUpdateCancel_1_1.addActionListener(e -> { displayDashboard(); });
		btnMntUpdateCancel_1_1.setBounds(10, 562, 275, 40);
		delete.add(btnMntUpdateCancel_1_1);

		JPanel abRecords = new JPanel();
		abRecords.setBackground(Color.WHITE);
		abRecords.setLayout(null);
		abRecords.setBounds(592, 0, 681, 617);
		addBorrower.add(abRecords);

		JPanel abHero = new JPanel();
		abHero.setLayout(null);
		abHero.setBackground(new Color(240, 248, 255));
		abHero.setBounds(0, 0, 681, 47);
		abRecords.add(abHero);

		JLabel lblBorrowerRecords = new JLabel("Borrower Records");
		lblBorrowerRecords.setForeground(new Color(30, 144, 255));
		lblBorrowerRecords.setFont(new Font("Segoe UI Black", Font.PLAIN, 20));
		lblBorrowerRecords.setBounds(10, 0, 306, 47);
		abHero.add(lblBorrowerRecords);

		textField = new JTextField();
		textField.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		textField.setColumns(10);
		textField.setBounds(10, 57, 150, 25);
		abRecords.add(textField);

		JButton btnFilter_1_1_1 = new JButton("⌕");
		btnFilter_1_1_1.setBounds(165, 57, 45, 25);
		abRecords.add(btnFilter_1_1_1);

		cmbBorrowType = new JComboBox<String>();
		cmbBorrowType.setModel(new DefaultComboBoxModel<>(BORROWERTYPES));
		cmbBorrowType.setBounds(220, 57, 120, 25);
		abRecords.add(cmbBorrowType);

		JButton btnClearFilter_1_1_1 = new JButton("Clear");
		btnClearFilter_1_1_1.setBounds(348, 57, 76, 25);
		abRecords.add(btnClearFilter_1_1_1);

		JButton btnUpdMntRefresh_1 = new JButton("⟳");
		btnUpdMntRefresh_1.addActionListener(e -> { displayBorrowerList(); });
		btnUpdMntRefresh_1.setBounds(625, 57, 46, 25);
		abRecords.add(btnUpdMntRefresh_1);

		JScrollPane scrlPaneBorrowerViewport = new JScrollPane();
		scrlPaneBorrowerViewport.setBounds(0, 92, 681, 525);
		abRecords.add(scrlPaneBorrowerViewport);

		tblBorrowers = new JTable();
		tblBorrowers.setFillsViewportHeight(true);
		tblBorrowers.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrlPaneBorrowerViewport.setViewportView(tblBorrowers);
		
		//frame.setVisible(true);
		
	}
	

	//---------------------------------------------------------------------------------------------------------------------------------
	// UNIVERSAL HELPER METHODS
	//---------------------------------------------------------------------------------------------------------------------------------
	
	
	JPanel getPanel() {
		return custodianDashboard;
	}
	
	void setCurrentDate(int target) {
		if(target == 0) txtDateBorrowed.setText(String.valueOf(LocalDate.now()));
		else if (target == 1) txtMntDateReported.setText(String.valueOf(LocalDate.now()));
		
	}

	public void initialiseDisplays() {
		
		loadPreviews();
		displayInventoryValues();
		displayBorrowRecords();
		displayMaintenanceReports();
		displayBorrowerList();
	}
	
	void displayDashboard() {
		card.show(cardContainer, "dashboard");
		clearAllFields();
		initialiseDisplays();
	}
	
	void displayNewBorrow() {
		card.show(cardContainer, "creation");
		formCard.show(formCreator, "borrowForm");
		lblCreationTitle.setText("Borrow Manager");
		refreshBorrowCreator();
	}
	
	void displayUpdateBorrow() {
		card.show(cardContainer, "updateBorrow");
		
	}
	
	void displayNewMaintenance() {
		card.show(cardContainer, "creation");
		formCard.show(formCreator, "maintenanceForm");
		lblCreationTitle.setText("Maintenance Manager");
		refreshMaintenanceCreator();
	}
	
	void displayUpdateMaintenance() {
		card.show(cardContainer, "updateMaintenance");
		
	}
	
	void displayBorrowerRegistration() {
		card.show(cardContainer, "borrowerRegistration");
		borrowManagingCard.show(borrowerManager, "registerBorrower");
		lblBorrowerTitle.setText("Register Borrower");
	}

	void displayManageBorrower() {
		card.show(cardContainer, "borrowerRegistration");
		borrowManagingCard.show(borrowerManager, "manageBorrower");
		lblBorrowerTitle.setText("Manage Borrowers");
	}
	
	void clearAllFields() {
		/*
			Clean this up tomorrow, future me - Paz
		*/
		txtAMntIssue.setText("");
		txtAMntUpdIssueReported.setText("");
		txtAMntUpdActionTaken.setText("");
		txtBrwEquipID.setText("");
		txtBrwEquipName.setText("");
		txtBrwEquipTag.setText("");
		txtBrwReturnByDate.setText("");
		txtDateBorrowed.setText("");
		txtBrwEquipCondition.setText("");

		txtMntEquipID.setText("");
		txtMntEquipName.setText("");
		txtMntEquipCondition.setText("");
		txtMntEquipTag.setText("");
		txtMntDateReported.setText("");
		txtbrwSearchBar.setText("");
		
		txtTransactionID.setText("");;
		txtUpdBorrowerID.setText("");
		txtUpdBorrowedEquip.setText("");
		txtBorrowDate.setText("");
		txtUpdCustodian.setText("");
		txtUpdCondOnBorrow.setText("");
		txtExpectedReturnDate.setText("");
		txtDateReturned.setText("");
		txtBrwEquipAvail.setText("");
		
		txtUpdMntSearch.setText("");
		txtMntCost.setText("");
		txtMntUpdTransactionID.setText("");
		txtMntUpdReport.setText("");
		txtMntUpdDate.setText("");
		txtMntUpdEquipID.setText("");
	}
	
	void loadPreviews() {
		
		BorrowPreview();
		MaintenancePreview();
		
	}
	
	
	void BorrowPreview() {

		String[][] list;
		
		try {
			list = backend.getBorrowPreview();
			}
		catch(SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}
			
		borrowPreview = new DefaultTableModel(list.length, 3) {
			  @Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }	
		};
		borrowPreview.setColumnIdentifiers(new String[] {
				"ID", "Equipment ID", "Status"
			});
		
		for(int r = 0; r < list.length; r++)
			for (int c = 0; c < list[r].length; c++)
				borrowPreview.setValueAt(list[r][c], r, c);
		
		tblBorrowPreview.setModel(borrowPreview);
		tblBorrowPreview.getTableHeader().setReorderingAllowed(false);
		
	}
	
	void MaintenancePreview() {

		String[][] list;
		
		try {
			list = backend.getMaintenancePreview();
			}
		catch(SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}
			
		maintenancePreview = new DefaultTableModel(list.length, 3) {
			  @Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }	
		};
		maintenancePreview.setColumnIdentifiers(new String[] {
				"ID", "Equipment ID", "Status"
			});
		
		for(int r = 0; r < list.length; r++)
			for (int c = 0; c < list[r].length; c++)
				maintenancePreview.setValueAt(list[r][c], r, c);
		
		tblMaintenancePreview.setModel(maintenancePreview);
		tblMaintenancePreview.getTableHeader().setReorderingAllowed(false);
		
	}
	
	private JLabel formLabel(String text, int x, int y, int w) {
		JLabel l = new JLabel(text);
		l.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
		l.setBounds(x, y, w, 14);
		return l;
	}

	private JLabel hintLabel(int x, int y, int w) {
		JLabel l = new JLabel("(YYYY-MM-DD)");
		l.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		l.setForeground(new Color(150, 150, 150));
		l.setHorizontalAlignment(SwingConstants.LEFT);
		l.setBounds(x, y, w, 14);
		return l;
	}
	
	// ----------------------------------------------------------------------------------------------------------------------
	
	
	void displayBorrowRecords() {
		
		parent.log(true, "Fetching Records from Database...");
		
		String[][] list;
			
		try {
			list = backend.getBorrowedRecordsList();
			}
		catch(SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}
			
		borrowList = new DefaultTableModel(list.length, 11) {
			  @Override
			    public boolean isCellEditable(int row, int column) {
			       return false;
			    }	
		};
		borrowList.setColumnIdentifiers(new String[] {
				"ID", "Borrower", "Equipment", "Borrow Date", "Return By", "Date Returned", "Processed By", "Cond. On Borrow", "Cond. On Return", "Status", "Remarks"
			});
		for(int r = 0; r < list.length; r++)
			for (int c = 0; c < list[r].length; c++)
				borrowList.setValueAt(list[r][c], r, c);
		
		tblborrowRecord.setModel(borrowList);
		borrowRecordSorter.setModel(borrowList);
		tblborrowRecord.setRowSorter(borrowRecordSorter);
		tblborrowRecord.getTableHeader().setReorderingAllowed(false);
		tblborrowRecord.getTableHeader().setResizingAllowed(false);
		
		parent.log(true, "Record fetch successful. Now Displaying Inventory.");
		parent.log(true, "Borrow Record View Update completed successfully.");
		
		
	}
	
	void displayInventoryValues() {
		
		parent.log(true, "Fetching Inventory from Database...");
		
		String[][] list;
			
		try {
			list = backend.getEquipmentList();
			}
		catch(SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}
			
		query = new DefaultTableModel(list.length, 13) {
			  @Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }	
		};
		query.setColumnIdentifiers(new String[] {
				"ID", "Property Tag", "Name", "Category", "Brand Model", "Serial Number", "Description", "RoomID", "Purchase Date", "Purchase Cost", "Condition", "Availability", "Date Added"
			});
		
		for(int r = 0; r < list.length; r++)
			for (int c = 0; c < list[r].length; c++)
				query.setValueAt(list[r][c], r, c);
		
		searchResult.setModel(query);
		inventorySorter.setModel(query);
		searchResult.setRowSorter(inventorySorter);
		searchResult.getTableHeader().setReorderingAllowed(false);
		searchResult.getTableHeader().setResizingAllowed(false);
		
		parent.log(true, "Inventory fetch successful. Now Displaying Inventory.");
		parent.log(true, "Inventory View Update completed successfully.");
		
	}
	
	
	void displayMaintenanceReports() {
		
		parent.log(true, "Fetching Reports from Database...");
		
		String[][] list;
			
		try {
			list = backend.getMaintenanceReportsList();
			}
		catch(SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}
			
		reportList = new DefaultTableModel(list.length, 8) {
			  @Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }	
		};
		reportList.setColumnIdentifiers(new String[] {
				"ID", "Equipment ID", "Reported Date", "Custodian ID", "Issue Description", "Action Taken", "Cost", "Status"
			});
		
		for(int r = 0; r < list.length; r++)
			for (int c = 0; c < list[r].length; c++)
				reportList.setValueAt(list[r][c], r, c);
		
		tblUpdReport.setModel(reportList);
		maintenanceSorter.setModel(reportList);
		tblUpdReport.setRowSorter(maintenanceSorter);
		tblUpdReport.getTableHeader().setReorderingAllowed(false);
		tblUpdReport.getTableHeader().setResizingAllowed(false);
		parent.log(true, "Inventory fetch successful. Now Displaying Inventory.");
		parent.log(true, "Inventory View Update completed successfully.");
		
	}
	
	void displayBorrowerList( ) {
		
		parent.log(true, "Fetching Borrower List from Database...");
		
		String[][] list;
			
		try {
			list = backend.getRegisteredBorrowersList();
			}
		catch(SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}
			
		registeredUserList = new DefaultTableModel(list.length, 8) {
			  @Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }	
		};
		registeredUserList.setColumnIdentifiers(new String[] {
				"ID", "First Name", "Last Name	", "Borrower Type", "ID Number", "Department/Grade", "Contact No.", "Email"
			});
		
		for(int r = 0; r < list.length; r++)
			for (int c = 0; c < list[r].length; c++)
				registeredUserList.setValueAt(list[r][c], r, c);
		
		tblBorrowers.setModel(registeredUserList);
		//inventorySorter.setModel(query);
		//tblBorrowers.setRowSorter(inventorySorter);
		tblBorrowers.getTableHeader().setReorderingAllowed(false);
		tblBorrowers.getTableHeader().setResizingAllowed(false);
		
		parent.log(true, "Borrower List fetch successful. Now Displaying Inventory.");
		parent.log(true, "Borrower List View Update completed successfully.");
		
	}
	
	void refreshBorrowCreator() {
		
		displayInventoryValues();
		UpdateFilterBox();
		loadBorrowers();
		setCurrentDate(0);
		
	}
	
	void refreshMaintenanceCreator() {
		displayInventoryValues();
		UpdateFilterBox();
		setCurrentDate(1);
	}
	
	void UpdateFilterBox() {
		parent.log(true, "Updating Inventory Categories...");
		String[] values;
		
		try {
			values = backend.getInventoryFilters();
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}
		
		cmbCategoryModel.removeAllElements();
		cmbCategoryModel.addElement("[Category]");
		for (int i = 0; i < values.length; i++)
		{
			cmbCategoryModel.addElement(values[i]);

			System.out.print(values[i] + " ");
		}
		
		parent.log(true, "Categories Updated successfully.");
		
	}
	
	boolean LoadRecord() {
		int id;
		
		try {
			id = Integer.parseInt(txtTransactionID.getText().trim());
		} catch (NumberFormatException e) {
			
			parent.log(false, "ID field is empty.");
			return false;
		}
		try {
			loadedRecord = backend.fetchBorrowRecord(id);
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return false;
		}
		
		
		
		borrowUpdateLoadValues(String.valueOf(loadedRecord.custodian()), String.valueOf(loadedRecord.borrowerID()),
				String.valueOf(loadedRecord.equipmentID()), String.valueOf(loadedRecord.dateBorrowed()), String.valueOf(loadedRecord.returbByDate()), String.valueOf(loadedRecord.dateReturned()), 
				String.valueOf(loadedRecord.conditionOnBorrow()), String.valueOf(loadedRecord.conditionOnReturn()), String.valueOf(loadedRecord.status()));
		return true;
	}
	
	void borrowUpdateLoadValues(String custodianID, String borrowerID, String equipmentID, String dateBorrowed, String returnByDate, String dateReturned, String conditionOnBorrow, String conditonOnReturn, String status) {
		txtUpdCustodian.setText(custodianID);
		txtUpdBorrowerID.setText(borrowerID);
		txtUpdBorrowedEquip.setText(equipmentID);
		txtBorrowDate.setText(dateBorrowed);
		txtUpdCondOnBorrow.setText(conditionOnBorrow);
		if(returnByDate != null && !returnByDate.equals("null")) txtExpectedReturnDate.setText(returnByDate);
		if(dateReturned != null && !dateReturned.equals("null")) txtDateReturned.setText(dateReturned);
		cmbCondOnReturn.setSelectedItem(conditonOnReturn);
		
		if(status.equals("Returned")) {
			txtDateReturned.setEditable(false);
			cmbCondOnReturn.setEnabled(false);
		} else {
			txtDateReturned.setEditable(true);
			cmbCondOnReturn.setEnabled(true);
			
		}
		
		
	}
	

	
	boolean loadItem(int target) {
		int id;
		JTextField targetField;
		if(target == 0) targetField = txtBrwEquipID;
		else targetField = txtMntEquipID;
		
		try {
		id = Integer.parseInt(targetField.getText().trim());
		} catch(NumberFormatException e) {
			
			parent.log(false, "Equipment field is empty.");
			return false;
		}
		
		try {
			loadedEquip = backend.fetchEquipmentDetails(id);
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return false;
		}
		
		boolean isAvailable;
		
		switch(target) {
		default:
		case 0: isAvailable = backend.isItemAvailable(id);  break;
		case 1: isAvailable = backend.isItemAvailable(id);  break;
		
		}
		if(!isAvailable) {
			parent.log(false, "Equipment is Under Maintenance and thus cannot accessed at the moment.");
			return false;
		}
		
		switch(target) {
		case 0: borrowLoadValues(loadedEquip.equipmentName(), loadedEquip.propertyTag(), loadedEquip.condition(), loadedEquip.availability()); break;
		case 1: maintenanceLoadValues(loadedEquip.equipmentName(), loadedEquip.propertyTag(), loadedEquip.condition(), loadedEquip.availability()); break;
		}
		parent.log(true, "Target Equipment Loaded successfully.");
		return true;
	}
	
	void maintenanceLoadValues(String name, String tag, String condition, String availability) {
		txtMntEquipName.setText(name);
		txtMntEquipTag.setText(tag);
		txtMntEquipCondition.setText(condition);
		
		
		
	}
	
	void borrowLoadValues(String name, String tag, String condition, String availability) {
		txtBrwEquipName.setText(name);
		txtBrwEquipTag.setText(tag);
		txtBrwEquipCondition.setText(condition);
		txtBrwEquipAvail.setText(availability);
		
	}
	
	boolean loadBorrower() {
		int id;
		
		try {
			id = Integer.parseInt(txtMngBorrowerTransaction.getText().trim());
		} catch (NumberFormatException e) {
			
			parent.log(false, "ID field is empty.");
			return false;
		}
		try {
			loadedProfile = backend.fetchBorrowerProfile(id);
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return false;
		}
		
		BorrowerLoadValues(String.valueOf(loadedProfile.borrowerID()), loadedProfile.firstName(), loadedProfile.lastName(), loadedProfile.BorrowType(),
				loadedProfile.idNumber(), loadedProfile.deptOrGrade(), loadedProfile.contactNo(), loadedProfile.email());
		
		return true;
	}
	
	void BorrowerLoadValues(String borrowerID,
							String firstName, String lastName, String borrowType, String id, String dept, String contact, String email) {
		txtMngBorrowerNameF.setText(firstName);
		txtMngBorrowerNameL.setText(lastName);
		cmbMngBorrowerType.setSelectedItem(borrowType);
		txtMngBorrowerID.setText(id);
		txtMngBorrowerDept.setText(dept);
		txtMngBorrowerContactNo.setText(contact);
		txtMngBorrowerEmail.setText(email);
	}
	
	void loadBorrowers() {
		parent.log(true, "Updating Borrower List...");
		BorrowerList list;
		try { 
			list = backend.fetchBorrowers();
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}
		
		String[] values = list.borrower();
		borrowerIDs = list.borrowerID();
		
		cmbBorrowerModel.removeAllElements();
		cmbBorrowerModel.addElement("[Borrower]");
		for (int i = 0; i < values.length; i++)
		{
			cmbBorrowerModel.addElement(values[i]);

			System.out.print(values[i] + " ");
		}
		
		parent.log(true, "Borrower List Updated successfully.");
	}
	
	
	boolean LoadReport() {
	int id;
		
		try {
			id = Integer.parseInt(txtMntUpdTransactionID.getText().trim());
		} catch (NumberFormatException e) {
			
			parent.log(false, "ID field is empty.");
			return false;
		}
		try {
			loadedReport = backend.fetchMaintenanceReport(id);
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return false;
		}
		
		
		
		maintenanceUpdateLoadValues(String.valueOf(loadedReport.equipmentID()), String.valueOf(loadedReport.reportedDate()), String.valueOf(loadedReport.custodian()), loadedReport.issueReport(), loadedReport.status());
		return true;
		
		
	}
	
	void maintenanceUpdateLoadValues(String equipID, String reportDay, String custodian, String issue, String status) {
		
		txtMntUpdEquipID.setText(equipID);
		txtMntUpdDate.setText(reportDay);
		txtMntUpdReport.setText(custodian);
		txtAMntUpdIssueReported.setText(issue);
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------
	// CREATE, UPDATE AND DELETE
	//---------------------------------------------------------------------------------------------------------------------------------
	
	void deleteBorrower() {
		int id;
		try { id = Integer.parseInt(txtMngBorrowerTransaction.getText().trim()); } catch(NumberFormatException e) {
			parent.log(false, "Invalid ID format for ID field.");
			return;
		}
		
		// TODO : ADD USER CONFIRMATION
		boolean result;
		
		try {
		result = backend.deleteBorrowerProfile(id);
		} catch(SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}
		
		if(result) parent.log(true, "Borrower Profile successfully deleted.");
	}
	
	
	void updateBorrower() {
		boolean loadBorrower = loadBorrower();
		
		if(!loadBorrower) {
			parent.log(false, "No Borrower Selected.");
			return;
		}
		
		String[] rawProfile = new String[] {
				txtMngBorrowerNameF.getText().trim(),
				txtMngBorrowerNameL.getText().trim(),
				cmbMngBorrowerType.getSelectedItem().toString().trim(),
				txtMngBorrowerID.getText().trim(),
				txtMngBorrowerDept.getText().trim(),
				txtMngBorrowerContactNo.getText().trim(),
				txtMngBorrowerEmail.getText().trim()
				};
		
		if (!mailValidator.validate(rawProfile[6])) {
			parent.log(false, "Invalid Email Format.");
			return;
		}
		
		if((rawProfile[6].length() < 9 || rawProfile[6].length() > 11)) {
			parent.log(false, "Invalid Contanct Number Format.");
			return;
		}
		
		boolean result;
		
		try {
			
			result = backend.RegisterBorrower(rawProfile);
			
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}
		
		if(result) parent.log(true, "New Borrower successfully registered.");
		
	}
	
	void registerBorrower() {
		String[] rawProfile = new String[] {
		txtNewBorrowNameF.getText().trim(),
		txtNewBorrowNameL.getText().trim(),
		cmbNewBorrowType.getSelectedItem().toString().trim(),
		txtNewBorrowID.getText().trim(),
		txtNewBorrowDept.getText().trim(),
		txtNewBorrowContact.getText().trim(),
		txtNewBorrowEmail.getText().trim()
		};
		
		if (!mailValidator.validate(rawProfile[6])) {
			parent.log(false, "Invalid Email Format.");
			return;
		}
		
		if((rawProfile[6].length() < 9 || rawProfile[6].length() > 11)) {
			parent.log(false, "Invalid Contanct Number Format.");
			return;
		}
		
		boolean result;
		
		try {
			
			result = backend.RegisterBorrower(rawProfile);
			
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}
		
		if(result) parent.log(true, "New Borrower successfully registered.");
	}
	
	
	void updateBorrowRecord() {
		boolean loadRecord = LoadRecord();
		
		if(!loadRecord) {
			parent.log(false, "No Equipment selected.");
			return;
		}
		
		boolean verify;
		try { verify = verifyRecordIntegrity(loadedRecord.equipmentID()); } catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}
		
		if(!verify) {
			parent.log(false, "The equipment related to this transaction has already been returned.");
			return;
		}
		
		if(loadedRecord.status().equals("Returned")) {
			parent.log(false, "Returned Equipment transactions cannot be edited.");
			return;
		}
		
		String
		custodian = txtUpdCustodian.getText().trim(),
		borrowerID = txtUpdBorrowerID.getText().trim(),
		equipID = txtUpdBorrowedEquip.getText().trim(),
		dateBorrowed = txtBorrowDate.getText().trim(),
		condOnBorrow = txtUpdCondOnBorrow.getText().trim(),
		returnByDate = txtExpectedReturnDate.getText().trim(),
		dateReturned = txtDateReturned.getText().trim(),
		conditionOnReturn = cmbCondOnReturn.getSelectedItem().toString();
		
		
		
		boolean result;
		try {
			result = backend.UpdateRecord(borrowerID, equipID, dateBorrowed, returnByDate, dateReturned, custodian, condOnBorrow, conditionOnReturn, "Returned", loadedRecord.transactionID());
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}
		
		if(result) parent.log(true, "Borrow Record sucessfully updated.");
		initialiseDisplays();
	}
	
	boolean verifyRecordIntegrity(int ID) throws SPEARException {
		EquipmentDetails equip = backend.fetchEquipmentDetails(ID);
		System.out.println(equip.availability());
		if(equip.availability().equals("Borrowed")) return true;
		
		return false;
	}
	
	
	/* Record Constructor Paremetres:
	 * int borrowerID,
		int equipmentID,
		String dateBorrowed,
		String returbByDate,
		String dateReturned,
		int custodian,
		String conditionOnBorrow,
		String conditionOnReturn
	 */
	void createBorrowRecord() {
		boolean loadItem = loadItem(0);
		
		if(!loadItem)
			return;
		
		
		String
		borrowerID = String.valueOf(getSelectedBorrower()),
		equipID = txtBrwEquipID.getText(),
		dateBorrowed = txtDateBorrowed.getText().trim(),
		returnByDate = txtBrwReturnByDate.getText().trim(),
		custodian = String.valueOf(parent.getSessionID()),
		conditionOnBorrow = txtBrwEquipCondition.getText().trim();
		
		boolean result;
		
		try {
			result = backend.CreateRecord(borrowerID, equipID, dateBorrowed, returnByDate, null, custodian, conditionOnBorrow, null, "Borrowed");
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}
		
		if(result) parent.log(result, "Borrow Record successfully published to Database.");
		initialiseDisplays();
	}
	
	Integer getSelectedBorrower() {
		int val = cmbBorrowerName.getSelectedIndex() - 1;
		if(val < 0) return -1;
		else return borrowerIDs[val];
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------
	// MAINTENANCE CREATION METHODS
	//---------------------------------------------------------------------------------------------------------------------------------
	
	void updateMaintenanceReport() {
		boolean loadReport = LoadReport();
		
		if(!loadReport)
			return;
		
		
		boolean verify;
		try { verify = verifyReportIntegrity(loadedReport.equipmentID()); } catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}
		
		if(verify) {
			parent.log(false, "The equipment related to this report is no longer under maintenance.");
			return;
		}
		
		String
		equipID = txtMntUpdEquipID.getText(),
		dateReported = txtMntUpdDate.getText().trim(),
		custodian = String.valueOf(parent.getSessionID()),
		issue = txtAMntUpdIssueReported.getText(),
		action = txtAMntUpdActionTaken.getText(),
		cost = txtMntCost.getText();
		
		boolean result;
		try {
			result = backend.UpdateReport(equipID, dateReported, custodian, issue, action, cost, "Resolved", loadedReport.maintenanceID());
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}
		
		if(result) parent.log(result, "Maintenance Report successfully published to Database.");
		initialiseDisplays();
	}
	
	
	
	/* Report Constructor Parametres:
	 		int equipmentID,
			Date reportedDate,
			int custodian,
			String issueReport,
			String actionsTaken,
			double cost,
			String status
	 * */
	 
	void createMaintenanceReport() {
		boolean loadItem = loadItem(1);
		
		if(!loadItem) {
			parent.log(false, "No Equipment selected.");
			return;
		}
		String
		equipID = txtMntEquipID.getText(),
		dateReported = txtMntDateReported.getText().trim(),
		custodian = String.valueOf(parent.getSessionID()),
		issue = txtAMntIssue.getText() ;
//		action = txtAMntActionTaken.getText(),
//		cost = txtMntCost.getText();
		
		boolean result;
		try {
			result = backend.CreateReport(equipID, dateReported, custodian, issue, null, null, "In Progress");
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}
		
		if(result) parent.log(result, "Maintenance Report successfully published to Database.");
		initialiseDisplays();
	}
	
	boolean verifyReportIntegrity(int ID) throws SPEARException {
		EquipmentDetails equip = backend.fetchEquipmentDetails(ID);
		System.out.println(equip.availability());
		if(!equip.availability().equals("Maintenance")) return true;
		
		return false;
	
	}
}

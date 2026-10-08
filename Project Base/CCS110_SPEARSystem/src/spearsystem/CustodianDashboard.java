package spearsystem;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;

import javax.swing.DefaultComboBoxModel;
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
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import spearsystem.CustodianDatabaseHandler.BorrowRecord;
import spearsystem.CustodianDatabaseHandler.BorrowerList;
import spearsystem.CustodianDatabaseHandler.EquipmentDetails;
import spearsystem.CustodianDatabaseHandler.MaintenanceReport;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class CustodianDashboard {
	
	final String[] RECORDSTATUS = {"Return", "Borrow"};
	final String[] REPORTSTATUS = {"In Progress", "Resolved"};
	
	final String[] EQUIPCONDITIONS = {"Good", "Worn", "Damaged"};
	final String[] EQUIPAVAILABILITY = {"Available", "Borrowed", "Maintenance"}; 
	private Integer[] borrowerIDs;
	
	private EquipmentDetails loadedEquip;
	private BorrowRecord loadedRecord;
	private MaintenanceReport loadedReport;
	
	private int activeScreen = 0;
	
	
	private JPanel custodianDashboard;
	private MainClient parent;
	private CustodianHandler backend;
	
	private CardLayout card = new CardLayout(0,0);
	private CardLayout formCard = new CardLayout(0,0);
	
	private JComboBox<String> cmbCondition;
	private JComboBox<String> cmbCategory;
	private JComboBox<String> cmbAvailability;
	private JComboBox<String> cmbReceivedBy;
	private JComboBox<String> cmbStatus;	
	private JComboBox<String> cmbBorrowerName;
	private JComboBox<String> cmbCondOnReturn;
	
	private JPanel cardContainer;
	private JPanel formCreator; // JPanel of the Forms

	private DefaultComboBoxModel<String> cmbCategoryModel;
	private DefaultComboBoxModel<String> cmbBorrowerModel;
	
	private JTextField searchBar;
	private JTable searchResult;
	
	
	private JTable tblBorrowPreview;
	private DefaultTableModel borrowPreview;
	
	private JTable tblMaintenancePreview;
	private DefaultTableModel maintenancePreview;
	
	private JTable tblborrowRecord;
	private JTable tblUpdReport;
	
	private DefaultTableModel query;
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
			String receiverFilter = cmbReceivedBy.getSelectedItem().toString();
			String statusFilter = cmbStatus.getSelectedItem().toString();
			
			// searchBar
			String id = entry.getStringValue(0);
			String tag = entry.getStringValue(1);
			String name = entry.getStringValue(2);
			
			if(!(searchFilter.isBlank() || searchFilter.isEmpty()) &&
				!(id.contains(searchFilter) || tag.contains(searchFilter) || name.contains(searchFilter))) return false;
			
			if(!receiverFilter.equals("[Custodian]") && !entry.getStringValue(11).equals(receiverFilter)) return false;
			
			if(!statusFilter.equals("[Status]") && !entry.getStringValue(12).equals(statusFilter)) return false;
			return true;
		}
		
		
	};
	
	private TableRowSorter<DefaultTableModel> maintenanceSorter = new TableRowSorter<>(reportList);
	private RowFilter<DefaultTableModel, Integer> maintenanceRF = new RowFilter<>() {
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
	private JTextField txtTransactionID;
	private JTextField txtUpdBorrowerID;
	private JTextField txtUpdBorrowedEquip;
	private JTextField txtBorrowDate;
	private JTextField txtUpdCustodian;
	private JTextField txtUpdCondOnBorrow;
	private JTextField txtExpectedReturnDate;
	private JTextField txtDateReturned;
	private JTextField txtBrwEquipAvail;
	
	private JTextField textField;
	private JTextField txtMntCost;
	private JTextField txtMntUpdTransactionID;
	private JTextField txtMntUpdReport;
	private JTextField txtMntUpdDate;
	private JTextField txtMntUpdEquipID;
	
	
	
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
		
		custodianDashboard = new JPanel();
		custodianDashboard.setLayout(null);
		custodianDashboard.setBounds(10, 54, 1400, 620);
		
		
		
		JLabel lblCustodian = new JLabel("Custodian");
		lblCustodian.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCustodian.setHorizontalAlignment(SwingConstants.LEFT);
		lblCustodian.setBounds(10, 11, 343, 32);
		custodianDashboard.add(lblCustodian);
		
		query = new DefaultTableModel();
		inventorySorter.setRowFilter(null);
		
		cardContainer = new JPanel();
		cardContainer.setBounds(0, 54, 1400, 566);
		custodianDashboard.add(cardContainer);
		cardContainer.setLayout(card);
		
		JPanel borrowFormCreation = new JPanel();
		borrowFormCreation.setBounds(10, 54, 1384, 555);
		borrowFormCreation.setLayout(null);
		
		cmbCategoryModel = new DefaultComboBoxModel<String>();
		cmbCategoryModel.addElement("[Category]");
		
		JPanel inventoryTable = new JPanel();
		inventoryTable.setBounds(314, 11, 1076, 544);
		borrowFormCreation.add(inventoryTable);
		inventoryTable.setLayout(null);
		
		cmbCondition = new JComboBox<String>();
		cmbCondition.setModel(new DefaultComboBoxModel<String>(new String[] {"[Condition]", "Good", "Damaged", "Worn"}));
		cmbCondition.setBounds(321, 0, 97, 22);
		inventoryTable.add(cmbCondition);
		
		cmbCategory = new JComboBox<String>();
		cmbCategory.setModel(cmbCategoryModel);
		cmbCategory.setBounds(214, 0, 97, 22);
		inventoryTable.add(cmbCategory);
		
		searchBar = new JTextField();
		searchBar.setColumns(10);
		searchBar.setBounds(10, 1, 115, 20);
		inventoryTable.add(searchBar);
		
		JButton btnRefresh = new JButton("⟳");
		btnRefresh.addActionListener(e -> { displayInventoryValues();});
		btnRefresh.setBounds(1014, 0, 62, 23);
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
		btnClearFilter.setBounds(607, 0, 75, 23);
		inventoryTable.add(btnClearFilter);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 33, 1076, 511);
		inventoryTable.add(scrollPane);
		
		searchResult = new JTable();
		
		scrollPane.setViewportView(searchResult);
		
		formCreator = new JPanel();
		formCreator.setBounds(10, 11, 294, 544);
		borrowFormCreation.add(formCreator);
		formCreator.setLayout(formCard);
		
		JPanel createBorrow = new JPanel();
		createBorrow.setLayout(null);
		
		JLabel lblBorrowForm = new JLabel("New Borrow Form");
		lblBorrowForm.setFont(new Font("Trebuchet MS", Font.BOLD, 21));
		lblBorrowForm.setHorizontalAlignment(SwingConstants.CENTER);
		lblBorrowForm.setBounds(10, 10, 274, 30);
		createBorrow.add(lblBorrowForm);
		
		txtBrwEquipID = new JTextField();
		txtBrwEquipID.addKeyListener(numericalOnly);
		txtBrwEquipID.setBounds(171, 63, 39, 18);
		createBorrow.add(txtBrwEquipID);
		txtBrwEquipID.setColumns(10);
		
		JLabel lblBrowEQuipID = new JLabel("Equipment ID");
		lblBrowEQuipID.setHorizontalAlignment(SwingConstants.LEFT);
		lblBrowEQuipID.setBounds(171, 51, 96, 12);
		createBorrow.add(lblBrowEQuipID);
		
		txtBrwEquipName = new JTextField();
		txtBrwEquipName.setEditable(false);
		txtBrwEquipName.setBounds(10, 110, 182, 18);
		createBorrow.add(txtBrwEquipName);
		txtBrwEquipName.setColumns(10);
		
		JLabel lblBrwEquipName = new JLabel("Equipment Name");
		lblBrwEquipName.setBounds(10, 92, 101, 12);
		createBorrow.add(lblBrwEquipName);
		
		txtBrwEquipTag = new JTextField();
		txtBrwEquipTag.setEditable(false);
		txtBrwEquipTag.setBounds(202, 110, 82, 18);
		createBorrow.add(txtBrwEquipTag);
		txtBrwEquipTag.setColumns(10);
		
		JLabel lblBrwEquipTag = new JLabel("Property Tag");
		lblBrwEquipTag.setBounds(202, 92, 82, 12);
		createBorrow.add(lblBrwEquipTag);
		
		JSeparator sprtrBrwTop = new JSeparator();
		sprtrBrwTop.setBounds(10, 40, 274, 6);
		createBorrow.add(sprtrBrwTop);
		
		JLabel lblEquipDetails = new JLabel("Equipment Details");
		lblEquipDetails.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEquipDetails.setBounds(10, 40, 113, 18);
		createBorrow.add(lblEquipDetails);
		
		JSeparator sprtrBrwTop_1 = new JSeparator();
		sprtrBrwTop_1.setBounds(10, 186, 274, 6);
		createBorrow.add(sprtrBrwTop_1);
		
		JLabel lblBorrowerDetails = new JLabel("Borrower Details");
		lblBorrowerDetails.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBorrowerDetails.setBounds(10, 193, 113, 18);
		createBorrow.add(lblBorrowerDetails);
		
		JButton btnBrwLoadID = new JButton("Select");
		btnBrwLoadID.addActionListener(e -> { loadItem(0); });
		btnBrwLoadID.setBounds(213, 62, 71, 20);
		createBorrow.add(btnBrwLoadID);
		
		cmbBorrowerModel = new DefaultComboBoxModel<String>();
		
		cmbBorrowerName = new JComboBox<String>();
		cmbBorrowerName.setModel(cmbBorrowerModel);
		cmbBorrowerName.setBounds(10, 233, 274, 20);
		createBorrow.add(cmbBorrowerName);
		
		JLabel lblBorrowerName = new JLabel("Borrower");
		lblBorrowerName.setBounds(10, 222, 82, 12);
		createBorrow.add(lblBorrowerName);
		
		txtBrwReturnByDate = new JTextField();
		txtBrwReturnByDate.setBounds(154, 276, 113, 20);
		createBorrow.add(txtBrwReturnByDate);
		txtBrwReturnByDate.setColumns(10);
		
		txtDateBorrowed = new JTextField();
		txtDateBorrowed.setText("2000-12-19");
		txtDateBorrowed.setEditable(false);
		txtDateBorrowed.setColumns(10);
		txtDateBorrowed.setBounds(10, 278, 113, 20);
		createBorrow.add(txtDateBorrowed);
		
		JLabel lblDateBorrowed = new JLabel("Date Borrowed:");
		lblDateBorrowed.setBounds(10, 264, 96, 14);
		createBorrow.add(lblDateBorrowed);
		
		JLabel lblRetunByDate = new JLabel("Return by Date:*");
		lblRetunByDate.setBounds(154, 264, 96, 14);
		createBorrow.add(lblRetunByDate);
		
		txtBrwEquipCondition = new JTextField();
		txtBrwEquipCondition.setEditable(false);
		txtBrwEquipCondition.setColumns(10);
		txtBrwEquipCondition.setBounds(10, 157, 101, 18);
		createBorrow.add(txtBrwEquipCondition);
		
		JLabel lblBrwEquipCondition = new JLabel("Condition");
		lblBrwEquipCondition.setBounds(10, 139, 82, 12);
		createBorrow.add(lblBrwEquipCondition);
		
		JButton btnBrwSubmit = new JButton("Create Record");
		btnBrwSubmit.addActionListener(e -> { createBorrowRecord(); });
		btnBrwSubmit.setBounds(10, 510, 151, 23);
		createBorrow.add(btnBrwSubmit);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(e -> { displayDashboard(); });
		btnCancel.setBounds(171, 510, 113, 23);
		createBorrow.add(btnCancel);
		
		JLabel lblyyyymmdd = new JLabel("(YYYY-MM-DD)");
		lblyyyymmdd.setHorizontalAlignment(SwingConstants.CENTER);
		lblyyyymmdd.setForeground(new Color(192, 192, 192));
		lblyyyymmdd.setBounds(154, 295, 113, 14);
		createBorrow.add(lblyyyymmdd);
		
		JPanel createMaintenance = new JPanel();
		createMaintenance.setLayout(null);
		
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
		borrowTablePreview.setBounds(381, 11, 344, 544);
		main.add(borrowTablePreview);
		borrowTablePreview.setLayout(null);
		
		JScrollPane brwPreviewViewport = new JScrollPane();
		brwPreviewViewport.setBounds(10, 143, 324, 390);
		borrowTablePreview.add(brwPreviewViewport);
		
		tblBorrowPreview = new JTable();
		tblBorrowPreview.setFillsViewportHeight(true);
		brwPreviewViewport.setViewportView(tblBorrowPreview);
		
		JPanel sideBar = new JPanel();
		sideBar.setBackground(new Color(0, 0, 255));
		sideBar.setForeground(new Color(0, 0, 255));
		sideBar.setBounds(0, 0, 371, 566);
		main.add(sideBar);
		sideBar.setLayout(null);
		
		JButton btnCreateBorrow = new JButton("New Borrow Record");
		btnCreateBorrow.addActionListener(e -> { displayNewBorrow(); });
		btnCreateBorrow.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnCreateBorrow.setBorderPainted(false);
		btnCreateBorrow.setFocusable(false);
		btnCreateBorrow.setBackground(new Color(0, 0, 255));
		btnCreateBorrow.setForeground(new Color(255, 255, 255));
		btnCreateBorrow.setBounds(10, 69, 351, 21);
		sideBar.add(btnCreateBorrow);
		
		JButton btnUpdateBorrow = new JButton("Update Borrow Record");
		btnUpdateBorrow.addActionListener(e -> { displayUpdateBorrow(); });
		btnUpdateBorrow.setForeground(Color.WHITE);
		btnUpdateBorrow.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnUpdateBorrow.setFocusable(false);
		btnUpdateBorrow.setBorderPainted(false);
		btnUpdateBorrow.setBackground(Color.BLUE);
		btnUpdateBorrow.setBounds(10, 101, 351, 21);
		sideBar.add(btnUpdateBorrow);
		
		JButton btnUpdateMaintenanceReport = new JButton("Update Maintenance Report");
		btnUpdateMaintenanceReport.addActionListener(e -> { displayUpdateMaintenance(); });
		btnUpdateMaintenanceReport.setForeground(Color.WHITE);
		btnUpdateMaintenanceReport.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnUpdateMaintenanceReport.setFocusable(false);
		btnUpdateMaintenanceReport.setBorderPainted(false);
		btnUpdateMaintenanceReport.setBackground(Color.BLUE);
		btnUpdateMaintenanceReport.setBounds(10, 165, 351, 21);
		sideBar.add(btnUpdateMaintenanceReport);
		
		JButton btnCreateMaintenance = new JButton("New Maintenance Report");
		btnCreateMaintenance.addActionListener(e -> { displayNewMaintenance(); });
		btnCreateMaintenance.setForeground(Color.WHITE);
		btnCreateMaintenance.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnCreateMaintenance.setFocusable(false);
		btnCreateMaintenance.setBorderPainted(false);
		btnCreateMaintenance.setBackground(Color.BLUE);
		btnCreateMaintenance.setBounds(10, 133, 351, 21);
		sideBar.add(btnCreateMaintenance);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 41, 1380, 2);
		custodianDashboard.add(separator);
		
		cardContainer.add(main, "dashboard");
		
		JPanel borrowTablePreview_1 = new JPanel();
		borrowTablePreview_1.setLayout(null);
		borrowTablePreview_1.setBounds(735, 11, 344, 544);
		main.add(borrowTablePreview_1);
		
		JScrollPane brwPreviewViewport_1 = new JScrollPane();
		brwPreviewViewport_1.setBounds(10, 143, 324, 390);
		borrowTablePreview_1.add(brwPreviewViewport_1);
		
		tblMaintenancePreview = new JTable();
		brwPreviewViewport_1.setViewportView(tblMaintenancePreview);
		tblMaintenancePreview.setFillsViewportHeight(true);
		cardContainer.add(borrowFormCreation, "creation");
		
		formCreator.add(createBorrow, "borrowForm");
		
		JLabel lblNote = new JLabel("* : If applicable");
		lblNote.setForeground(new Color(128, 128, 128));
		lblNote.setBounds(10, 485, 257, 14);
		createBorrow.add(lblNote);
		
		JLabel lblBorrowStatus = new JLabel("Availability");
		lblBorrowStatus.setBounds(171, 139, 96, 14);
		createBorrow.add(lblBorrowStatus);
		
		txtBrwEquipAvail = new JTextField();
		txtBrwEquipAvail.setEditable(false);
		txtBrwEquipAvail.setColumns(10);
		txtBrwEquipAvail.setBounds(171, 157, 101, 18);
		createBorrow.add(txtBrwEquipAvail);
		formCreator.add(createMaintenance, "maintenanceForm");
		
		card.show(cardContainer, "dashboard");
		
		JPanel updateBorrow = new JPanel();
		cardContainer.add(updateBorrow, "updateBorrow");
		updateBorrow.setLayout(null);
		
		JScrollPane updtBorrowViewport = new JScrollPane();
		updtBorrowViewport.setBounds(314, 44, 1076, 511);
		updateBorrow.add(updtBorrowViewport);
		
		borrowList = new DefaultTableModel();
		
		tblborrowRecord = new JTable();
		tblborrowRecord.setFillsViewportHeight(true);
		updtBorrowViewport.setViewportView(tblborrowRecord);
		
		JButton btnRefresh_1 = new JButton("⟳");
		btnRefresh_1.addActionListener(e -> { displayBorrowRecords(); });
		btnRefresh_1.setBounds(1328, 11, 62, 23);
		updateBorrow.add(btnRefresh_1);
		
		JButton btnClearFilter_1 = new JButton("Clear");
		btnClearFilter_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnClearFilter_1.setBounds(921, 11, 75, 23);
		updateBorrow.add(btnClearFilter_1);
		
		JButton btnFilter_1 = new JButton("⌕");
		btnFilter_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnFilter_1.setBounds(849, 11, 62, 23);
		updateBorrow.add(btnFilter_1);
		
		cmbStatus = new JComboBox<String>();
		cmbStatus.setBounds(742, 11, 97, 22);
		updateBorrow.add(cmbStatus);
		
		cmbReceivedBy = new JComboBox<String>();
		cmbReceivedBy.setBounds(635, 11, 97, 22);
		updateBorrow.add(cmbReceivedBy);
		
		txtbrwSearchBar = new JTextField();
		txtbrwSearchBar.setColumns(10);
		txtbrwSearchBar.setBounds(324, 12, 115, 20);
		updateBorrow.add(txtbrwSearchBar);
		
		JSeparator sprtrBrwTop_3 = new JSeparator();
		sprtrBrwTop_3.setBounds(20, 41, 274, 6);
		updateBorrow.add(sprtrBrwTop_3);
		
		JLabel lblUpdateBorrow = new JLabel("Update Borrow Form");
		lblUpdateBorrow.setHorizontalAlignment(SwingConstants.CENTER);
		lblUpdateBorrow.setFont(new Font("Trebuchet MS", Font.BOLD, 21));
		lblUpdateBorrow.setBounds(20, 11, 274, 30);
		updateBorrow.add(lblUpdateBorrow);
		
		JLabel lblTransactionID = new JLabel("Transaction ID");
		lblTransactionID.setBounds(53, 44, 97, 14);
		updateBorrow.add(lblTransactionID);
		
		txtTransactionID = new JTextField();
		txtTransactionID.setBounds(53, 57, 97, 20);
		updateBorrow.add(txtTransactionID);
		txtTransactionID.setColumns(10);
		
		JLabel lblBorrowID = new JLabel("Borrower ID");
		lblBorrowID.setBounds(53, 88, 97, 14);
		updateBorrow.add(lblBorrowID);
		
		txtUpdBorrowerID = new JTextField();
		txtUpdBorrowerID.setEditable(false);
		txtUpdBorrowerID.setColumns(10);
		txtUpdBorrowerID.setBounds(53, 100, 97, 20);
		updateBorrow.add(txtUpdBorrowerID);
		
		JLabel lblBorrowedEquipID = new JLabel("Equipment ID");
		lblBorrowedEquipID.setBounds(160, 88, 97, 14);
		updateBorrow.add(lblBorrowedEquipID);
		
		txtUpdBorrowedEquip = new JTextField();
		txtUpdBorrowedEquip.setEditable(false);
		txtUpdBorrowedEquip.setColumns(10);
		txtUpdBorrowedEquip.setBounds(160, 100, 97, 20);
		updateBorrow.add(txtUpdBorrowedEquip);
		
		JLabel lblUpdDateBorrowed = new JLabel("Date Borrowed");
		lblUpdDateBorrowed.setBounds(53, 126, 97, 14);
		updateBorrow.add(lblUpdDateBorrowed);
		
		txtBorrowDate = new JTextField();
		txtBorrowDate.setEditable(false);
		txtBorrowDate.setColumns(10);
		txtBorrowDate.setBounds(53, 138, 97, 20);
		updateBorrow.add(txtBorrowDate);
		
		JLabel lblUpdCustodian = new JLabel("Received By");
		lblUpdCustodian.setBounds(160, 45, 97, 14);
		updateBorrow.add(lblUpdCustodian);
		
		txtUpdCustodian = new JTextField();
		txtUpdCustodian.setEditable(false);
		txtUpdCustodian.setColumns(10);
		txtUpdCustodian.setBounds(160, 57, 97, 20);
		updateBorrow.add(txtUpdCustodian);
		
		JLabel lblUpdConditionOnBorrow = new JLabel("Condition on Borrow");
		lblUpdConditionOnBorrow.setBounds(160, 126, 97, 14);
		updateBorrow.add(lblUpdConditionOnBorrow);
		
		txtUpdCondOnBorrow = new JTextField();
		txtUpdCondOnBorrow.setEditable(false);
		txtUpdCondOnBorrow.setColumns(10);
		txtUpdCondOnBorrow.setBounds(160, 138, 97, 20);
		updateBorrow.add(txtUpdCondOnBorrow);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(20, 220, 284, 6);
		updateBorrow.add(separator_1);
		
		JButton btnUpdSelect = new JButton("Selecct");
		btnUpdSelect.addActionListener( e -> { LoadRecord(); });
		btnUpdSelect.setBounds(111, 190, 89, 23);
		updateBorrow.add(btnUpdSelect);
		
		txtExpectedReturnDate = new JTextField();
		txtExpectedReturnDate.setEditable(false);
		txtExpectedReturnDate.setBounds(20, 262, 274, 20);
		updateBorrow.add(txtExpectedReturnDate);
		txtExpectedReturnDate.setColumns(10);
		
		JLabel lblUpdReturnDateBy = new JLabel("Return by Date:");
		lblUpdReturnDateBy.setBounds(20, 247, 130, 14);
		updateBorrow.add(lblUpdReturnDateBy);
		
		txtDateReturned = new JTextField();
		txtDateReturned.setEditable(false);
		txtDateReturned.setColumns(10);
		txtDateReturned.setBounds(20, 308, 274, 20);
		updateBorrow.add(txtDateReturned);
		
		JLabel lblUpdDateReturned = new JLabel("Date Returned");
		lblUpdDateReturned.setBounds(20, 293, 130, 14);
		updateBorrow.add(lblUpdDateReturned);
		
		JLabel lblUpdConditionOnReturn = new JLabel("Condition On Return");
		lblUpdConditionOnReturn.setBounds(20, 339, 130, 14);
		updateBorrow.add(lblUpdConditionOnReturn);
		
		cmbCondOnReturn = new JComboBox<>();
		cmbCondOnReturn.setModel(new DefaultComboBoxModel<>(EQUIPCONDITIONS));
		cmbCondOnReturn.setBounds(20, 353, 274, 22);
		updateBorrow.add(cmbCondOnReturn);
		
		JLabel lbldateformat = new JLabel("(YYYY-MM-DD)");
		lbldateformat.setHorizontalAlignment(SwingConstants.CENTER);
		lbldateformat.setForeground(new Color(128, 128, 128));
		lbldateformat.setBounds(20, 283, 274, 14);
		updateBorrow.add(lbldateformat);
		
		JLabel lbldateformat_1 = new JLabel("(YYYY-MM-DD)");
		lbldateformat_1.setFont(new Font("Tahoma", Font.PLAIN, 8));
		lbldateformat_1.setHorizontalAlignment(SwingConstants.CENTER);
		lbldateformat_1.setForeground(Color.GRAY);
		lbldateformat_1.setBounds(20, 328, 138, 14);
		updateBorrow.add(lbldateformat_1);
		
		JButton btnBrwUpdCancel = new JButton("Cancel");
		btnBrwUpdCancel.addActionListener(e -> {displayDashboard();});
		btnBrwUpdCancel.setBounds(20, 503, 274, 23);
		updateBorrow.add(btnBrwUpdCancel);
		
		JButton btnBrwUpdate = new JButton("Update Record");
		btnBrwUpdate.addActionListener(e -> { updateBorrowRecord(); });
		btnBrwUpdate.setBounds(20, 478, 274, 23);
		updateBorrow.add(btnBrwUpdate);
		
		JPanel updateMaintenance = new JPanel();
		cardContainer.add(updateMaintenance, "updateMaintenance");
		updateMaintenance.setLayout(null);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(324, 12, 115, 20);
		updateMaintenance.add(textField);
		
		JComboBox<String> cmbReceivedBy_1 = new JComboBox<String>();
		cmbReceivedBy_1.setBounds(635, 11, 97, 22);
		updateMaintenance.add(cmbReceivedBy_1);
		
		JComboBox<String> cmbStatus_1 = new JComboBox<String>();
		cmbStatus_1.setBounds(742, 11, 97, 22);
		updateMaintenance.add(cmbStatus_1);
		
		JButton btnFilter_1_1 = new JButton("⌕");
		btnFilter_1_1.setBounds(849, 11, 62, 23);
		updateMaintenance.add(btnFilter_1_1);
		
		JButton btnClearFilter_1_1 = new JButton("Clear");
		btnClearFilter_1_1.setBounds(921, 11, 75, 23);
		updateMaintenance.add(btnClearFilter_1_1);
		
		JButton btnUpdMntRefresh = new JButton("⟳");
		btnUpdMntRefresh.addActionListener(e -> { displayMaintenanceReports(); });
		btnUpdMntRefresh.setBounds(1328, 11, 62, 23);
		updateMaintenance.add(btnUpdMntRefresh);
		
		JLabel lblMntAction = new JLabel("Actions Taken:");
		lblMntAction.setBounds(10, 319, 89, 14);
		updateMaintenance.add(lblMntAction);
		
		JLabel lblRepairCost = new JLabel("Maintenance Cost          ₱");
		lblRepairCost.setBounds(10, 439, 141, 14);
		updateMaintenance.add(lblRepairCost);
		
		txtMntCost = new JTextField();
		txtMntCost.setColumns(10);
		txtMntCost.setBounds(151, 436, 133, 20);
		AbstractDocument mntCostdoc = (AbstractDocument) txtMntCost.getDocument();
		mntCostdoc.setDocumentFilter(moneyInput);
		updateMaintenance.add(txtMntCost);
		
		JLabel lblMaintenanceForm_1 = new JLabel("Update Maintenance Report");
		lblMaintenanceForm_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblMaintenanceForm_1.setFont(new Font("Trebuchet MS", Font.BOLD, 21));
		lblMaintenanceForm_1.setBounds(10, 11, 274, 30);
		updateMaintenance.add(lblMaintenanceForm_1);
		
		JSeparator sprtrBrwTop_2_1 = new JSeparator();
		sprtrBrwTop_2_1.setBounds(10, 38, 274, 6);
		updateMaintenance.add(sprtrBrwTop_2_1);
		
		JLabel lblEquipDetails_1_1 = new JLabel("Equipment Details");
		lblEquipDetails_1_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEquipDetails_1_1.setBounds(10, 39, 113, 18);
		updateMaintenance.add(lblEquipDetails_1_1);
		
		JLabel lblMntTransaction = new JLabel("Transaction ID");
		lblMntTransaction.setHorizontalAlignment(SwingConstants.LEFT);
		lblMntTransaction.setBounds(10, 63, 96, 12);
		updateMaintenance.add(lblMntTransaction);
		
		txtMntUpdTransactionID = new JTextField();
		txtMntUpdTransactionID.setColumns(10);
		txtMntUpdTransactionID.setBounds(132, 60, 60, 18);
		updateMaintenance.add(txtMntUpdTransactionID);
		
		JLabel lblMntUpdReportedBy = new JLabel("Reported By");
		lblMntUpdReportedBy.setBounds(36, 132, 82, 12);
		updateMaintenance.add(lblMntUpdReportedBy);
		
		txtMntUpdReport = new JTextField();
		txtMntUpdReport.setEditable(false);
		txtMntUpdReport.setColumns(10);
		txtMntUpdReport.setBounds(36, 150, 97, 18);
		updateMaintenance.add(txtMntUpdReport);
		
		JButton btnMntUpdSelect = new JButton("Select");
		btnMntUpdSelect.addActionListener(e -> { LoadReport(); });
		btnMntUpdSelect.setBounds(200, 59, 84, 20);
		updateMaintenance.add(btnMntUpdSelect);
		
		JLabel lblDateReported_1 = new JLabel("Date Reported:");
		lblDateReported_1.setBounds(142, 89, 113, 14);
		updateMaintenance.add(lblDateReported_1);
		
		txtMntUpdDate = new JTextField();
		txtMntUpdDate.setEditable(false);
		txtMntUpdDate.setColumns(10);
		txtMntUpdDate.setBounds(142, 103, 113, 20);
		updateMaintenance.add(txtMntUpdDate);
		
		JLabel lblBorrowedEquipID_1 = new JLabel("Equipment ID");
		lblBorrowedEquipID_1.setBounds(36, 89, 97, 14);
		updateMaintenance.add(lblBorrowedEquipID_1);
		
		txtMntUpdEquipID = new JTextField();
		txtMntUpdEquipID.setEditable(false);
		txtMntUpdEquipID.setColumns(10);
		txtMntUpdEquipID.setBounds(36, 101, 97, 20);
		updateMaintenance.add(txtMntUpdEquipID);
		
		JSeparator sprtrBrwTop_1_1_1 = new JSeparator();
		sprtrBrwTop_1_1_1.setBounds(10, 177, 274, 6);
		updateMaintenance.add(sprtrBrwTop_1_1_1);
		
		JLabel lblMaintenanceDetails_1 = new JLabel("Maintenance Details");
		lblMaintenanceDetails_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMaintenanceDetails_1.setBounds(10, 177, 113, 18);
		updateMaintenance.add(lblMaintenanceDetails_1);
		
		JScrollPane srllPaneATViewport = new JScrollPane();
		srllPaneATViewport.setBounds(10, 335, 272, 93);
		updateMaintenance.add(srllPaneATViewport);
		
		
		JScrollPane srllPaneIssueViewport = new JScrollPane();
		srllPaneIssueViewport.setBounds(10, 215, 272, 93);
		updateMaintenance.add(srllPaneIssueViewport);

		txtAMntUpdIssueReported = new JTextArea();
		txtAMntUpdIssueReported.setEditable(false);
		srllPaneIssueViewport.setViewportView(txtAMntUpdIssueReported);
		
		txtAMntUpdActionTaken = new JTextArea();
		srllPaneATViewport.setViewportView(txtAMntUpdActionTaken);
	
		
		
		JLabel lblMntUpdIssue = new JLabel("Reported Issue");
		lblMntUpdIssue.setBounds(10, 199, 89, 14);
		updateMaintenance.add(lblMntUpdIssue);
		
		JButton btnMntUpdate = new JButton("Update Record");
		btnMntUpdate.addActionListener(e -> { updateMaintenanceReport(); });
		btnMntUpdate.setBounds(10, 506, 274, 23);
		updateMaintenance.add(btnMntUpdate);
		
		JButton btnMntUpdateCancel = new JButton("Cancel");
		btnMntUpdateCancel.addActionListener(e -> { displayDashboard(); });
		btnMntUpdateCancel.setBounds(10, 531, 274, 23);
		updateMaintenance.add(btnMntUpdateCancel);
		
		JScrollPane updtMaintenanceViewport = new JScrollPane();
		updtMaintenanceViewport.setBounds(314, 44, 1076, 511);
		updateMaintenance.add(updtMaintenanceViewport);
		
		tblUpdReport = new JTable();
		updtMaintenanceViewport.setViewportView(tblUpdReport);
		tblUpdReport.setFillsViewportHeight(true);
		
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
	}
	
	void displayDashboard() {
		card.show(cardContainer, "dashboard");
		clearAllFields();
	}
	
	void displayNewBorrow() {
		card.show(cardContainer, "creation");
		formCard.show(formCreator, "borrowForm");
		refreshBorrowCreator();
	}
	
	void displayUpdateBorrow() {
		card.show(cardContainer, "updateBorrow");
		
	}
	
	void displayNewMaintenance() {
		card.show(cardContainer, "creation");
		formCard.show(formCreator, "maintenanceForm");
		refreshMaintenanceCreator();
	}
	
	void displayUpdateMaintenance() {
		card.show(cardContainer, "updateMaintenance");
		
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
		
		textField.setText("");
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
		
		tblMaintenancePreview.setModel(borrowPreview);
		tblMaintenancePreview.getTableHeader().setReorderingAllowed(false);
		
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
	}
	
	boolean verifyReportIntegrity(int ID) throws SPEARException {
		EquipmentDetails equip = backend.fetchEquipmentDetails(ID);
		System.out.println(equip.availability());
		if(!equip.availability().equals("Maintenance")) return true;
		
		return false;
	}
}

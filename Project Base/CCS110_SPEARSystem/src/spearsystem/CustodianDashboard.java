package spearsystem;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
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
import javax.swing.JToggleButton;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import spearsystem.DatabaseHandler.BorrowRecord;
import spearsystem.DatabaseHandler.MaintenanceReport;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class CustodianDashboard {

	
	private JPanel custodianDashboard;
	private MainClient parent;
	private DatabaseHandler backend;
	
	private String sessionUser;
	private int sessionID;
	
	private CardLayout card = new CardLayout(0,0);
	private CardLayout formCard = new CardLayout(0,0);
	private JComboBox<String> cmbCondition;
	private JComboBox<String> cmbCategory;
	private JComboBox<String> cmbAvailability;
	
	private JComboBox<String> cmbBorrowerName;
	private JPanel cardContainer;
	JPanel formCreator; // JPanel of the Forms

	private DefaultComboBoxModel<String> cmbCategoryModel;
	private DefaultComboBoxModel<String> cmbBorrowerModel;
	
	private JTextField searchBar;
	private JTable searchResult;
	private JTable tblBorrowPreview;
	
	private DefaultTableModel query;
	private DefaultTableModel borrowList;
	
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
	
	private JTextArea txtAMntIssue;
	private JTextArea txtAMntActionTaken;
	private JTextField txtBrwEquipID;
	private JTextField txtBrwEquipName;
	private JTextField txtBrwEquipTag;
	private JTextField txtReturnByDate;
	private JTextField txtDateBorrowed;
	private JTextField txtBrwEquipCondition;
	
	
	private Integer[] borrowerIDs;
	private JTextField txtMntEquipID;
	private JTextField txtMntEquipName;
	private JTextField txtMntEquipCondition;
	private JTextField txtMntEquipTag;
	private JTextField txtMntDateReported;
	private JTextField txtMntCost;
	/**
	 * Create the application.
	 */
	public CustodianDashboard(MainClient parent, DatabaseHandler backend) {
		this.parent = parent;
		this.backend = backend;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
//		frame = new JFrame();
//		frame.setBounds(10, 11, 1423, 654);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		backend = new DatabaseHandler();
		
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
		btnRefresh.addActionListener(e -> {workUpdateBorrowMenu();});
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
		searchResult.getTableHeader().setReorderingAllowed(false);
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
		txtBrwEquipID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
		});
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
		sprtrBrwTop_1.setBounds(10, 240, 274, 6);
		createBorrow.add(sprtrBrwTop_1);
		
		JLabel lblBorrowerDetails = new JLabel("Borrower Details");
		lblBorrowerDetails.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBorrowerDetails.setBounds(10, 247, 113, 18);
		createBorrow.add(lblBorrowerDetails);
		
		JButton btnBrwLoadID = new JButton("Select");
		btnBrwLoadID.addActionListener(e -> { loadItem(0); });
		btnBrwLoadID.setBounds(213, 62, 71, 20);
		createBorrow.add(btnBrwLoadID);
		
		cmbBorrowerModel = new DefaultComboBoxModel<String>();
		
		cmbBorrowerName = new JComboBox<String>();
		cmbBorrowerName.setModel(cmbBorrowerModel);
		cmbBorrowerName.setBounds(10, 286, 274, 20);
		createBorrow.add(cmbBorrowerName);
		
		JLabel lblBorrowerName = new JLabel("Borrower");
		lblBorrowerName.setBounds(10, 275, 82, 12);
		createBorrow.add(lblBorrowerName);
		
		txtReturnByDate = new JTextField();
		txtReturnByDate.setBounds(10, 367, 113, 20);
		createBorrow.add(txtReturnByDate);
		txtReturnByDate.setColumns(10);
		
		txtDateBorrowed = new JTextField();
		txtDateBorrowed.setText("2000-12-19");
		txtDateBorrowed.setEditable(false);
		txtDateBorrowed.setColumns(10);
		txtDateBorrowed.setBounds(10, 327, 113, 20);
		createBorrow.add(txtDateBorrowed);
		
		JLabel lblDateBorrowed = new JLabel("Date Borrowed:");
		lblDateBorrowed.setBounds(10, 313, 96, 14);
		createBorrow.add(lblDateBorrowed);
		
		JLabel lblRetunByDate = new JLabel("Return by Date:");
		lblRetunByDate.setBounds(10, 355, 96, 14);
		createBorrow.add(lblRetunByDate);
		
		txtBrwEquipCondition = new JTextField();
		txtBrwEquipCondition.setEditable(false);
		txtBrwEquipCondition.setColumns(10);
		txtBrwEquipCondition.setBounds(10, 157, 82, 18);
		createBorrow.add(txtBrwEquipCondition);
		
		JLabel lblBrwEquipCondition = new JLabel("Condition");
		lblBrwEquipCondition.setBounds(10, 139, 82, 12);
		createBorrow.add(lblBrwEquipCondition);
		
		JButton btnBrwSubmit = new JButton("Create Record");
		btnBrwSubmit.addActionListener(e -> { createBorrowRecord(); });
		btnBrwSubmit.setBounds(10, 426, 151, 23);
		createBorrow.add(btnBrwSubmit);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(e -> { displayDashboard(); });
		btnCancel.setBounds(171, 426, 113, 23);
		createBorrow.add(btnCancel);
		
		JLabel lblyyyymmdd = new JLabel("(YYYY-MM-DD)");
		lblyyyymmdd.setHorizontalAlignment(SwingConstants.CENTER);
		lblyyyymmdd.setForeground(new Color(192, 192, 192));
		lblyyyymmdd.setBounds(10, 386, 96, 14);
		createBorrow.add(lblyyyymmdd);
		
		JToggleButton tglReturnDateIndefinite = new JToggleButton("Indefinite");
		tglReturnDateIndefinite.setBounds(133, 366, 82, 23);
		createBorrow.add(tglReturnDateIndefinite);
		
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
		lblDateReported.setBounds(171, 185, 113, 14);
		createMaintenance.add(lblDateReported);
		
		txtMntDateReported = new JTextField();
		txtMntDateReported.setEditable(false);
		txtMntDateReported.setColumns(10);
		txtMntDateReported.setBounds(171, 199, 113, 20);
		createMaintenance.add(txtMntDateReported);
		
		JLabel lblMntIssue = new JLabel("Reported Issue:");
		lblMntIssue.setBounds(10, 205, 113, 14);
		createMaintenance.add(lblMntIssue);
		
		JLabel lblMntAction = new JLabel("Actions Taken:");
		lblMntAction.setBounds(10, 326, 89, 14);
		createMaintenance.add(lblMntAction);
		
		JScrollPane txtMntIssueViewport = new JScrollPane();
		txtMntIssueViewport.setBounds(10, 224, 274, 95);
		createMaintenance.add(txtMntIssueViewport);
		
		txtAMntIssue = new JTextArea();
		txtMntIssueViewport.setViewportView(txtAMntIssue);
		
		JScrollPane txtMntIssueViewport_1 = new JScrollPane();
		txtMntIssueViewport_1.setBounds(10, 343, 274, 95);
		createMaintenance.add(txtMntIssueViewport_1);
		
		txtAMntActionTaken = new JTextArea();
		txtMntIssueViewport_1.setViewportView(txtAMntActionTaken);
		
		JLabel lblRepairCost = new JLabel("Maintenance Cost          ₱");
		lblRepairCost.setBounds(10, 446, 141, 14);
		createMaintenance.add(lblRepairCost);
		
		txtMntCost = new JTextField();
		txtMntCost.setBounds(151, 443, 133, 20);
		createMaintenance.add(txtMntCost);
		txtMntCost.setColumns(10);
		
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
		borrowTablePreview.setBounds(436, 11, 471, 544);
		main.add(borrowTablePreview);
		borrowTablePreview.setLayout(null);
		
		JScrollPane brwPreviewViewport = new JScrollPane();
		brwPreviewViewport.setBounds(10, 93, 451, 440);
		borrowTablePreview.add(brwPreviewViewport);
		
		tblBorrowPreview = new JTable();
		brwPreviewViewport.setViewportView(tblBorrowPreview);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 0, 255));
		panel.setForeground(new Color(0, 0, 255));
		panel.setBounds(0, 0, 371, 566);
		main.add(panel);
		panel.setLayout(null);
		
		JButton btnCreateBorrow = new JButton("New Borrow Record");
		btnCreateBorrow.addActionListener(e -> { displayNewBorrow(); });
		btnCreateBorrow.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnCreateBorrow.setBorderPainted(false);
		btnCreateBorrow.setFocusable(false);
		btnCreateBorrow.setBackground(new Color(0, 0, 255));
		btnCreateBorrow.setForeground(new Color(255, 255, 255));
		btnCreateBorrow.setBounds(10, 69, 351, 21);
		panel.add(btnCreateBorrow);
		
		JButton btnUpdateBorrow = new JButton("Update Borrow Record");
		btnUpdateBorrow.setForeground(Color.WHITE);
		btnUpdateBorrow.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnUpdateBorrow.setFocusable(false);
		btnUpdateBorrow.setBorderPainted(false);
		btnUpdateBorrow.setBackground(Color.BLUE);
		btnUpdateBorrow.setBounds(10, 101, 351, 21);
		panel.add(btnUpdateBorrow);
		
		JButton btnUpdateMaintenanceReport = new JButton("Update Maintenance Report");
		btnUpdateMaintenanceReport.setForeground(Color.WHITE);
		btnUpdateMaintenanceReport.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnUpdateMaintenanceReport.setFocusable(false);
		btnUpdateMaintenanceReport.setBorderPainted(false);
		btnUpdateMaintenanceReport.setBackground(Color.BLUE);
		btnUpdateMaintenanceReport.setBounds(10, 165, 351, 21);
		panel.add(btnUpdateMaintenanceReport);
		
		JButton btnCreateMaintenance = new JButton("New Maintenance Report");
		btnCreateMaintenance.addActionListener(e -> { displayNewMaintenance(); });
		btnCreateMaintenance.setForeground(Color.WHITE);
		btnCreateMaintenance.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnCreateMaintenance.setFocusable(false);
		btnCreateMaintenance.setBorderPainted(false);
		btnCreateMaintenance.setBackground(Color.BLUE);
		btnCreateMaintenance.setBounds(10, 133, 351, 21);
		panel.add(btnCreateMaintenance);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 41, 1380, 2);
		custodianDashboard.add(separator);
		
		cardContainer.add(main, "dashboard");
		cardContainer.add(borrowFormCreation, "creation");
		
		formCreator.add(createBorrow, "borrowForm");
		formCreator.add(createMaintenance, "maintenanceForm");
		
		card.show(cardContainer, "dashboard");
		
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

	private void workUpdateBorrowMenu() {
		SwingWorker<?, ?> e = new SwingWorker<>() {

			@Override
			protected Object doInBackground() throws Exception {
				refreshBorrowCreator();
				return null;
			}
		};
		
		e.execute();
	}
	
	void displayDashboard() {
		card.show(cardContainer, "dashboard");
		
	}
	
	void displayNewBorrow() {
		card.show(cardContainer, "creation");
		formCard.show(formCreator, "borrowForm");
		refreshBorrowCreator();
	}
	
	void displayNewMaintenance() {
		card.show(cardContainer, "creation");
		formCard.show(formCreator, "maintenanceForm");
		refreshMaintenanceCreator();
	}
	
	void displayValues() {
		
		parent.log(true, "Fetching Inventory from Database...");
		
		String[][] list;
			list = backend.getEquipmentList();
		
		if(list == null) {
			parent.log(false, "An error was encountered while fetching Inventory from the Database. Try refreshing the table.");
			return;
			
		}
			
		query = new DefaultTableModel(list.length, 13) {
			  @Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }	
		};
		
		for(int r = 0; r < list.length; r++)
			for (int c = 0; c < list[r].length; c++)
				query.setValueAt(list[r][c], r, c);
		
		searchResult.setModel(query);
		inventorySorter.setModel(query);
		searchResult.setRowSorter(inventorySorter);
		
		parent.log(true, "Inventory fetch successful. Now Displaying Inventory.");
		parent.log(true, "Inventory View Update completed successfully.");
		
	}
	
	void refreshBorrowCreator() {
		
		displayValues();
		UpdateFilterBox();
		loadBorrowers();
		setCurrentDate(0);
		
	}
	
	void refreshMaintenanceCreator() {
		displayValues();
		UpdateFilterBox();
		setCurrentDate(1);
	}
	
	void UpdateFilterBox() {
		parent.log(true, "Updating Inventory Categories...");
		String[] values = backend.getInventoryFilters();
		
		if(values == null) {
			parent.log(false, "Category Update failed. Try refreshing the table.");
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
	
	void loadItem(int target) {
		int id;
		JTextField targetField;
		if(target == 0) targetField = txtBrwEquipID;
		else targetField = txtMntEquipID;
		
		try {
		id = Integer.parseInt(targetField.getText().trim());
		} catch(NumberFormatException e) {
			
			parent.log(false, "Equipment field is empty.");
			return;
		}
		String[] values;
		
		values = backend.fetchEquipmentDetails(id);
		
		if(values.length <= 0) { parent.log(false, "Failed to Load Equipment: Selected item does not exist."); return; }
		
		switch(target) {
		case 0: borrowLoadValues(values[1], values[2], values[10]); break;
		case 1: maintenanceLoadValues(values[1], values[2], values[10]); break;
		}
		parent.log(true, "Target Equipment Loaded successfully.");
	}
	
	void maintenanceLoadValues(String name, String tag, String condition) {
		txtMntEquipName.setText(name);
		txtMntEquipTag.setText(tag);
		txtMntEquipCondition.setText(condition);
		
	}
	
	void borrowLoadValues(String name, String tag, String condition) {
		txtBrwEquipName.setText(name);
		txtBrwEquipTag.setText(tag);
		txtBrwEquipCondition.setText(condition);
		
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------
	// BORROW CREATION METHODS
	//---------------------------------------------------------------------------------------------------------------------------------
	
	
	void loadBorrowers() {
		parent.log(true, "Updating Borrower List...");
		String[] values = backend.getBorrowerList();
		borrowerIDs = backend.getBorrowerIDList();
		if(values == null) {
			parent.log(false, "Borrower Update failed. Try refreshing the table.");
			return;
		}
		
		cmbBorrowerModel.removeAllElements();
		cmbBorrowerModel.addElement("[Borrower]");
		for (int i = 0; i < values.length; i++)
		{
			cmbBorrowerModel.addElement(values[i]);

			System.out.print(values[i] + " ");
		}
		
		parent.log(true, "Borrower List Updated successfully.");
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
		int borrowerID;
		int equipID; boolean check;
		Date returnByDate;
		Date dateBorrowed = Date.valueOf(txtDateBorrowed.getText().trim());
		
		borrowerID = getSelectedBorrower();
		if(borrowerID == -1) {
			
			parent.log(false, "No Borrower Selected.");
			return;	
		}
		
		
		try {
		equipID = Integer.parseInt(txtBrwEquipID.getText().trim());
		} catch (NumberFormatException e) {
			parent.log(false, "Equipment field is empty.");
			return;
		}
		
		check = backend.checkItemValidity(equipID);
		if (!check) {
			
			parent.log(false, "Selected Equipment does not exist.");
			return;
		}
		
		
		
		try {
		returnByDate = Date.valueOf(txtReturnByDate.getText().trim());
		} catch (Exception e) {
			
		returnByDate = null;
			
		}
		
		
		int custodian = 1;
		String conditionOnBorrow = txtBrwEquipCondition.getText().trim();
		
		
		BorrowRecord record = new BorrowRecord(borrowerID, equipID, dateBorrowed, returnByDate, null, custodian, conditionOnBorrow, null);
		
		
		boolean result = backend.createBorrowRecord(record);
						 backend.updateEquipmentAvailability(1, equipID);
		if(result) { parent.log(result, "New Maintenance Record created."); }
		else parent.log(result, "Failed to create New Maintenance Record");
	}
	
	Integer getSelectedBorrower() {
		int val = cmbBorrowerName.getSelectedIndex() - 1;
		if(val < 0) return -1;
		else return borrowerIDs[val];
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------
	// MAINTENANCE CREATION METHODS
	//---------------------------------------------------------------------------------------------------------------------------------
	
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
		int equipID; boolean check;
		String issue; String action = null;
		double cost;
		String status = "In Progress";
		
		Date dateReported = Date.valueOf(txtMntDateReported.getText().trim());
		
		try {
		equipID = Integer.parseInt(txtMntEquipID.getText().trim());
		} catch (NumberFormatException e) {
			parent.log(false, "Equipment field is empty.");
			return;
		}
		
		check = backend.checkItemValidity(equipID);
		if (!check) {
			
			parent.log(false, "Selected Equipment does not exist.");
			return;
		}
		
		issue = txtAMntIssue.getText();
		if(issue.isBlank() || issue.isEmpty()) {
				
			parent.log(false, "Empty Value: Must have a valid 'Report Issue'.");
			return;
			
		}
		
		try {
			cost = simplifyCost(Double.parseDouble(txtMntCost.getText()));
		} catch(NumberFormatException e) {
			
			parent.log(false, "Cost Input is invalid.");
			return;
		}
		
		if(cost >= 999999999) {
			parent.log(false, "Cost Input is invalid.");
			return;
			
		}
		
		int custodian = 1;
		
		
		MaintenanceReport report = new MaintenanceReport(equipID, dateReported, custodian, issue, action, cost, status);
		
		
		boolean result = backend.createMaintenanceReport(report);
						 backend.updateEquipmentAvailability(1, equipID);
		if(result) { parent.log(result, "New Maintenance Record created."); }
		else parent.log(result, "Failed to create New Maintenance Record");
	}
	
	double simplifyCost(double val) {
		
		BigDecimal bd = BigDecimal.valueOf(val);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
}

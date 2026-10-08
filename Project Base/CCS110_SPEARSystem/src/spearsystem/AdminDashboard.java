package spearsystem;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
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

import kenUI.ButtonOne;
import kenUI.GradientPanel;
import spearsystem.CustodianDatabaseHandler.BorrowRecord;
import spearsystem.CustodianDatabaseHandler.BorrowerList;
import spearsystem.CustodianDatabaseHandler.EquipmentDetails;
import spearsystem.CustodianDatabaseHandler.MaintenanceReport;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JCheckBox;
import javax.swing.ImageIcon;

public class AdminDashboard {

	final String[] RECORDSTATUS = { "Returned", "Borrowed" };

	final String[] EQUIPCONDITIONS = { "Good", "Worn", "Damaged" };
	final String[] EQUIPAVAILABILITY = { "Available", "Borrowed", "Maintenance" };

	private JPanel custodianDashboard;
	private MainClient parent;
	private CustodianHandler backend;

	private CardLayout card = new CardLayout(0, 0);
	private CardLayout formCard = new CardLayout(0, 0);

	private JComboBox<String> cmbCondition;
	private JComboBox<String> cmbCategory;
	private JComboBox<String> cmbAvailability;
	private JComboBox<String> cmbMaintenanceStatus;
	private JComboBox<String> cmbConditionOnReturn;
	private JComboBox<String> cmbBorrowStatus;
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
	private JTable tblborrowRecord;

	private DefaultTableModel query;
	private DefaultTableModel borrowList;

	private JTextArea txtAMntIssue;
	private JTextArea txtAMntActionTaken;
	private JTextField txtBrwEquipID;
	private JTextField txtBrwEquipName;
	private JTextField txtBrwEquipTag;
	private JTextField txtBrwReturnByDate;
	private JTextField txtDateBorrowed;
	private JTextField txtBrwEquipCondition;

	private Integer[] borrowerIDs;
	private JTextField txtMntEquipID;
	private JTextField txtMntEquipName;
	private JTextField txtMntEquipCondition;
	private JTextField txtMntEquipTag;
	private JTextField txtMntDateReported;
	private JTextField txtMntCost;
	private JTextField txtBrwDateReturned;
	private JTextField txtbrwSearchBar;
	private JToggleButton toggleUpdMarkAsReturned;
	private JCheckBox checkUpdateItems;

	private TableRowSorter<DefaultTableModel> inventorySorter = new TableRowSorter<>(query);
	private RowFilter<DefaultTableModel, Integer> inventoryRF = new RowFilter<>() {
		@Override
		@SuppressWarnings("rawtypes")
		public boolean include(Entry entry) {

			String searchFilter = searchBar.getText();
			String categoryFilter = cmbCategory.getSelectedItem().toString();
			String conditionFilter = cmbCondition.getSelectedItem().toString();
			String availabilityFilter = cmbAvailability.getSelectedItem().toString();

			// searchBar
			String id = entry.getStringValue(0);
			String tag = entry.getStringValue(1);
			String name = entry.getStringValue(2);

			if (!(searchFilter.isBlank() || searchFilter.isEmpty())
					&& !(id.contains(searchFilter) || tag.contains(searchFilter) || name.contains(searchFilter)))
				return false;

			if (!categoryFilter.equals("[Category]") && !entry.getStringValue(3).equals(categoryFilter))
				return false;

			if (!conditionFilter.equals("[Condition]") && !entry.getStringValue(11).equals(conditionFilter))
				return false;

			if (!availabilityFilter.equals("[Availability]") && !entry.getStringValue(12).equals(availabilityFilter))
				return false;
			return true;
		}

	};

	private TableRowSorter<DefaultTableModel> borrowRecordSorter = new TableRowSorter<>(borrowList);
	private RowFilter<DefaultTableModel, Integer> borrowRecordRF = new RowFilter<>() {
		@Override
		@SuppressWarnings("rawtypes")
		public boolean include(Entry entry) {

			String searchFilter = txtbrwSearchBar.getText();
			String receiverFilter = cmbReceivedBy.getSelectedItem().toString();
			String statusFilter = cmbStatus.getSelectedItem().toString();

			// searchBar
			String id = entry.getStringValue(0);
			String tag = entry.getStringValue(1);
			String name = entry.getStringValue(2);

			if (!(searchFilter.isBlank() || searchFilter.isEmpty())
					&& !(id.contains(searchFilter) || tag.contains(searchFilter) || name.contains(searchFilter)))
				return false;

			if (!receiverFilter.equals("[Custodian]") && !entry.getStringValue(11).equals(receiverFilter))
				return false;

			if (!statusFilter.equals("[Status]") && !entry.getStringValue(12).equals(statusFilter))
				return false;
			return true;
		}

	};

	KeyAdapter numericalOnly = new KeyAdapter() {
		@Override
		public void keyTyped(KeyEvent e) {
			if (!Character.isDigit(e.getKeyChar()))
				e.consume();
		}
	};

	DocumentFilter moneyInput = new DocumentFilter() {
		public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet a)
				throws BadLocationException {

			String txt = fb.getDocument().getText(0, fb.getDocument().getLength());
			txt += str;
			if ((fb.getDocument().getLength() + str.length() - length) <= 20
					&& txt.matches("^[0-9]+[.]?[0-9]{0,10}$")) {
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

	/**
	 * Create the application.
	 */
	public AdminDashboard(MainClient parent, CustodianHandler backend) {
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

		backend = new CustodianHandler();

		custodianDashboard = new JPanel();
		custodianDashboard.setLayout(null);
		custodianDashboard.setBounds(10, 54, 1283, 700);

		query = new DefaultTableModel();
		inventorySorter.setRowFilter(null);

		cardContainer = new JPanel();
		cardContainer.setBackground(new Color(255, 255, 255));
		cardContainer.setBounds(0, 72, 1284, 628);
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
		cmbCondition
				.setModel(new DefaultComboBoxModel<String>(new String[] { "[Condition]", "Good", "Damaged", "Worn" }));
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
		btnRefresh.addActionListener(e -> {
			workUpdateBorrowMenu();
		});
		btnRefresh.setBounds(1014, 0, 62, 23);
		inventoryTable.add(btnRefresh);

		cmbAvailability = new JComboBox<String>();
		cmbAvailability.setModel(
				new DefaultComboBoxModel<>(new String[] { "[Availability]", "Available", "In Use", "In Maintenance" }));
		cmbAvailability.setBounds(428, 0, 97, 22);
		inventoryTable.add(cmbAvailability);

		JButton btnFilter = new JButton("⌕");
		btnFilter.addActionListener(e -> {
			inventorySorter.setRowFilter(inventoryRF);
		});
		btnFilter.setBounds(535, 0, 62, 23);
		inventoryTable.add(btnFilter);

		JButton btnClearFilter = new JButton("Clear");
		btnClearFilter.addActionListener(e -> {
			inventorySorter.setRowFilter(null);
		});
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
		btnBrwLoadID.addActionListener(e -> {
			loadItem(0);
		});
		btnBrwLoadID.setBounds(213, 62, 71, 20);
		createBorrow.add(btnBrwLoadID);

		cmbBorrowerModel = new DefaultComboBoxModel<String>();

		cmbBorrowerName = new JComboBox<String>();
		cmbBorrowerName.setModel(cmbBorrowerModel);
		cmbBorrowerName.setBounds(10, 232, 274, 20);
		createBorrow.add(cmbBorrowerName);

		JLabel lblBorrowerName = new JLabel("Borrower");
		lblBorrowerName.setBounds(10, 221, 82, 12);
		createBorrow.add(lblBorrowerName);

		txtBrwReturnByDate = new JTextField();
		txtBrwReturnByDate.setBounds(10, 313, 113, 20);
		createBorrow.add(txtBrwReturnByDate);
		txtBrwReturnByDate.setColumns(10);

		txtDateBorrowed = new JTextField();
		txtDateBorrowed.setText("2000-12-19");
		txtDateBorrowed.setEditable(false);
		txtDateBorrowed.setColumns(10);
		txtDateBorrowed.setBounds(10, 273, 113, 20);
		createBorrow.add(txtDateBorrowed);

		JLabel lblDateBorrowed = new JLabel("Date Borrowed:");
		lblDateBorrowed.setBounds(10, 259, 96, 14);
		createBorrow.add(lblDateBorrowed);

		JLabel lblRetunByDate = new JLabel("Return by Date:*");
		lblRetunByDate.setBounds(10, 301, 96, 14);
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
		btnBrwSubmit.addActionListener(e -> {
			createBorrowRecord();
		});
		btnBrwSubmit.setBounds(10, 510, 151, 23);
		createBorrow.add(btnBrwSubmit);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(e -> {
			displayDashboard();
		});
		btnCancel.setBounds(171, 510, 113, 23);
		createBorrow.add(btnCancel);

		JLabel lblyyyymmdd = new JLabel("(YYYY-MM-DD)");
		lblyyyymmdd.setHorizontalAlignment(SwingConstants.CENTER);
		lblyyyymmdd.setForeground(new Color(192, 192, 192));
		lblyyyymmdd.setBounds(10, 332, 113, 14);
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
		btnMntLoadID.addActionListener(e -> {
			loadItem(1);
		});
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
		// txtMntCost.addKeyListener(numericsAndDecimal);
		AbstractDocument mntCostdoc = (AbstractDocument) txtMntCost.getDocument();
		mntCostdoc.setDocumentFilter(moneyInput);
		txtMntCost.setBounds(151, 443, 133, 20);
		createMaintenance.add(txtMntCost);
		txtMntCost.setColumns(10);

		JButton btnMntSubmit = new JButton("Create Report");
		btnMntSubmit.addActionListener(e -> {
			createMaintenanceReport();
		});
		btnMntSubmit.setBounds(10, 510, 151, 23);
		createMaintenance.add(btnMntSubmit);

		JButton btnMntCancel = new JButton("Cancel");
		btnMntCancel.addActionListener(e -> {
			displayDashboard();
		});
		btnMntCancel.setBounds(171, 510, 113, 23);
		createMaintenance.add(btnMntCancel);

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
		button.addActionListener(e -> {
			displayNewBorrow();
		});
		button.setBounds(24, 145, 225, 55);
		sidebar.add(button);
		button.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		button.setText("Register Custodian");
		// -

		// Custom Button 2
		ButtonOne button2 = new ButtonOne(new Color(231, 76, 60), new Color(192, 57, 43));
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button2.setBounds(24, 281, 225, 55);
		sidebar.add(button2);
		button2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		button2.setText("Delete Custodian");
		// -

		// Custom Button 3
		ButtonOne button3 = new ButtonOne(new Color(230, 126, 34), new Color(211, 84, 0));
		button3.addActionListener(e -> {
			displayUpdateBorrow();
		});
		button3.setBounds(24, 211, 225, 55);
		sidebar.add(button3);
		button3.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		button3.setText("Update Custodian");
		// -

		// Custom Button 4
		ButtonOne button4 = new ButtonOne(new Color(255, 105, 180), new Color(219, 48, 130));
		button4.addActionListener(e -> {
			displayNewMaintenance();
		});
		button4.setBounds(28, 395, 99, 55);
		sidebar.add(button4);
		button4.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		button4.setText("View");
		
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
		separator_2_1.setBounds(24, 347, 225, 2);
		sidebar.add(separator_2_1);
		
		ImageIcon originalIcon2 = new ImageIcon(LoginScreen.class.getResource("/ui/custodian.png"));
		Image resizedImage2 = originalIcon2.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			
		
		ButtonOne button2_1 = new ButtonOne(new Color(26, 188, 156), new Color(22, 160, 133));
		button2_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button2_1.setText("Edit");
		button2_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		button2_1.setBounds(150, 395, 95, 55);
		sidebar.add(button2_1);
		
		ButtonOne button2_1_1 = new ButtonOne(new Color(155, 89, 182), new Color(142, 68, 173)
);
		button2_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button2_1_1.setText("Borrowing");
		button2_1_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		button2_1_1.setBounds(28, 516, 99, 55);
		sidebar.add(button2_1_1);
		
		JLabel lblWhatHere = new JLabel("Inventory Management");
		lblWhatHere.setForeground(new Color(30, 144, 255));
		lblWhatHere.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblWhatHere.setBounds(24, 360, 225, 27);
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
		whiteOutline_1.setBounds(20, 207, 233, 64);
		sidebar.add(whiteOutline_1);
		
		ButtonOne whiteOutline_1_1 = new ButtonOne(new Color(190, 55, 120), Color.BLACK);
		whiteOutline_1_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		whiteOutline_1_1.setEnabled(false);
		whiteOutline_1_1.setBounds(24, 391, 107, 64);
		sidebar.add(whiteOutline_1_1);
		
		ButtonOne whiteOutline_1_1_1 = new ButtonOne(new Color(160, 45, 38), Color.BLACK);
		whiteOutline_1_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		whiteOutline_1_1_1.setText("New Borrow Record");
		whiteOutline_1_1_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		whiteOutline_1_1_1.setEnabled(false);
		whiteOutline_1_1_1.setBounds(20, 277, 233, 64);
		sidebar.add(whiteOutline_1_1_1);
		
		ButtonOne whiteOutline_1_1_1_1 = new ButtonOne(new Color(15, 135, 120), Color.BLACK);
		whiteOutline_1_1_1_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		whiteOutline_1_1_1_1.setEnabled(false);
		whiteOutline_1_1_1_1.setBounds(146, 391, 103, 64);
		sidebar.add(whiteOutline_1_1_1_1);
		
		ButtonOne whiteOutline_1_1_1_1_1 = new ButtonOne(new Color(112, 55, 145), Color.BLACK);
		whiteOutline_1_1_1_1_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		whiteOutline_1_1_1_1_1.setEnabled(false);
		whiteOutline_1_1_1_1_1.setBounds(24, 512, 107, 64);
		sidebar.add(whiteOutline_1_1_1_1_1);
		
		JSeparator separator_2_1_1 = new JSeparator();
		separator_2_1_1.setForeground(new Color(30, 144, 255));
		separator_2_1_1.setBounds(24, 461, 225, 2);
		sidebar.add(separator_2_1_1);
		
		JLabel lblWhatHere_1 = new JLabel("Inventory Management");
		lblWhatHere_1.setForeground(new Color(30, 144, 255));
		lblWhatHere_1.setFont(new Font("Segoe UI Black", Font.PLAIN, 19));
		lblWhatHere_1.setBounds(24, 474, 225, 27);
		sidebar.add(lblWhatHere_1);
		
		ButtonOne button2_1_1_1 = new ButtonOne(new Color(231, 76, 60), new Color(192, 57, 43));
		button2_1_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button2_1_1_1.setText("Maintenance");
		button2_1_1_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		button2_1_1_1.setBounds(138, 516, 107, 55);
		sidebar.add(button2_1_1_1);
		
		ButtonOne whiteOutline_1_1_1_1_1_1 = new ButtonOne(new Color(160, 45, 38), Color.BLACK);
		whiteOutline_1_1_1_1_1_1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
		whiteOutline_1_1_1_1_1_1.setEnabled(false);
		whiteOutline_1_1_1_1_1_1.setBounds(134, 512, 115, 64);
		sidebar.add(whiteOutline_1_1_1_1_1_1);
		
		

		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(30, 144, 255));
		separator.setBounds(10, 62, 1380, 2);
		custodianDashboard.add(separator);

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
		
		Object[][] testData = {
		    {"Laser Cutter", "ICT equipment", "Active"},
		    {"3D Printer", "AV Equipment", "Maintenance"},
		    {"CNC Router", "Photography", "Idle"}
		};
		String[] columnNames = {"ItemName", "Category", "Status"};
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
		cardContainer.add(borrowFormCreation, "creation");

		Object[][] borrowingData = {
		    {"Laser Cutter", "Felonia", "2026-10-08", "Pending"},
		    {"3D Printer", "Losamia", "2026-10-07", "Approved"},
		    {"CNC Router", "Paz", "2026-10-05", "Returned"}
		};
		String[] borrowingColumns = {"Item", "Borrower", "Date", "Status"};

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

		cardContainer.add(borrowFormCreation, "creation");
		
		formCreator.add(createBorrow, "borrowForm");

		JLabel lblyyyymmdd_1 = new JLabel("(YYYY-MM-DD)");
		lblyyyymmdd_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblyyyymmdd_1.setForeground(Color.LIGHT_GRAY);
		lblyyyymmdd_1.setBounds(154, 332, 113, 14);
		createBorrow.add(lblyyyymmdd_1);

		txtBrwDateReturned = new JTextField();
		txtBrwDateReturned.setColumns(10);
		txtBrwDateReturned.setBounds(154, 313, 113, 20);
		createBorrow.add(txtBrwDateReturned);

		JLabel lblDateReturned = new JLabel("Date Returned:*");
		lblDateReturned.setBounds(154, 301, 96, 14);
		createBorrow.add(lblDateReturned);

		JLabel lblNote = new JLabel("* : If applicable");
		lblNote.setForeground(new Color(128, 128, 128));
		lblNote.setBounds(10, 485, 257, 14);
		createBorrow.add(lblNote);

		cmbBorrowStatus = new JComboBox<String>();
		cmbBorrowStatus.setModel(new DefaultComboBoxModel<>(RECORDSTATUS));
		cmbBorrowStatus.setBounds(151, 272, 116, 22);
		createBorrow.add(cmbBorrowStatus);

		JLabel lblBorrowStatus = new JLabel("Borrow Status:");
		lblBorrowStatus.setBounds(154, 259, 96, 14);
		createBorrow.add(lblBorrowStatus);

		JLabel lblBrwEquipCondOnReturn = new JLabel("Condition");
		lblBrwEquipCondOnReturn.setBounds(154, 139, 82, 12);
		createBorrow.add(lblBrwEquipCondOnReturn);

		cmbConditionOnReturn = new JComboBox<String>();
		cmbConditionOnReturn.setModel(new DefaultComboBoxModel<>(EQUIPCONDITIONS));
		cmbConditionOnReturn.setBounds(151, 155, 133, 22);
		createBorrow.add(cmbConditionOnReturn);
		formCreator.add(createMaintenance, "maintenanceForm");

		JLabel lblMaintenanceStatus = new JLabel("Maintenance Status:");
		lblMaintenanceStatus.setBounds(10, 471, 113, 14);
		createMaintenance.add(lblMaintenanceStatus);

		cmbMaintenanceStatus = new JComboBox<>();
		cmbMaintenanceStatus.setModel(new DefaultComboBoxModel<>(new String[] { "In Progress", "Resolved" }));
		cmbMaintenanceStatus.setBounds(151, 467, 133, 22);
		createMaintenance.add(cmbMaintenanceStatus);

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
		btnRefresh_1.addActionListener(e -> {
			displayBorrowRecords();
		});
		btnRefresh_1.setBounds(1328, 11, 62, 23);
		updateBorrow.add(btnRefresh_1);

		JButton btnClearFilter_1 = new JButton("Clear");
		btnClearFilter_1.setBounds(921, 11, 75, 23);
		updateBorrow.add(btnClearFilter_1);

		JButton btnFilter_1 = new JButton("⌕");
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
		btnUpdSelect.addActionListener(e -> {
			LoadRecord();
		});
		btnUpdSelect.setBounds(111, 190, 89, 23);
		updateBorrow.add(btnUpdSelect);

		txtExpectedReturnDate = new JTextField();
		txtExpectedReturnDate.setBounds(20, 262, 274, 20);
		updateBorrow.add(txtExpectedReturnDate);
		txtExpectedReturnDate.setColumns(10);

		JLabel lblUpdReturnDateBy = new JLabel("Return by Date:");
		lblUpdReturnDateBy.setBounds(20, 247, 130, 14);
		updateBorrow.add(lblUpdReturnDateBy);

		txtDateReturned = new JTextField();
		txtDateReturned.setEditable(false);
		txtDateReturned.setColumns(10);
		txtDateReturned.setBounds(20, 308, 138, 20);
		updateBorrow.add(txtDateReturned);

		JLabel lblUpdDateReturned = new JLabel("Date Returned");
		lblUpdDateReturned.setBounds(20, 293, 130, 14);
		updateBorrow.add(lblUpdDateReturned);

		toggleUpdMarkAsReturned = new JToggleButton("Returned");
		toggleUpdMarkAsReturned.addChangeListener(e -> {
			enableUPDButtons();
		});
		toggleUpdMarkAsReturned.setBounds(173, 307, 121, 23);
		updateBorrow.add(toggleUpdMarkAsReturned);

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

		JButton btnMntCancel_1 = new JButton("Cancel");
		btnMntCancel_1.setBounds(160, 503, 134, 23);
		updateBorrow.add(btnMntCancel_1);

		JButton btnMntUpdate = new JButton("Update Record");
		btnMntUpdate.addActionListener(e -> {
			updateBorrowRecord();
		});
		btnMntUpdate.setBounds(20, 478, 274, 23);
		updateBorrow.add(btnMntUpdate);

		JButton btnDeleteRecord = new JButton("Delete");
		btnDeleteRecord.setBounds(20, 503, 130, 23);
		updateBorrow.add(btnDeleteRecord);

		checkUpdateItems = new JCheckBox("Update Equipment Condition");
		checkUpdateItems.setBounds(20, 382, 274, 23);
		updateBorrow.add(checkUpdateItems);
		
		JLabel lblAdmin = new JLabel("Admin Dashboard");
		lblAdmin.setIcon(new ImageIcon(resizedImage2));
		lblAdmin.setForeground(new Color(30, 144, 255));
		lblAdmin.setFont(new Font("Segoe UI Black", Font.PLAIN, 35));
		lblAdmin.setBounds(10, 0, 396, 64);
		custodianDashboard.add(lblAdmin);

		// frame.setVisible(true);

	}

	// ---------------------------------------------------------------------------------------------------------------------------------
	// UNIVERSAL HELPER METHODS
	// ---------------------------------------------------------------------------------------------------------------------------------

	JPanel getPanel() {
		return custodianDashboard;
	}

	void setCurrentDate(int target) {
		if (target == 0)
			txtDateBorrowed.setText(String.valueOf(LocalDate.now()));
		else if (target == 1)
			txtMntDateReported.setText(String.valueOf(LocalDate.now()));

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

	void displayUpdateBorrow() {
		card.show(cardContainer, "updateBorrow");

	}

	void displayNewMaintenance() {
		card.show(cardContainer, "creation");
		formCard.show(formCreator, "maintenanceForm");
		refreshMaintenanceCreator();
	}

	void displayBorrowRecords() {

		parent.log(true, "Fetching Inventory from Database...");

		String[][] list;

		try {
			list = backend.getBorrowedRecordsList();
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}

		borrowList = new DefaultTableModel(list.length, 11) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		for (int r = 0; r < list.length; r++)
			for (int c = 0; c < list[r].length; c++)
				borrowList.setValueAt(list[r][c], r, c);

		tblborrowRecord.setModel(borrowList);
		borrowRecordSorter.setModel(borrowList);
		tblborrowRecord.setRowSorter(borrowRecordSorter);

		parent.log(true, "Record fetch successful. Now Displaying Inventory.");
		parent.log(true, "Borrow Record View Update completed successfully.");

	}

	void displayInventoryValues() {

		parent.log(true, "Fetching Inventory from Database...");

		String[][] list;

		try {
			list = backend.getEquipmentList();
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}

		query = new DefaultTableModel(list.length, 13) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};

		for (int r = 0; r < list.length; r++)
			for (int c = 0; c < list[r].length; c++)
				query.setValueAt(list[r][c], r, c);

		searchResult.setModel(query);
		inventorySorter.setModel(query);
		searchResult.setRowSorter(inventorySorter);

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
		for (int i = 0; i < values.length; i++) {
			cmbCategoryModel.addElement(values[i]);

			System.out.print(values[i] + " ");
		}

		parent.log(true, "Categories Updated successfully.");

	}

	void LoadRecord() {
		int id;

		try {
			id = Integer.parseInt(txtTransactionID.getText().trim());
		} catch (NumberFormatException e) {

			parent.log(false, "ID field is empty.");
			return;
		}

		BorrowRecord values;

		try {
			values = backend.fetchBorrowRecord(id);
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}

		borrowUpdateLoadValues(String.valueOf(values.custodian()), String.valueOf(values.ID()),
				String.valueOf(values.equipmentID()), String.valueOf(values.dateBorrowed()),
				String.valueOf(values.returbByDate()), String.valueOf(values.dateReturned()),
				String.valueOf(values.conditionOnBorrow()), String.valueOf(values.conditionOnReturn()),
				String.valueOf(values.status()));
	}

	void borrowUpdateLoadValues(String custodianID, String borrowerID, String equipmentID, String dateBorrowed,
			String returnByDate, String dateReturned, String conditionOnBorrow, String conditonOnReturn,
			String status) {
		txtUpdCustodian.setText(custodianID);
		txtUpdBorrowerID.setText(borrowerID);
		txtUpdBorrowedEquip.setText(equipmentID);
		txtBorrowDate.setText(dateBorrowed);
		txtUpdCondOnBorrow.setText(conditionOnBorrow);
		if (returnByDate != null && !returnByDate.equals("null"))
			txtExpectedReturnDate.setText(returnByDate);
		if (dateReturned != null && !dateReturned.equals("null"))
			txtDateReturned.setText(dateReturned);
		cmbCondOnReturn.setSelectedItem(conditonOnReturn);

		if (status.equals("Returned"))
			toggleUpdMarkAsReturned.setSelected(true);

	}

	void enableUPDButtons() {
		if (toggleUpdMarkAsReturned.isSelected()) {
			txtDateReturned.setEditable(true);
			cmbCondOnReturn.setEditable(true);
		} else {

			txtDateReturned.setEditable(false);
			cmbCondOnReturn.setEditable(false);

		}

	}

	void loadItem(int target) {
		int id;
		JTextField targetField;
		if (target == 0)
			targetField = txtBrwEquipID;
		else
			targetField = txtMntEquipID;

		try {
			id = Integer.parseInt(targetField.getText().trim());
		} catch (NumberFormatException e) {

			parent.log(false, "Equipment field is empty.");
			return;
		}
		EquipmentDetails values;

		try {
			values = backend.fetchEquipmentDetails(id);
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}

		boolean isAvailable = backend.isItemAvailable(id);
		if (!isAvailable) {
			parent.log(false, "Equipment is In Use/Under Maintenance and thus cannot accessed at the moment.");
			return;
		}

		switch (target) {
		case 0:
			borrowLoadValues(values.equipmentName(), values.propertyTag(), values.condition());
			break;
		case 1:
			maintenanceLoadValues(values.equipmentName(), values.propertyTag(), values.condition());
			break;
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
		for (int i = 0; i < values.length; i++) {
			cmbBorrowerModel.addElement(values[i]);

			System.out.print(values[i] + " ");
		}

		parent.log(true, "Borrower List Updated successfully.");
	}

	// ---------------------------------------------------------------------------------------------------------------------------------
	// CREATE, UPDATE AND DELETE
	// ---------------------------------------------------------------------------------------------------------------------------------

	void updateBorrowRecord() {
		String transactionID = txtTransactionID.getText().trim(), returnByDate = txtExpectedReturnDate.getText().trim(),
				dateReturned = txtDateReturned.getText().trim(),
				conditionOnReturn = cmbCondOnReturn.getSelectedItem().toString(),
				status = toggleUpdMarkAsReturned.isSelected() ? "Returned" : "Borrowed",
				updateItems = String.valueOf(checkUpdateItems.isSelected());

		boolean result;
		try {
			result = backend.UpdateRecord(transactionID, returnByDate, dateReturned, conditionOnReturn, status,
					updateItems);
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}

		if (result)
			parent.log(true, "Borrow Record sucessfully updated.");
	}

	/*
	 * Record Constructor Paremetres: int borrowerID, int equipmentID, String
	 * dateBorrowed, String returbByDate, String dateReturned, int custodian, String
	 * conditionOnBorrow, String conditionOnReturn
	 */
	void createBorrowRecord() {
		String borrowerID = String.valueOf(getSelectedBorrower()), equipID = txtBrwEquipID.getText(),
				dateBorrowed = txtDateBorrowed.getText().trim(), returnByDate = txtBrwReturnByDate.getText().trim(),
				dateReturned = txtBrwDateReturned.getText().trim(), custodian = String.valueOf(parent.getSessionID()),
				conditionOnBorrow = txtBrwEquipCondition.getText().trim(),
				conditionOnReturn = cmbConditionOnReturn.getSelectedItem().toString(),
				status = cmbBorrowStatus.getSelectedItem().toString();

		boolean result;

		try {
			result = backend.CreateRecord(borrowerID, equipID, dateBorrowed, returnByDate, dateReturned, custodian,
					conditionOnBorrow, conditionOnReturn, status);
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}

		if (result)
			parent.log(result, "Borrow Record successfully published to Database.");

	}

	Integer getSelectedBorrower() {
		int val = cmbBorrowerName.getSelectedIndex() - 1;
		if (val < 0)
			return -1;
		else
			return borrowerIDs[val];
	}

	// ---------------------------------------------------------------------------------------------------------------------------------
	// MAINTENANCE CREATION METHODS
	// ---------------------------------------------------------------------------------------------------------------------------------

	/*
	 * Report Constructor Parametres: int equipmentID, Date reportedDate, int
	 * custodian, String issueReport, String actionsTaken, double cost, String
	 * status
	 */

	void createMaintenanceReport() {
		String equipID = txtMntEquipID.getText(), dateReported = txtMntDateReported.getText().trim(),
				custodian = String.valueOf(parent.getSessionID()), issue = txtAMntIssue.getText(),
				action = txtAMntActionTaken.getText(), cost = txtMntCost.getText(),
				status = cmbMaintenanceStatus.getSelectedItem().toString();

		boolean result;
		try {
			result = backend.CreateReport(equipID, dateReported, custodian, issue, action, cost, status);
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}

		if (result)
			parent.log(result, "Maintenance Report successfully published to Database.");
	}
}

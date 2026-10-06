package spearsystem;

import java.awt.CardLayout;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JSeparator;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class CustodianDashboard {

	
	private JPanel custodianDashboard;
	private MainClient parent;
	private DatabaseHandler backend;
	
	private String sessionUser;
	
	private CardLayout card = new CardLayout(0,0);
	private CardLayout formCard = new CardLayout(0,0);
	private JComboBox<String> cmbCondition;
	private JComboBox<String> cmbCategory;
	private JComboBox<String> cmbAvailability;
	private JPanel cardContainer;
	JPanel formCreator; // JPanel of the Forms

	private DefaultComboBoxModel<String> cmbCategoryModel;
	
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
	private JTextField txtBrwEquipID;
	private JTextField txtBrwEquipName;
	private JTextField txtBrwEquipTag;
	
	

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
		cardContainer.add(borrowFormCreation, "creation");
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
		btnRefresh.addActionListener(e -> {displayValues();});
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
		formCreator.add(createBorrow, "borrowForm");
		createBorrow.setLayout(null);
		
		JLabel lblBorrowForm = new JLabel("New Borrow Form");
		lblBorrowForm.setFont(new Font("Trebuchet MS", Font.BOLD, 21));
		lblBorrowForm.setHorizontalAlignment(SwingConstants.CENTER);
		lblBorrowForm.setBounds(10, 10, 274, 30);
		createBorrow.add(lblBorrowForm);
		
		txtBrwEquipID = new JTextField();
		txtBrwEquipID.setBounds(10, 108, 60, 18);
		createBorrow.add(txtBrwEquipID);
		txtBrwEquipID.setColumns(10);
		
		JLabel lblBrowEQuipID = new JLabel("Equipment ID");
		lblBrowEQuipID.setHorizontalAlignment(SwingConstants.LEFT);
		lblBrowEQuipID.setBounds(10, 90, 96, 12);
		createBorrow.add(lblBrowEQuipID);
		
		txtBrwEquipName = new JTextField();
		txtBrwEquipName.setEditable(false);
		txtBrwEquipName.setBounds(10, 154, 182, 18);
		createBorrow.add(txtBrwEquipName);
		txtBrwEquipName.setColumns(10);
		
		JLabel lblBrwEquipName = new JLabel("Equipment Name");
		lblBrwEquipName.setBounds(10, 136, 101, 12);
		createBorrow.add(lblBrwEquipName);
		
		txtBrwEquipTag = new JTextField();
		txtBrwEquipTag.setEditable(false);
		txtBrwEquipTag.setBounds(202, 154, 82, 18);
		createBorrow.add(txtBrwEquipTag);
		txtBrwEquipTag.setColumns(10);
		
		JLabel lblBrwEquipTag = new JLabel("Property Tag");
		lblBrwEquipTag.setBounds(202, 136, 82, 12);
		createBorrow.add(lblBrwEquipTag);
		
		JSeparator sprtrBrwTop = new JSeparator();
		sprtrBrwTop.setBounds(10, 50, 274, 6);
		createBorrow.add(sprtrBrwTop);
		
		JLabel lblEquipDetails = new JLabel("Equipment Details");
		lblEquipDetails.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEquipDetails.setBounds(10, 57, 113, 18);
		createBorrow.add(lblEquipDetails);
		
		JSeparator sprtrBrwTop_1 = new JSeparator();
		sprtrBrwTop_1.setBounds(10, 240, 274, 6);
		createBorrow.add(sprtrBrwTop_1);
		
		JLabel lblBorrowerDetails = new JLabel("Borrower Details");
		lblBorrowerDetails.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBorrowerDetails.setBounds(10, 247, 113, 18);
		createBorrow.add(lblBorrowerDetails);
		
		JButton btnLoadID = new JButton("Load");
		btnLoadID.addActionListener(e -> { loadItem(); });
		btnLoadID.setBounds(200, 107, 84, 20);
		createBorrow.add(btnLoadID);
		
		JComboBox cmbBorroweName = new JComboBox();
		cmbBorroweName.setBounds(10, 286, 101, 20);
		createBorrow.add(cmbBorroweName);
		
		JLabel lblBorrowerName = new JLabel("Borrower");
		lblBorrowerName.setBounds(10, 275, 82, 12);
		createBorrow.add(lblBorrowerName);
		
		JPanel main = new JPanel();
		main.setBackground(new Color(255, 255, 255));
		cardContainer.add(main, "dashboard");
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
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(10, 69, 351, 21);
		panel.add(btnNewButton);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 41, 1380, 2);
		custodianDashboard.add(separator);
		
		
		
		card.show(cardContainer, "creation");
		
		//frame.setVisible(true);
		
	}
	
	JPanel getPanel() {
		return custodianDashboard;
	}
	
	
	void displayValues() {
		
		parent.log(true, "Fetching Inventory from Database...");
		
		String[][] list;
			list = backend.getEquipmentList();
		
		if(list == null) {
			parent.log(false, "An error was encountered while fetching Inventory from the Database. Try refreshing the table.");
			return;
			
		}
			
		query = new DefaultTableModel(list.length, 13);
		for(int r = 0; r < list.length; r++)
			for (int c = 0; c < list[r].length; c++)
				query.setValueAt(list[r][c], r, c);
		
		searchResult.setModel(query);
		inventorySorter.setModel(query);
		searchResult.setRowSorter(inventorySorter);
		
		parent.log(true, "Inventory fetch successful. Now Displaying Inventory.");
		UpdateFilterBox();
		
		parent.log(true, "Inventory View Update completed successfully.");
		
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
	
	void loadItem() {
		String[] values;
		
		try{
		values = backend.fetchEquipmentDetails(Integer.parseInt(txtBrwEquipID.getText().trim()));
		} catch(NumberFormatException e) {
			
			parent.log(false, "Failed to Load Equipment: Input is not a valid ID.");
			
			return;
		}
		
		borrowLoadValues(values[1], values[2]);
		
	}
	
	void borrowLoadValues(String name, String tag) {
		txtBrwEquipName.setText(name);
		txtBrwEquipTag.setText(tag);
		
	}
}

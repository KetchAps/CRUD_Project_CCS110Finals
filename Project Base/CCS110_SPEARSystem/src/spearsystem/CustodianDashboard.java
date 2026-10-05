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


public class CustodianDashboard {

	
	private JPanel custodianDashboard;
	private MainClient parent;
	private DatabaseHandler backend;
	
	private String sessionUser;
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
		//frame.getContentPane().add(custodianDashboard);
		
		JTabbedPane custodianWorkArea = new JTabbedPane(JTabbedPane.TOP);
		custodianWorkArea.setBounds(10, 54, 502, 547);
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
		scrollPane.setBounds(522, 79, 862, 522);
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

}

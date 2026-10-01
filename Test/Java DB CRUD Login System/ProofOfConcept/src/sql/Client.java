package sql;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JScrollPane;

public class Client {

	private DatabaseHandler backend;
	
	private JFrame frame;
	
	private CardLayout card = new CardLayout(0, 0);
	
	private JTextField txtName;
	private JPasswordField txtPass;
	private JTextField txtBorrowID;
	private JTextField txtEquipBorrow;
	private JTextField txtAmount;
	private JTextField txtRecordID;
	private JCheckBox checkIsReturned;
	
	
	private JTextArea txtLogs;
	private JLabel lblLoginFeedback;
	
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

	
	
	public Client() throws SQLException {
		initialize();
		
		boolean conTest = backend.initialiseConnections();
		loginScreenText(conTest, backend.getExceptionCause());
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		backend = new DatabaseHandler();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 850, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(card);
		
		JPanel panelLogin = new JPanel();
		frame.getContentPane().add(panelLogin, "login");
		panelLogin.setLayout(null);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setFont(new Font("Trebuchet MS", Font.BOLD, 30));
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogin.setBounds(305, 22, 215, 66);
		panelLogin.add(lblLogin);
		
		JLabel lblName = new JLabel("Name");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setBounds(337, 102, 46, 14);
		panelLogin.add(lblName);
		
		JLabel lblPass = new JLabel("Pass");
		lblPass.setHorizontalAlignment(SwingConstants.CENTER);
		lblPass.setBounds(337, 124, 46, 14);
		panelLogin.add(lblPass);
		
		txtName = new JTextField();
		txtName.setBounds(386, 99, 86, 20);
		panelLogin.add(txtName);
		txtName.setColumns(10);
		
		txtPass = new JPasswordField();
		txtPass.setBounds(386, 121, 86, 20);
		panelLogin.add(txtPass);
		
		JButton BTNLogin = new JButton("Login");
		BTNLogin.addActionListener(e -> { BTNLoginClick(); });
		BTNLogin.setBounds(360, 149, 89, 23);
		panelLogin.add(BTNLogin);
		
		lblLoginFeedback = new JLabel("");
		lblLoginFeedback.setForeground(new Color(0, 128, 0));
		lblLoginFeedback.setHorizontalAlignment(SwingConstants.CENTER);
		lblLoginFeedback.setBounds(249, 183, 340, 14);
		panelLogin.add(lblLoginFeedback);
		
		JPanel panelEmployee = new JPanel();
		frame.getContentPane().add(panelEmployee, "employee");
		panelEmployee.setLayout(null);
		
		JPanel pnlAddRecord = new JPanel();
		pnlAddRecord.setBounds(10, 11, 404, 304);
		panelEmployee.add(pnlAddRecord);
		pnlAddRecord.setLayout(null);
		
		JLabel lblNewRecord = new JLabel("New Borrower Record");
		lblNewRecord.setFont(new Font("Verdana", Font.PLAIN, 25));
		lblNewRecord.setBounds(70, 11, 274, 64);
		pnlAddRecord.add(lblNewRecord);
		
		JLabel lblNewLabel = new JLabel("Borrower ID");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 113, 82, 20);
		pnlAddRecord.add(lblNewLabel);
		
		txtBorrowID = new JTextField();
		txtBorrowID.setBounds(102, 113, 86, 20);
		pnlAddRecord.add(txtBorrowID);
		txtBorrowID.setColumns(10);
		
		JLabel lblEquipBorrow = new JLabel("Equipment");
		lblEquipBorrow.setHorizontalAlignment(SwingConstants.CENTER);
		lblEquipBorrow.setBounds(10, 144, 82, 20);
		pnlAddRecord.add(lblEquipBorrow);
		
		txtEquipBorrow = new JTextField();
		txtEquipBorrow.setColumns(10);
		txtEquipBorrow.setBounds(102, 144, 86, 20);
		pnlAddRecord.add(txtEquipBorrow);
		
		JLabel lblAmount = new JLabel("Date");
		lblAmount.setHorizontalAlignment(SwingConstants.CENTER);
		lblAmount.setBounds(10, 175, 82, 20);
		pnlAddRecord.add(lblAmount);
		
		txtAmount = new JTextField();
		txtAmount.setColumns(10);
		txtAmount.setBounds(102, 175, 86, 20);
		pnlAddRecord.add(txtAmount);
		
		checkIsReturned = new JCheckBox("Returned?");
		checkIsReturned.setHorizontalAlignment(SwingConstants.CENTER);
		checkIsReturned.setBounds(44, 202, 97, 23);
		pnlAddRecord.add(checkIsReturned);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(e -> { RecordSubmitted(); });
		btnSubmit.setBounds(72, 232, 89, 23);
		pnlAddRecord.add(btnSubmit);
		
		txtRecordID = new JTextField();
		txtRecordID.setEditable(false);
		txtRecordID.setColumns(10);
		txtRecordID.setBounds(102, 86, 86, 20);
		pnlAddRecord.add(txtRecordID);
		
		JLabel lblRecordID = new JLabel("Record ID");
		lblRecordID.setHorizontalAlignment(SwingConstants.CENTER);
		lblRecordID.setBounds(10, 86, 82, 20);
		pnlAddRecord.add(lblRecordID);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 326, 404, 124);
		panelEmployee.add(scrollPane);
		
		txtLogs = new JTextArea();
		scrollPane.setViewportView(txtLogs);
		txtLogs.setEditable(false);
	}
	
	
	void BTNLoginClick() {
		
		String user = txtName.getText();
		String pass = String.valueOf(txtPass.getPassword());
		
		tryAuthenticate(user, pass);
		txtName.setText("");
		txtPass.setText("");
	}
	
	
	       
	
	void tryAuthenticate(String user, String pass) {
		
		boolean authResult = backend.authenticate(user, pass);
		
		if(authResult) {
			
			card.show(frame.getContentPane(), "employee");
		
			loginScreenText(true, "");
			txtLogs.append("[" + LocalTime.now() + "] " + user + " successfully logged in.\n");
			System.out.println("[" + LocalTime.now() + "] " + user + " successfully logged in.");
		}
		else
		{
			
			loginScreenText(false, backend.getExceptionCause());	
			txtLogs.append("[" + LocalTime.now() + "] " + backend.getExceptionCause() + ", Login Failed.\n");
			System.out.println("[" + LocalTime.now() + "] " + backend.getExceptionCause() +", Login Failed.");
			
		}
	}
	
	
	
	void RecordSubmitted() {
		
		String recordID = txtRecordID.getText();
		String borrowerID = txtBorrowID.getText();
		String equipBorrowed = txtEquipBorrow.getText();
		String borrowDate = txtAmount.getText();
		
		txtLogs.append("[" + LocalTime.now() + "] Attempting to perform query...\n");
		
		boolean qResult = backend.writeData(borrowerID, equipBorrowed, borrowDate);
		
		if(qResult) txtLogs.append("[" + LocalTime.now() + "] Operation Executed.\n");
		else txtLogs.append("[" + LocalTime.now() + "] Query Encountered Errors.\n");
	}
	
	//Helper Methods
	
	void loginScreenText(boolean success, String msg) {
		
		if(success) {
			
			lblLoginFeedback.setForeground(new Color(0, 128, 0));
			lblLoginFeedback.setText("Login Successful.");
		} else {
			
			lblLoginFeedback.setForeground(new Color(128, 0, 0));
			lblLoginFeedback.setText("Authentication Failed: " + msg);
			
		}
		
	}
}

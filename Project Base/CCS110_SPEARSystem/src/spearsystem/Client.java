package spearsystem;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.CharArrayReader;
import java.io.Reader;
import java.time.LocalTime;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class Client {

	private JFrame frame;
	private DatabaseHandler backend;
	private JTextField authUsername;
	private JPasswordField authPassword;

	
	private CardLayout card = new CardLayout(0,0);
	private JPanel userArea;
	private JTextArea areaLog;
	private JLabel lblLoginState;
	
	private String sessionUser;
	
	private JMenuItem menuOptLogout;
	
	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the application.
	 */
	public Client() {
		initialize();
		startupProcess();
	}

	
	private void initialize() {
		
		backend = new DatabaseHandler();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1080, 920);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel mainPane = new JPanel();
		mainPane.setBounds(10, 21, 1044, 849);
		frame.getContentPane().add(mainPane);
		mainPane.setLayout(null);
		
		userArea = new JPanel();
		userArea.setBounds(10, 11, 1024, 601);
		mainPane.add(userArea);
		userArea.setLayout(card);
		
		JPanel loginScreen = new JPanel();
		userArea.add(loginScreen, "login");
		loginScreen.setLayout(null);
		
		JLabel labelLogin = new JLabel("S.P.E.A.R System");
		labelLogin.setFont(new Font("Trebuchet MS", Font.PLAIN, 34));
		labelLogin.setHorizontalAlignment(SwingConstants.CENTER);
		labelLogin.setBounds(355, 11, 302, 117);
		loginScreen.add(labelLogin);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setBounds(365, 139, 85, 14);
		loginScreen.add(lblUsername);
		
		authUsername = new JTextField();
		authUsername.setBounds(460, 136, 197, 20);
		loginScreen.add(authUsername);
		authUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(365, 167, 85, 14);
		loginScreen.add(lblPassword);
		
		authPassword = new JPasswordField();
		authPassword.setBounds(460, 164, 197, 20);
		loginScreen.add(authPassword);
		
		JButton btnAuthenticate = new JButton("Log In");
		btnAuthenticate.addActionListener(e -> { tryAuthenticate(); });
		btnAuthenticate.setBounds(424, 225, 135, 23);
		loginScreen.add(btnAuthenticate);
		
		lblLoginState = new JLabel("-");
		lblLoginState.setBounds(10, 576, 1004, 14);
		loginScreen.add(lblLoginState);
		
		JPanel custodianDashboard = new JPanel();
		userArea.add(custodianDashboard, "custodianDash");
		custodianDashboard.setLayout(null);
		
		JTabbedPane custodianWorkArea = new JTabbedPane(JTabbedPane.TOP);
		custodianWorkArea.setBounds(10, 54, 1004, 540);
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
		
		JPanel adminDashboard = new JPanel();
		adminDashboard.setLayout(null);
		userArea.add(adminDashboard, "adminDash");
		
		JTabbedPane adminWorkArea = new JTabbedPane(JTabbedPane.TOP);
		adminWorkArea.setBounds(10, 50, 1004, 540);
		adminDashboard.add(adminWorkArea);
		
		JPanel equipManagement = new JPanel();
		adminWorkArea.addTab("Inventory Management", null, equipManagement, null);
		
		JPanel userManagement = new JPanel();
		adminWorkArea.addTab("User Management", null, userManagement, null);
		
		JLabel lblAdmin = new JLabel("Administrator");
		lblAdmin.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblAdmin.setHorizontalAlignment(SwingConstants.LEFT);
		lblAdmin.setBounds(10, 11, 343, 32);
		adminDashboard.add(lblAdmin);
		
		areaLog = new JTextArea();
		areaLog.setEditable(false);
		areaLog.setBounds(10, 623, 1024, 225);
		mainPane.add(areaLog);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1064, 22);
		frame.getContentPane().add(menuBar);
		
		JMenu menuMain = new JMenu("SETTINGS");
		menuBar.add(menuMain);
		
		menuOptLogout = new JMenuItem("Logout");
		menuOptLogout.addActionListener(e -> { Logout(); });
		menuOptLogout.setEnabled(false);
		menuMain.add(menuOptLogout);
	}
	
	void startupProcess() {
		
		boolean result = backend.checkConnection();
		String connStatMsg = backend.getConnStatus();
		
		loginScreenText(result, connStatMsg);
		
	}
	
	void tryAuthenticate() {
		
		String user = authUsername.getText();
		Reader pass = new CharArrayReader(authPassword.getPassword());
		
		boolean result = backend.authenticate(user, pass);
		
		if(result) {
			sessionUser = user;
			menuOptLogout.setEnabled(true);
			
			if(sessionUser.equals("admin")) {
				card.show(userArea, "adminDash");
				areaLog.append("\n[" + LocalTime.now() + "] Administrator Override: Login Successful.");
				
			}
			else {
				card.show(userArea, "custodianDash");
				areaLog.append("\n[" + LocalTime.now() + "] " + sessionUser + " Login Successful.");
			}
		}
		else {
			
			loginScreenText(result, backend.getAuthStatus());
			areaLog.append("\n[" + LocalTime.now() + "] Connection Error.");
			
		}
		
		authUsername.setText("");
		authPassword.setText("");
	}

	void Logout() {
		if(sessionUser == null) return;
		
		card.show(userArea, "login");
		areaLog.append("\n[" + LocalTime.now() + "]" + sessionUser +" Logged out.");
		sessionUser = null;
		menuOptLogout.setEnabled(false);
		startupProcess();
	}
	
	// Helper Methods
	void loginScreenText(boolean success, String msg) {
		
		if(success) {
			
			lblLoginState.setForeground(new Color(0, 128, 0));
			lblLoginState.setText("Authentication Success: " + msg);
		} else {
			
			lblLoginState.setForeground(new Color(128, 0, 0));
			lblLoginState.setText("Authentication Failed: " + msg);
			
		}
		
	}
}

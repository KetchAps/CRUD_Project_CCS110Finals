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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.RowFilter.Entry;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JComboBox;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainClient {

	private JFrame frame;
	
	private LoginHandler authHandler;
	private CustodianHandler custodianHandler;
	
	
	private CardLayout card = new CardLayout(0,0);
	private JPanel userArea;
	
	private String sessionUser;
	private int sessionID;
	
	private JMenuItem menuOptLogout;
	private JLabel lblFeedback;
	private JTextArea areaLog;

	private final Color GOOD = new Color(0, 128, 0);
	private final Color BAD = new Color(128, 0, 0);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainClient window = new MainClient();
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
	public MainClient() {
		initialize();
		testConnection();
	}

	
	private void initialize() {
		
		authHandler = new LoginHandler();
		custodianHandler = new CustodianHandler();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1290, 752);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel mainPane = new JPanel();
		mainPane.setBounds(0, 21, 1283, 700);
		frame.getContentPane().add(mainPane);
		mainPane.setLayout(null);
		
		userArea = new JPanel();
		userArea.setBounds(0, 0, 1290, 700);
		mainPane.add(userArea);
		userArea.setLayout(card);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1434, 22);
		frame.getContentPane().add(menuBar);
		
		JMenu menuMain = new JMenu("SETTINGS");
		menuMain.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		menuBar.add(menuMain);
		
		menuOptLogout = new JMenuItem("Logout");
		menuOptLogout.setEnabled(false);
		menuMain.add(menuOptLogout);
		
		LoginScreen loginScrn = new LoginScreen(this, authHandler);
		userArea.add(loginScrn.getPanel(), "login");
		
		CustodianDashboard dashB = new CustodianDashboard(this, custodianHandler);
		userArea.add(dashB.getPanel(), "custodian");
		
		AdminDashboard dashC = new AdminDashboard(null, custodianHandler);
		userArea.add(dashC.getPanel(), "admin");
		
		lblFeedback = new JLabel("-");
		lblFeedback.setBounds(20, 623, 1004, 14);
		mainPane.add(lblFeedback);
		
		JScrollPane scrllLogViewport = new JScrollPane();
		scrllLogViewport.setBounds(0, 699, 1284, 139);
		mainPane.add(scrllLogViewport);
		
		areaLog = new JTextArea();
		scrllLogViewport.setViewportView(areaLog);

	}
	
	
	void testConnection() {
		
		SwingWorker<?, ?> w = new SwingWorker<>() {

			@Override
			protected Object doInBackground() throws Exception {
				startupProcess();
				return null;
			}
		};
		w.execute();
	}
	
	
	void startupProcess() {
		
		boolean result = authHandler.checkConnection();
		String connStatMsg = authHandler.getConnStatus();

		log(result, connStatMsg);
		
	}
	
	public void setSessionCredentials(String user, int id) {
		if(!(user.isBlank() || user.isEmpty())) sessionUser = user;
		if (id > 0) sessionID = id;
	}
	
	public String getSessionUser() {
		return sessionUser;
		
	}
	
	public int getSessionID() {
		return sessionID;
	}


	public void log(boolean isGood, String msg) {
		
		if(isGood)
			lblFeedback.setForeground(GOOD);
		else
			lblFeedback.setForeground(BAD);
		
		lblFeedback.setText(msg);
		areaLog.append("\n[" + LocalTime.now() + "] " + msg);
		
	}

	public void displayLogin() {
		card.show(userArea, "login");
		menuOptLogout.setEnabled(false);
	}
	
	public void displayCustodian() {
		card.show(userArea, "custodian");
		menuOptLogout.setEnabled(true);
		
	}
	public void displayAdmin() {
		card.show(userArea, "admin");
		menuOptLogout.setEnabled(true);
	 }
}

package spearsystem;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.time.LocalTime;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public class MainClient {

	private JFrame frame;

	private LoginHandler authHandler;
	private CustodianHandler custodianHandler;

	private CustodianDashboard custodianScreen;
	private AdminDashboard adminScreen;

	private CardLayout card = new CardLayout(0, 0);
	private JPanel userArea;

	private String sessionUser;
	private int sessionID;

	private JMenuItem menuOptLogout;
	private JLabel lblFeedback;
	private JTextArea areaLog;

	private final Color GOOD = new Color(0, 128, 0);
	private final Color BAD = new Color(128, 0, 0);

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

	public MainClient() {
		initialize();
		testConnection();
	}

	private void initialize() {

		authHandler = new LoginHandler();
		custodianHandler = new CustodianHandler();

		frame = new JFrame();
		frame.setBounds(100, 100, 1290, 910); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel mainPane = new JPanel();
		mainPane.setBounds(0, 21, 1283, 850); 
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

		menuOptLogout.addActionListener(e -> logout());

		LoginScreen loginScrn = new LoginScreen(this, authHandler);
		userArea.add(loginScrn.getPanel(), "login");

		custodianScreen = new CustodianDashboard(this, custodianHandler);
		userArea.add(custodianScreen.getPanel(), "custodian");

		adminScreen = new AdminDashboard(this, 0);
		userArea.add(adminScreen.getPanel(), "admin");

		lblFeedback = new JLabel("-");
		lblFeedback.setBounds(22, 711, 1004, 14); // Adjusted positioning to sit cleanly below userArea
		mainPane.add(lblFeedback);

		JScrollPane scrllLogViewport = new JScrollPane();
		scrllLogViewport.setBounds(20, 736, 1240, 103); // Changed y from 699 to 635 so it's visible inside mainPane
		mainPane.add(scrllLogViewport);

		areaLog = new JTextArea();
		areaLog.setEditable(false); // Recommended to make logs read-only
		scrllLogViewport.setViewportView(areaLog);

		card.show(userArea, "login");
	}

	void testConnection() {
		SwingWorker<Boolean, Void> w = new SwingWorker<>() {
			private String connStatMsg;

			@Override
			protected Boolean doInBackground() throws Exception {
				boolean result = authHandler.checkConnection();
				connStatMsg = authHandler.getConnStatus();
				return result;
			}

			@Override
			protected void done() {
				try {
					boolean result = get();
					log(result, connStatMsg);
				} catch (Exception e) {
					log(false, "Connection check failed: " + e.getMessage());
				}
			}
		};
		w.execute();
	}

	public void setSessionCredentials(String user, int id) {
		if (user != null && !user.isBlank()) sessionUser = user;
		if (id > 0) sessionID = id;
	}

	public String getSessionUser() {
		return sessionUser;
	}

	public int getSessionID() {
		return sessionID;
	}

	public void log(boolean isGood, String msg) {
		if (isGood)
			lblFeedback.setForeground(GOOD);
		else
			lblFeedback.setForeground(BAD);

		lblFeedback.setText(msg);
		areaLog.append("\n[" + LocalTime.now() + "] " + msg);
	}

	void logout() {
		sessionUser = null;
		sessionID = 0;
		log(true, "Logged out.");
		displayLogin();
	}

	public void displayLogin() {
		card.show(userArea, "login");
		menuOptLogout.setEnabled(false);
	}

	public void displayCustodian() {
		custodianScreen.displayDashboard();
		card.show(userArea, "custodian");
		menuOptLogout.setEnabled(true);
	}

	public void displayAdmin() {
		adminScreen.setSessionID(sessionID);
		card.show(userArea, "admin");
		menuOptLogout.setEnabled(true);
	}
}

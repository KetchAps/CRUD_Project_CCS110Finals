package spearsystem;

import java.awt.Font;
import java.awt.Image;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import kenUI.ButtonOne;
import spearsystem.LoginHandler.AuthStatus;
import ui.loginPanel;

import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class LoginScreen {

	private MainClient parent;
	
	private JPanel loginScreen;
	private LoginHandler backend;
	
	private String sessionUser;
	private JTextField authUsername;
	private JPasswordField authPassword;
	
	public LoginScreen(MainClient parent, LoginHandler backend) {
		this.parent = parent;
		this.backend = backend;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		backend = new LoginHandler();

		loginScreen = new JPanel();
		loginScreen.setBounds(10, 54, 1240, 700);
		loginScreen.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(737, 0, 503, 700);
		loginScreen.add(panel);
		panel.setLayout(null);
		
		JLabel labelLogin = new JLabel("S.P.E.A.R");
		labelLogin.setForeground(new Color(30, 144, 255));
		labelLogin.setHorizontalAlignment(SwingConstants.CENTER);
		labelLogin.setFont(new Font("Segoe UI Black", Font.BOLD, 60));
		labelLogin.setBounds(100, 171, 302, 81);
		panel.add(labelLogin);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setForeground(new Color(30, 144, 255));
		lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setBounds(168, 277, 167, 39);
		panel.add(lblUsername);
		
		authUsername = new JTextField();
		authUsername.setFont(new Font("Segoe UI Light", Font.PLAIN, 20));
		authUsername.setColumns(10);
		authUsername.setBounds(100, 317, 302, 43);
		panel.add(authUsername);
		
		authPassword = new JPasswordField();
		authPassword.setFont(new Font("Segoe UI Light", Font.PLAIN, 20));
		authPassword.setBounds(100, 428, 302, 43);
		panel.add(authPassword);
		
		ButtonOne button = new ButtonOne();
		button.setBounds(170, 514, 162, 49);
		panel.add(button);
		button.setFont(new Font("Segoe UI Black", Font.BOLD, 22));
		button.addActionListener(e -> { tryAuthenticate(); });
		button.setText("Login");
		
		ImageIcon originalIcon = new ImageIcon(LoginScreen.class.getResource("/ui/spearKen.png"));
		Image resizedImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
		
		JLabel spearLogo = new JLabel("");
		spearLogo.setBounds(180, 31, 143, 143);
		spearLogo.setIcon(new ImageIcon(resizedImage));
		panel.add(spearLogo);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setForeground(new Color(30, 144, 255));
		lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		lblPassword.setBounds(168, 388, 167, 39);
		panel.add(lblPassword);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(30, 144, 255));
		panel_1.setBounds(0, 0, 738, 700);
		loginScreen.add(panel_1);
	}
	
	JPanel getPanel() {
		return loginScreen;
	}
	
	void tryAuthenticate() {
		
		String user = authUsername.getText();
		Reader pass = new CharArrayReader(authPassword.getPassword());
		AuthStatus result;
		
		try { result = backend.authenticate(user, pass); } catch (SPEARException e) { parent.log(false, e.getMessage()); return; }
		
		try { pass.close(); } catch (IOException e) { e.printStackTrace(); }
		
		parent.setSessionCredentials(result.user(), result.ID());
		parent.log(true, user + " successfully logged in.");
		
		if(result.isAdmin()) { parent.displayAdmin(); }
		else				   parent.displayCustodian();
		
		authUsername.setText("");
		authPassword.setText("");
	}

	void Logout() {
		if(sessionUser == null) return;
		
		sessionUser = null;
	}
}

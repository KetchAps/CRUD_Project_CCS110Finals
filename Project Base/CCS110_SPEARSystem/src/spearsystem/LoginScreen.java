package spearsystem;

import java.awt.Font;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginScreen {

	private MainClient parent;
	
	private JPanel loginScreen;
	private LoginHandler backend;
	private JTextField authUsername;
	private JPasswordField authPassword;
	
	private String sessionUser;
	
	
	
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
		loginScreen.setBounds(10, 54, 1400, 620);
		loginScreen.setLayout(null);
		
		JLabel labelLogin = new JLabel("S.P.E.A.R System");
		labelLogin.setFont(new Font("Trebuchet MS", Font.PLAIN, 34));
		labelLogin.setHorizontalAlignment(SwingConstants.CENTER);
		labelLogin.setBounds(500, 11, 302, 117);
		loginScreen.add(labelLogin);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setBounds(510, 139, 85, 14);
		loginScreen.add(lblUsername);
		
		authUsername = new JTextField();
		authUsername.setBounds(605, 136, 197, 20);
		loginScreen.add(authUsername);
		authUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(510, 167, 85, 14);
		loginScreen.add(lblPassword);
		
		authPassword = new JPasswordField();
		authPassword.setBounds(605, 164, 197, 20);
		loginScreen.add(authPassword);
		
		JButton btnAuthenticate = new JButton("Log In");
		btnAuthenticate.addActionListener(e -> { tryAuthenticate(); });
		btnAuthenticate.setBounds(569, 225, 135, 23);
		loginScreen.add(btnAuthenticate);
		
	}
	
	JPanel getPanel() {
		return loginScreen;
	}
	

	
	
	
	void tryAuthenticate() {
		
		String user = authUsername.getText();
		Reader pass = new CharArrayReader(authPassword.getPassword());
		
		boolean result = backend.authenticate(user, pass);
		
		try {
			pass.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(result) {
			sessionUser = user;
			parent.log(result, user + " successfully logged in.");
			if(sessionUser.equals("admin")) {
				parent.displayAdmin();
				
			}
			else {
					
				parent.displayCustodian();
			}
		}
		else {

			parent.log(result, "Authentication Failed: " + user + "is not recogninsed.");

			
		}
		
		authUsername.setText("");
		authPassword.setText("");
	}

	void Logout() {
		if(sessionUser == null) return;
		
		sessionUser = null;
	}
	
	// Helper Methods
	
		
	
	
}

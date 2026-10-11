package spearsystem;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import kenUI.ButtonOne;
import spearsystem.LoginHandler.AuthStatus;

public class LoginScreen {

	private MainClient parent;

	private JPanel loginScreen;
	private LoginHandler backend;
	private JTextField authUsername;
	private JPasswordField authPassword;
	private JComboBox<String> roleBox;

	public LoginScreen(MainClient parent, LoginHandler backend) {
		this.parent = parent;
		this.backend = backend;
		initialize();
	}

	private void paintRound(Graphics g, JComponent c) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fillRoundRect(1, 1, c.getWidth() - 3, c.getHeight() - 3, c.getHeight(), c.getHeight());
		g2.setColor(Color.BLACK);
		g2.setStroke(new java.awt.BasicStroke(2));
		g2.drawRoundRect(1, 1, c.getWidth() - 3, c.getHeight() - 3, c.getHeight(), c.getHeight());
		g2.dispose();
	}

		private class RoundField extends JTextField {
		RoundField() {
			setOpaque(false);
			setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 22));
			setFont(new Font("Segoe UI", Font.PLAIN, 16));
		}

		@Override
		protected void paintComponent(Graphics g) {
			paintRound(g, this);
			super.paintComponent(g);
		}
	}

		private class RoundPassword extends JPasswordField {
		RoundPassword() {
			setOpaque(false);
			setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 22));
			setFont(new Font("Segoe UI", Font.PLAIN, 16));
		}

		@Override
		protected void paintComponent(Graphics g) {
			paintRound(g, this);
			super.paintComponent(g);
		}
	}

	private void initialize() {

		Color topColor = new Color(212, 255, 253);
		Color bottomColor = new Color(247, 214, 190);

		loginScreen = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setPaint(new GradientPaint(0, 0, topColor, 0, getHeight(), bottomColor));
				g2.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		loginScreen.setLayout(null);
		loginScreen.setBounds(10, 54, 1400, 620);

		JPanel card = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(255, 255, 255, 120));
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 60, 60);
				g2.dispose();
			}
		};
		card.setOpaque(false);
		card.setLayout(null);
		card.setBounds(800, 20, 420, 650);
		loginScreen.add(card);

		// LOGO
		ImageIcon logoIcon = new ImageIcon(LoginScreen.class.getResource("/ui/spearKen.png"));
		Image logoImg = logoIcon.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
		JLabel lblLogo = new JLabel(new ImageIcon(logoImg));
		lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogo.setBounds(155, 60, 110, 110);
		card.add(lblLogo);

		// TITLE
		JLabel labelLogin = new JLabel("S.P.E.A.R.");
		labelLogin.setFont(new Font("Segoe UI", Font.BOLD, 40));
		labelLogin.setHorizontalAlignment(SwingConstants.CENTER);
		labelLogin.setBounds(0, 170, 420, 60);
		card.add(labelLogin);

		// USERNAME
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setBounds(0, 255, 420, 25);
		card.add(lblUsername);

		authUsername = new RoundField();
		authUsername.setBounds(60, 285, 300, 50);
		card.add(authUsername);

		// PASSWORD
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(0, 350, 420, 25);
		card.add(lblPassword);

		authPassword = new RoundPassword();
		authPassword.setBounds(60, 380, 300, 50);
		authPassword.addActionListener(e -> tryAuthenticate());
		card.add(authPassword);

		// ROLE
		JLabel lblRole = new JLabel("Role:");
		lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblRole.setBounds(70, 465, 60, 40);
		card.add(lblRole);

		roleBox = new JComboBox<>(new String[] { "Custodian", "Admin" });
		roleBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		roleBox.setBackground(Color.WHITE);
		roleBox.setBorder(new LineBorder(Color.BLACK, 2, true));
		roleBox.setBounds(135, 465, 225, 40);
		card.add(roleBox);

		// LOG IN BUTTON
		ButtonOne btnAuthenticate = new ButtonOne(new Color(82, 113, 255), new Color(60, 90, 230));
		btnAuthenticate.setText("LOG IN");
		btnAuthenticate.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		btnAuthenticate.addActionListener(e -> tryAuthenticate());
		btnAuthenticate.setBounds(40, 545, 340, 65);
		card.add(btnAuthenticate);
	}

	JPanel getPanel() {
		return loginScreen;
	}

	void tryAuthenticate() {

		String user = authUsername.getText();
		Reader pass = new CharArrayReader(authPassword.getPassword());
		AuthStatus result;

		try {
			result = backend.authenticate(user, pass);
		} catch (SPEARException e) {
			parent.log(false, e.getMessage());
			return;
		}

		try { pass.close(); } catch (IOException e) { e.printStackTrace(); }

		String chosen = (String) roleBox.getSelectedItem();

		if (chosen.equals("Admin") && !result.isAdmin()) {
			parent.log(false, "This account is not an admin.");
			authPassword.setText("");
			return;
		}

		parent.setSessionCredentials(result.user(), result.ID());
		parent.log(true, user + " successfully logged in.");

		if (chosen.equals("Admin")) parent.displayAdmin();
		else                        parent.displayCustodian();

		authUsername.setText("");
		authPassword.setText("");
		roleBox.setSelectedIndex(0);
	}
}
package ui;

import java.awt.EventQueue;
import java.awt.Image;
import controller.AppController;
import kenUI.ButtonOne;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXDatePicker;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class loginPanel extends JFrame {

	private static final long serialVersionUID = 1L;

	private AppController controller;
	
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	private JButton btnViewObjectives;
	private JButton btnViewObjectives_2;
	private JButton btnViewObjectives_4;
	private JButton btnViewObjectives_5;

	private JPanel welcome;
	private JPanel users;
	private JPanel about;
	private JPanel objectives;
	private JLabel lblSchoolProperty;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					loginPanel frame = new loginPanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public loginPanel() {
		// AppController
		controller = new AppController(this);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 840);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// =========================
		// LOGIN PANEL
		// =========================

		ImageIcon originalIcon = new ImageIcon(loginPanel.class.getResource("/ui/spearKen.png"));

		Image resizedImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);

		JPanel loginPane = new JPanel();
		loginPane.setBounds(624, 0, 550, 801);
		contentPane.add(loginPane);
		loginPane.setLayout(null);

		JLabel spearLogo = new JLabel();
		spearLogo.setBounds(173, 29, 200, 200);
		loginPane.add(spearLogo);
		spearLogo.setIcon(new ImageIcon(resizedImage));

		JLabel title = new JLabel("S.P.E.A.R");
		title.setForeground(new Color(30, 144, 255));
		title.setFont(new Font("Segoe UI Black", Font.PLAIN, 50));
		title.setBounds(155, 209, 236, 76);
		loginPane.add(title);

		textField = new JTextField();
		textField.setFont(new Font("Segoe UI Light", Font.PLAIN, 25));
		textField.setBounds(99, 356, 352, 57);
		loginPane.add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setFont(new Font("Segoe UI Light", Font.PLAIN, 25));
		textField_1.setColumns(10);
		textField_1.setBounds(99, 495, 352, 57);
		loginPane.add(textField_1);
		
		JLabel lbluserName = new JLabel("User Name");
		lbluserName.setForeground(new Color(30, 144, 255));
		lbluserName.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		lbluserName.setBounds(213, 311, 124, 34);
		loginPane.add(lbluserName);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(new Color(30, 144, 255));
		lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		lblPassword.setBounds(222, 450, 106, 34);
		loginPane.add(lblPassword);
		
		ButtonOne button = new ButtonOne();
		button.setBounds(194, 589, 162, 49);
		loginPane.add(button);
		button.setFont(new Font("Segoe UI Black", Font.BOLD, 22));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = textField.getText();
				String password = textField_1.getText();
				
				controller.login(username, password);
				System.out.println("Clicked! ButtonOne");
			}
		});
		button.setText("Login");
		
		JXDatePicker dpTest = new JXDatePicker();
		dpTest.setFormats("yyyy-MM-dd");
		dpTest.setBounds(99, 670, 200, 28);
		loginPane.add(dpTest);
		
		// =========================
		// LAYERED PANE
		// =========================

		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBackground(new Color(30, 144, 255));
		layeredPane.setBounds(0, 0, 627, 801);
		contentPane.add(layeredPane);

		// =========================
		// PANEL 1 - WELCOME
		// =========================

		welcome = new JPanel();
		layeredPane.setLayer(welcome, 3);
		welcome.setBackground(new Color(30, 144, 255));
		welcome.setBounds(0, 0, 628, 801);
		welcome.setLayout(null);
		layeredPane.add(welcome);

		JLabel lblNewLabel = new JLabel("Weclome To SPEAR!");
		lblNewLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 25));
		lblNewLabel.setBounds(33, 110, 269, 51);
		lblNewLabel.setForeground(new Color(255, 255, 255));
		welcome.add(lblNewLabel);

		// Create button FIRST
		btnViewObjectives = new JButton("View Objectives");

		// Then add listener
		btnViewObjectives.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				layeredPane.removeAll();
				layeredPane.add(objectives);

				layeredPane.repaint();
				layeredPane.revalidate();
			}
		});

		btnViewObjectives.setForeground(new Color(30, 144, 255));
		btnViewObjectives.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 25));
		btnViewObjectives.setBackground(new Color(255, 255, 255));
		btnViewObjectives.setBounds(209, 726, 209, 41);
		welcome.add(btnViewObjectives);

		lblSchoolProperty = new JLabel("School Property & Equipment Asset Record System");
		lblSchoolProperty.setForeground(new Color(255, 255, 255));
		lblSchoolProperty.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblSchoolProperty.setBounds(33, 154, 500, 34);
		welcome.add(lblSchoolProperty);
		
		// =========================
		// PANEL 2 - OBJECTIVES
		// =========================

		objectives = new JPanel();
		layeredPane.setLayer(objectives, 2);
		objectives.setLayout(null);
		objectives.setBackground(new Color(30, 144, 255));
		objectives.setBounds(0, 0, 628, 801);
		layeredPane.add(objectives);

		JLabel lblPanel = new JLabel("PANEL 2");
		lblPanel.setForeground(Color.WHITE);
		lblPanel.setFont(new Font("Tahoma", Font.BOLD, 27));
		lblPanel.setBounds(214, 372, 128, 51);
		objectives.add(lblPanel);

		btnViewObjectives_2 = new JButton("Meet our Users");

		btnViewObjectives_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				layeredPane.removeAll();
				layeredPane.add(users);

				layeredPane.repaint();
				layeredPane.revalidate();
			}
		});

		btnViewObjectives_2.setForeground(new Color(30, 144, 255));
		btnViewObjectives_2.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 25));
		btnViewObjectives_2.setBackground(new Color(255, 255, 255));
		btnViewObjectives_2.setBounds(209, 726, 210, 41);
		objectives.add(btnViewObjectives_2);

		// =========================
		// PANEL 3 - USERS
		// =========================

		users = new JPanel();
		layeredPane.setLayer(users, 1);
		users.setBackground(new Color(30, 144, 255));
		users.setBounds(0, 0, 628, 801);
		users.setLayout(null);
		layeredPane.add(users);

		JLabel lblPanel_1 = new JLabel("PANEL 3");
		lblPanel_1.setForeground(Color.WHITE);
		lblPanel_1.setFont(new Font("Tahoma", Font.BOLD, 27));
		lblPanel_1.setBounds(214, 372, 128, 51);
		users.add(lblPanel_1);

		btnViewObjectives_4 = new JButton("Learn more");

		btnViewObjectives_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				layeredPane.removeAll();
				layeredPane.add(about);

				layeredPane.repaint();
				layeredPane.revalidate();
			}
		});

		btnViewObjectives_4.setForeground(new Color(30, 144, 255));
		btnViewObjectives_4.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 25));
		btnViewObjectives_4.setBackground(new Color(255, 255, 255));
		btnViewObjectives_4.setBounds(212, 726, 204, 41);
		users.add(btnViewObjectives_4);

		// =========================
		// PANEL 4 - ABOUT
		// =========================

		about = new JPanel();
		layeredPane.setLayer(about, 0);
		about.setLayout(null);
		about.setBackground(new Color(30, 144, 255));
		about.setBounds(0, 0, 628, 801);
		layeredPane.add(about);

		JLabel lblPanel_2 = new JLabel("PANEL 4");
		lblPanel_2.setForeground(Color.WHITE);
		lblPanel_2.setFont(new Font("Tahoma", Font.BOLD, 27));
		lblPanel_2.setBounds(214, 372, 128, 51);
		about.add(lblPanel_2);

		btnViewObjectives_5 = new JButton("Back to welcome");

		btnViewObjectives_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				layeredPane.removeAll();
				layeredPane.add(welcome);

				layeredPane.repaint();
				layeredPane.revalidate();
			}
		});

		btnViewObjectives_5.setForeground(new Color(30, 144, 255));
		btnViewObjectives_5.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 25));
		btnViewObjectives_5.setBackground(new Color(255, 255, 255));
		btnViewObjectives_5.setBounds(192, 726, 243, 41);
		about.add(btnViewObjectives_5);
	}
}
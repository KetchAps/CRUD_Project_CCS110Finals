package controller;

import javax.swing.JFrame;
import ui.dashboard;

public class AppController {

	private JFrame frame;

	public AppController(JFrame frame) {
		this.frame = frame;
	}

	public void login(String username, String password) {
		if (username.equals("admin") && password.equals("admin")) {
			dashboard dashboardWindow = new dashboard();
			dashboardWindow.setVisible(true);
			frame.dispose();
		} else {
			System.out.println("Invalid username or password!");
		}
	}
}
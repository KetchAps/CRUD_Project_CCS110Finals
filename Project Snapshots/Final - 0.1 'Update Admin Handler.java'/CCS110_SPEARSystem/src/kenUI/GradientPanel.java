package kenUI;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class GradientPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private Color colorTop;
	private Color colorBottom;

	public GradientPanel() {
		this(new Color(245, 248, 253), new Color(225, 234, 245));
	}

	public GradientPanel(Color colorTop, Color colorBottom) {
		this.colorTop = colorTop;
		this.colorBottom = colorBottom;

		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		GradientPaint gp = new GradientPaint(0, 0, colorTop, 0, getHeight(), colorBottom);

		g2.setPaint(gp);
		g2.fillRect(0, 0, getWidth(), getHeight());

		g2.dispose();
		super.paintComponent(g);
	}

	public Color getColorTop() {
		return colorTop;
	}

	public void setColorTop(Color colorTop) {
		this.colorTop = colorTop;
		repaint();
	}

	public Color getColorBottom() {
		return colorBottom;
	}

	public void setColorBottom(Color colorBottom) {
		this.colorBottom = colorBottom;
		repaint();
	}
}

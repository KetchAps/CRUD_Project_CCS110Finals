package kenUI;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.Timer;

public class ButtonOne extends JButton {
	private static final long serialVersionUID = 1L;

	// 0 = normal, 1 = hover
	private float progress = 0f;

	// 0 = released, 1 = pressed
	private float pressProgress = 0f;

	// Our own hover flag (doesn't depend on the model's timing)
	private boolean hovering = false;

	// Colors
	private final Color normalColor = new Color(30, 144, 255); // Dodger Blue
	private final Color hoverColor = new Color(34, 76, 214); // Deep Sapphire (Rich hue shift)
	private final Color glowColor = new Color(30, 144, 255, 60); // Semi-transparent blue for hover glow

	// Animation speeds
	private static final float HOVER_STEP = 0.05f; // Takes ~200ms to complete (Smooth & snappy)
	private static final float PRESS_STEP = 0.15f; // Quick click response

	// Press distance
	private static final int PRESS_SHIFT_PX = 3;

	private Timer timer;

	public ButtonOne() {

		// remove the outline of swing button but no animation so we customized it
		setBorderPainted(false);
		setFocusPainted(false);
		setContentAreaFilled(false);
		setOpaque(false);

		timer = new Timer(10, e -> {

			float hoverTarget = hovering ? 1f : 0f;
			float pressTarget = getModel().isPressed() ? 1f : 0f;

			progress = moveToward(progress, hoverTarget, HOVER_STEP);
			pressProgress = moveToward(pressProgress, pressTarget, PRESS_STEP);

			repaint();

			// Stop only when BOTH animations have arrived
			if (progress == hoverTarget && pressProgress == pressTarget) {
				timer.stop();
			}
		});

		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				hovering = true;
				timer.start();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				hovering = false;
				timer.start();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				timer.start();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				timer.start();
			}
		});

		// Safety net: restart whenever the button model changes (pressed, armed, etc.)
		addChangeListener(e -> timer.start());
	}

	// Moves a value toward a target by 'step' without overshooting
	private float moveToward(float current, float target, float step) {
		if (current < target) {
			return Math.min(current + step, target);
		} else if (current > target) {
			return Math.max(current - step, target);
		}
		return current;
	}

	@Override
	protected void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g.create();

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Cubic ease-out 
		float easedProgress = 1f - (1f - progress) * (1f - progress) * (1f - progress);

		// Press effect
		int shift = (int) (pressProgress * PRESS_SHIFT_PX);

		// glow padding
		int glowPadding = 2;
		int x = glowPadding;
		int y = glowPadding + shift;
		int width = getWidth() - (glowPadding * 2);
		int height = getHeight() - (glowPadding * 2) - PRESS_SHIFT_PX;

		if (progress > 0) {
			int currentGlowAlpha = (int) (glowColor.getAlpha() * easedProgress);
			Color currentGlow = new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(),
					currentGlowAlpha);
			g2.setColor(currentGlow);

			g2.fillRoundRect(x - 2, y - 2, width + 4, height + 4, 34, 34);
		}

		// blend
		int red = (int) (normalColor.getRed() + (hoverColor.getRed() - normalColor.getRed()) * easedProgress);
		int green = (int) (normalColor.getGreen() + (hoverColor.getGreen() - normalColor.getGreen()) * easedProgress);
		int blue = (int) (normalColor.getBlue() + (hoverColor.getBlue() - normalColor.getBlue()) * easedProgress);

		g2.setColor(new Color(red, green, blue));

		g2.fillRoundRect(x, y, width, height, 30, 30);

		g2.setColor(Color.WHITE);
		g2.setFont(getFont());

		// center font
		FontMetrics metrics = g2.getFontMetrics();
		int textWidth = metrics.stringWidth(getText());
		int textX = x + (width - textWidth) / 2;
		int textY = y + (height - metrics.getHeight()) / 2 + metrics.getAscent();

		g2.drawString(getText(), textX, textY);

		g2.dispose();
	}
}

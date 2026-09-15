package demo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class HUD {
	private Font hudFont;

	public HUD() {
		this.hudFont = new Font("Arial", Font.BOLD, 22);
	}

	public void render(Graphics2D g, int screenWidth, int screenHeight, int killCount) {
		g.setFont(hudFont);
		g.setColor(Color.WHITE);

		String text = "Targets Destroyed: " + killCount;
		int stringWidth = g.getFontMetrics().stringWidth(text);
		int margin = 20;
		int x = screenWidth - stringWidth - margin;
		int y = margin + g.getFontMetrics().getAscent();

		// Background shadow for text readability
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(x - 8, y - g.getFontMetrics().getAscent() - 4, stringWidth + 16, g.getFontMetrics().getHeight() + 8);

		g.setColor(Color.YELLOW);
		g.drawString(text, x, y);
	}
}

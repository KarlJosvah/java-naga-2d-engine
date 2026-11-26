package demoelement.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import demoelement.core.BoardContext;
import engine.gamestate.StateElement;
import engine.tools.AssetsLoader;

public class SnakeHudElement implements StateElement {
	private static final Color OVERLAY_BG = new Color(0, 0, 0, 180);
	private static final Color TEXT_COLOR = Color.WHITE;
	private static final Color ACCENT_COLOR = new Color(180, 240, 90);

	private final BoardContext boardContext;
	private final SnakeElement snakeElement;

	private Font scoreFont;
	private Font overlayFont;

	public SnakeHudElement(BoardContext boardContext, SnakeElement snakeElement) {
		this.boardContext = boardContext;
		this.snakeElement = snakeElement;

		if (AssetsLoader.font_android_101 != null) {
			this.scoreFont = AssetsLoader.font_android_101.deriveFont(Font.BOLD, 18f);
			this.overlayFont = AssetsLoader.font_android_101.deriveFont(Font.BOLD, 28f);
		} else {
			this.scoreFont = new Font("SansSerif", Font.BOLD, 16);
			this.overlayFont = new Font("SansSerif", Font.BOLD, 26);
		}
	}

	@Override
	public void render(Graphics2D g, int renderWidth, int renderHeight) {
		int originX = boardContext.getOriginX();
		int originY = boardContext.getOriginY();
		int boardWidth = boardContext.getBoardWidth();
		int boardHeight = boardContext.getBoardHeight();

		// Top HUD: Score
		g.setColor(TEXT_COLOR);
		g.setFont(scoreFont);
		g.drawString("Score: " + snakeElement.getScore(), originX + 12, originY + 24);

		// Overlay if Paused or Game Over
		String overlayTitle = null;
		String overlaySubtitle = null;

		if (snakeElement.isGameOver()) {
			overlayTitle = "GAME OVER";
			overlaySubtitle = "Press R to Restart";
		} else if (snakeElement.isPaused()) {
			overlayTitle = "PAUSED";
			overlaySubtitle = "Press SPACE or P to Resume";
		}

		if (overlayTitle != null) {
			drawOverlay(g, overlayTitle, overlaySubtitle, originX, originY, boardWidth, boardHeight);
		}
	}

	private void drawOverlay(Graphics2D g, String title, String subtitle, int x, int y, int width, int height) {
		g.setColor(OVERLAY_BG);
		g.fillRect(x, y, width, height);

		g.setFont(overlayFont);
		FontMetrics fm = g.getFontMetrics();
		int titleX = x + (width - fm.stringWidth(title)) / 2;
		int titleY = y + (height / 2) - 10;

		g.setColor(ACCENT_COLOR);
		g.drawString(title, titleX, titleY);

		if (subtitle != null) {
			g.setFont(scoreFont);
			FontMetrics subFm = g.getFontMetrics();
			int subX = x + (width - subFm.stringWidth(subtitle)) / 2;
			int subY = titleY + 36;
			g.setColor(TEXT_COLOR);
			g.drawString(subtitle, subX, subY);
		}
	}
}

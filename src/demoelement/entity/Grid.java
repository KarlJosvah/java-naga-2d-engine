package demoelement.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import demoelement.core.BoardContext;
import engine.gamestate.StateElement;

public class Grid implements StateElement {
	private static final Color BOARD_COLOR = new Color(12, 20, 12);
	private static final Color GRID_COLOR = new Color(255, 255, 255, 16);
	private static final Color BORDER_COLOR = new Color(70, 100, 70);

	private final BoardContext boardContext;

	public Grid(BoardContext boardContext) {
		this.boardContext = boardContext;
	}

	@Override
	public void render(Graphics2D g, int renderWidth, int renderHeight) {
		this.boardContext.updateLayout(renderWidth, renderHeight);

		int x = this.boardContext.getOriginX();
		int y = this.boardContext.getOriginY();
		int width = this.boardContext.getBoardWidth();
		int height = this.boardContext.getBoardHeight();
		int cellSize = this.boardContext.getCellSize();
		int columns = this.boardContext.getColumns();
		int rows = this.boardContext.getRows();

		// Board background
		g.setColor(BOARD_COLOR);
		g.fillRect(x, y, width, height);

		// Grid lines
		g.setColor(GRID_COLOR);
		for (int col = 1; col < columns; col++) {
			int lineX = x + col * cellSize;
			g.drawLine(lineX, y, lineX, y + height);
		}
		for (int row = 1; row < rows; row++) {
			int lineY = y + row * cellSize;
			g.drawLine(x, lineY, x + width, lineY);
		}

		// Outer border
		g.setColor(BORDER_COLOR);
		g.drawRect(x, y, width, height);
	}
}

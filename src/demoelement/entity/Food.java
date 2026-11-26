package demoelement.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import demoelement.core.BoardContext;
import demoelement.core.Cell;
import engine.gamestate.StateElement;

public class Food implements StateElement {
	private static final Color FOOD_COLOR = new Color(230, 70, 70);

	private final BoardContext boardContext;
	private final Random random = new Random();
	private Cell position;

	public Food(BoardContext boardContext) {
		this.boardContext = boardContext;
	}

	public Cell getPosition() {
		return this.position;
	}

	public void respawn(Collection<Cell> occupiedCells) {
		List<Cell> freeCells = new ArrayList<>();
		int cols = this.boardContext.getColumns();
		int rows = this.boardContext.getRows();

		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				Cell cell = new Cell(x, y);
				if (occupiedCells == null || !occupiedCells.contains(cell)) {
					freeCells.add(cell);
				}
			}
		}

		if (freeCells.isEmpty()) {
			this.position = null;
		} else {
			this.position = freeCells.get(this.random.nextInt(freeCells.size()));
		}
	}

	@Override
	public void render(Graphics2D g, int renderWidth, int renderHeight) {
		if (this.position == null) {
			return;
		}

		int cellSize = this.boardContext.getCellSize();
		int pad = Math.max(1, cellSize / 6);
		int px = this.boardContext.toScreenX(this.position.getX()) + pad;
		int py = this.boardContext.toScreenY(this.position.getY()) + pad;
		int size = cellSize - 2 * pad;

		g.setColor(FOOD_COLOR);
		g.fillOval(px, py, size, size);
	}
}

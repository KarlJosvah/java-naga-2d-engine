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

public class FoodElement implements StateElement {
	private static final Color FOOD_COLOR = new Color(230, 70, 70);

	private final BoardContext boardContext;
	private final Random random = new Random();
	private Cell position;

	public FoodElement(BoardContext boardContext) {
		this.boardContext = boardContext;
	}

	public Cell getPosition() {
		return position;
	}

	public void respawn(Collection<Cell> occupiedCells) {
		List<Cell> freeCells = new ArrayList<>();
		int cols = boardContext.getColumns();
		int rows = boardContext.getRows();

		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				Cell cell = new Cell(x, y);
				if (occupiedCells == null || !occupiedCells.contains(cell)) {
					freeCells.add(cell);
				}
			}
		}

		if (freeCells.isEmpty()) {
			position = null;
		} else {
			position = freeCells.get(random.nextInt(freeCells.size()));
		}
	}

	@Override
	public void render(Graphics2D g, int renderWidth, int renderHeight) {
		if (position == null) {
			return;
		}

		int cellSize = boardContext.getCellSize();
		int pad = Math.max(1, cellSize / 6);
		int px = boardContext.toScreenX(position.getX()) + pad;
		int py = boardContext.toScreenY(position.getY()) + pad;
		int size = cellSize - 2 * pad;

		g.setColor(FOOD_COLOR);
		g.fillOval(px, py, size, size);
	}
}

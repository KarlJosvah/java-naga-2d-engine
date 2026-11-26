package demoelement.core;

/**
 * Stores grid dimensions and calculates viewport-relative bounds for board rendering.
 */
public class BoardContext {
	public static final int DEFAULT_COLUMNS = 24;
	public static final int DEFAULT_ROWS = 18;

	private final int columns;
	private final int rows;

	private int cellSize;
	private int boardWidth;
	private int boardHeight;
	private int originX;
	private int originY;

	public BoardContext() {
		this(DEFAULT_COLUMNS, DEFAULT_ROWS);
	}

	public BoardContext(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
	}

	public void updateLayout(int viewportWidth, int viewportHeight) {
		this.cellSize = Math.max(1, Math.min(viewportWidth / columns, viewportHeight / rows));
		this.boardWidth = this.cellSize * columns;
		this.boardHeight = this.cellSize * rows;
		this.originX = (viewportWidth - this.boardWidth) / 2;
		this.originY = (viewportHeight - this.boardHeight) / 2;
	}

	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	public int getCellSize() {
		return cellSize;
	}

	public int getBoardWidth() {
		return boardWidth;
	}

	public int getBoardHeight() {
		return boardHeight;
	}

	public int getOriginX() {
		return originX;
	}

	public int getOriginY() {
		return originY;
	}

	public int toScreenX(int gridX) {
		return originX + gridX * cellSize;
	}

	public int toScreenY(int gridY) {
		return originY + gridY * cellSize;
	}

	public boolean isWithinBounds(int gridX, int gridY) {
		return gridX >= 0 && gridX < columns && gridY >= 0 && gridY < rows;
	}
}

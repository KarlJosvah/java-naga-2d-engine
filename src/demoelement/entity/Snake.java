package demoelement.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import demoelement.core.BoardContext;
import demoelement.core.Cell;
import demoelement.core.Direction;
import engine.gamestate.GameState;
import engine.gamestate.InputEvent;
import engine.gamestate.StateElement;

public class Snake implements StateElement {
	private static final Color HEAD_COLOR = new Color(180, 240, 90);
	private static final Color BODY_COLOR = new Color(70, 180, 70);

	private static final double MOVE_INTERVAL_SECONDS = 0.12; // ~8 ticks at 60 TPS

	private final BoardContext boardContext;
	private final Food food;
	private final Deque<Cell> body = new ArrayDeque<>();

	private Direction direction = Direction.RIGHT;
	private Direction nextDirection = Direction.RIGHT;

	private double moveTimer = 0.0;
	private int score = 0;
	private boolean gameOver = false;
	private boolean paused = false;

	public Snake(BoardContext boardContext, Food food) {
		this.boardContext = boardContext;
		this.food = food;
	}

	@Override
	public void init(GameState parentState) {
		this.reset();
	}

	public void reset() {
		this.body.clear();
		int headX = this.boardContext.getColumns() / 2;
		int headY = this.boardContext.getRows() / 2;

		this.body.addLast(new Cell(headX, headY));
		this.body.addLast(new Cell(headX - 1, headY));
		this.body.addLast(new Cell(headX - 2, headY));

		this.direction = Direction.RIGHT;
		this.nextDirection = Direction.RIGHT;
		this.score = 0;
		this.moveTimer = 0.0;
		this.gameOver = false;
		this.paused = false;

		if (this.food != null) {
			this.food.respawn(this.body);
		}
	}

	public void setDirection(Direction newDirection) {
		if (newDirection == null) {
			return;
		}
		if (newDirection.isOpposite(this.direction) || newDirection.isOpposite(this.nextDirection)) {
			return;
		}
		this.nextDirection = newDirection;
	}

	public void togglePause() {
		this.paused = !this.paused;
	}

	public boolean isPaused() {
		return this.paused;
	}

	public boolean isGameOver() {
		return this.gameOver;
	}

	public int getScore() {
		return this.score;
	}

	public List<Cell> getBody() {
		return Collections.unmodifiableList(new ArrayList<>(this.body));
	}

	@Override
	public void tick(double elapsedSecond, long loopID) {
		if (this.paused || this.gameOver) {
			return;
		}

		this.moveTimer += elapsedSecond;
		if (this.moveTimer >= MOVE_INTERVAL_SECONDS) {
			this.moveTimer -= MOVE_INTERVAL_SECONDS;
			this.move();
		}
	}

	private void move() {
		this.direction = this.nextDirection;
		Cell head = this.body.peekFirst();
		if (head == null) {
			return;
		}

		Cell newHead = new Cell(head.getX() + this.direction.getDx(), head.getY() + this.direction.getDy());

		// Wall collision
		if (!this.boardContext.isWithinBounds(newHead.getX(), newHead.getY())) {
			this.gameOver = true;
			return;
		}

		// Self collision
		boolean eating = this.food != null && newHead.equals(this.food.getPosition());
		if (this.body.contains(newHead) && (eating || !newHead.equals(this.body.peekLast()))) {
			this.gameOver = true;
			return;
		}

		this.body.addFirst(newHead);
		if (eating) {
			this.score++;
			this.food.respawn(this.body);
		} else {
			this.body.removeLast();
		}
	}

	@Override
	public void render(Graphics2D g, int renderWidth, int renderHeight) {
		int cellSize = this.boardContext.getCellSize();
		int pad = Math.max(1, cellSize / 8);
		int innerSize = cellSize - 2 * pad;
		int round = Math.max(2, cellSize / 3);

		boolean isHead = true;
		for (Cell cell : this.body) {
			int px = this.boardContext.toScreenX(cell.getX()) + pad;
			int py = this.boardContext.toScreenY(cell.getY()) + pad;

			g.setColor(isHead ? HEAD_COLOR : BODY_COLOR);
			g.fillRoundRect(px, py, innerSize, innerSize, round, round);
			isHead = false;
		}
	}

	@Override
	public void input(InputEvent event) {
		if (event.getType() != InputEvent.Type.KEY_PRESSED) {
			return;
		}

		int code = event.getKeyCode();
		switch (code) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W:
				this.setDirection(Direction.UP);
				event.consume();
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				this.setDirection(Direction.DOWN);
				event.consume();
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				this.setDirection(Direction.LEFT);
				event.consume();
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				this.setDirection(Direction.RIGHT);
				event.consume();
				break;
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_P:
				this.togglePause();
				event.consume();
				break;
			case KeyEvent.VK_R:
				if (this.gameOver) {
					this.reset();
					event.consume();
				}
				break;
			default:
				break;
		}
	}
}

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

public class SnakeElement implements StateElement {
	private static final Color HEAD_COLOR = new Color(180, 240, 90);
	private static final Color BODY_COLOR = new Color(70, 180, 70);

	private static final double MOVE_INTERVAL_SECONDS = 0.12; // ~8 ticks at 60 TPS

	private final BoardContext boardContext;
	private final FoodElement foodElement;
	private final Deque<Cell> body = new ArrayDeque<>();

	private Direction direction = Direction.RIGHT;
	private Direction nextDirection = Direction.RIGHT;

	private double moveTimer = 0.0;
	private int score = 0;
	private boolean gameOver = false;
	private boolean paused = false;

	public SnakeElement(BoardContext boardContext, FoodElement foodElement) {
		this.boardContext = boardContext;
		this.foodElement = foodElement;
	}

	@Override
	public void init(GameState parentState) {
		reset();
	}

	public void reset() {
		body.clear();
		int headX = boardContext.getColumns() / 2;
		int headY = boardContext.getRows() / 2;

		body.addLast(new Cell(headX, headY));
		body.addLast(new Cell(headX - 1, headY));
		body.addLast(new Cell(headX - 2, headY));

		direction = Direction.RIGHT;
		nextDirection = Direction.RIGHT;
		score = 0;
		moveTimer = 0.0;
		gameOver = false;
		paused = false;

		if (foodElement != null) {
			foodElement.respawn(body);
		}
	}

	public void setDirection(Direction newDirection) {
		if (newDirection == null) {
			return;
		}
		if (newDirection.isOpposite(direction) || newDirection.isOpposite(nextDirection)) {
			return;
		}
		this.nextDirection = newDirection;
	}

	public void togglePause() {
		this.paused = !this.paused;
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public int getScore() {
		return score;
	}

	public List<Cell> getBody() {
		return Collections.unmodifiableList(new ArrayList<>(body));
	}

	@Override
	public void tick(double elapsedSecond, long loopID) {
		if (paused || gameOver) {
			return;
		}

		moveTimer += elapsedSecond;
		if (moveTimer >= MOVE_INTERVAL_SECONDS) {
			moveTimer -= MOVE_INTERVAL_SECONDS;
			move();
		}
	}

	private void move() {
		direction = nextDirection;
		Cell head = body.peekFirst();
		if (head == null) {
			return;
		}

		Cell newHead = new Cell(head.getX() + direction.getDx(), head.getY() + direction.getDy());

		// Wall collision
		if (!boardContext.isWithinBounds(newHead.getX(), newHead.getY())) {
			gameOver = true;
			return;
		}

		// Self collision
		boolean eating = foodElement != null && newHead.equals(foodElement.getPosition());
		if (body.contains(newHead) && (eating || !newHead.equals(body.peekLast()))) {
			gameOver = true;
			return;
		}

		body.addFirst(newHead);
		if (eating) {
			score++;
			foodElement.respawn(body);
		} else {
			body.removeLast();
		}
	}

	@Override
	public void render(Graphics2D g, int renderWidth, int renderHeight) {
		int cellSize = boardContext.getCellSize();
		int pad = Math.max(1, cellSize / 8);
		int innerSize = cellSize - 2 * pad;
		int round = Math.max(2, cellSize / 3);

		boolean isHead = true;
		for (Cell cell : body) {
			int px = boardContext.toScreenX(cell.getX()) + pad;
			int py = boardContext.toScreenY(cell.getY()) + pad;

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
				setDirection(Direction.UP);
				event.consume();
				break;
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S:
				setDirection(Direction.DOWN);
				event.consume();
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A:
				setDirection(Direction.LEFT);
				event.consume();
				break;
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D:
				setDirection(Direction.RIGHT);
				event.consume();
				break;
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_P:
				togglePause();
				event.consume();
				break;
			case KeyEvent.VK_R:
				if (gameOver) {
					reset();
					event.consume();
				}
				break;
			default:
				break;
		}
	}
}

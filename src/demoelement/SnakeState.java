package demoelement;

import java.awt.Graphics2D;

import demoelement.core.BoardContext;
import demoelement.entity.Food;
import demoelement.entity.Grid;
import demoelement.entity.Snake;
import demoelement.entity.SnakeHud;
import engine.gamestate.GameState;
import engine.gamestate.GameStateHandler;
import engine.gamestate.StateID;

public class SnakeState extends GameState {

	public static final StateID ID = StateID.of("SNAKE_ELEMENT_DEMO");

	static {
		GameStateHandler.registerState(SnakeState.ID, new SnakeState());
	}

	private BoardContext boardContext;
	private Grid grid;
	private Food food;
	private Snake snake;
	private SnakeHud hud;

	public SnakeState() {
	}

	@Override
	public void init() {
		this.clearElements();

		this.boardContext = new BoardContext();
		this.food = new Food(this.boardContext);
		this.snake = new Snake(this.boardContext, this.food);
		this.grid = new Grid(this.boardContext);
		this.hud = new SnakeHud(this.boardContext, this.snake);

		this.addElement(this.grid);
		this.addElement(this.food);
		this.addElement(this.snake);
		this.addElement(this.hud);
	}

	@Override
	public void tick(double elapsedSecond, long loopID) {
		this.tickElements(elapsedSecond, loopID);
	}

	@Override
	public void render(Graphics2D g, int renderWidth, int renderHeight) {
		this.renderElements(g, renderWidth, renderHeight);
	}

	@Override
	public void closeState() {
		// Elements automatically closed and cleared by GameState
	}
}

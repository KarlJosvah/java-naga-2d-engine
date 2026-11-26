package demoelement;

import java.awt.Color;
import java.awt.Graphics2D;

import demoelement.core.BoardContext;
import demoelement.entity.FoodElement;
import demoelement.entity.GridElement;
import demoelement.entity.SnakeElement;
import demoelement.entity.SnakeHudElement;
import engine.gamestate.GameState;
import engine.gamestate.GameStateHandler;
import engine.gamestate.StateID;

public class SnakeState extends GameState {

	public static final StateID ID = StateID.of("SNAKE_ELEMENT_DEMO");

	static {
		GameStateHandler.registerState(SnakeState.ID, new SnakeState());
	}

	private BoardContext boardContext;
	private GridElement gridElement;
	private FoodElement foodElement;
	private SnakeElement snakeElement;
	private SnakeHudElement hudElement;

	public SnakeState() {
	}

	@Override
	public void init() {
		// Clean up any previously added elements on fresh init
		clearElements();

		boardContext = new BoardContext();
		foodElement = new FoodElement(boardContext);
		snakeElement = new SnakeElement(boardContext, foodElement);
		gridElement = new GridElement(boardContext);
		hudElement = new SnakeHudElement(boardContext, snakeElement);

		// Register elements in drawing/update order
		addElement(gridElement);
		addElement(foodElement);
		addElement(snakeElement);
		addElement(hudElement);
	}

	@Override
	public void tick(double elapsedSecond, long loopID) {
		tickElements(elapsedSecond, loopID);
	}

	@Override
	public void render(Graphics2D g, int renderWidth, int renderHeight) {
		renderElements(g, renderWidth, renderHeight);
	}

	@Override
	public void closeState() {
		// State elements are automatically cleaned up by GameState
	}
}

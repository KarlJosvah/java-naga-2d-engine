package engine.gamestate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.awt.Graphics2D;

import engine.Main;
import engine.GameLoop;

import engine.gamestate.NewState;

public class GameStateHandler {

// ======================================================================================================================================================

	private static final Map<StateID, GameState> states = new HashMap<StateID, GameState>();
	private static StateID defaultStateID = null;

	private final ConcurrentLinkedQueue<InputEvent> inputQueue = new ConcurrentLinkedQueue<InputEvent>();
	private GameState activeState = null;
	private StateID currentStateID = null;
	private GameLoop gameLoop = null;

	private HashMap<Integer, Boolean> keyUsed = new HashMap<Integer, Boolean>();

// ======================================================================================================================================================

	public static void setDefaultStateID(StateID id) {
		GameStateHandler.defaultStateID = id;
	}

	public static StateID getDefaultStateID() {
		return GameStateHandler.defaultStateID;
	}

// ======================================================================================================================================================

	public GameStateHandler(GameLoop gameLoop, StateID defaultStateID) {
		this.gameLoop = gameLoop;
		this.init(defaultStateID);
	}

	public GameStateHandler(GameLoop gameLoop) {
		this(gameLoop, GameStateHandler.defaultStateID);
	}

// ======================================================================================================================================================

	public static void registerState(StateID id, GameState state) {
		if (id == null || state == null) {
			Main.throwException(new IllegalArgumentException("StateID and GameState cannot be null"));
		}
		GameStateHandler.states.put(id, state);
	}

	public void init(StateID defaultStateID) {
		GameStateHandler.registerState(StateID.NEW_GAME, new NewState());
		StateID targetState = (defaultStateID != null) ? defaultStateID : GameStateHandler.defaultStateID;
		if (targetState == null) {
			Main.throwException(new IllegalStateException("Default StateID is not set in GameStateHandler!"));
		}
		this.changeState(targetState);
	}

	public void tick(double elapsedSecond, long loopID) {
		this.processInput();
		if (this.activeState != null) {
			this.activeState.tick(elapsedSecond, loopID);
		}
	}

	public void render(Graphics2D g, int renderWidth, int renderHeight) {
		if (this.activeState != null) {
			this.activeState.render(g, renderWidth, renderHeight);
		}
	}

	public void exit() {
		this.gameLoop.exit();
	}

// ======================================================================================================================================================

	public void changeState(StateID id) {
		GameState next = GameStateHandler.states.get(id);
		if (next == null) {
			Main.throwException(new IllegalArgumentException("Unknown state: " + id));
			return;
		}
		this.closeState();
		this.currentStateID = id;
		this.activeState = next;
		this.activeState.init(this);
	}

	public StateID getCurrentStateID() {
		return this.currentStateID;
	}

	public static GameState getState(StateID id) {
		return GameStateHandler.states.get(id);
	}

	public void closeState() {
		if (this.activeState != null) {
			this.activeState.closeState();
		}
	}

// ======================================================================================================================================================

	public void queueInput(InputEvent event) {
		this.inputQueue.add(event);
	}

	public void processInput() {
		InputEvent event;
		while ((event = this.inputQueue.poll()) != null) {
			if (event.getType() == InputEvent.Type.KEY_PRESSED) {
				this.keyUsed.put(event.getKeyCode(), true);
			} else if (event.getType() == InputEvent.Type.KEY_RELEASED) {
				this.keyUsed.put(event.getKeyCode(), false);
			}

			if (this.activeState != null) {
				this.activeState.input(event);
			}

			if (!event.isHandled() && this.activeState != null) {
				this.activeState.unhandled_input(event);
			}

			if (!event.isHandled() && this.gameLoop != null) {
				this.gameLoop.unhandledInput(event);
			}
		}
	}

	public boolean isKeyDown(int keyCode) {
		if (!this.keyUsed.containsKey(keyCode)) {
			return false;
		}
		return this.keyUsed.get(keyCode);
	}
}
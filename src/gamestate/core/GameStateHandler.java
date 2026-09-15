package gamestate.core;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.awt.Graphics2D;

import root.GameLoop;

import gamestate.MenuState;
import gamestate.NewState;
import gamestate.DemoState;

public class GameStateHandler {
	private final Map<StateID, GameState> states;
	private final ConcurrentLinkedQueue<InputEvent> inputQueue = new ConcurrentLinkedQueue<InputEvent>();
	private GameState activeState = null;
	private StateID currentStateID = null;
	private GameLoop gameLoop = null;

	private HashMap<Integer, Boolean> keyUsed = new HashMap<Integer, Boolean>();

// ======================================================================================================================================================

	public GameStateHandler(GameLoop gameLoop) {
		this.gameLoop = gameLoop;
		this.states = new EnumMap<StateID, GameState>(StateID.class);
		this.states.put(StateID.MENU, new MenuState());
		this.states.put(StateID.NEW_GAME, new NewState());
		this.states.put(StateID.DEMO, new DemoState());
		this.changeState(StateID.MENU);
	}

	public void exit() {
		this.gameLoop.exit();
	}

// ======================================================================================================================================================

	public void changeState(StateID id) {
		GameState next = this.states.get(id);
		if(next == null) {
			throw new IllegalArgumentException("Unknown state: " + id);
		}
		this.closeState();
		this.currentStateID = id;
		this.activeState = next;
		this.init();
	}

	public StateID getCurrentStateID() {
		return this.currentStateID;
	}

	public GameState getState(StateID id) {
		return this.states.get(id);
	}

	public void closeState() {
		if(this.activeState != null) {
			this.activeState.closeState();
		}
	}

// ======================================================================================================================================================

	public void init() {
		this.activeState.init(this);
	}

	public void queueInput(InputEvent event) {
		this.inputQueue.add(event);
	}

	public void processInput() {
		InputEvent event;
		while((event = this.inputQueue.poll()) != null) {
			if(event.getType() == InputEvent.Type.KEY_PRESSED) {
				this.keyUsed.put(event.getKeyCode(), true);
			} else if(event.getType() == InputEvent.Type.KEY_RELEASED) {
				this.keyUsed.put(event.getKeyCode(), false);
			}

			if(this.activeState != null) {
				this.activeState.input(event);
			}

			if(!event.isHandled() && this.activeState != null) {
				this.activeState.unhandled_input(event);
			}

			if(!event.isHandled() && this.gameLoop != null) {
				this.gameLoop.unhandledInput(event);
			}
		}
	}

	public void tick(double elapsedSecond, long loopID) {
		this.processInput();
		this.activeState.tick(elapsedSecond, loopID);
	}

	public void render(Graphics2D g, int renderWidth, int renderHeight) {
		this.activeState.render(g, renderWidth, renderHeight);
	}

// ======================================================================================================================================================

	public boolean isKeyDown(int keyCode) {
		if(!this.keyUsed.containsKey(keyCode)) {
			return false;
		}
		return this.keyUsed.get(keyCode);
	}
}

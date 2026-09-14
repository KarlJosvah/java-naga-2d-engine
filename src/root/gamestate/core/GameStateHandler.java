package root.gamestate.core;

import java.util.HashMap;
import java.awt.Graphics2D;

import root.GameLoop;

import root.gamestate.NewState;

public class GameStateHandler {
	private GameState activeState = null;
	private GameLoop gameLoop = null;

	private HashMap<Integer, Boolean> keyUsed = new HashMap<Integer, Boolean>();

// ======================================================================================================================================================

	public GameStateHandler(GameLoop gameLoop) {
		this.gameLoop = gameLoop;
		this.changeState(new NewState());
	}

	public void exit() {
		this.gameLoop.exit();
	}

// ======================================================================================================================================================

	public void changeState(GameState newState)	 {
		this.closeState();
		this.activeState = newState;
		this.init();
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

	public void tick(double elapsedSecond, long loopID) {
		this.activeState.tick(elapsedSecond, loopID);
	}

	public void render(Graphics2D g, int renderWidth, int renderHeight) {
		this.activeState.render(g, renderWidth, renderHeight);
	}

// ======================================================================================================================================================

	public void keyPressed(int keyCode) {
		this.activeState.keyPressed(keyCode);
		this.keyUsed.put(keyCode, true);
	}

	public void keyReleased(int keyCode) {
		this.keyUsed.put(keyCode, false);
	}

	public boolean isKeyDown(int keyCode) {
		if(!this.keyUsed.containsKey(keyCode)) {
			return false;
		}
		return this.keyUsed.get(keyCode);
	}

	public void mouseClicked(int x, int y, int button) {
		this.activeState.mouseClicked(x, y, button);
	}

	public void mousePressed(int x, int y, int button) {
		this.activeState.mousePressed(x, y, button);
	}

	public void mouseReleased(int x, int y, int button) {
		this.activeState.mouseReleased(x, y, button);
	}

	public void mouseDragged(int x, int y) {
		this.activeState.mouseDragged(x, y);
	}

	public void mouseMoved(int x, int y) {
		this.activeState.mouseMoved(x, y);
	}
}
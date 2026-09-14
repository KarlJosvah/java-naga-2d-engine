package root.gamestate;

import java.awt.Graphics2D;

import java.awt.event.KeyEvent;

import tools.Function;

import root.gamestate.core.GameState;
import root.gamestate.core.GameStateHandler;

public class NewState extends GameState {

	public NewState() {
	}

// ======================================================================================================================================================

	@Override
	public void init(GameStateHandler stateHandler) {
		__setGameStateHandler(stateHandler);
	}

	@Override
	public void tick(double elapsedSecond, long loopID) {
	}

	@Override
	public void render(Graphics2D g, int renderWidth, int renderHeight) {
	}

// ======================================================================================================================================================
	
	@Override
	public void closeState() {
	}

	@Override
	public void keyPressed(int keyCode) {
		if(keyCode == KeyEvent.VK_ENTER) {
		}
	}

	@Override
	public void mouseClicked(int x, int y, int button) {
	}

	@Override
	public void mousePressed(int x, int y, int button) {
	}

	@Override
	public void mouseReleased(int x, int y, int button) {
	}

	@Override
	public void mouseDragged(int x, int y) {
	}

	@Override
	public void mouseMoved(int x, int y) {
	}
}
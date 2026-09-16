package gamestate;

import java.awt.Graphics2D;

import java.awt.event.KeyEvent;

import tools.Function;

import gamestate.GameState;
import gamestate.GameStateHandler;

public class NewState extends GameState {

	public NewState() {
	}

// ======================================================================================================================================================

	@Override
	public void init() {
	}

	@Override
	public void tick(double elapsedSecond, long loopID) {
	}

	@Override
	public void render(Graphics2D g, int renderWidth, int renderHeight) {
	}

	@Override
	public void closeState() {
	}

// ======================================================================================================================================================
	
	@Override
	public void keyPressed(int keyCode) {
		if (keyCode == KeyEvent.VK_ENTER) {
		}
	}
}
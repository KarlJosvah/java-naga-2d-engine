package engine.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

import engine.Main;

public class KeyListenerMain extends KeyAdapter {
	private Main mainFrame = null;

	public KeyListenerMain(Main mainFrame) {
		this.mainFrame = mainFrame;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		this.mainFrame.keyPressed(event.getKeyCode(), event.getKeyChar());
	}

	@Override
	public void keyReleased(KeyEvent event) {
		this.mainFrame.keyReleased(event.getKeyCode());
	}
}
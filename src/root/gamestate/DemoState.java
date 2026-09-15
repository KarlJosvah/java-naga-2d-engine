package root.gamestate;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import demo.Demo;
import root.Main;
import root.gamestate.core.GameState;
import root.gamestate.core.GameStateHandler;
import root.gamestate.core.InputEvent;

public class DemoState extends GameState {
	private Demo demo;

	public DemoState() {
	}

// ======================================================================================================================================================

	@Override
	public void init() {
		this.demo = new Demo();
		this.demo.init(Main.WIDTH, Main.HEIGHT);
	}

	@Override
	public void tick(double elapsedSecond, long loopID) {
		if (this.demo != null) {
			this.demo.tick(elapsedSecond, this::isKeyDown);
		}
	}

	@Override
	public void render(Graphics2D g, int renderWidth, int renderHeight) {
		if (this.demo != null) {
			this.demo.render(g, renderWidth, renderHeight);
		}
	}

	@Override
	public void closeState() {
		if (this.demo != null) {
			this.demo.setShooting(false);
		}
	}

// ======================================================================================================================================================

	@Override
	public void input(InputEvent event) {
		super.input(event);

		if (event.isMouse() && this.demo != null) {
			if (event.getX() >= 0 && event.getY() >= 0) {
				this.demo.updateMousePosition(event.getX(), event.getY());
			}

			if (event.getType() == InputEvent.Type.MOUSE_PRESSED && event.getButton() == MouseEvent.BUTTON1) {
				this.demo.setShooting(true);
				event.consume();
			} else if (event.getType() == InputEvent.Type.MOUSE_RELEASED && event.getButton() == MouseEvent.BUTTON1) {
				this.demo.setShooting(false);
				event.consume();
			}
		} else if (event.getType() == InputEvent.Type.KEY_PRESSED) {
			int k = event.getKeyCode();
			if(java.util.Set.of(
				KeyEvent.VK_W,
				KeyEvent.VK_A,
				KeyEvent.VK_S,
				KeyEvent.VK_D,
				KeyEvent.VK_UP,
				KeyEvent.VK_LEFT,
				KeyEvent.VK_DOWN,
				KeyEvent.VK_RIGHT
			).contains(k)) {
				event.consume();
			}
		}
	}

	@Override
	public void mouseMoved(int x, int y) {
		super.updateMousePosition(x, y);
	}

	@Override
	public void mouseDragged(int x, int y) {
		super.updateMousePosition(x, y);
	}
}

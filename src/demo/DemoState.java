package demo;

import java.util.Set;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import engine.Main;
import gamestate.GameState;
import gamestate.GameStateHandler;
import gamestate.InputEvent;
import gamestate.StateID;

public class DemoState extends GameState {

// ======================================================================================================================================================

	public static final StateID ID = StateID.of("DEMO");

	static {
		GameStateHandler.registerState(DemoState.ID, new DemoState());
	}

	private Demo demo;

// ======================================================================================================================================================

	public DemoState() {
	}

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
			if (Set.of(
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
}

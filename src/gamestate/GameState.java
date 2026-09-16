package gamestate;

import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.RadialGradientPaint;

public abstract class GameState {

// ======================================================================================================================================================

	protected GameStateHandler stateHandler;

	private int mousePositionX = -1;
	private int mousePositionY = -1;
	protected Color mouseHoverColor = new Color(40, 40, 40);
	protected int mouseHoverSize = 300;

// ======================================================================================================================================================

	final void init(GameStateHandler stateHandler) {
		this.stateHandler = stateHandler;
		this.init();
	}

	public abstract void init();
	public abstract void tick(double elapsedSecond, long loopID);
	public abstract void render(Graphics2D g, int renderWidth, int renderHeight);
	public abstract void closeState();

// ======================================================================================================================================================

	protected final boolean isKeyDown(int keyCode) {
		return this.stateHandler.isKeyDown(keyCode);
	}

	public void input(InputEvent event) {
		if (event.isMouse() && event.getX() >= 0 && event.getY() >= 0) {
			updateMousePosition(event.getX(), event.getY());
		}

		switch (event.getType()) {
			case KEY_PRESSED:
				keyPressed(event.getKeyCode());
				break;
			case KEY_RELEASED:
				keyReleased(event.getKeyCode());
				break;
			case MOUSE_CLICKED:
				mouseClicked(event.getX(), event.getY(), event.getButton());
				break;
			case MOUSE_PRESSED:
				mousePressed(event.getX(), event.getY(), event.getButton());
				break;
			case MOUSE_RELEASED:
				mouseReleased(event.getX(), event.getY(), event.getButton());
				break;
			case MOUSE_DRAGGED:
				mouseDragged(event.getX(), event.getY());
				break;
			case MOUSE_MOVED:
				mouseMoved(event.getX(), event.getY());
				break;
			default:
				break;
		}
	}

	public void unhandled_input(InputEvent event) {
	}

	protected void keyPressed(int keyCode) {}
	protected void keyReleased(int keyCode) {}
	protected void mouseClicked(int x, int y, int button) {}
	protected void mousePressed(int x, int y, int button) {}
	protected void mouseReleased(int x, int y, int button) {}
	protected void mouseDragged(int x, int y) {}
	protected void mouseMoved(int x, int y) {}

// ======================================================================================================================================================

	public final void changeState(StateID id) {
		this.stateHandler.changeState(id);
	}

// ======================================================================================================================================================

	protected final void renderMouseHover(Graphics2D g) {
		Graphics2D g2d = (Graphics2D) g.create();

		int x = this.mousePositionX;
		int y = this.mousePositionY;
		int width = this.mouseHoverSize;
		int height = this.mouseHoverSize;
		int ovalX = x - (width / 2);
		int ovalY = y - (height / 2);

		Point2D center = new Point2D.Float(x, y);
		float radius = Math.max(width, height) / 2.0f;
		float[] dist = {0.0f, 1.0f};
		Color[] colors = {this.mouseHoverColor, new Color(0, 0, 0, 0)};

		RadialGradientPaint rgp = new RadialGradientPaint(center, radius, dist, colors);

		g2d.setPaint(rgp);
		g2d.fillOval(ovalX, ovalY, width, height);
		g2d.dispose();
	}

	protected final void updateMousePosition(int x, int y) {
		this.mousePositionX = x;
		this.mousePositionY = y;
	}

	protected Point getMousePosition() {
		return new Point(this.mousePositionX, this.mousePositionY);
	}

	protected final void setMouseHoverColor(Color newColor) {
		this.mouseHoverColor = newColor;
	}

	protected final void setMouseHoverSize(int size) {
		this.mouseHoverSize = size;
	}
}

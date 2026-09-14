package root.gamestate.core;

import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.RadialGradientPaint;

public abstract class GameState {

	protected GameStateHandler stateHandler;

	private int mousePositionX = -1;
	private int mousePositionY = -1;
	protected Color mouseHoverColor = new Color(40, 40, 40);
	protected int mouseHoverSize = 300;

// ======================================================================================================================================================

	public abstract void init(GameStateHandler stateHandler);
	public abstract void tick(double elapsedSecond, long loopID);
	public abstract void render(Graphics2D g, int renderWidth, int renderHeight);
	public abstract void closeState();

// ======================================================================================================================================================

	protected final boolean isKeyDown(int keyCode) {
		return this.stateHandler.isKeyDown(keyCode);
	}

	public abstract void keyPressed(int keyCode);
	public abstract void mouseClicked(int x, int y, int button);
	public abstract void mousePressed(int x, int y, int button);
	public abstract void mouseReleased(int x, int y, int button);
	public abstract void mouseDragged(int x, int y);
	public abstract void mouseMoved(int x, int y);

// ======================================================================================================================================================

	protected final void __setGameStateHandler(GameStateHandler stateHandler) {
		this.stateHandler = stateHandler;
	}

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
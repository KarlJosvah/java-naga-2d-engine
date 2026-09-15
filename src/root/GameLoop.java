package root;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import tools.Function;
import tools.AssetsLoader;

import root.gamestate.core.GameStateHandler;
import root.gamestate.core.InputEvent;

public class GameLoop {
	public static final int TARGET_TPS = 60;
	public static volatile int TARGET_FPS = 120;
	public static final Color DEFAULT_BG_COLOR = Color.BLACK;

	private Main mainFrame = null;
	private GameStateHandler stateHandler = null;

	private long fps = 0;
	private long tps = 0;

	private Font fpsFont = null;

	public GameLoop(Main mainFrame) {
		this.mainFrame = mainFrame;
		this.init();
	}

	public void exit() {
		this.mainFrame.exit();
	}

// ======================================================================================================================================================

	public void setFPS(long fps) {
		this.fps = fps;
	}

	public void setTPS(long tps) {
		this.tps = tps;
	}

	public void queueInput(InputEvent event) {
		if(this.stateHandler != null) {
			this.stateHandler.queueInput(event);
		}
	}

	public void unhandledInput(InputEvent event) {
		if(this.mainFrame != null) {
			this.mainFrame.unhandledInput(event);
		}
	}

// ======================================================================================================================================================

	public void init() {
		this.stateHandler = new GameStateHandler(this);
		try {
			this.fpsFont = AssetsLoader.loadFont("Android 101.ttf", Font.BOLD, 12);
		} catch(Exception e) {
			Main.throwException(e);
		}
	}

// ======================================================================================================================================================

	public void tick(double elapsedSecond, long loopID) {
		this.stateHandler.tick(elapsedSecond, loopID);
	}

// ======================================================================================================================================================

	public void render(Graphics2D g, int width, int height) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(GameLoop.DEFAULT_BG_COLOR);
		g.fillRect(0, 0, width, height);

		this.stateHandler.render(g, width, height);

		this.displayFPS(g);
	}

	private void displayFPS(Graphics2D g) {
		if(this.fpsFont != null) {
			g.setFont(this.fpsFont);
		}
		g.setColor(Color.GREEN);
		String fpsSTR = "" + Function.numberFormat(this.fps) + " FPS - " + Function.numberFormat(this.tps) + " TPS";
		g.drawString(fpsSTR, 5, 15);
	}
}
package engine;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.event.KeyEvent;

import javax.sound.sampled.Clip;

import engine.tools.Function;
import engine.tools.AssetsLoader;

import engine.Main;

import engine.gamestate.GameState;
import engine.gamestate.GameStateHandler;
import engine.gamestate.StateID;
import engine.gamestate.InputEvent;

import demo.DemoState;

public class MenuState extends GameState {

// ======================================================================================================================================================

	public static final StateID ID = StateID.of("MENU");
	
	static {
		GameStateHandler.registerState(MenuState.ID, new MenuState());
	}

// ======================================================================================================================================================

	public enum MenuOption {
		CONTINUE("Continue"),
		NEW_GAME("New Game"),
		DEMO("Play Demo"),
		SETTINGS("Settings"),
		QUIT_GAME("Quit Game");

		private final String label;

		MenuOption(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}

// ======================================================================================================================================================

	private Font menuFont = null;
	private MenuOption[] menuOptions = MenuOption.values();
	private int selectedIndex = 0;

	public static final Color OPTION_COLOR = Color.WHITE;
	public static final Color SELECTED_OPTION_COLOR = Function.getColorFromHex("#0206BA");

	private static final String COIN_SOUND_FILE_PATH = "pickup-coin-2.wav";
	private static final String MENU_FONT_FILE_PATH = "Android 101.ttf";
	private Clip coinSound = null;

// ======================================================================================================================================================

	public MenuState() {
	}

// ======================================================================================================================================================

	@Override
	public void init() {
		this.loadMenuAssets();
		this.selectedIndex = 0;
	}

	@Override
	public void tick(double elapsedSecond, long loopID) {
	}

	@Override
	public void render(Graphics2D g, int renderWidth, int renderHeight) {
		this.renderMenuOptions(g, renderWidth, renderHeight);
	}
	
	@Override
	public void closeState() {
	}

// ======================================================================================================================================================

	private void loadMenuAssets() {
		try {
			this.menuFont = AssetsLoader.loadFont(MenuState.MENU_FONT_FILE_PATH, Font.BOLD, 40);
		} catch(Exception e) {
			Main.throwException(e);
		}

		try {
			this.coinSound = AssetsLoader.loadClip(MenuState.COIN_SOUND_FILE_PATH, 80, this);
		} catch(Exception e) {
			Main.throwException(e);
		}
	}

// ======================================================================================================================================================

	private void renderMenuOptions(Graphics2D g, int renderWidth, int renderHeight) {
		if (this.menuFont != null) {
			g.setFont(this.menuFont);
		}

		int fontHeight = g.getFontMetrics().getHeight();
		int yOffset = (renderHeight - fontHeight * (menuOptions.length)) / 2;

		for (int i = 0; i < menuOptions.length; i++) {
			if (i == this.selectedIndex) {
				g.setColor(MenuState.SELECTED_OPTION_COLOR);
			} else {
				g.setColor(MenuState.OPTION_COLOR);
			}

			String label = menuOptions[i].getLabel();
			int x = (renderWidth - g.getFontMetrics().stringWidth(label)) / 2;
			g.drawString(label, x, yOffset);
			yOffset += fontHeight;
		}
	}

// ======================================================================================================================================================

	@Override
	public void input(InputEvent event) {
		super.input(event);
		if (event.getType() == InputEvent.Type.KEY_PRESSED) {
			int keyCode = event.getKeyCode();
			if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_ENTER) {
				event.consume();
			}
		}
	}

	@Override
	public void keyPressed(int keyCode) {
		if (keyCode == KeyEvent.VK_DOWN) {
			this.coinSound();
			this.selectedIndex++;
			if (this.selectedIndex >= this.menuOptions.length) {
				this.selectedIndex = 0;
			}
		} else if (keyCode == KeyEvent.VK_UP) {
			this.coinSound();
			this.selectedIndex--;
			if (this.selectedIndex < 0) {
				this.selectedIndex = this.menuOptions.length - 1;
			}
		}
		if (keyCode == KeyEvent.VK_ENTER) {
			this.coinSound();
			this.changeState();
		}
	}

	@Override
	public void mouseMoved(int x, int y) {
		super.updateMousePosition(x, y);
	}

// ======================================================================================================================================================

	private void coinSound() {
		if (this.coinSound != null) {
			this.coinSound.stop();
			this.coinSound.setFramePosition(0);
			this.coinSound.start();
		}
	}

	private void changeState() {
		switch (this.menuOptions[this.selectedIndex]) {
			case CONTINUE:
				break;
			case NEW_GAME:
				this.changeState(StateID.NEW_GAME);
				break;
			case DEMO:
				this.changeState(DemoState.ID);
				break;
			case SETTINGS:
				break;
			case QUIT_GAME:
				super.stateHandler.exit();
				break;
			default:
				break;
		}
	}
}
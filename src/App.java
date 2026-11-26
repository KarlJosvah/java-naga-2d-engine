import engine.Main;
import engine.GameLoop;
import engine.gamestate.GameStateHandler;
import demo.MenuState;
import demoelement.SnakeState;

public class App {

// ======================================================================================================================================================

	public static void init() {
		// Set the default state ID for the engine application
		Main.VSYNC(false);
		GameLoop.setTargetFPS(30);
		GameLoop.setDefaultBackgroundColor(java.awt.Color.GRAY);
		GameStateHandler.setDefaultStateID(MenuState.ID);
		GameStateHandler.setDefaultStateID(SnakeState.ID);
	}

// ======================================================================================================================================================
}

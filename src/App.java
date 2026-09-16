import engine.gamestate.GameStateHandler;
import engine.MenuState;
import demo.DemoState;

public class App {

// ======================================================================================================================================================

	public static void init() {
		// Set the default state ID for the engine application
		GameStateHandler.setDefaultStateID(MenuState.ID);
	}

// ======================================================================================================================================================
}

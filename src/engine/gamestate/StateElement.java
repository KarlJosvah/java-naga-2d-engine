package engine.gamestate;

import java.awt.Graphics2D;

/**
 * Lifecycle contract for components/elements managed inside a {@link GameState}.
 * All methods have default implementations so elements only implement the hooks they require.
 */
public interface StateElement {

	default void init(GameState parentState) {}

	default void tick(double elapsedSecond, long loopID) {}

	default void render(Graphics2D g, int renderWidth, int renderHeight) {}

	default void input(InputEvent event) {}

	default void close() {}
}

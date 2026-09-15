package gamestate.core;

import java.util.Objects;

public final class StateID {
	private final String id;

	private StateID(String id) {
		this.id = id;
	}

	public static StateID of(String id) {
		return new StateID(id);
	}

	// Built-in state IDs
	public static final StateID MENU = StateID.of("MENU");
	public static final StateID NEW_GAME = StateID.of("NEW_GAME");
	public static final StateID DEMO = StateID.of("DEMO");

	public String getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StateID stateID = (StateID) o;
		return Objects.equals(id, stateID.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return id;
	}
}

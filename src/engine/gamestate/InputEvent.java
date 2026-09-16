package engine.gamestate;

public class InputEvent {

// ======================================================================================================================================================
	
	public enum Type {
		KEY_PRESSED,
		KEY_RELEASED,
		KEY_TYPED,
		MOUSE_CLICKED,
		MOUSE_PRESSED,
		MOUSE_RELEASED,
		MOUSE_DRAGGED,
		MOUSE_MOVED
	}

// ======================================================================================================================================================

	private final Type type;
	private final int keyCode;
	private final char keyChar;
	private final int x;
	private final int y;
	private final int button;
	private boolean handled = false;

// ======================================================================================================================================================	

	public InputEvent(Type type, int keyCode, char keyChar, int x, int y, int button) {
		this.type = type;
		this.keyCode = keyCode;
		this.keyChar = keyChar;
		this.x = x;
		this.y = y;
		this.button = button;
	}

// ======================================================================================================================================================

	public static InputEvent createKeyEvent(Type type, int keyCode, char keyChar) {
		return new InputEvent(type, keyCode, keyChar, -1, -1, -1);
	}

	public static InputEvent createMouseEvent(Type type, int x, int y, int button) {
		return new InputEvent(type, -1, '\0', x, y, button);
	}

// ======================================================================================================================================================

	public Type getType() {
		return type;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public char getKeyChar() {
		return keyChar;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getButton() {
		return button;
	}

// ======================================================================================================================================================

	public boolean isHandled() {
		return handled;
	}

	public void consume() {
		this.handled = true;
	}

	public void markHandled() {
		this.handled = true;
	}

// ======================================================================================================================================================

	public boolean isKey() {
		return type == Type.KEY_PRESSED || type == Type.KEY_RELEASED || type == Type.KEY_TYPED;
	}

	public boolean isMouse() {
		return !isKey();
	}
}
# Java Naga 2D Engine

A modular, lightweight 2D game engine for Java, built on AWT/Swing graphics, providing hardware acceleration, flexible state management, and a high-precision game loop.

---

## 🚀 Key Features

* **Modular Engine Library**: Packaged as `naga-engine.jar`, allowing the engine to be embedded directly into other Java projects as a library dependency.
* **State Management System**: Clean `GameStateHandler` lifecycle managing transitions between `GameState` instances (`init`, `tick`, `render`, `closeState`).
* **High-Precision Game Loop**: Dual tick-rate and frame-rate handling (TPS/FPS) utilizing `LockSupport` nano-sleeps and high-resolution timing.
* **Abstracted Input Processing**: Concurrent queue-based `InputEvent` pipeline for thread-safe keyboard and mouse event handling.
* **Assets & Utility Suite**: Dedicated `AssetsLoader` for fonts, sound clips, and images, alongside screen resolution and color helper utilities.

---

## 📁 Repository & Package Structure

```text
src/
├── App.java                 # Project entry point configuration
├── engine/                  # Core Engine (packaged into naga-engine.jar)
│   ├── Main.java            # Main application window & engine loop launcher
│   ├── GameLoop.java        # TPS / FPS rendering loop
│   ├── gamestate/           # GameState, GameStateHandler, InputEvent, StateID
│   ├── listener/            # AWT/Swing Event Listeners
│   └── tools/               # AssetsLoader, Function utilities
└── demo/                    # Demo Application Implementation
    ├── DemoState.java       # Example game state
    ├── MenuState.java       # Main menu state
    └── ...
```

---

## 💡 Quick Start & Usage

### 1. Application Entry Point (`App.java`)

Define an `App` class in your project's default (`src/`) package. `engine.Main` automatically discovers `App.init()` via reflection upon launch to set up initial game states and defaults.

```java
import engine.gamestate.GameStateHandler;
import demo.MenuState;

public class App {
    public static void init() {
        // Register default state for your application
        GameStateHandler.setDefaultStateID(MenuState.ID);
    }
}
```

---

### 2. Creating a Custom Game State

Extend `engine.gamestate.GameState` and register it with `GameStateHandler`:

```java
package mygame;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.KeyEvent;

import engine.gamestate.GameState;
import engine.gamestate.GameStateHandler;
import engine.gamestate.StateID;

public class MyState extends GameState {

    public static final StateID ID = StateID.of("MY_GAME_STATE");

    static {
        // Register state with GameStateHandler
        GameStateHandler.registerState(MyState.ID, new MyState());
    }

    @Override
    public void init() {
        // Initialization logic when state becomes active
    }

    @Override
    public void tick(double elapsedSecond, long loopID) {
        // Logic updates per frame (e.g. physics, movement)
        if (isKeyDown(KeyEvent.VK_W)) {
            // Move player up
        }
    }

    @Override
    public void render(Graphics2D g, int renderWidth, int renderHeight) {
        // Drawing logic
        g.setColor(Color.WHITE);
        g.drawString("Hello Naga Engine!", 100, 100);
    }

    @Override
    public void closeState() {
        // Cleanup resources when leaving state
    }
}
```

---

### 3. State Navigation

Switch between registered game states at any time from within a `GameState`:

```java
// Switch to a different state
this.changeState(MyState.ID);
```

---

## 🛠️ Building & Running

### Windows Build Script (`.vscode/run.bat` / `run.bat.example`)

1. Compiles `src/engine/**/*.java` into `lib/naga-engine.jar`.
2. Compiles application code (`src/App.java`, `src/demo/**/*.java`) into `bin/`.
3. Launches the engine via `engine.Main`.

```powershell
.\.vscode\run.bat
```

### Manual Compilation

```bash
# 1. Compile engine into JAR
mkdir -p bin-engine lib
find src/engine -name "*.java" > engine_sources.txt
javac -source 11 -target 11 --add-exports java.desktop/sun.java2d.pipe.hw=ALL-UNNAMED -cp "lib/*" -d bin-engine/ @engine_sources.txt
jar cf lib/naga-engine.jar -C bin-engine/ .
rm -rf bin-engine engine_sources.txt

# 2. Compile Application Code
mkdir -p bin
find src -name "*.java" | grep -v "src/engine/" > app_sources.txt
javac -source 11 -target 11 --add-exports java.desktop/sun.java2d.pipe.hw=ALL-UNNAMED -cp "lib/*:lib/naga-engine.jar" -d bin/ @app_sources.txt
rm app_sources.txt

# 3. Run Engine
java --add-exports java.desktop/sun.java2d.pipe.hw=ALL-UNNAMED -Djava.library.path="lib" -cp ".:bin/:lib/*" engine.Main
```

---

## 📦 Releases & CI/CD

This repository includes a GitHub Actions workflow (`.github/workflows/release.yml`). Pushing a git version tag (e.g. `v1.0.0`) automatically creates a GitHub Release containing:

* **`naga-engine.jar`**: Reusable standalone engine library.
* **`naga-demo-bundle.zip`**: Complete demo package containing sources, compiled binaries, and libraries.

```bash
git tag v1.0.0
git push origin main --tags
```

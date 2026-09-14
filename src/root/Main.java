package root;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.GraphicsDevice;
import java.awt.image.BufferStrategy;
import java.util.concurrent.locks.LockSupport;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import root.listener.KeyListenerMain;
import root.listener.MouseListenerMain;

import tools.Function;

import java.awt.BufferCapabilities;
import java.awt.GraphicsConfiguration;

import sun.java2d.pipe.hw.ExtendedBufferCapabilities;
import sun.java2d.pipe.hw.ExtendedBufferCapabilities.VSyncType;

public class Main extends JFrame implements Runnable {
	public static final String TITLE = "A Java Game";
	public static final int WIDTH = (int) Function.getScreenResolution().getWidth();
	public static final int HEIGHT = (int) Function.getScreenResolution().getHeight();

	public static volatile boolean DEBUG_KEY_LISTENER = false;

	private Canvas canvas;
	private BufferStrategy bufferStrategy;
	private GameLoop gameLoop;

	private volatile boolean running = false;
	private Thread gameThread;

// ======================================================================================================================================================

	public static void main(String[] args) {
		Main.enableHighResolutionTimer();

		System.setProperty("sun.java2d.opengl", "true"); // Force OpenGL pipeline
		System.setProperty("sun.java2d.d3d", "false"); // Disable Direct3D if on Windows
		System.setProperty("sun.java2d.noddraw", "true"); // Disable DirectDraw lock

		SwingUtilities.invokeLater(() -> {
			Main main = new Main();
			main.start();
		});
	}

// ======================================================================================================================================================

	public Main() {
		this.init();
		this.config();
		// this.setVisible(true);
		Function.setFullScreenExclusive(this);
		// Function.getDefaultConfiguration();

		this.createUnsyncedBufferStrategy();
		// this.canvas.createBufferStrategy(3);
		this.bufferStrategy = this.canvas.getBufferStrategy();
		this.canvas.requestFocus();

		this.gameLoop = new GameLoop(this);
	}

	private void init() {
		this.setTitle(Main.TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setResizable(false);

		this.canvas = new Canvas();
		this.canvas.setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
		this.canvas.setMinimumSize(new Dimension(Main.WIDTH, Main.HEIGHT));
		this.canvas.setMaximumSize(new Dimension(Main.WIDTH, Main.HEIGHT));
		this.canvas.setFocusable(true);

		this.add(this.canvas);
		this.pack();
		this.setLocationRelativeTo(null);
	}

	private void config() {
		KeyListenerMain keyListener = new KeyListenerMain(this);
		MouseListenerMain mouseListener = new MouseListenerMain(this);

		this.canvas.addKeyListener(keyListener);
		this.canvas.addMouseListener(mouseListener);
		this.canvas.addMouseMotionListener(mouseListener);
	}

	public synchronized void start() {
		if(this.running) return;
		this.running = true;
		this.gameThread = new Thread(this, "GameLoop-Thread");
		this.gameThread.start();
	}

	public synchronized void stop() {
		if(!this.running) return;
		this.running = false;
	}

	public void exit() {
		this.stop();
		this.dispose();
		System.exit(0);
	}

// ======================================================================================================================================================

	public void renderFrame(double renderAlpha) {
		if(this.bufferStrategy == null) return;
		do {
			do {
				Graphics2D g = (Graphics2D) this.bufferStrategy.getDrawGraphics();
				try {
					this.gameLoop.render(g, Main.WIDTH, Main.HEIGHT);
				} finally {
					g.dispose();
				}
			} while(this.bufferStrategy.contentsRestored());
			this.bufferStrategy.show();
		} while(this.bufferStrategy.contentsLost());

		Toolkit.getDefaultToolkit().sync();
	}

	@Override
	public void run() {
		final double oneSecondNs = 1E9;
		final double timePerTick = oneSecondNs / GameLoop.TARGET_TPS;
		final double maxDeltaSkip = 10.0;

		long lastTime = System.nanoTime();
		long lastRenderTime = System.nanoTime();
		double delta = 0;

		long lastTimer = System.currentTimeMillis();
		long nbFrameRendered = 0;
		long nbTickExecuted = 0;
		long loopID = 0;

		int targetFps = GameLoop.TARGET_FPS;
		int cachedTargetFps = targetFps;
		double targetFrameTimeNs = oneSecondNs / targetFps;

		if(targetFps > Function.getDisplayRefreshRate()) {
			Main.throwException(new Exception("GPU overwork: TARGET_FPS is higher than device refresh rate"));
		}

		while(this.running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			lastTime = now;

			if(delta > maxDeltaSkip) {
				delta = maxDeltaSkip;
			}

			while(delta >= 1) {
				double elapsedSecond = (1.0 / GameLoop.TARGET_TPS);
				this.gameLoop.tick(elapsedSecond, loopID);
				nbTickExecuted++;
				loopID++;
				delta--;
			}

			targetFps = GameLoop.TARGET_FPS;
			if(targetFps != cachedTargetFps) {
				cachedTargetFps = targetFps;
				targetFrameTimeNs = (targetFps > 0) ? oneSecondNs / targetFps : 0;
			}

			if(targetFps <= 0) {
				// Unlimited: render every iteration, VSync will naturally cap
				double renderAlpha = delta;
				this.renderFrame(renderAlpha);
				nbFrameRendered++;
				lastRenderTime = System.nanoTime();
			} else {
				long timeSinceLastRender = System.nanoTime() - lastRenderTime;

				if(timeSinceLastRender >= targetFrameTimeNs) {
					// Time to render a frame
					lastRenderTime += (long) targetFrameTimeNs; // Set BEFORE render so VSync wait is counted

					if(System.nanoTime() - lastRenderTime > (long) targetFrameTimeNs) {
						lastRenderTime = System.nanoTime();
					}

					double renderAlpha = delta;
					this.renderFrame(renderAlpha);
					nbFrameRendered++;
				} else {
					// Sleep to avoid busy-spinning; yield the CPU until close to target time
					long remainingNs = (long) (targetFrameTimeNs - timeSinceLastRender);
					if(remainingNs > 2_000_000L) {
						// Sleep for most of the remaining time, leaving ~1ms margin for precision
						LockSupport.parkNanos(remainingNs - 1_000_000L);
					} else if(remainingNs > 0) {
						// Close enough — spin-wait for precision
						Thread.onSpinWait();
					}
				}
			}

			if(System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				this.gameLoop.setFPS(nbFrameRendered);
				this.gameLoop.setTPS(nbTickExecuted);
				nbFrameRendered = 0;
				nbTickExecuted = 0;
			}
		}
	}

// ======================================================================================================================================================

	public void keyPressed(int keyCode, char keyChar) {
		switch (keyCode) {
			case KeyEvent.VK_ESCAPE:
				this.exit();
				break;
			case KeyEvent.VK_F1:
				Main.DEBUG_KEY_LISTENER = !Main.DEBUG_KEY_LISTENER;
				break;
			default:
				break;
		}
		if(Main.DEBUG_KEY_LISTENER) {
			System.out.println(keyCode + " " + keyChar);
		}
		if(this.gameLoop != null) {
			this.gameLoop.keyPressed(keyCode);
		}
	}

	public void keyReleased(int keyCode) {
		if(this.gameLoop != null) {
			this.gameLoop.keyReleased(keyCode);
		}
	}

	public void mouseClicked(int x, int y, int button) {
		if(this.gameLoop != null) {
			this.gameLoop.mouseClicked(x, y, button);
		}
	}

	public void mousePressed(int x, int y, int button) {
		if(this.gameLoop != null) {
			this.gameLoop.mousePressed(x, y, button);
		}
	}

	public void mouseReleased(int x, int y, int button) {
		if(this.gameLoop != null) {
			this.gameLoop.mouseReleased(x, y, button);
		}
	}

	public void mouseDragged(int x, int y) {
		if(this.gameLoop != null) {
			this.gameLoop.mouseDragged(x, y);
		}
	}

	public void mouseMoved(int x, int y) {
		if(this.gameLoop != null) {
			this.gameLoop.mouseMoved(x, y);
		}
	}

// ======================================================================================================================================================

	public static void throwException(Exception e) {
		// Do Nothing for now
		// e.printStackTrace();
	}

// ======================================================================================================================================================

	private void createUnsyncedBufferStrategy() {
		GraphicsConfiguration gc = this.canvas.getGraphicsConfiguration();
		BufferCapabilities defaultCaps = gc.getBufferCapabilities();

		try {
			ExtendedBufferCapabilities noSyncCaps = new ExtendedBufferCapabilities(
				defaultCaps.getFrontBufferCapabilities(),
				defaultCaps.getBackBufferCapabilities(),
				defaultCaps.getFlipContents(),
				VSyncType.VSYNC_OFF
			);
			this.canvas.createBufferStrategy(3, noSyncCaps);
		} catch (Exception e) {
			// Fallback if the driver/pipeline doesn't support forcing vsync off
			this.canvas.createBufferStrategy(3);
			Main.throwException(new Exception("createBufferStrategy with VSYNC_OFF not Supported"));
		}
		this.bufferStrategy = this.canvas.getBufferStrategy();
	}

	public static void enableHighResolutionTimer() {
		Thread timerThread = new Thread(() -> {
			try {
				Thread.sleep(Long.MAX_VALUE);
			} catch (InterruptedException ignored) {}
		}, "HighResTimer");
		timerThread.setDaemon(true);
		timerThread.start();
	}
}
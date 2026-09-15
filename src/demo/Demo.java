package demo;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.IntPredicate;

import demo.core.Camera;
import demo.core.Rectangle;
import demo.entity.Player;
import demo.entity.Target;
import demo.particule.Bullet;
import demo.world.Map;
import demo.world.StaticBody;

public class Demo {
	private Map map;
	private Player player;
	private Camera camera;
	private HUD hud;

	private List<Bullet> bullets;
	private List<Target> targets;

	private int killCount = 0;
	private double targetSpawnTimer = 0;
	private double targetSpawnInterval = 2.5; // seconds
	private int maxTargets = 8;
	private Random random = new Random();

	private boolean isShooting = false;
	private int mouseX = -1;
	private int mouseY = -1;
	private double shotCooldownTimer = 0.15;
	private final double fireDelaySeconds = 0.15; // 150ms delay between shots

	public Demo() {
	}

	public void init(int viewportWidth, int viewportHeight) {
		this.map = new Map(2000, 2000);
		this.player = new Player(map.getWidth() / 2.0, map.getHeight() / 2.0);
		this.camera = new Camera(viewportWidth, viewportHeight);
		this.hud = new HUD();

		this.bullets = new ArrayList<Bullet>();
		this.targets = new ArrayList<Target>();

		this.killCount = 0;
		this.targetSpawnTimer = 0;
		this.isShooting = false;
		this.shotCooldownTimer = this.fireDelaySeconds;

		// Initial targets spawn
		for (int i = 0; i < 4; i++) {
			spawnTarget();
		}
	}

	public void setShooting(boolean shooting) {
		this.isShooting = shooting;
	}

	public void updateMousePosition(int x, int y) {
		if (x >= 0 && y >= 0) {
			this.mouseX = x;
			this.mouseY = y;
		}
	}

	public void tick(double elapsedSecond, IntPredicate isKeyDown) {
		// 1. Move Player
		player.tick(elapsedSecond, isKeyDown);

		// 2. Resolve Player vs Obstacle collisions (AABB push-back)
		resolvePlayerCollisions();

		// 3. Update Bullets
		Iterator<Bullet> bulletIt = bullets.iterator();
		while (bulletIt.hasNext()) {
			Bullet b = bulletIt.next();
			b.tick(elapsedSecond);

			// Check bullet out of map bounds
			if (b.getX() < 0 || b.getX() > map.getWidth() || b.getY() < 0 || b.getY() > map.getHeight()) {
				b.destroy();
			}

			// Check bullet vs obstacle collisions
			if (b.isAlive()) {
				for (StaticBody obstacle : map.getObstacles()) {
					if (b.getCollisionShape().collidesWith(obstacle.getCollisionShape())) {
						b.destroy();
						break;
					}
				}
			}

			// Check bullet vs target collisions
			if (b.isAlive()) {
				for (Target t : targets) {
					if (t.isAlive() && b.getCollisionShape().collidesWith(t.getCollisionShape())) {
						b.destroy();
						t.destroy();
						killCount++;
						break;
					}
				}
			}

			if (!b.isAlive()) {
				bulletIt.remove();
			}
		}

		// Remove dead targets
		targets.removeIf(t -> !t.isAlive());

		// 4. Target Spawning Logic
		targetSpawnTimer += elapsedSecond;
		if (targetSpawnTimer >= targetSpawnInterval) {
			targetSpawnTimer = 0;
			if (targets.size() < maxTargets) {
				spawnTarget();
			}
		}

		// 5. Update Camera
		camera.setViewportSize(camera.getViewportWidth(), camera.getViewportHeight());
		camera.follow(player.getCenterX(), player.getCenterY());
		camera.clamp(map.getWidth(), map.getHeight());

		// 6. Handle Continuous Shooting
		this.shotCooldownTimer += elapsedSecond;
		if (this.isShooting && this.mouseX >= 0 && this.mouseY >= 0) {
			if (this.shotCooldownTimer >= this.fireDelaySeconds) {
				this.shoot(this.mouseX, this.mouseY);
				this.shotCooldownTimer = 0;
			}
		}
	}

	private void resolvePlayerCollisions() {
		Rectangle pBounds = player.getBounds();

		for (StaticBody obstacle : map.getObstacles()) {
			Rectangle oBounds = obstacle.getBounds();
			if (pBounds.intersects(oBounds)) {
				// Calculate overlap on both axes
				double overlapXLeft = (pBounds.getX() + pBounds.getWidth()) - oBounds.getX();
				double overlapXRight = (oBounds.getX() + oBounds.getWidth()) - pBounds.getX();
				double overlapYTop = (pBounds.getY() + pBounds.getHeight()) - oBounds.getY();
				double overlapYBottom = (oBounds.getY() + oBounds.getHeight()) - pBounds.getY();

				double minOverlapX = Math.min(overlapXLeft, overlapXRight);
				double minOverlapY = Math.min(overlapYTop, overlapYBottom);

				// Push player out along the axis of smallest overlap
				if (minOverlapX < minOverlapY) {
					if (overlapXLeft < overlapXRight) {
						player.setX(pBounds.getX() - minOverlapX);
					} else {
						player.setX(pBounds.getX() + minOverlapX);
					}
				} else {
					if (overlapYTop < overlapYBottom) {
						player.setY(pBounds.getY() - minOverlapY);
					} else {
						player.setY(pBounds.getY() + minOverlapY);
					}
				}
				pBounds = player.getBounds();
			}
		}
	}

	private void spawnTarget() {
		int targetSize = 25;
		int maxRetries = 30;

		for (int attempt = 0; attempt < maxRetries; attempt++) {
			double spawnX = 50 + random.nextDouble() * (map.getWidth() - 100 - targetSize);
			double spawnY = 50 + random.nextDouble() * (map.getHeight() - 100 - targetSize);

			Rectangle candidate = new Rectangle(spawnX, spawnY, targetSize, targetSize);

			if (map.isPositionFree(candidate)) {
				targets.add(new Target(spawnX, spawnY));
				break;
			}
		}
	}

	public void shoot(int screenX, int screenY) {
		double[] worldCoords = camera.screenToWorld(screenX, screenY);
		double targetX = worldCoords[0];
		double targetY = worldCoords[1];

		bullets.add(new Bullet(player.getCenterX(), player.getCenterY(), targetX, targetY));
	}

	public void render(Graphics2D g, int screenWidth, int screenHeight) {
		camera.setViewportSize(screenWidth, screenHeight);

		// Save original transform
		AffineTransform origTx = g.getTransform();

		// Apply camera transform (World coordinates)
		camera.applyTransform(g);

		// Render World (Map, Targets, Player, Bullets)
		map.render(g);

		for (Target t : targets) {
			t.render(g);
		}

		player.render(g);

		for (Bullet b : bullets) {
			b.render(g);
		}

		// Restore transform to screen space
		g.setTransform(origTx);

		// Render HUD (Screen space)
		hud.render(g, screenWidth, screenHeight, killCount);
	}
}

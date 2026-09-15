package demo.particule;

import java.awt.Color;
import java.awt.Graphics2D;

import demo.core.CollisionShape;
import demo.core.Rectangle;

public class Bullet {
	private double x;
	private double y;
	private final double width = 8;
	private final double height = 8;
	private double dirX;
	private double dirY;
	private double speed = 700.0; // pixels per second
	private final Color color = Color.RED;
	private final CollisionShape collisionShape;
	private boolean alive = true;

	public Bullet(double startX, double startY, double targetX, double targetY) {
		this.x = startX - width / 2.0;
		this.y = startY - height / 2.0;
		this.collisionShape = new CollisionShape(x, y, width, height);

		double dx = targetX - startX;
		double dy = targetY - startY;
		double distance = Math.sqrt(dx * dx + dy * dy);

		if (distance > 0) {
			this.dirX = dx / distance;
			this.dirY = dy / distance;
		} else {
			this.dirX = 1;
			this.dirY = 0;
		}
	}

	public void tick(double elapsedSecond) {
		if (!alive) return;
		this.x += dirX * speed * elapsedSecond;
		this.y += dirY * speed * elapsedSecond;
		this.collisionShape.setPosition(this.x, this.y);
	}

	public void render(Graphics2D g) {
		if (!alive) return;
		g.setColor(this.color);
		g.fillRect((int) this.x, (int) this.y, (int) this.width, (int) this.height);
	}

	public void destroy() {
		this.alive = false;
	}

	public boolean isAlive() {
		return alive;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public CollisionShape getCollisionShape() {
		return collisionShape;
	}

	public Rectangle getBounds() {
		return collisionShape.getBounds();
	}
}

package demo.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import demo.core.CollisionShape;
import demo.core.Rectangle;

public class Target {
	private double x;
	private double y;
	private final double width = 25;
	private final double height = 25;
	private final Color color = Color.YELLOW;
	private final CollisionShape collisionShape;
	private boolean alive = true;

	public Target(double x, double y) {
		this.x = x;
		this.y = y;
		this.collisionShape = new CollisionShape(x, y, width, height);
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

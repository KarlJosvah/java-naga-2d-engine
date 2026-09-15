package demo.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.function.IntPredicate;

import demo.core.CollisionShape;
import demo.core.Rectangle;

public class Player {
	private double x;
	private double y;
	private final double width = 30;
	private final double height = 30;
	private double speed = 300.0; // pixels per second
	private final Color color = Color.CYAN;
	private final CollisionShape collisionShape;

	public Player(double startX, double startY) {
		this.x = startX;
		this.y = startY;
		this.collisionShape = new CollisionShape(x, y, width, height);
	}

	public void tick(double elapsedSecond, IntPredicate isKeyDown) {
		double dx = 0;
		double dy = 0;

		if (isKeyDown.test(KeyEvent.VK_W) || isKeyDown.test(KeyEvent.VK_UP)) {
			dy -= 1.0;
		}
		if (isKeyDown.test(KeyEvent.VK_S) || isKeyDown.test(KeyEvent.VK_DOWN)) {
			dy += 1.0;
		}
		if (isKeyDown.test(KeyEvent.VK_A) || isKeyDown.test(KeyEvent.VK_LEFT)) {
			dx -= 1.0;
		}
		if (isKeyDown.test(KeyEvent.VK_D) || isKeyDown.test(KeyEvent.VK_RIGHT)) {
			dx += 1.0;
		}

		if (dx != 0 && dy != 0) {
			double norm = Math.sqrt(dx * dx + dy * dy);
			dx /= norm;
			dy /= norm;
		}

		this.x += dx * speed * elapsedSecond;
		this.y += dy * speed * elapsedSecond;
		this.collisionShape.setPosition(this.x, this.y);
	}

	public void render(Graphics2D g) {
		g.setColor(this.color);
		g.fillRect((int) this.x, (int) this.y, (int) this.width, (int) this.height);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
		this.collisionShape.setPosition(this.x, this.y);
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
		this.collisionShape.setPosition(this.x, this.y);
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
		this.collisionShape.setPosition(this.x, this.y);
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getCenterX() {
		return x + width / 2.0;
	}

	public double getCenterY() {
		return y + height / 2.0;
	}

	public CollisionShape getCollisionShape() {
		return collisionShape;
	}

	public Rectangle getBounds() {
		return collisionShape.getBounds();
	}
}

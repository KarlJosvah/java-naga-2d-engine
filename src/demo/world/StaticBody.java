package demo.world;

import java.awt.Color;
import java.awt.Graphics2D;

import demo.core.CollisionShape;
import demo.core.Rectangle;

public class StaticBody {
	private final double x;
	private final double y;
	private final double width;
	private final double height;
	private Color color = Color.GRAY;
	private final CollisionShape collisionShape;

	public StaticBody(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.collisionShape = new CollisionShape(x, y, width, height);
	}

	public StaticBody(double x, double y, double width, double height, Color color) {
		this(x, y, width, height);
		this.color = color;
	}

	public void render(Graphics2D g) {
		g.setColor(this.color);
		g.fillRect((int) this.x, (int) this.y, (int) this.width, (int) this.height);
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

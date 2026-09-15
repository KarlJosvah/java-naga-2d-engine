package demo.core;

public class CollisionShape {
	private final Rectangle bounds;

	public CollisionShape(double x, double y, double width, double height) {
		this.bounds = new Rectangle(x, y, width, height);
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setPosition(double x, double y) {
		this.bounds.setPosition(x, y);
	}

	public void setSize(double width, double height) {
		this.bounds.setWidth(width);
		this.bounds.setHeight(height);
	}

	public boolean collidesWith(CollisionShape other) {
		if (other == null || other.getBounds() == null) return false;
		return this.bounds.intersects(other.getBounds());
	}

	public boolean containsPoint(double px, double py) {
		return this.bounds.contains(px, py);
	}
}

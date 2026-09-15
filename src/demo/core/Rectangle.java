package demo.core;

public class Rectangle {
	private double x;
	private double y;
	private double width;
	private double height;

	public Rectangle(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public void set(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getCenterX() {
		return x + width / 2.0;
	}

	public double getCenterY() {
		return y + height / 2.0;
	}

	public boolean intersects(Rectangle other) {
		return this.x < other.x + other.width &&
		       this.x + this.width > other.x &&
		       this.y < other.y + other.height &&
		       this.y + this.height > other.y;
	}

	public boolean contains(double px, double py) {
		return px >= this.x && px <= this.x + this.width &&
		       py >= this.y && py <= this.y + this.height;
	}
}

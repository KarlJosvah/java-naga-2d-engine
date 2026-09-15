package demo.core;

import java.awt.Graphics2D;

public class Camera {
	private double x;
	private double y;
	private int viewportWidth;
	private int viewportHeight;

	public Camera(int viewportWidth, int viewportHeight) {
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.x = 0;
		this.y = 0;
	}

	public void follow(double targetCenterX, double targetCenterY) {
		this.x = targetCenterX - (viewportWidth / 2.0);
		this.y = targetCenterY - (viewportHeight / 2.0);
	}

	public void clamp(double mapWidth, double mapHeight) {
		if (mapWidth > viewportWidth) {
			if (this.x < 0) this.x = 0;
			if (this.x > mapWidth - viewportWidth) this.x = mapWidth - viewportWidth;
		} else {
			this.x = (mapWidth - viewportWidth) / 2.0;
		}

		if (mapHeight > viewportHeight) {
			if (this.y < 0) this.y = 0;
			if (this.y > mapHeight - viewportHeight) this.y = mapHeight - viewportHeight;
		} else {
			this.y = (mapHeight - viewportHeight) / 2.0;
		}
	}

	public void applyTransform(Graphics2D g) {
		g.translate(-x, -y);
	}

	public double[] screenToWorld(int screenX, int screenY) {
		return new double[] { screenX + x, screenY + y };
	}

	public int[] worldToScreen(double worldX, double worldY) {
		return new int[] { (int) (worldX - x), (int) (worldY - y) };
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getViewportWidth() {
		return viewportWidth;
	}

	public int getViewportHeight() {
		return viewportHeight;
	}

	public void setViewportSize(int width, int height) {
		this.viewportWidth = width;
		this.viewportHeight = height;
	}
}

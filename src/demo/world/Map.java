package demo.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import demo.core.Rectangle;

public class Map {
	private final double width;
	private final double height;
	private final List<StaticBody> obstacles;
	private final Color bgColor = new Color(30, 30, 30);
	private final Color borderColor = Color.WHITE;

	public Map(double width, double height) {
		this.width = width;
		this.height = height;
		this.obstacles = new ArrayList<StaticBody>();

		this.initDefaultMap();
	}

	private void initDefaultMap() {
		double wallThickness = 20.0;
		Color wallColor = new Color(80, 80, 80);

		// Boundaries (Top, Bottom, Left, Right)
		obstacles.add(new StaticBody(0, 0, width, wallThickness, wallColor));
		obstacles.add(new StaticBody(0, height - wallThickness, width, wallThickness, wallColor));
		obstacles.add(new StaticBody(0, 0, wallThickness, height, wallColor));
		obstacles.add(new StaticBody(width - wallThickness, 0, wallThickness, height, wallColor));

		// Inner Obstacles
		Color obstacleColor = new Color(100, 100, 120);
		obstacles.add(new StaticBody(300, 300, 150, 100, obstacleColor));
		obstacles.add(new StaticBody(800, 200, 100, 250, obstacleColor));
		obstacles.add(new StaticBody(1300, 400, 200, 100, obstacleColor));
		obstacles.add(new StaticBody(400, 800, 250, 120, obstacleColor));
		obstacles.add(new StaticBody(1000, 900, 150, 150, obstacleColor));
		obstacles.add(new StaticBody(700, 1300, 200, 150, obstacleColor));
		obstacles.add(new StaticBody(1400, 1200, 120, 200, obstacleColor));
		obstacles.add(new StaticBody(300, 1400, 180, 180, obstacleColor));
	}

	public void render(Graphics2D g) {
		// Draw Map Background
		g.setColor(bgColor);
		g.fillRect(0, 0, (int) width, (int) height);

		// Draw Obstacles & Walls
		for (StaticBody obstacle : obstacles) {
			obstacle.render(g);
		}

		// Draw Border Outline
		g.setColor(borderColor);
		g.drawRect(0, 0, (int) width, (int) height);
	}

	public boolean isPositionFree(Rectangle area) {
		for (StaticBody obstacle : obstacles) {
			if (obstacle.getBounds().intersects(area)) {
				return false;
			}
		}
		return true;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public List<StaticBody> getObstacles() {
		return obstacles;
	}
}

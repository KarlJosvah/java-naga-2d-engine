package engine.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import engine.Main;

public class MouseListenerMain extends MouseAdapter {
	private Main mainFrame = null;

	public MouseListenerMain(Main mainFrame) {
		this.mainFrame = mainFrame;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		this.mainFrame.mouseClicked(event.getX(), event.getY(), event.getButton());
	}

	@Override
	public void mousePressed(MouseEvent event) {
		this.mainFrame.mousePressed(event.getX(), event.getY(), event.getButton());
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		this.mainFrame.mouseReleased(event.getX(), event.getY(), event.getButton());
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		this.mainFrame.mouseDragged(event.getX(), event.getY());
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		this.mainFrame.mouseMoved(event.getX(), event.getY());
	}
}
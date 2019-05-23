import java.awt.Color;
import java.awt.Graphics;

public class Ball extends PhysicsObject {

	public Ball(int x, int y, int w, int h, double m, double xv, double yv, double a, double c) {
		super(x, y, w, h, m, xv, yv, a, c);
	}
	
	public void draw(Graphics window) {
		window.setColor(getColor());
		window.drawOval(getX(), getY(), getWidth(), getHeight());
	}
	
	public void move(Graphics window) {
		window.setColor(getColor());
		setXVelocity(getXVelocity() + getAcceleration());
		setX(getX() + (int) getXVelocity());
		setY(getY() + (int) getYVelocity());
		window.setColor(getColor());
		window.fillRect(getX(), getY(), getWidth(), getHeight());
	}
	
}

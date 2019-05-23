import java.awt.Color;
import java.awt.Graphics;

public class PhysicsObject {

	private double mass;
	private double xvelocity;
	private double yvelocity;
	private double acceleration;
	private double momentum;
	private double force;
	private int width;
	private int height;
	private int x;
	private int y;
	private double charge;
	private Color color;

	public PhysicsObject(int x, int y, int w, int h, double m, double xv, double yv, double a, double c) {
		width = w;
		height = h;
		mass = m;
		xvelocity = xv;
		yvelocity = yv;
		acceleration = a;
		force = m * a;
		momentum = 0; //calculate
		this.x = x;
		this.y = y;
		charge = c;
	}
	
	public Color getColor() {
		return color;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public double getMass() {
		return mass;
	}
	public double getXVelocity() {
		return xvelocity;
	}
	public double getYVelocity() {
		return yvelocity;
	}
	public double getAcceleration() {
		return acceleration;
	}
	public double getMomentum() {
		return momentum;
	}
	public double getForce() {
		return force;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	
	public void setColor(Color c) {
		this.color = c;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setMass(double m) {
		mass = m;
	}
	public void setXVelocity(double v) {
		xvelocity = v;
	}
	public void setYVelocity(double v) {
		yvelocity = v;
	}
	public void setAcceleration(double a) {
		acceleration = a;
	}
	public void setMomentum(double p) {
		momentum = p;
	}
	public void setForce(double f) {
		force = f;
	}
	public void setWidth(int w) {
		width = w;
	}
	public void setHeight(int h) {
		height = h;
	}
	
	public void draw(Graphics window) {
		window.setColor(color);
		window.drawRect(x, y, width, height);
	}
	
	public void move(Graphics window) {
		window.setColor(color);
		xvelocity+=acceleration;
		setX(x + (int) xvelocity);
		window.drawRect(x + (int) xvelocity, y, width, height);
	}
	
	public void updateMomentum() {
		setMomentum(getXVelocity() * getMass());
	}
	
}

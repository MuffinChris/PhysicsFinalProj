import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PhysicsObject extends Shape{

	private double mass;
	private Vector velocity;
	private Vector acceleration;
	private Vector momentum;
	private Vector force;
	private int width;
	private int height;
	private int x;
	private int y;
	private double charge;
	private Color color;
	private int priority;
	private double energy;
	private boolean frozen;

	public PhysicsObject(int x, int y, int w, int h, Color c, double m, Vector v, Vector a, double charge, int priority) {
		super(x, y, w, h);
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		color = c;
		mass = m;
		velocity = v;
		acceleration = a;
		this.charge = charge;
		momentum = new Vector(0, 0);
		force = new Vector(0, 0);
		this.priority = priority;
		frozen = false;
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

	public Vector getVelocity() {
		return velocity;
	}

	public Vector getAcceleration() {
		return acceleration;
	}

	public Vector getMomentum() {
		return momentum;
	}

	public Vector getForce() {
		return force;
	}

	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getPriority() {
		return priority;
	}

	public double getEnergy() {
		return energy;
	}
	public double getCharge() {
		return charge;
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
	public void setVelocity(Vector v) {
		velocity = v;
	}
	public void setAcceleration(Vector a) {
		acceleration = a;
	}
	public void setMomentum(Vector p) {
		momentum = p;
	}
	public void setForce(Vector f) {
		force = f;
	}
	public void setWidth(int w) {
		width = w;
	}
	public void setHeight(int h) {
		height = h;
	}
	public void setPriority(int p) {
		priority = p;
	}
	public void setEnergy(double e) {
		energy = e;
	}

	public void setCharge(double c) {
		charge = c;
	}

	public void updateEnergy() {
		energy = 0.5 * mass * Math.pow(getVelocity().getRate(), 2);
	}

	public double getXEnergy() {
		return 0.5 * mass * Math.pow(getVelocity().getXR(), 2);
	}

	public double getYEnergy() {
		return 0.5 * mass * Math.pow(getVelocity().getYR(), 2);
	}
	
	public void draw(Graphics window) {
		window.setColor(color);
		window.fillRect(x, y, width, height);
		window.setColor(Color.BLACK);
		DecimalFormat df = new DecimalFormat("#.##");
		window.drawString(df.format(charge), x, y);
	}
	
	public void move(Graphics window) {
		if (frozen) {
			draw(window);
			return;
		}
		window.setColor(color);
		velocity.setXR(velocity.getXR() + acceleration.getXR());
		setX(getX() + (int) Math.round(velocity.getXR()));
		velocity.setYR(velocity.getYR() + acceleration.getYR());
		setY(getY() + (int) Math.round(velocity.getYR()));
		velocity.calculateVector();
		acceleration.calculateVector();
		setCX(getX());
		setCY(getY());
		//setForce(new Vector(getForce().getXR() - (mass * 9.8 * 0.33), getForce().getYR() - (mass * 9.8 * 0.33)));
		draw(window);
	}

	public void updateMomentum() {
		momentum.setXR(mass * velocity.getXR());
		momentum.setYR(mass * velocity.getYR());
		momentum.calculateVector();
	}

	public void updateForce() {
		force.setXR(mass * acceleration.getXR());
		force.setYR(mass * acceleration.getYR());
		force.calculateVector();
	}

	public void updateAcceleration() {
		acceleration.setXR(force.getXR() / mass);
		acceleration.setYR(force.getYR() / mass);
	}

	private int losHits = 0;

	public void runCollisions(List<PhysicsObject> objects, Graphics window) {
		for (PhysicsObject o : objects) {
			if (!o.equals(this) && !o.frozen) {
				boolean slow = inLineOfSight(o);
				if (slow && Simulation.debug) {
					losHits++;
				}
				if (!slow && Simulation.debug && losHits >= 1) {
					System.out.println("Line of Sight Ticks: " + losHits);
					losHits = 0;
				}
				if ((getCX() >= o.getX() && getX() <= o.getCX()) && (getCY() >= o.getY() && getY() <= o.getCY())) {
					if (Simulation.debug) {
						System.out.println("<< Collision Event >>");
					}
					updateMomentum();
					o.updateMomentum();
					double ma = getMass();
					double mb = o.getMass();
					double va = getVelocity().getXR();
					double vb = o.getVelocity().getXR();
					double vay = getVelocity().getYR();
					double vby = o.getVelocity().getYR();
					getVelocity().setXR(((ma - mb)/(ma+mb)) * va + ((2 * mb * vb)/(ma + mb)));
					o.getVelocity().setXR(((va * 2 * ma)/(ma+mb)) + (((ma-mb) * vb)/(ma + mb)));
					getVelocity().setYR(((ma - mb)/(ma+mb)) * vay + ((2 * mb * vby)/(ma + mb)));
					o.getVelocity().setYR(((vay * 2 * ma)/(ma+mb)) + (((ma-mb) * vby)/(ma + mb)));
					double deltaXP = getMomentum().getXR();
					double deltaYP = getMomentum().getYR();
					double deltaXPO = o.getMomentum().getXR();
					double deltaYPO = o.getMomentum().getYR();
					updateMomentum();
					o.updateMomentum();
					// TRYING TO GET FORCE TO WORK...
					/*deltaXP-=getMomentum().getXR();
					deltaYP-=getMomentum().getYR();
					deltaXPO-=getMomentum().getXR();
					deltaYPO-=getMomentum().getYR();

					if (getForce().getRate() != 0 || o.getForce().getRate() != 0) {
						// Time of collision is assumed to be 5ms due to it being the default tickrate.
						double xForce =deltaXP/0.005;
						double yForce =deltaYP/0.005;

						double finalXForce = getForce().getXR() + xForce;
						double finalYForce = getForce().getYR() + yForce;
						double finalXForceO = o.getForce().getXR() + xForce;
						double finalYForceO = o.getForce().getYR() + yForce;

						double prev = getForce().getXR();
						if (prev < 0) {
							prev = -1;
						} else {
							prev = 1;
						}
						double prevY = getForce().getYR();
						if (prevY < 0) {
							prevY = -1;
						} else {
							prevY = 1;
						}
						if (finalXForce < 0 && prev == 1) {
							finalXForce = 0;
						}
						if (finalXForce > 0 && prev == -1) {
							finalXForce = 0;
						}

						if (finalYForce < 0 && prevY == 1) {
							finalYForce = 0;
						}
						if (finalYForce > 0 && prevY == -1) {
							finalYForce = 0;
						}

						double prevO = o.getForce().getXR();
						if (prevO < 0) {
							prevO = -1;
						} else {
							prevO = 1;
						}
						double prevYO = o.getForce().getYR();
						if (prevYO < 0) {
							prevYO = -1;
						} else {
							prevYO = 1;
						}
						if (finalXForceO < 0 && prevO == 1) {
							finalXForceO = 0;
						}
						if (finalXForceO > 0 && prevO == -1) {
							finalXForceO = 0;
						}

						if (finalYForceO < 0 && prevYO == 1) {
							finalYForceO = 0;
						}
						if (finalYForceO > 0 && prevYO == -1) {
							finalYForceO = 0;
						}

						setForce(new Vector(finalXForce, finalYForce));
						o.setForce(new Vector(finalXForceO, finalYForceO));
					}*/
					int tries = 0;
					while ((getCX() >= o.getX() && getX() <= o.getCX()) && (getCY() >= o.getY() && getY() <= o.getCY()) && tries <= 20) {
						move(window);
						o.move(window);
						tries++;
					}
					if (tries > 20) {
						freeze();
					}
				}
			}
		}
	}

	public boolean inLineOfSight(PhysicsObject o) {
		double slope = getVelocity().getYR() / getVelocity().getXR();
		double oslope = o.getVelocity().getYR() / o.getVelocity().getXR();
		if (((slope * -1)/(10) + slope <= oslope || (slope * -1)/(10) + slope >= oslope) && ((slope * -1)/(10) - slope <= oslope || (slope * -1)/(10) - slope >= oslope)){
			if (slope < 0) {
				if (Math.sqrt(Math.pow(getX() - o.getCX(), 2) + Math.pow(getY() - o.getCY(), 2)) <= getWidth()) {
					//SimuInstance.setSlow(true);
					return true;
				}
			} else {
				if (Math.sqrt(Math.pow(getCX() - o.getX(), 2) + Math.pow(getCY() - o.getY(), 2)) <= getWidth()) {
					//SimuInstance.setSlow(true);
					return true;
				}
			}
		}
		//SimuInstance.setSlow(false);
		return false;
		/*List<Double> xpoints = new ArrayList<Double>();
		List<Double> ypoints = new ArrayList<Double>();
		double x = getX();
		double y = getY();
		for (double i = 0.5; i <= Math.abs(Simulation.WIDTH/slope); i+=0.1) {
			xpoints.add(getVelocity().getYR() * i + x);
			ypoints.add(getVelocity().getXR() * i + y);
		}
		int index = 0;
		for (Double d : xpoints) {
			if (d <= o.getX() )
		}*/
	}

	public void updateEdgeStatus(Graphics window) {
		if (frozen || mass == 0) {
			return;
		}
		if (getCX() >= Simulation.WIDTH || getX() <= 0) {
			force.setXR(0);
			getVelocity().setXR(-getVelocity().getXR());
		}
		if (getCY() >= Simulation.HEIGHT || getY() <= 0) {
			force.setYR(0);
			getVelocity().setYR(-getVelocity().getYR());
		}
		updateAcceleration();
		int tries = 0;
		while (tries <= 20 && (getCY() >= Simulation.HEIGHT || getY() <= 0) || (getCX() >= Simulation.WIDTH || getX() <= 0)) {
			move(window);
			tries++;

		}
		updateMomentum();
	}

	public void freeze() {
		frozen = true;
	}
	
}

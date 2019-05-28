import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author yaod5171
 */
public class Ball extends Shape /*implements Collideable*/ {

    private double x;
    private double y;
    private double radius;
    private double mass;
    private double charge;

    private Vector velocity;
    private Vector acceleration;
    private Vector momentum;
    private Vector force;
    private double energy;

    private Color color;

    protected int xPos, yPos;
    private int priority;
    private boolean frozen;

    /*public Ball(double x, double y) {
        this(x, y, 5, 25);
    }

    public Ball(double x, double y, int radius, int mass) {
        this(x, y, radius, mass, Color.RED);
    }

    public Ball(double x, double y, Color color) {
        this(x, y, 5, 25, color);
    }

    public Ball(double x, double y, int radius, int mass, Color color) {
        super(x, y, radius, radius);
        this.radius = radius;
        this.mass = mass;
        this.color = color;
        updatePos();
    }*/

    public Ball(double x, double y, double radius, Color c, double mass, Vector v, Vector a, double charge, int priority) {
        super(x, y, radius, radius);
        this.setX(x);
        this.setY(y);
        this.radius = radius;
        this.mass = mass;
        this.setCharge(charge);
        setVelocity(v);
        setAcceleration(a);
        setMomentum(new Vector(0, 0));
        setForce(new Vector(0, 0));
        this.setCharge(charge);
        this.priority = priority;
        frozen = false;
    }

    public void updatePos() {
        xPos = (int)x;
        yPos = (int)y;
    }

    /**
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @return the mass
     */
    public double getMass() {
        return mass;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * @param mass the mass to set
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * calculate and return the speed
     *
     * @return the speed
     */
    public double getSpeed() {
        return Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
    }

    /**
     * calculate and return the direction
     *
     * @return the direction in radians
     */
    public double getDir() {
        return Math.atan2(vy, vx);
    }

    /**
     * Set the speed/dir
     *
     * @param speed the speed to set
     * @param dir the dir to set
     */
    public void setSpeedDir(double speed, double dir) {
        vx = speed * Math.cos(dir);
        vy = speed * Math.sin(dir);
    }

    /**
     * Draw the ball to a window
     *
     * @param window the window to draw to
     */
    public void draw(Graphics window) {
        window.setColor(getColor());
        window.fillOval(xPos - getRadius(), yPos - getRadius(), 2 * getRadius(), 2 * getRadius());
        window.setColor(Color.BLACK);
        //window.drawLine(xPos, yPos, xPos + (int) (10 * getSpeed() * Math.cos(getDir())), yPos + (int) (10 * getSpeed() * Math.sin(getDir())));
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

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector acceleration) {
        this.acceleration = acceleration;
    }

    public Vector getMomentum() {
        return momentum;
    }

    public void setMomentum(Vector momentum) {
        this.momentum = momentum;
    }

    public Vector getForce() {
        return force;
    }

    public void setForce(Vector force) {
        this.force = force;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    /**
     * Bounce the ball off another ball if they've collided.
     *
     * @param obj the other ball to check
     */
    public void collideWithBall(Ball obj) {
        if (sqDist(obj.getX(), obj.getY()) <= Tools.square(getRadius() + obj.getRadius())) {
            //find the angle of collision
            double collisionAngle = Math.atan2(obj.getY() - this.getY(), obj.getX() - this.getX()) - Math.PI / 2;
            //calculate each ball's angle of incidence from the angle of collision
            double thisIncidence = this.getDir() - collisionAngle;
            double objIncidence = obj.getDir() - collisionAngle;
            //calculate each ball's velocity components
            double thisComponent = this.getSpeed() * Math.sin(thisIncidence);
            double objComponent = obj.getSpeed() * Math.sin(objIncidence);
            double thisParallel = this.getSpeed() * Math.cos(thisIncidence);
            double objParallel = obj.getSpeed() * Math.cos(objIncidence);
            //calculate the momentum of each ball along the collision
            double thisMomentum = thisComponent * this.getMass();
            double objMomentum = objComponent * obj.getMass();
            //completely switch momentums; this is a perfectly elastic collision.
            double temp = objMomentum;
            objMomentum = thisMomentum;
            thisMomentum = temp;
            //convert back to velocity
            thisComponent = thisMomentum / this.getMass();
            objComponent = objMomentum / obj.getMass();
            //re-calculate the speed and direction
            double thisSpeed = Math.sqrt(Tools.square(thisComponent) + Tools.square(thisParallel));
            double thisDir = Math.atan2(thisComponent, thisParallel) + collisionAngle;
            double objSpeed = Math.sqrt(Tools.square(objComponent) + Tools.square(objParallel));
            double objDir = Math.atan2(objComponent, objParallel) + collisionAngle;
            //finally, reassign the speed of each.
            this.setSpeedDir(thisSpeed, thisDir);
            obj.setSpeedDir(objSpeed, objDir);
//            //oh, and move them out of the way so they don't get stuck to each other.
//            double[] collisionPoint = {(this.getX()*this.radius+obj.getX()*obj.getRadius())/(radius+obj.getRadius()),
//                (this.getY()*this.radius+obj.getY()*obj.getRadius())/(radius+obj.getRadius())};
//            obj.setX(-Math.cos(collisionAngle)*obj.getRadius() + collisionPoint[0]);
//            obj.setY(-Math.sin(collisionAngle)*obj.getRadius() + collisionPoint[1]);
//            this.setX(Math.cos(collisionAngle)*this.getRadius() + collisionPoint[0]);
//            this.setX(Math.sin(collisionAngle)*this.getRadius() + collisionPoint[1]);
//
//            this.move();
//            obj.move();
        }
    }

    /**
     * Bounce the ball off a wall if they've collided.
     *
     * @param obj the wall to check
     */
    public void collideWithWall(Wall obj) {
        double wx = obj.getX();
        double wy = obj.getY();
        int wid = obj.getWidth();
        int ht = obj.getHeight();

        int bounceMode = 0;
        double[] bouncePoint = new double[2];

        //is a collision possible? if not, don't run the other tests.
        if (wx < getX() + getRadius() && getX() - getRadius() < wx + wid
                && wy < getY() + getRadius() && getY() - getRadius() < wy + ht) {

            //left and right sides
            if (wy < getY() && getY() < wy + ht) {
                if (wx < getX() + getRadius() && getX() - getRadius() < wx + wid) {
                    bounceMode = 1;
                }
            }
            //top and bottom sides
            if (wx < getX() && getX() < wx + wid) {
                if (wy < getY() + getRadius() && getY() - getRadius() < wy + ht) {
                    bounceMode = 2;
                }
            }
            //corners
            double[][] points = {{wx, wy}, {wx + wid, wy}, {wx, wy + ht}, {wx + wid, wy + ht}};
            for (double[] point : points) {
                if (sqDist(point[0], point[1]) < Tools.square(getRadius())) {
                    bounceMode = 3;
                    bouncePoint = point;
                }
            }

            //if a bounce has occured:
            if (bounceMode == 0) {
                //do nothing; no collision has occured.
            } else if (bounceMode == 1) {
                setVX(-getVX());
                if (getX() < wx) {
                    setX(wx - radius);
                } else {
                    setX(wx + wid + radius);
                }
            } else if (bounceMode == 2) {
                setVY(-getVY());
                if (getY() < wy) {
                    setY(wy - radius);
                } else {
                    setY(wy + ht + radius);
                }
            } else if (bounceMode == 3) {
                setVX(-getVX());
                setVY(-getVY());
            }
        }
    }
}

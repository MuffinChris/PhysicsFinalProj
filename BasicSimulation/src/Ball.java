package BallPit;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author yaod5171
 */
public class Ball extends MovingObject /*implements Collideable*/ {

    private int size;
    private int weight;
    private Color color;

    public Ball(double x, double y) {
        this(x, y, 5, 25, colorByDensity(5, 25));
    }

    public Ball(double x, double y, int size, int weight) {
        this(x, y, size, weight, colorByDensity(size, weight));
    }

    public Ball(double x, double y, Color color) {
        this(x, y, 5, 25, color);
    }

    public Ball(double x, double y, int size, int weight, Color color) {
        super(x, y);
        this.size = size;
        this.weight = weight;
        this.color = color;
        updatePos();
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Calculates a color based on the density
     *
     * @param size the radius of the ball
     * @param weight the weight of the ball
     * @return a color scaled to the density of the ball
     */
    static Color colorByDensity(int size, int weight) {
        double densityTop = 5;
        double density = (double) weight / Tools.square(size);
        float hue;
        if (density < densityTop) {
            hue = (float) (density / densityTop);
            hue = 1 - hue;/*
             hue += 0.2;
             hue = (float)Math.min(hue, 0.8);*/

        } else {
            hue = 0;
        }

        return Color.getHSBColor(hue, 1, 1);
    }

    /**
     * Check if a point has collided with the ball
     *
     * @param x x of the point
     * @param y y of the point
     * @return true if point is inside or on ball
     */
    public boolean pointCollide(int x, int y) {
        return (Tools.square(x - this.xPos) + Tools.square(y - this.yPos) <= Tools.square(getSize()));
    }

    /**
     * Bounce the ball off another ball if they've collided.
     *
     * @param obj the other ball to check
     */
    public void collideWithBall(Ball obj) {
        if (sqDist(obj.getX(), obj.getY()) <= Tools.square(getSize() + obj.getSize())) {
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
            double thisMomentum = thisComponent * this.getWeight();
            double objMomentum = objComponent * obj.getWeight();
            //completely switch momentums; this is a perfectly elastic collision.
            double temp = objMomentum;
            objMomentum = thisMomentum;
            thisMomentum = temp;
            //convert back to velocity
            thisComponent = thisMomentum / this.getWeight();
            objComponent = objMomentum / obj.getWeight();
            //re-calculate the speed and direction
            double thisSpeed = Math.sqrt(Tools.square(thisComponent) + Tools.square(thisParallel));
            double thisDir = Math.atan2(thisComponent, thisParallel) + collisionAngle;
            double objSpeed = Math.sqrt(Tools.square(objComponent) + Tools.square(objParallel));
            double objDir = Math.atan2(objComponent, objParallel) + collisionAngle;
            //finally, reassign the speed of each.
            this.setSpeedDir(thisSpeed, thisDir);
            obj.setSpeedDir(objSpeed, objDir);
//            //oh, and move them out of the way so they don't get stuck to each other.
//            double[] collisionPoint = {(this.getX()*this.size+obj.getX()*obj.getSize())/(size+obj.getSize()),
//                (this.getY()*this.size+obj.getY()*obj.getSize())/(size+obj.getSize())};
//            obj.setX(-Math.cos(collisionAngle)*obj.getSize() + collisionPoint[0]);
//            obj.setY(-Math.sin(collisionAngle)*obj.getSize() + collisionPoint[1]);
//            this.setX(Math.cos(collisionAngle)*this.getSize() + collisionPoint[0]);
//            this.setX(Math.sin(collisionAngle)*this.getSize() + collisionPoint[1]);
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
        if (wx < getX() + getSize() && getX() - getSize() < wx + wid
                && wy < getY() + getSize() && getY() - getSize() < wy + ht) {

            //left and right sides
            if (wy < getY() && getY() < wy + ht) {
                if (wx < getX() + getSize() && getX() - getSize() < wx + wid) {
                    bounceMode = 1;
                }
            }
            //top and bottom sides
            if (wx < getX() && getX() < wx + wid) {
                if (wy < getY() + getSize() && getY() - getSize() < wy + ht) {
                    bounceMode = 2;
                }
            }
            //corners
            double[][] points = {{wx, wy}, {wx + wid, wy}, {wx, wy + ht}, {wx + wid, wy + ht}};
            for (double[] point : points) {
                if (sqDist(point[0], point[1]) < Tools.square(getSize())) {
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
                    setX(wx - size);
                } else {
                    setX(wx + wid + size);
                }
            } else if (bounceMode == 2) {
                setVY(-getVY());
                if (getY() < wy) {
                    setY(wy - size);
                } else {
                    setY(wy + ht + size);
                }
            } else if (bounceMode == 3) {
                setVX(-getVX());
                setVY(-getVY());
            }
        }
    }

    /**
     * Bounce off a wall.
     *
     * @param obj the wall to bounce off of
     */
    public void bounceOffWall(Wall obj) {
        //corner bounce or side bounce?

    }

    /**
     * Draw the ball to a window
     *
     * @param window the window to draw to
     */
    public void draw(Graphics window) {
        window.setColor(getColor());
        window.fillOval(xPos - getSize(), yPos - getSize(), 2 * getSize(), 2 * getSize());
        window.setColor(Color.BLACK);
        //window.drawLine(xPos, yPos, xPos + (int) (10 * getSpeed() * Math.cos(getDir())), yPos + (int) (10 * getSpeed() * Math.sin(getDir())));
    }

}

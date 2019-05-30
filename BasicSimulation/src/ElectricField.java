import java.awt.*;

public class ElectricField extends PhysicsObject {

    private String direction;
    private double magnitude;

    public ElectricField(int x, int y, int w, int h, Color c, double m, Vector v, Vector a, double charge, int priority, String direction, double magnitude) {
        super(x,y,w,h,c,m,v,a,charge,priority);
        this.direction = direction;
        this.magnitude = magnitude;
    }

    public void setMagnitude(double m) {
        magnitude = m;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setDirection(String s) {
        direction = s;
    }

    public String getDirection() {
        return direction;
    }


    public void draw(Graphics window) {
        super.draw(window);
        window.setColor(new Color(255, 210, 25));
        if (direction.equals("NORTH")) {
            window.drawLine(getX(), getY(), getX(), 0);
            window.drawLine(getX() + (getWidth() / 2), getY(), getX() + (getWidth() / 2), 0);
            window.drawLine(getCX(), getY(), getCX(), 0);
        }
        if (direction.equals("WEST")) {
            window.drawLine(getX(), getY(), 0, getY());
            window.drawLine(getX(), getY() + getHeight()/2, 0, getY() + getHeight()/2);
            window.drawLine(getX(), getCY(), 0, getCY());
        }
        if (direction.equals("EAST")) {
            window.drawLine(getX(), getY(), Simulation.WIDTH, getY());
            window.drawLine(getX(), getY() + getHeight()/2, Simulation.WIDTH, getY() + getHeight()/2);
            window.drawLine(getX(), getCY(), Simulation.WIDTH, getCY());
        }
        if (direction.equals("SOUTH")) {
            window.drawLine(getX(), getY(), getX(), Simulation.HEIGHT);
            window.drawLine(getX() + (getWidth() / 2), getY(), getX() + (getWidth() / 2), Simulation.HEIGHT);
            window.drawLine(getCX(), getY(), getCX(), Simulation.HEIGHT);
        }
    }


}

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



}

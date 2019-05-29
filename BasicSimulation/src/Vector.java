public class Vector {

    private double angle;
    private double rate;
    private double xr;
    private double yr;

    public Vector(double x, double y) {
        xr = x;
        yr = y;
        calculateVector();
    }

    public double getAngle() {
        return angle;
    }

    public double getRate() {
        return rate;
    }

    public double getXR() {
        return xr;
    }

    public double getYR() {
        return yr;
    }

    public void setAngle(double a) {
        angle = a;
    }

    public void setRate(double r) {
        rate = r;
    }

    public void setXR(double x) {
        xr = x;
        calculateVector();
    }

    public void setYR(double y) {
        yr = y;
        calculateVector();
    }

    public void calculateVector() {
        double hypot = Math.sqrt(Math.pow(xr, 2) + Math.pow(yr, 2));
        double ang = Math.atan(yr/xr);
        setAngle(ang);
        setRate(hypot);
    }

}

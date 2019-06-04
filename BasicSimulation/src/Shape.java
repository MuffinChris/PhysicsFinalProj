public class Shape {

    int cx;
    int cy;
    int w;
    int h;

    public Shape(int x, int y, int w, int h) {
        cx = x + w;
        cy = y + h;
        this.w = w;
        this.h = h;
    }

    public void updateShape(int x, int y, int w, int h) {
        cx = x + w;
        cy = y + h;
        this.w = w;
        this.h = h;
    }

    public int getCX() {
        return cx;
    }

    public int getCY() {
        return cy;
    }

    public void setCX(int x) {
        cx = x + w;
    }

    public void setCY(int y) {
        cy = y + h;
    }

}

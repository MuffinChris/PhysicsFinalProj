import java.awt.*;
import java.util.List;

public class Hole extends Shape {

    private int x;
    private int y;
    private int width;
    private int height;

    public Hole(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.x = x;
        this.y = y;
        this.width= width;
        this.height= height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int draw(Graphics window, List<PhysicsObject> objects) {
        window.setColor(Color.GREEN);
        window.fillOval(x, y, width, height);
        for (PhysicsObject o : objects) {
            if (Math.sqrt(Math.pow((x + 25) - o.getX(), 2) + Math.pow((y + 25) - o.getY(), 2)) <= 50) {
                o.setX(100);
                o.setY(100);
                setX((int) (Math.random() * 1000 + 100));
                setY((int) (Math.random() * 700 + 100));
                return 1;
            }
        }
        return 0;
    }

}

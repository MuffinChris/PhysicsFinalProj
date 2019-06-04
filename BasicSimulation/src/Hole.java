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

    public int draw(Graphics window, List<PhysicsObject> objects, List<ElectricField> fields) {
        window.setColor(Color.GREEN);
        window.fillOval(x, y, width, height);
        for (PhysicsObject o : objects) {
            if (Math.sqrt(Math.pow((x + 25) - (o.getX() + o.getWidth()/2), 2) + Math.pow((y + 25) - (o.getY() + o.getHeight()/2), 2)) <= 60) {
                o.setX((int) (Math.random() * 1000 + 100));
                o.setY((int) (Math.random() * 700 + 100));
                setX((int) (Math.random() * 1000 + 100));
                setY((int) (Math.random() * 700 + 100));
                for (ElectricField e : fields) {
                    System.out.println(e.getX() + " " + e.getY() + " " + e.getWidth() + " " + e.getHeight() + " " + e.getDirection());
                    e.setX((int) (Math.random() * 1000 + 100));
                    e.setY((int) (Math.random() * 700 + 100));
                    e.setWidth((int) (Math.random() * 200) + 100);
                    e.setHeight((int) (Math.random() * 200) + 100);

                    double rand = Math.random();
                    if (rand <=0.24) {
                        e.setDirection("NORTH");
                    }
                    if (rand > 0.24 && rand < 0.5) {
                        e.setDirection("WEST");
                    }
                    if (rand >= 0.5 && rand < 0.75) {
                        e.setDirection("EAST");
                    }
                    if (rand >= 0.75 ) {
                        e.setDirection("SOUTH");
                    }
                    System.out.println(e.getX() + " " + e.getY() + " " + e.getWidth() + " " + e.getHeight() + " " + e.getDirection());

                }
                return 1;
            }
        }
        return 0;
    }

}

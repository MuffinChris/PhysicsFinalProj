import java.awt.*;

public class ChargeLocation {

    private int x;
    private int y;
    private Color color;

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Color getColor() {
        return color;
    }

    public ChargeLocation(int x, int y, Color c) {
        this.x = x;
        this.y = y;
        color = c;
    }

    public void draw(Graphics window) {
        window.setColor(color);
        window.fillRect(x, y, 100, 100);
    }

}

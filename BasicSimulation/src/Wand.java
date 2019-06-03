import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

public class Wand implements MouseListener, MouseMotionListener {

    private int x;
    private int y;
    private int mx;
    private int my;
    private double charge;
    private Image image;
    private Image imc;
    private boolean chargeclick;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }



    public Wand() {
        x = 200;
        y = 200;
        mx = x;
        my = y;
        charge = 0.0;
        chargeclick = false;
        try {
            image = ImageIO.read(new File("BasicSimulation/src/WandUncharged.png"));
            imc = ImageIO.read(new File("BasicSimulation/src/ChargeClick.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateImage() {
        if (charge == 0) {
            try {
                image = ImageIO.read(new File("BasicSimulation/src/WandUncharged.png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (charge < 0) {
            try {
                image = ImageIO.read(new File("BasicSimulation/src/WandNegative.png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                image = ImageIO.read(new File("BasicSimulation/src/WandPositive.png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void draw(Graphics window, ChargeLocation c1, ChargeLocation c2) {
        window.drawImage(image, x, y, 50, 175, null);
        window.setColor(Color.BLACK);
        updateImage();
        DecimalFormat df = new DecimalFormat("#.##");
        window.drawString(df.format(charge) + "", x, y);
        if (mx >= c1.getX() && mx <= c1.getX() + 100 && my >= c1.getY() && my <= c1.getY() + 100) {
            charge+=0.01;
        }
        if (mx >= c2.getX() && mx <= c2.getX() + 100 && my >= c2.getY() && my <= c2.getY() + 100) {
            charge-=0.01;
        }
    }

    public void drawCharged(Graphics window, List<PhysicsObject> objects) {
        if (chargeclick) {
            window.drawImage(imc, x - 80, y - 80, 200, 200, null);
            for (PhysicsObject o : objects) {
                if (Math.sqrt(Math.pow((x + 120) - o.getX(), 2) + Math.pow((y + 120) - o.getY(), 2)) <= 200) {
                    if (Simulation.REALISTICELECTRIC) {
                        double totalcharge = o.getCharge() + charge;
                        o.setCharge(totalcharge/2);
                        charge = totalcharge/2;
                    } else {
                        if (charge < 0) {
                            o.setCharge(o.getCharge() + charge);
                            charge = 0;
                        } else if (charge > 0) {
                            o.setCharge(o.getCharge() + charge);
                            charge = 0;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        chargeclick = true;

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        chargeclick = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getXOnScreen();
        y = e.getYOnScreen();
        x-=25;
        y-=60;
        mx = e.getXOnScreen();
        my = e.getYOnScreen();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getXOnScreen();
        y = e.getYOnScreen();
        x-=25;
        y-=60;
        mx = e.getXOnScreen();
        my = e.getYOnScreen();
    }
}

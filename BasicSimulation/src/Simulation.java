import javax.swing.JFrame;
import java.awt.Component;

public class Simulation extends JFrame
{
  public static final int WIDTH = 1600;
  public static final int HEIGHT = 900;
  public static boolean debug = true;
  // Charge conduction (balancing) vs adding charge.
  public static boolean REALISTICELECTRIC = false;

  public Simulation()
  {
    super("ELECTRIC FIELD GAME!!! (INNOVATIVE TITLE)");
    setSize(WIDTH,HEIGHT);

    SimuInstance theGame = new SimuInstance();
    ((Component)theGame).setFocusable(true);

    getContentPane().add(theGame);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  public static void main( String args[] )
  {
    Simulation run = new Simulation();
  }
}
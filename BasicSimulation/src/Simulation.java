import javax.swing.JFrame;
import java.awt.Component;

public class Simulation extends JFrame
{
  public static final int WIDTH = 1200;
  public static final int HEIGHT = 600;
  public static boolean debug = true;

  public Simulation()
  {
    super("MOMENTUM SIMULATION");
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
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SimuInstance extends Canvas implements Runnable
{
	private BufferedImage back;
	private PhysicsObject ball;
	private PhysicsObject otherball;
	private PriorityList objects;
	private List<ElectricField> fields;
	private static boolean slow;
	private ElectricField testfield;
	private ElectricField testfield2;

  public SimuInstance()
  {
	  ball = new PhysicsObject(150, 380, 50, 50, Color.BLUE, 100, new Vector(1, 1), new Vector(0, 0), 1, 2);
	  ball.setColor(Color.BLUE);
	  otherball = new PhysicsObject(200, 480, 50, 50, Color.RED, 100, new Vector(-1, 1), new Vector(0, 0), 1, 1);
	  otherball.setColor(Color.RED);

	  testfield = new ElectricField(400, 400, 200, 50, Color.YELLOW, 0, new Vector(0, 0), new Vector(0, 0), 0, 10, "NORTH", 1);
	  testfield2 = new ElectricField(400, 100, 50, 200, Color.YELLOW, 0, new Vector(0, 0), new Vector(0, 0), 0, 11, "EAST", 1);


	  objects = new PriorityList();
	  objects.getList().add(ball);
	  objects.getList().add(otherball);
	  objects.sortByPrio();

	  fields = new ArrayList<ElectricField>();
	  fields.add(testfield);
	  fields.add(testfield2);

	  slow = false;
	  new Thread(this).start();
	  setVisible(true);
  }

  public void update(Graphics window)
  {
	  paint(window);
  }

  public void paint( Graphics window )
  {
    Graphics2D twoDGraph = (Graphics2D)window;
    if(back==null)
      back = (BufferedImage)(createImage(getWidth(),getHeight()));
    Graphics graphToBack = back.createGraphics();

    graphToBack.setColor(Color.WHITE);
    graphToBack.fillRect(0,0,Simulation.WIDTH,Simulation.HEIGHT);

    for (PhysicsObject o : objects.getList()) {
        o.runCollisions(objects.getList(), graphToBack);
    }

    for (PhysicsObject o : objects.getList()) {
        o.updateEdgeStatus(graphToBack);
    }

    for (PhysicsObject o : objects.getList()) {
    	o.move(graphToBack);
    }
    
	for (ElectricField e : fields) {
		e.draw(graphToBack);
	}

	for (PhysicsObject o : objects.getList()) {
		boolean moving = false;
		for (ElectricField e : fields) {
			if (o.getCharge() != 0) {
				if (e.getDirection().equals("NORTH")) {
					if (o.getX() <= e.getCX() && o.getCX() >= e.getX() && o.getCY() <= e.getY()) {
						o.setForce(new Vector(o.getForce().getXR(), o.getForce().getYR() - e.getMagnitude() * o.getCharge()));
						moving = true;
					}
				}
				if (e.getDirection().equals("SOUTH")) {
					if (o.getX() <= e.getCX() && o.getCX() >= e.getX() && o.getY() >= e.getCY()) {
						o.setForce(new Vector(o.getForce().getXR(), o.getForce().getYR() + e.getMagnitude() * o.getCharge()));
						moving = true;
					}
				}
				if (e.getDirection().equals("EAST")) {
					if (o.getY() <= e.getCY() && o.getCY() >= e.getY() && o.getX() >= e.getCX()) {
						o.setForce(new Vector(o.getForce().getXR() + e.getMagnitude() * o.getCharge(), o.getForce().getYR()));
						moving = true;
					}
				}
				if (e.getDirection().equals("WEST")) {
					if (o.getY() <= e.getCY() && o.getCY() >= e.getY() && o.getCX() <= e.getX()) {
						o.setForce(new Vector(o.getForce().getXR() - e.getMagnitude() * o.getCharge(), o.getForce().getYR()));
						moving = true;
					}
				}
				if (!moving) {
					o.setForce(new Vector(0,0));
				}
				o.updateAcceleration();
				// fix this! Does not update accel when force is 0.
			}
		}
	}
    
    twoDGraph.drawImage(back, null, 0, 0);
  }

  public static void setSlow(boolean s) {
  	slow = s;
  }

  @SuppressWarnings("static-access")
	public void run()
	  {
	    try
	    {
	      while(true)
	      {
	      	int mil = 10;
	        Thread.currentThread().sleep(mil);
	        repaint();
	      }
	    }catch(Exception e)
	    {
	    }
	  }
}
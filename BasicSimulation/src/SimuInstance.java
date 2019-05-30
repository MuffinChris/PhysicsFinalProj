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
	private Wand wand;
	private ChargeLocation positive;
	private ChargeLocation negative;
	private Hole hole;
	private int score;

  public SimuInstance()
  {
  	  score = 0;
	  ball = new PhysicsObject(150, 380, 50, 50, Color.BLUE, 100, new Vector(1, 1), new Vector(0, 0), 1, 50);
	  ball.setColor(Color.BLUE);
	  otherball = new PhysicsObject(200, 480, 50, 50, Color.RED, 100, new Vector(-1, 1), new Vector(0, 0), 1, 49);
	  otherball.setColor(Color.RED);
	  testfield = new ElectricField(600, 600, 200, 50, Color.YELLOW, 0, new Vector(0, 0), new Vector(0, 0), 0, 10, "NORTH", 1);
	  testfield2 = new ElectricField(200, 100, 50, 300, Color.YELLOW, 0, new Vector(0, 0), new Vector(0, 0), 0, 11, "EAST", 1);

	  hole = new Hole(1100, 600, 50, 50);

	  objects = new PriorityList();
	  objects.getList().add(ball);
	  objects.getList().add(otherball);
	  objects.sortByPrio();

	  fields = new ArrayList<ElectricField>();
	  fields.add(testfield);
	  fields.add(testfield2);

	  positive = new ChargeLocation(500, 800, new Color(255, 107, 111));
	  negative = new ChargeLocation(400, 800, new Color(48, 180, 175));

	  wand = new Wand();
	  addMouseListener(wand);
	  addMouseMotionListener(wand);

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

    positive.draw(graphToBack);
    negative.draw(graphToBack);

    for (ElectricField e : fields) {
    	e.draw(graphToBack);
    }

    for (PhysicsObject o : objects.getList()) {
        o.runCollisions(objects.getList(), graphToBack);
    }

    for (PhysicsObject o : objects.getList()) {
        o.updateEdgeStatus(graphToBack);
    }

    for (PhysicsObject o : objects.getList()) {
    	o.move(graphToBack);
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
			}
		}
	}

	score+=hole.draw(graphToBack, objects.getList());

	wand.draw(graphToBack, positive, negative);
	wand.drawCharged(graphToBack, objects.getList());

	graphToBack.setColor(Color.BLACK);
	graphToBack.drawString("Score: " + score, hole.getX(), hole.getY());

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
	      	int mil = 30;
	        Thread.currentThread().sleep(mil);
	        repaint();
	      }
	    }catch(Exception e)
	    {
	    }
	  }
}
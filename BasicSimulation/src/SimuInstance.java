import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
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
	private ElectricField testfield3;
	private ElectricField testfield4;
	private ElectricField testfield5;
	private Wand wand;
	private ChargeLocation positive;
	private ChargeLocation negative;
	private Hole hole;
	private int score;
	private File scoref;
	private int timer;
	private int mil;
	private boolean status;

  public SimuInstance()
  {
  	status = true;
  	mil = 30;
  	timer = 33 * 120;
  	  scoref = new File("BasicSimulation/src/Score.txt");
  	  if (!scoref.exists()) {
  	  	try {
			scoref.createNewFile();
		} catch (Exception e) {
  	  		e.printStackTrace();
		}
	  }
  	  score = 0;
	  ball = new PhysicsObject(150, 380, 50, 50, Color.BLUE, 100, new Vector(1, 1), new Vector(0, 0), 1, 50);
	  ball.setColor(Color.BLUE);
	  otherball = new PhysicsObject(200, 480, 50, 50, Color.RED, 100, new Vector(-1, 1), new Vector(0, 0), 1, 49);
	  otherball.setColor(Color.RED);
	  testfield = new ElectricField(600, 600, 200, 50, Color.YELLOW, 0, new Vector(0, 0), new Vector(0, 0), 0, 10, "NORTH", 1);
	  testfield2 = new ElectricField(200, 100, 50, 300, Color.YELLOW, 0, new Vector(0, 0), new Vector(0, 0), 0, 11, "EAST", 1);
	  testfield3 = new ElectricField(50, 450, 50, 100, Color.YELLOW, 0, new Vector(0, 0), new Vector(0, 0), 0, 11, "EAST", 1);
	  testfield4 = new ElectricField(400, 600, 50, 100, Color.YELLOW, 0, new Vector(0, 0), new Vector(0, 0), 0, 11, "WEST", 1);
	  testfield5 = new ElectricField(300, 300, 100, 50, Color.YELLOW, 0, new Vector(0, 0), new Vector(0, 0), 0, 11, "SOUTH", 1);

	  hole = new Hole(1100, 600, 30, 30);

	  objects = new PriorityList();
	  objects.getList().add(ball);
	  objects.getList().add(otherball);
	  objects.sortByPrio();

	  fields = new ArrayList<ElectricField>();
	  fields.add(testfield);
	  fields.add(testfield2);
	  fields.add(testfield3);
	  fields.add(testfield4);
	  fields.add(testfield5);


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
		Vector force = new Vector(0, 0);
		for (ElectricField e : fields) {
			if (o.getCharge() != 0) {
				boolean north = false;
				boolean south = false;
				boolean west = false;
				boolean east = false;
				if (e.getDirection().equals("NORTH")) {
					if (o.getX() <= e.getCX() && o.getCX() >= e.getX() && o.getCY() <= e.getY()) {
						force.setXR(force.getXR());
						force.setYR(force.getYR() - e.getMagnitude() * o.getCharge());
						//o.setForce(new Vector(o.getForce().getXR(), o.getForce().getYR() - e.getMagnitude() * o.getCharge()));
						moving = true;
						north = true;
					}
				}
				if (e.getDirection().equals("SOUTH")) {
					if (o.getX() <= e.getCX() && o.getCX() >= e.getX() && o.getY() >= e.getCY()) {
						force.setXR(force.getXR());
						force.setYR(force.getYR() + e.getMagnitude() * o.getCharge());
						//System.out.println(force.getYR());
						//o.setForce(new Vector(o.getForce().getXR(), o.getForce().getYR() + e.getMagnitude() * o.getCharge()));
						moving = true;
						south = true;
					}
				}
				if (e.getDirection().equals("EAST")) {
					if (o.getY() <= e.getCY() && o.getCY() >= e.getY() && o.getX() >= e.getCX()) {
						force.setXR(force.getXR() + e.getMagnitude() * o.getCharge());
						force.setYR(force.getYR());
						//o.setForce(new Vector(o.getForce().getXR() + e.getMagnitude() * o.getCharge(), o.getForce().getYR()));
						moving = true;
						east = true;
					}
				}
				if (e.getDirection().equals("WEST")) {
					if (o.getY() <= e.getCY() && o.getCY() >= e.getY() && o.getCX() <= e.getX()) {
						force.setXR(force.getXR() - e.getMagnitude() * o.getCharge());
						force.setYR(force.getYR());
						//o.setForce(new Vector(o.getForce().getXR() - e.getMagnitude() * o.getCharge(), o.getForce().getYR()));
						moving = true;
						west = true;
					}
				}
			}
		}
		if (moving) {
			o.setForce(force);
		}
		if (!moving) {
			o.setForce(new Vector(0,0));
		}
		o.updateAcceleration();
		if (!status) {
			graphToBack.setColor(Color.RED);
			graphToBack.drawString("GAME OVER!", 500, 500);
		}
	}

	int scorebefore = score;
	score+=hole.draw(graphToBack, objects.getList(), fields);
	if (score > scorebefore) {
		timer+=33 * 20;
	}
	int highscore = 0;
	boolean cont = true;
	try {

	  	FileReader reader = new FileReader(scoref);
	  	BufferedReader br = new BufferedReader(reader);
	  	String sc = br.readLine();
	  	if (sc == null) {
	  		cont = false;
			FileWriter writer = new FileWriter(scoref);
			BufferedWriter bw = new BufferedWriter(writer);
			if (sc == null || sc == "") {
				bw.write("0");
				bw.flush();
				bw.close();
			}
		}
	  	if (cont) {
			if (Integer.valueOf(sc) instanceof Integer) {
				highscore = Integer.valueOf(sc);

				//System.out.println(highscore);
			}
			if (highscore < score) {
				br.close();
				scoref.delete();
				scoref.createNewFile();
				scoref = new File("BasicSimulation/src/Score.txt");
				reader = new FileReader(scoref);
				br = new BufferedReader(reader);
				FileWriter writer = new FileWriter(scoref);
				BufferedWriter bw = new BufferedWriter(writer);
				bw.write(String.valueOf(score));
				bw.flush();
				bw.close();
			}
			br.close();
		} else {
	  		br.close();
		}
	  } catch (Exception e) {
		  e.printStackTrace();
	  }

	wand.draw(graphToBack, positive, negative);
	wand.drawCharged(graphToBack, objects.getList());

	graphToBack.setColor(Color.BLACK);
	graphToBack.drawString("HighScore: " + highscore, hole.getX(), hole.getY()-15);
	graphToBack.drawString("Score: " + score, hole.getX(), hole.getY());
	graphToBack.drawString("TIME LEFT: " + (timer / 33), hole.getX(), hole.getY()-30);

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
	      while(status)
	      {
	      	timer--;
	      	if (timer<=0) {
	      		status = false;
			}
	      	int mil = 30;
	        Thread.currentThread().sleep(mil);
	        repaint();
	      }
	    }catch(Exception e)
	    {
	    }
	  }
}
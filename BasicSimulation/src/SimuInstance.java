import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.image.BufferedImage;

public class SimuInstance extends Canvas implements Runnable
{
	private BufferedImage back;
	private Ball ball;
	private Ball otherball;
  public SimuInstance()
  {
	  ball = new Ball(100, 380, 50, 50, 100, 1, 1, 0, 0);
	  ball.setColor(Color.BLUE);
	  otherball = new Ball(500, 400, 25, 25, 100, 2,1,0,0);
	  otherball.setColor(Color.RED);
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
    
    ball.move(graphToBack);
    otherball.move(graphToBack);
    
    int bx = ball.getX();
    int by = ball.getY();
    int bcx = bx + ball.getWidth();
    int bcy = by + ball.getHeight();
    int ox = otherball.getX();
    int oy = otherball.getY();
    int ocx = ox + otherball.getWidth();
    int ocy = oy + otherball.getHeight();
    
    if (bcx >= Simulation.WIDTH || bx <= 0) {
    	ball.setXVelocity(-ball.getXVelocity());
        ball.move(window);
        ball.move(window);
        ball.move(window);
    	ball.updateMomentum();
    }

    if (ocx >= Simulation.WIDTH || ox <= 0) {
    	otherball.setXVelocity(-otherball.getXVelocity());
        otherball.move(window);
        otherball.move(window);
        otherball.move(window);
    	otherball.updateMomentum();
    }

    if (bcy >= Simulation.HEIGHT || by <= 0) {
      ball.setYVelocity(-ball.getYVelocity());
      ball.move(window);
      ball.move(window);
      ball.move(window);
      ball.updateMomentum();
    }

    if (ocy >= Simulation.HEIGHT || oy <= 0) {
      otherball.setYVelocity(-otherball.getYVelocity());
      otherball.move(window);
      otherball.move(window);
      otherball.move(window);
      otherball.updateMomentum();
    }
    
    if (ocx > bx && ox < bcx && ((ocy >= by && (oy <= bcy)))) {
    	ball.setXVelocity((otherball.getMomentum() / ball.getMass()));
    	otherball.setXVelocity((ball.getMomentum() / otherball.getMass()));
        ball.setYVelocity((otherball.getMomentum() / ball.getMass()));
        otherball.setYVelocity((ball.getMomentum() / otherball.getMass()));
    	ball.updateMomentum();
    	otherball.updateMomentum();
    }
    
    twoDGraph.drawImage(back, null, 0, 0);
  }
  @SuppressWarnings("static-access")
	public void run()
	  {
	    try
	    {
	      while(true)
	      {
	        Thread.currentThread().sleep(5);
	        repaint();
	      }
	    }catch(Exception e)
	    {
	    }
	  }
}
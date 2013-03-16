import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;


@SuppressWarnings("serial")
public class MainCanvas extends Canvas implements Runnable
	{
	public static final int PWIDTH = 400;
	public static final int PHEIGHT = 400;
	
	public static MainCanvas instance = null;
	
	public Thread animator;
	public boolean running = false;
	public boolean gameOver = false; 

    public Graphics2D dbg = null;
    public BufferStrategy strategy = null;

	int FPS,SFPS;
	int fpscount;

	boolean LEFT, RIGHT, UP, DOWN;

	int mouseX, mouseY;

	public MainCanvas()
	{
		instance = this;
		
		setBackground(Color.white);
		setPreferredSize( new Dimension(PWIDTH, PHEIGHT));
		setFocusable(true);
		requestFocus();
	}	

	public void addNotify()
	{
		super.addNotify();
		startGame();
	}

	public void startGame()
	{
		if (animator == null || !running) {
			animator = new Thread(this);
			animator.start();
		}
	}

	public void stopGame()
	{
		running = false;
	}


	public void run()
	{
		running = true;
		
		long DifTime,TempoAnterior;
		int segundo = 0;
		
		DifTime = 0;
		TempoAnterior = System.currentTimeMillis();
		
		createBufferStrategy(2);

		strategy = getBufferStrategy();
			
		while(running) {
		
    		gameUpdate(DifTime);
			dbg = (Graphics2D) strategy.getDrawGraphics();
			dbg.setClip(0, 0, PWIDTH, PHEIGHT);
			dbg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gameRender();
			dbg.dispose();
			strategy.show();
		
			try {
				Thread.sleep(0);
			}	
			catch(InterruptedException ex){}
			
			DifTime = System.currentTimeMillis() - TempoAnterior;
			TempoAnterior = System.currentTimeMillis();
			
			if(segundo!=((int)(TempoAnterior/1000))){
				FPS = SFPS;
				SFPS = 1;
				segundo = ((int)(TempoAnterior/1000));
			}else{
				SFPS++;
			}
		
		}
		System.exit(0);
	}

	int timerfps = 0;
	
	int pingtimer = 0;
	int timerDownload = 0;

	private void gameUpdate(long diftime)
	{ 
		
	}

	private void gameRender()
	{
		dbg.setColor(Color.white);
		dbg.fillRect (0, 0, PWIDTH, PHEIGHT);	
		dbg.setColor(Color.BLUE);
	}	
}

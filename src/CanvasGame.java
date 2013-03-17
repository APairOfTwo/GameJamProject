import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class CanvasGame extends Canvas {
	public static CanvasGame instance = null;
	public static ElementManager gameElements = new ElementManager();
	public static CharBilly billy;
	public static TileMap map;
	
	public static BufferedImage charsetBilly;
	public static BufferedImage charsetBillyPeB;
	public static BufferedImage charsetEnemyBug;
	public static BufferedImage charsetEnemyBugPeB;
	public static BufferedImage charsetEnemyPlatform;
	public static BufferedImage charsetEnemyPlatformPeB;
	public static BufferedImage imagePlatform;
	public static BufferedImage tileset;
	public static BufferedImage tilesetPeB;
	public static BufferedImage backDay;
	public static BufferedImage backDayPeB;
	public static BufferedImage backNight;
	public BufferedImage loadingScreen = GamePanel.loadImage("backgrounds/loadingBackground.png");
	
	public static int rule = AlphaComposite.SRC_OVER;
	public static Composite comp;
	public float alpha = 1f;
	public float decreaseTimeTick = 2000;
	public boolean descTime = true;
	
	public static String strMap01 = new String("maps/mapacompleto.map");
	public static String strTileset01 = new String("maps/tileset2222.png");
	public static String strTilesetPeB = new String("maps/tilesetPeB.png");
	public static String strElements01 = new String("csv/teste2.csv");
	
	Random rand = new Random();
	
	public static ArrayList<Character> enemiesList = new ArrayList<Character>();
	public static ArrayList<Projectile> projectilesList = new ArrayList<Projectile>();
	public static ArrayList<Terminal> checkpoints = new ArrayList<Terminal>();
	public static ArrayList<Effect> effectsList = new ArrayList<Effect>();
	
	public static boolean LEFT, RIGHT, JUMP, FIRE, INTERACTION;
	public static boolean enableJump, enableFire, enableColor, enableRain, enableTransition, enableTilesetColor;
	public static boolean MOUSE_PRESSED;
	public static int MOUSE_X, MOUSE_Y;
	public static int MOUSE_CLICK_X, MOUSE_CLICK_Y;
	public static boolean loading;
	public int loadTime;
	public int mapPositionX;
	public int mapPositionY;
	public int respawnTime = 2000;
	public static int projectilesCounter = 0;
	
    private RainDrop[] rainDrops = new RainDrop[100];
    
    public static int numBugs = 0;
    public static int numBugsCorrected = 0;
    
    public static int numTerminal = 0;
    public static int numTerminalActivated = 0;
	
	public CanvasGame(int levelId) {
		instance = this;
		
		//MainApplet.menuMusic.stop(); TODO
		
		charsetBilly = GamePanel.loadImage("sprites/nerd.png");
		charsetBillyPeB = GamePanel.loadImage("sprites/nerdPeB.png");
		charsetEnemyBug = GamePanel.loadImage("sprites/bug.png");
		charsetEnemyBugPeB = GamePanel.loadImage("sprites/bugPeB.png");
		charsetEnemyPlatform = GamePanel.loadImage("sprites/bugPlatform.png");
		imagePlatform = GamePanel.loadImage("sprites/platform.png");
		backDay = GamePanel.loadImage("backgrounds/day_sky.png");
		backDayPeB = GamePanel.loadImage("backgrounds/day_skyPeB.png");
		backNight = GamePanel.loadImage("backgrounds/night_sky.png");
		
		MOUSE_X = 0;
		MOUSE_Y = 0;
		MOUSE_CLICK_X = 0;
		MOUSE_CLICK_Y = 0;
		MOUSE_PRESSED = false;
		enableJump = false;
		enableFire = false;
		enableRain = false;
		enableTransition = false;
		enableColor = false;
		loading = true;
        createRainDrops();
	}
	
	@Override
	public void selfSimulates(long diffTime) {
		if(!loading) {
			if(billy.isAlive) {
				billy.selfSimulates(diffTime);
				mapPositionX = (int)billy.x;
				mapPositionY = (int)billy.y;
				map.Positions(mapPositionX-GamePanel.PANEL_WIDTH/2, mapPositionY-GamePanel.PANEL_HEIGHT/2);
			} else {
				billy.respawnCountTime += diffTime;
				if(billy.respawnCountTime >= respawnTime) {
					billy.respawnCountTime = 0;
					billy.respawn();
				}
			}
		
			for(int i = 0; i < projectilesList.size(); i++){
				projectilesList.get(i).selfSimulates(diffTime);
				if(!projectilesList.get(i).active){
					projectilesList.remove(i);
					i--;
				}
			}
			
			for(int i = 0; i < enemiesList.size(); i++) {
				enemiesList.get(i).selfSimulates(diffTime);
				if(!enemiesList.get(i).isAlive){
					enemiesList.remove(i);
					i--;
					numBugsCorrected++;
				}
			}

			if(checkpoints.get(0).isActive) { enableJump = true; }
			if(checkpoints.get(1).isActive) { enableFire = true; checkpoints.get(1).featureMsg = GamePanel.loadImage("sprites/msgBugs.png"); }
			if(checkpoints.get(2).isActive) { enableColor = true; checkpoints.get(2).featureMsg = GamePanel.loadImage("sprites/msgCor.png"); }
			if(checkpoints.get(3).isActive) { enableTransition = true; checkpoints.get(3).featureMsg = GamePanel.loadImage("sprites/msgGeral.png"); }
			if(checkpoints.get(4).isActive) { enableRain = true; }
			if(!enableTilesetColor && enableColor) {
				map.TileSet = GamePanel.loadImage(strTileset01);
				enableTilesetColor = true;
			}
			
			for(Terminal c : checkpoints) {
				c.selfSimulates(diffTime);
				if(c.respawnBilly && !c.hasBeenActived) {
					billy.respawnCountTime += diffTime;
					if(billy.respawnCountTime >= 1000) {
						billy.respawnCountTime = 0;
						billy.respawn();
						c.respawnBilly = false;
						c.hasBeenActived = true;
					}
				}
			}
			
			for(int i = 0; i < effectsList.size(); i++){
				effectsList.get(i).selfSimulates(diffTime);
				if(effectsList.get(i).active == false){
					effectsList.remove(i);
					i--;
				}
			}
			
			if(enableTransition) {
				decreaseTimeTick += diffTime;
				if(decreaseTimeTick >= 500) {
					decreaseTimeTick = 0;
					if(descTime) {
						alpha -= 0.01f;
					} else {
						alpha += 0.01f;
					}
					if(alpha < 0.01 || alpha >= 1){
						descTime = !descTime;
					}
				}
			}

		} else {
			loadTime += diffTime;
			if(loadTime >= 2000) {
				loadTime = 0;
				loading = false;
				//MainApplet.music1.loop(); //TODO
			}
		}
	}
	
	@Override
	public void selfDraws(Graphics2D dbg) {
		if(enableTransition) {
			dbg.drawImage(backNight, 0, 0, null);
			
	        comp = AlphaComposite.getInstance(rule , alpha);
	        dbg.setComposite(comp);
			
	        dbg.drawImage(backDay, 0, 0, null);
	        
	        comp = AlphaComposite.getInstance(rule , 1f);
	        dbg.setComposite(comp);
		} else {
			if(enableColor) {
				dbg.drawImage(backDay, 0, 0, null);
			} else {
				dbg.drawImage(backDayPeB, 0, 0, null);
			}
		}

		map.selfDraws(dbg);
		
		for(int i = 0; i < projectilesList.size(); i++){
			projectilesList.get(i).selfDraws(dbg, map.MapX, map.MapY);
		}
		for(int i = 0; i < enemiesList.size(); i++) {
			enemiesList.get(i).selfDraws(dbg, map.MapX, map.MapY);
		}
		for(Terminal c : checkpoints) {
			c.selfDraws(dbg, map.MapX, map.MapY);
		}
		for(int i = 0; i < effectsList.size(); i++){
			effectsList.get(i).selfDraws(dbg, map.MapX, map.MapY);
		}
		
		billy.selfDraws(dbg, map.MapX, map.MapY);
		
		if(enableRain) {
	        for(int i = 0; i< rainDrops.length; ++i) {
	        	dbg.setColor(Color.WHITE);
	        	rainDrops[i].selfDraws(dbg, map.MapX, map.MapY);
	            if(rainDrops[i].x > GamePanel.PANEL_WIDTH || rainDrops[i].y > GamePanel.PANEL_HEIGHT)
	            	createRainDrop(i);
	        }
		}
		
		dbg.setColor(Color.RED);
		dbg.fillRect(10, 25, 10 * numBugs, 10);
		dbg.setColor(Color.GREEN);
		dbg.fillRect(10, 25, 10 * numBugsCorrected, 10);
		dbg.drawString("Bugs Corrigidos: "+numBugsCorrected, 10, 18);
		
		dbg.setColor(Color.RED);
		dbg.fillRect(10, 57, 20 * numTerminal, 10);
		dbg.setColor(Color.GREEN);
		dbg.fillRect(10, 57, 20 * numTerminalActivated, 10);
		dbg.drawString("Desenvolvimento do Jogo", 10, 50);
		
		if(loading) {
			dbg.setColor(Color.BLACK);
			dbg.fillRect(0, 0, GamePanel.PANEL_WIDTH, GamePanel.PANEL_HEIGHT);
			dbg.drawImage(loadingScreen, 242, 274, 559, 326, 0, 0, loadingScreen.getWidth(), loadingScreen.getHeight(), null);
		}
	}

	@Override
	public void keyPressed(KeyEvent k){
		int keyCode = k.getKeyCode();
		if(keyCode == KeyEvent.VK_A)		{ LEFT = true; }
		if(keyCode == KeyEvent.VK_D)		{ RIGHT = true; }
		if(keyCode == KeyEvent.VK_W)		{ JUMP  = true; }
		if(keyCode == KeyEvent.VK_CONTROL)		{ INTERACTION  = true;}
		if(keyCode == KeyEvent.VK_F)	{ GamePanel.showFps = !GamePanel.showFps; }
		if(keyCode == KeyEvent.VK_ESCAPE) {
			if(CanvasPause.instance == null) {
				CanvasPause pause = new CanvasPause();
			}
			GamePanel.canvasActive = CanvasPause.instance;
		}
	}

	@Override
	public void keyReleased(KeyEvent k){
		int keyCode = k.getKeyCode();
		if(keyCode == KeyEvent.VK_A)		{ LEFT  = false; }
		if(keyCode == KeyEvent.VK_D)		{ RIGHT = false; }
		if(keyCode == KeyEvent.VK_W)		{ JUMP  = false; }
		if(keyCode == KeyEvent.VK_CONTROL)		{ INTERACTION  = false; }
	}

	@Override
	public void mouseMoved(MouseEvent m) {
		MOUSE_X = m.getX();
		MOUSE_Y = m.getY();
	}

	@Override
	public void mouseDragged(MouseEvent m) {
		MOUSE_X = m.getX();
		MOUSE_Y = m.getY();
	}
	
	@Override
	public void mousePressed(MouseEvent m) {
		MOUSE_PRESSED = true;
		FIRE = true;
		MOUSE_CLICK_X = m.getX();
		MOUSE_CLICK_Y = m.getY();
	}

	@Override
	public void mouseReleased(MouseEvent m) { 
		MOUSE_PRESSED = false;
		FIRE = false;
	}
	
	public static void resetControls() {
		LEFT = false;
		RIGHT = false;
		JUMP = false; 
		FIRE = false;
	}
	
	public void createRainDrops() {
		for(int i = 0; i < rainDrops.length; ++i)
			createRainDrop(i);
    }
	
    public void createRainDrop(int index) {
    	int ranX = (int) (Math.random() * (GamePanel.PANEL_WIDTH+400)-400);
        int ranY = (int) (0);
        int ranVel = (int) (Math.random() * 10);
        rainDrops[index] = new RainDrop(ranX, ranY, ranVel);
    }
	
	public static void setGameLevel(int levelId) {
		if(levelId == 1) {
			enemiesList.clear();
			projectilesList.clear();
			checkpoints.clear();
			gameElements.elementsList.clear();
			tileset = GamePanel.loadImage(strTilesetPeB);
			map = new TileMap(CanvasGame.tileset, (GamePanel.PANEL_WIDTH>>4)+(((GamePanel.PANEL_WIDTH&0x000f)>0)?1:0), (GamePanel.PANEL_HEIGHT>>4)+(((GamePanel.PANEL_HEIGHT%16)>0)?1:0));
			map.OpenMap(strMap01);
			gameElements = new ElementManager(strElements01);
			gameElements.decodeElements();
		}
	}
}
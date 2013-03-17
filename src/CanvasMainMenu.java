import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CanvasMainMenu extends Canvas {

	public static CanvasMainMenu instance = null;
	private BufferedImage background;
	private GameButton btnNewGame, btnOptions, btnExit;
	public static int MOUSE_X, MOUSE_Y;
	public static int MOUSE_CLICK_X, MOUSE_CLICK_Y;
	public static boolean MOUSE_PRESSED;
	
	public CanvasMainMenu() {
		instance = this;
		background = GamePanel.loadImage("backgrounds/tela_inicial.png");
		btnNewGame = new GameButton(GamePanel.PANEL_WIDTH/2 - 50, GamePanel.PANEL_HEIGHT/2, "buttons/botaoIniciar.png", "buttons/botaoIniciar.png");
		btnOptions = new GameButton(GamePanel.PANEL_WIDTH/2 - 50, GamePanel.PANEL_HEIGHT/2 + 150, "buttons/botaoSobre.png", "buttons/botaoSobre.png");
		btnExit = new GameButton(GamePanel.PANEL_WIDTH/2 - 50, GamePanel.PANEL_HEIGHT/2 + 225, "buttons/botaoSair.png", "buttons/botaoSair.png");
	}
	
	@Override
	public void selfSimulates(long diffTime) {
		if(btnNewGame.isMouseOver(MOUSE_X, MOUSE_Y)){ btnNewGame.setState(1); }
		else { btnNewGame.setState(0); }
		
		if(btnOptions.isMouseOver(MOUSE_X, MOUSE_Y)){ btnOptions.setState(1); }
		else { btnOptions.setState(0); }
		
		if(btnExit.isMouseOver(MOUSE_X, MOUSE_Y)){ btnExit.setState(1); }
		else { btnExit.setState(0); }
		
		
		if(MOUSE_PRESSED && btnNewGame.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			GamePanel.levelId = 1;
			GamePanel.canvasActive = new CanvasGame(GamePanel.levelId);
			CanvasGame.setGameLevel(GamePanel.levelId);
			MOUSE_PRESSED = false;
		}
		
		if(MOUSE_PRESSED && btnOptions.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			GamePanel.canvasActive = new CanvasOptions();
			MOUSE_PRESSED = false;
		}
		
		if(MOUSE_PRESSED && btnExit.isMouseOver(MOUSE_CLICK_X, MOUSE_CLICK_Y)) {
			System.exit(0);
		}
	}

	@Override
	public void selfDraws(Graphics2D dbg) {
		dbg.drawImage(background, 0, 0, GamePanel.PANEL_WIDTH, GamePanel.PANEL_HEIGHT, 0, 0, background.getWidth(), background.getHeight(), null);
		btnNewGame.selfDraws(dbg);
		btnOptions.selfDraws(dbg);
		btnExit.selfDraws(dbg);
	}

	@Override
	public void keyPressed(KeyEvent k){ }

	@Override
	public void keyReleased(KeyEvent k){ }
	
	@Override
	public void mouseMoved(MouseEvent m){ 
		MOUSE_X = m.getX();
		MOUSE_Y = m.getY();
	}
	
	@Override
	public void mouseDragged(MouseEvent e){ }
	
	@Override
	public void mouseReleased(MouseEvent e){
		MOUSE_PRESSED = false;
	}

	@Override
	public void mousePressed(MouseEvent m){
		MOUSE_PRESSED = true;
		MOUSE_CLICK_X = m.getX();
		MOUSE_CLICK_Y = m.getY();
	}
}
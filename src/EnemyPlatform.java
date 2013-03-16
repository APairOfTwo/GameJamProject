import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;


public class EnemyPlatform extends Character {
	public static int BUG_MODE = 0;
	public static int PLATFORM_MODE = 1;
	public int activeMode = BUG_MODE;
	private Random rand = new Random();
	BufferedImage imagePlatform = null;
	
	public EnemyPlatform(float x, float y, BufferedImage charset, BufferedImage imagePlatform, int charsetX, int charsetY) {
		super(x, y, charset, charsetX, charsetY, 110, 120, 4);
		this.imagePlatform = imagePlatform;		
		int temp = rand.nextInt(2);
		if(temp == 0) moveDirection = -1;
		else moveDirection = 1;
	}

	@Override
	public void selfSimulates(long diffTime){	
		super.selfSimulates(diffTime);
		
		if(this.activeMode == BUG_MODE) {
			if(moveDirection == 1) {
				animation = 1;
			} else if(moveDirection == -1) {
				animation = 0;
			} else {
				timeAnimating = 0;
			}
		} else {
			if(CanvasGame.billy.getBounds().intersects(getBounds())) {
				CanvasGame.billy.y = CanvasGame.billy.oldY;
				CanvasGame.billy.onTheFloor = true;
			}
		}
	}
	
	@Override
	public void hitByProjectile() {
		if(this.activeMode == BUG_MODE) {
			this.activeMode = PLATFORM_MODE;
		}
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		if(this.activeMode == BUG_MODE)
			super.selfDraws(dbg, mapX, mapY);
		else {
			dbg.drawImage(imagePlatform, (int)(x-mapX), (int)(y-mapY), (int)((x+imagePlatform.getWidth())-mapX), (int)((y+imagePlatform.getHeight())-mapY),
					(0), (0), imagePlatform.getWidth(), imagePlatform.getHeight(), null);
		}
	}
	
	public Rectangle getBounds() {
		Rectangle r = new Rectangle((int)(x-CanvasGame.map.MapX), (int)(y-CanvasGame.map.MapY), imagePlatform.getWidth(), imagePlatform.getHeight());
		return r;
	}
}

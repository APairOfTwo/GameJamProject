import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;


public class EnemyPlatform extends Character {
	boolean isPlat = false;
	private Random rand = new Random();
	BufferedImage imagePlatform = null;
	
	public EnemyPlatform(float x, float y, BufferedImage charset, BufferedImage charsetPeB, BufferedImage imagePlatform, int charsetX, int charsetY) {
		super(x, y, charset, charsetPeB, charsetX, charsetY, 59, 35, 1);
		this.imagePlatform = imagePlatform;
		int temp = rand.nextInt(2);
		if(temp == 0) moveDirection = -1;
		else moveDirection = 1;
	}

	@Override
	public void selfSimulates(long diffTime){	
		super.selfSimulates(diffTime);
		
		
		if(!isPlat && this.getBounds().intersects(CanvasGame.billy.getBounds())) {
			CanvasGame.deathCounter++;
			CanvasGame.billy.isAlive = false;
			bloodAngle = Math.atan2(100, 1);
			bloodAngle += Math.PI;
			for(int i = 0; i < 20; i++) {
				bloodAuxAngle = bloodAngle - (Math.PI/4) + ((Math.PI/2) * Math.random());
				vel = (float)(50 + 50 * Math.random());
				vX = (float)(Math.cos(bloodAuxAngle) * vel);
				vY = (float)(Math.sin(bloodAuxAngle) * vel);
				CanvasGame.effectsList.add(new Effect(x+frameWidth/2, y+frameHeight/2, vX, vY, 600, 255, 0, 0));
			}
		
		if(!this.isPlat) {
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
		}}
	}
	
	@Override
	public void hitByProjectile() {
		this.isPlat = true;
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		if(!this.isPlat)
			super.selfDraws(dbg, mapX, mapY);
		else {
			dbg.drawImage(imagePlatform, (int)(x-mapX), (int)((y+10)-mapY), (int)((x+imagePlatform.getWidth())-mapX), (int)((y+imagePlatform.getHeight()+10)-mapY),
					(0), (0), imagePlatform.getWidth(), imagePlatform.getHeight(), null);
		}
	}
	
	public Rectangle getBounds() {
		Rectangle r = new Rectangle((int)(x-CanvasGame.map.MapX), (int)(y-CanvasGame.map.MapY), imagePlatform.getWidth(), imagePlatform.getHeight());
		return r;
	}
}

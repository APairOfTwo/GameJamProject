import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class EnemyBug extends Character {
	final float DEFAULT_SPEED = 109;
	double projDx, projDy, projDist;
	double velX, velY;
	private int changeDirectionRate = 200;
	Projectile proj;
	
	public EnemyBug(float x, float y, BufferedImage charset, BufferedImage charsetPeB, int charsetX, int charsetY) {
		super(x, y, charset, charsetPeB, charsetX, charsetY, 39, 30, 1);
		speed = DEFAULT_SPEED;
	}

	@Override
	public void selfSimulates(long diffTime){	
		super.selfSimulates(diffTime);
		
		if(!onTheFloor) {
			y += gravity * diffTime / 1000.0f;
		}
		
		oldX = x;
		oldY = y;
		
		if((x < 5)) {
			x = 5;
			if(fireTimer > changeDirectionRate) {
				fireTimer = 0;
				moveDirection *= -1;
			}
		}
		if((x+frameWidth > (CanvasGame.map.Largura << 4)-5)) {
			x = (((CanvasGame.map.Largura << 4)-5)-frameWidth);
			if(fireTimer > changeDirectionRate) {
				fireTimer = 0;
				moveDirection *= -1;
			}
		}
		if((y < 5)) { y = 5; }
		if((y+frameHeight > (CanvasGame.map.Altura << 4)-5)) { isAlive = false; }
		
		velX = speed * diffTime / 1000.0f;
		velY = speed * diffTime / 1000.0f;
		
		if(moveDirection == 1) {
			x += speed * diffTime / 1000.0f;
		} else if(moveDirection == -1) {
			x -= speed * diffTime / 1000.0f;
		}
		
		if(floorCollision((int)((x+40)/16), (int)((x+35)/16), (int)((x+30)/16), (int)((y+37)/16), (int)((y+36)/16), (int)((y+35)/16))) {
			y = oldY;
			if((int)oldY % 16 != 0) {
				y -= 1;
			}
			onTheFloor = true;
		} else {
			onTheFloor = false;
		}
		
		if(lateralCollision((int)((x+39)/16), (int)((x+35)/16), (int)((y+22)/16), (int)((y+20)/16), (int)((y+15)/16))) {
			x = oldX;
			if(fireTimer > changeDirectionRate) {
				fireTimer = 0;
				moveDirection *= -1;
				System.out.println("aqui");
			}
		}
		
		if(spykeCollision((int)((x+40)/16), (int)((x+50)/16), (int)((y+35)/16), (int)((y+20)/16))) {
			bloodAngle = Math.atan2(100, 1);
			bloodAngle += Math.PI;
			for(int i = 0; i < 20; i++) {
				bloodAuxAngle = bloodAngle - (Math.PI/4) + ((Math.PI/2) * Math.random());
				vel = (float)(50 + 50 * Math.random());
				vX = (float)(Math.cos(bloodAuxAngle) * vel);
				vY = (float)(Math.sin(bloodAuxAngle) * vel);
				CanvasGame.effectsList.add(new Effect(x+frameWidth/2, y+frameHeight/2, vX, vY, 600, 255, 0, 0));
			}
			isAlive = false;
		}
		
		for(Element e : CanvasGame.gameElements.elementsList) {
			if(e.itemId == 8) {
				if(this.getBounds().intersects((e.blockX<<4)-CanvasGame.map.MapX, (e.blockY<<4)-CanvasGame.map.MapY, 16, 64)) {
					if(fireTimer > changeDirectionRate) {
						fireTimer = 0;
						moveDirection *= -1;
					}
				}
			}
		}
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		super.selfDraws(dbg, mapX, mapY);
	}
	
	public Rectangle getBounds() {
		Rectangle r = new Rectangle((int)(x-CanvasGame.map.MapX+23), (int)(y-CanvasGame.map.MapY+30), 53, 40);
		return r;
	}
}

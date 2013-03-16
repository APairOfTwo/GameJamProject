import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.w3c.dom.css.Rect;


public class Terminal extends Sprite {
	int x, y;
	boolean isActive;
	boolean hasBeenActived = false;
	boolean respawnBilly, respawnZombie;
	BufferedImage checkpointOff = GamePanel.loadImage("sprites/checkpointOff.png");
	BufferedImage checkpointOn = GamePanel.loadImage("sprites/checkpointOn.png");
	BufferedImage checkpointMsg = GamePanel.loadImage("sprites/checkpointMsg.png");
	int msgX, msgY, msgWidth, msgHeight;
	boolean showMsg = false;
	long msgTime = 0;
	int collidedCounter = 0;
	public Rectangle mapBlock;
	int mapBlockX, mapBlockY, mapBlockWidth, mapBlockHeight;
	
	public Terminal(int x, int y) {
		this.x = x;
		this.y = y;
		this.isActive = false;
		this.respawnBilly = false;
		this.respawnZombie = false;
		this.msgX = (x - (checkpointMsg.getWidth()/2)) + (checkpointOn.getWidth()/2);
		this.msgY = y - (checkpointOn.getHeight());
		this.msgWidth = (msgX + checkpointMsg.getWidth());
		this.msgHeight = (msgY + checkpointMsg.getHeight());
		this.mapBlockX = (x+GamePanel.PANEL_WIDTH/4);
		this.mapBlockY = 0;
		this.mapBlockWidth = ((x + GamePanel.PANEL_WIDTH/4) + GamePanel.PANEL_WIDTH / 2);
		this.mapBlockHeight = (GamePanel.PANEL_HEIGHT + 500);
		this.mapBlock = new Rectangle(mapBlockX, mapBlockY, mapBlockWidth, mapBlockHeight);
		
	}

	@Override
	public void selfSimulates(long diffTime) {
		if(CanvasGame.billy.getBounds().intersects(x-CanvasGame.map.MapX, y-CanvasGame.map.MapY, 16, 64) && CanvasGame.INTERACTION) {
			collidedCounter = 1;
			if(!isActive && collidedCounter == 1) {
				showMsg = true;
				collidedCounter = 0;
			}
			isActive = true;
		}
		
		if(!isActive && CanvasGame.billy.getBounds().intersects(mapBlockX-CanvasGame.map.MapX, mapBlockY-CanvasGame.map.MapY, mapBlockWidth-CanvasGame.map.MapX, mapBlockHeight-CanvasGame.map.MapY)) {
			CanvasGame.billy.x = CanvasGame.billy.oldX;
		}

		if(showMsg) {
			msgTime += diffTime;
			msgX -= 0.02;
			msgY -= 0.02;
			msgWidth -= 0.02;
			msgHeight -= 0.02;
		}
		if(msgTime >= 2000) {
			showMsg = false;
		}
		
		
	}

	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		if(this.isActive) {
			dbg.drawImage(checkpointOn, (int)(x-mapX), (int)(y-mapY), (int)((x+checkpointOn.getWidth())-mapX), (int)((y+checkpointOn.getHeight())-mapY), 0, 0, checkpointOn.getWidth(), checkpointOn.getHeight(), null);
		} else {
			dbg.drawImage(checkpointOff, (int)(x-mapX), (int)(y-mapY), (int)((x+checkpointOff.getWidth())-mapX), (int)((y+checkpointOff.getHeight())-mapY), 0, 0, checkpointOff.getWidth(), checkpointOff.getHeight(), null);
			dbg.setColor(Color.WHITE);
			dbg.fillRect((int)(mapBlockX-mapX), (int)(mapBlockY-mapY), (int)(mapBlockWidth-mapX), (int)(mapBlockHeight-mapY));
		}
		if(showMsg) {
			dbg.drawImage(checkpointMsg, (int)(msgX-mapX), (int)(msgY-mapY), (int)(msgWidth-mapX), (int)(msgHeight-mapY), 0, 0, checkpointMsg.getWidth(), checkpointMsg.getHeight(), null);
		}
	}
}
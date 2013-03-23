import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class Terminal extends Sprite {
	int x, y;
	boolean isActive;
	boolean hasBeenActived = false;
	boolean respawnBilly, respawnZombie;
	public static BufferedImage featureMsg = GamePanel.loadImage("sprites/msgTerminal.png");
	BufferedImage checkpointOff = GamePanel.loadImage("sprites/terminalOff.png");
	BufferedImage checkpointOn = GamePanel.loadImage("sprites/terminalOn.png");
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
		this.msgX = (x - (featureMsg.getWidth()/2)) + (checkpointOn.getWidth()/2);
		this.msgY = y - (checkpointOn.getHeight());
		this.msgWidth = (msgX + featureMsg.getWidth());
		this.msgHeight = (msgY + featureMsg.getHeight());
		this.mapBlockX = (x+GamePanel.PANEL_WIDTH/4);
		this.mapBlockY = (y-500);
		this.mapBlockWidth = ((x + GamePanel.PANEL_WIDTH/4) + GamePanel.PANEL_WIDTH / 2);
		this.mapBlockHeight = ((y-500) + 1000);
		this.mapBlock = new Rectangle(mapBlockX, mapBlockY, mapBlockWidth, mapBlockHeight);
		
	}

	@Override
	public void selfSimulates(long diffTime) {
		this.msgX = (x - (featureMsg.getWidth()/2)) + (checkpointOn.getWidth()/2);
		this.msgY = y - (checkpointOn.getHeight());
		this.msgWidth = (msgX + featureMsg.getWidth());
		this.msgHeight = (msgY + featureMsg.getHeight());
		
		if(!CanvasGame.checkpoints.get(0).isActive) {
			if(CanvasGame.billy.getBounds().intersects(x-CanvasGame.map.MapX, y-CanvasGame.map.MapY, 56, 50)) {
				if(!isActive) {
					showMsg = true;
					collidedCounter = 0;
				}
			}
		}
		
		if(CanvasGame.billy.getBounds().intersects(x-CanvasGame.map.MapX, y-CanvasGame.map.MapY, 56, 50) && CanvasGame.INTERACTION) {
			collidedCounter = 1;
			msgTime = 0;
			if(!isActive && collidedCounter == 1) {
				showMsg = true;
				collidedCounter = 0;
				CanvasGame.numTerminalActivated++;
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
		if(msgTime >= 3000) {
			showMsg = false;
		}
	}

	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		if(this.isActive) {
			dbg.drawImage(checkpointOn, (int)(x-mapX), (int)(y-mapY), (int)((x+checkpointOn.getWidth())-mapX), (int)((y+checkpointOn.getHeight())-mapY), 0, 0, checkpointOn.getWidth(), checkpointOn.getHeight(), null);
		} else {
			dbg.drawImage(checkpointOff, (int)(x-mapX), (int)(y-mapY), (int)((x+checkpointOff.getWidth())-mapX), (int)((y+checkpointOff.getHeight())-mapY), 0, 0, checkpointOff.getWidth(), checkpointOff.getHeight(), null);
			
			CanvasGame.comp = AlphaComposite.getInstance(CanvasGame.rule , 0.5f);
			dbg.setComposite(CanvasGame.comp);
			
			dbg.setColor(Color.RED);
			dbg.fillRect((int)(mapBlockX-mapX), (int)(mapBlockY-mapY), (int)(mapBlockWidth-mapX), (int)(mapBlockHeight-mapY));
			
			CanvasGame.comp = AlphaComposite.getInstance(CanvasGame.rule , 1f);
			dbg.setComposite(CanvasGame.comp);
		}
		if(showMsg) {
			dbg.drawImage(featureMsg, (int)(msgX-mapX), (int)(msgY-mapY), (int)(msgWidth-mapX), (int)(msgHeight-mapY), 0, 0, featureMsg.getWidth(), featureMsg.getHeight(), null);
		}
	}
}
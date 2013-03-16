import java.awt.Color;
import java.awt.Graphics2D;


public class Rain {
	
	public float x, y;
	public int length = 500;
	public int speed = 500;
	public boolean active = true;
	
	public Rain(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void selfSimulates(long diffTime) {
		x += (speed / 2) * diffTime / 1000.0f;
		y += speed * diffTime / 1000.0f;
	}

	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		dbg.setColor(Color.BLACK);
		dbg.drawLine((int)x-mapX, (int)y-mapY, (int)x-mapX+25, (int)y-mapY+50);
	}
}

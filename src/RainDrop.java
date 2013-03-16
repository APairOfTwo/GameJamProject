import java.awt.Graphics2D;


public class RainDrop extends Sprite{
	public int x, y, vel;
	public static int inclination = 3;

	public RainDrop(int x, int y, int v)
	{
		this.x = x;
		this.y = y;
		this.vel = v;
	}

	@Override
	public void selfSimulates(long diffTime) { }

	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {
		dbg.drawLine(x, y, x + inclination, y + inclination);
		x += inclination + vel;
		y += inclination + vel;
	}
}
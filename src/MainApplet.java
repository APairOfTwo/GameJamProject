
import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JApplet;


public class MainApplet extends JApplet {
	
	public static GamePanel panel = null;
	
	@Override
	public void init() {
		super.init();
		
		panel = new GamePanel();

		setSize(GamePanel.PANEL_WIDTH, GamePanel.PANEL_HEIGHT);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(panel, BorderLayout.CENTER);

		setVisible(true);
	}
	
}


import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JApplet;
import javax.swing.JFrame;


public class MainApplet extends JApplet {
	
	public static MainCanvas mpanel = null;
	
	@Override
	public void init() {
		super.init();
		
		mpanel = new MainCanvas();

		setSize(MainCanvas.PWIDTH, MainCanvas.PHEIGHT);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(mpanel, BorderLayout.CENTER);

		setVisible(true);
	}
	
}

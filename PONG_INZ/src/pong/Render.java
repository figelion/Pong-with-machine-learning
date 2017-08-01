package pong;

import java.awt.Graphics;
import java.io.IOException;

import javax.swing.JPanel; 

public class Render extends JPanel
{

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		try {
			PONG_INZ.pong.render(g);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}

package pong;

import java.awt.Color;
import java.awt.Graphics;


public class Paddle 
{

	public int paddleNumber;
	
	public int x, y, width=40, height=175;
	
	public int score;
	
	
	public Paddle(PONG_INZ pong, int paddleNumber)
	{
		
		this.paddleNumber = paddleNumber;
	
 		if(this.paddleNumber == 1)
		{
			this.x=0;
			
		}
		
		if(this.paddleNumber == 2)
		{
			this.x=pong.width - width;
			
		}
		
		this.y=pong.height /2 - this.height /2 ;
	}


	public void render(Graphics g) 
	{
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width, height);
		
	}


	public void move(boolean up)
	{
		int speed =10;
		
		if (up)
		{
			if(y - speed > 0)
			{
				y-= speed;
			}
			else
			{
				y = 0;
			}
		}
		else
		{
			if(y  + height + speed < PONG_INZ.pong.height)
			{
				y+= speed;
			}
			else
			{
				y = PONG_INZ.pong.height - height;
			}
		}
	}





}
package pong;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball 
{
	
	public int x, y, width = 25, height = 25;
	
	public int motionX, motionY;
	
	public Random random;
	
	private PONG_INZ pong;
	
	public int amountOfHits;
	public int amountOfHits2;
	public int amountOfHitsW1;
	public int amountOfHitsW2;
	public int amountOfHitsW3;
	public int amountOfHitsW4;
	
	public boolean bounced=false;
	public boolean spawned=false;
	
	public Ball(PONG_INZ pong)
	{
		this.pong = pong;
		
		this.random = new Random();
		
		spawn();
	}
	
	public void update(Paddle paddle1, Paddle paddle2)
	{
		int speed =4;
		
		
		this.x += motionX * speed;
		this.y += motionY * speed;
		
		if( this.y + height - motionY > pong.height || this.y + motionY < 0)
		{
			if(this.motionY < 0)
			{
				this.y = 0;
				this.motionY = random.nextInt(4);
				if(motionY == 0)
				{
					motionY = 1;
				}
				
			}
			else
			{
				this.y = pong.height - height;
				this.motionY = -random.nextInt(4);
				if(motionY == 0)
				{
					motionY = -1;
				}
			}
			
			////////// check the state /////////
		}
		
		
		
		if(checkCollision(paddle1) == 1 )
		{
			
			bounced= false;
			//// TEaching
			pong.prize = 1;
			pong.delta = pong.prize - pong.Q[pong.state][pong.action];
			pong.Q[pong.state][pong.action] = pong.Q[pong.state][pong.action] + pong.beta*pong.delta; 
			//// koncepcja z 1 s (nr 2)
			pong.state  = 0;
			this.motionX = 1 + (amountOfHits / 3);
			this.motionY = -2 + random.nextInt(4);
			
			if(motionY == 0)
			{
				motionY = 1;
			}
			
			amountOfHits ++;
			amountOfHits2 ++;
		}
		if(checkCollision(paddle2) == 1 )
		{
			
			this.motionX = -1 - (amountOfHits / 3);
			this.motionY = -2 + random.nextInt(4);
			
			
			bounced = true;
			pong.position4 = false;
			pong.position3 = false;
			pong.position2 = false;
			pong.position1 = false;
			
			if(motionY == 0)
			{
				motionY = 1;
			}
			
			amountOfHits ++;
			amountOfHits2 ++;
		}
		if(checkCollision(paddle2) == 2 )
		{
			
			paddle1.score++;
			spawn();
		}
		else if(checkCollision(paddle1) == 2 )
		{
			
			paddle2.score++;
			//// TEaching
			pong.prize = -1;
			pong.delta = pong.prize - pong.Q[pong.state][pong.action];
			pong.Q[pong.state][pong.action] = pong.Q[pong.state][pong.action] + pong.beta*pong.delta; 
			//// koncepcja z 1 s (nr 2)
			spawn();
		}
	}
	
	public void spawn()
	{
		amountOfHits = 0;
		spawned = true;
		pong.position4 = false;
		pong.position3 = false;
		pong.position2 = false;
		pong.position1 = false;
		pong.state = 0;
		
		this.x = pong.width / 2 - this.width / 2;
		this.y = pong.height / 2 - this.height / 2;
		
		this.motionY = -2 + random.nextInt(4);
		
		if(motionY == 0)
		{
			motionY = 1;
		}
		
		if(random.nextBoolean())
		{
			motionX = 1;
		}
		else
		{
			motionX = -1;
		}
		this.motionX = -1 + random.nextInt(1);
	}
	
	
	
	public int checkCollision(Paddle paddle)
	{
		
		
		
		if(this.x < paddle.x + paddle.width && this.x + width  > paddle.x && this.y < paddle.y + paddle.height && this.y + height > paddle.y)
		{
			
			return 1;
		
		}
		
		else if((paddle.x >25 + x + width && paddle.paddleNumber == 1) || (paddle.x < x  && paddle.paddleNumber == 2))
		{
			
			return 2;
			
		}
		
				
		return 0; //nothing
	}
	
	
	
	public void render(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillOval(x, y, width, height);
	}
}

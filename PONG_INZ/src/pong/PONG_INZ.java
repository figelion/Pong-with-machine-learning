package pong;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;
import java.math.*;

import javax.swing.JFrame;
import javax.swing.Timer;




public class PONG_INZ implements ActionListener, KeyListener, Serializable
{
	

	private static final long serialVersionUID = 1L;

	public static PONG_INZ pong;
	
	public int width = 700, height = 700;
	
	public Render renderer;
	
	public Paddle player1;
	public Paddle player2;
	
	public Ball ball;
	
	public boolean bot = false, selectDifficulty; 
	
	
	// TEACHING ////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	public boolean botTeaching = false;
	public boolean playPlayer = false;
	public boolean theend = false;
	public boolean ini = false;
	public int prize = 0 ;
	public int enh = 0;
	
	public boolean position1 = false, position2 = false, position3 = false,
								position4 = false, position5 = false;
	
	
	public double x1=0,x2=0,y1=0,y2=0 ;
	public int minorState = 0;
	public double majorState = 0;
	public int state = 0;
	public double a=0;
	public int wins = 0;
	public int win1 =0;
	public int win2 =0;
	public int win3 =0;
	public int win4 =0;
	
	public int chooseMove ;
	public int action;
	public int[] A= new int[4];
	public int[] greedyAction;
	public int amountGreedyAction;
	public int amountofGames=0;
	boolean readyMove = false;
	
	public double epsilon = 0.05;
	public double gamma = 0.9;
	public double beta = 0.1;
	
	public double[][] Q = new double[4681][4];
	public double delta = 0;
	public double maxQ ;
	
	
	///////////////
	
	public boolean w,s,up,down;
	
	public Random random;
	
	public int gameStatus = 0, scorelimit; // 0- menu 1-paused 2- playing  3 -the end
	public int playerWon = 0;
	
	public int botMoves = 0;
	public int botDifficulty = 0;
	public int botCooldown = 0;
	
	
	
	public PONG_INZ() throws FileNotFoundException, IOException, ClassNotFoundException
	{
		
		Timer timer= new Timer(20,  this);
		random = new Random();
		JFrame jframe= new JFrame("PONG");
		
		
		
			
			for(int i=0; i< Q.length; i++)
				for(int j=0; j< Q[i].length; j++)
					Q[i][j] = 0;
		
			for(int i=0; i< 4; i++)
				A[i]=i;	
	
	/*	ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Q.ser"));
			out.writeObject(Q);
			out.flush();
			out.close();
		
				
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("Q.ser"));
			Q = (double[][]) in.readObject();
			in.close();
			
			System.out.println("Pole 339 3 =" +Q[339][3]);*/
			
			
			
		renderer= new Render();
		
		jframe.setSize(width + 16, height + 38);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(renderer);
		jframe.addKeyListener(this);
		
		
		timer.start();
	}
		
 
	public void start()
	{
		gameStatus = 2;
		player1= new Paddle(this,1);
		player2= new Paddle(this,2);
		ball= new Ball(this);
	}
	
	public void update()
	{
		
		if(player1.score >= scorelimit)
		{
			playerWon = 1;
			gameStatus = 3;
		}
		if(player2.score >= scorelimit)
		{
			playerWon = 2;
			gameStatus = 3;
		}
		
		//// TEACHING //////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		if (botTeaching)
		{
		
			if (ball.spawned)
			{
				if (ball.x<330)
				{
					ball.bounced = true;
					ball.spawned = false;
				}
				else if (ball.x > 370)
				{
					ball.bounced = false;
					ball.spawned = false;
				}
			}
		
		//// STATE ////
		
			
				if (ball.x <= 330 && ball.x > 310 && ball.bounced && !position4 )
				{
					readyMove=false;
					x2=ball.x;
					y2=ball.y;
					position4 = true;
					
					minorState= choose(y2);
					majorState = Math.pow(minorState, 4);
				}
				else if (ball.x <= 265 && ball.x > 245 && position4 && !position3 )
				{
					x2=ball.x;
					y2=ball.y;
					position3 = true;
					
					minorState= choose(y2);
					majorState = majorState + Math.pow(minorState, 3);
				}
				else if (ball.x <= 190 && ball.x > 170 && position3 && !position2 )
				{
					x2=ball.x;
					y2=ball.y;
					position2 = true;
					
					
					minorState= choose(y2);
					majorState = majorState + Math.pow(minorState, 2);
				}
				else if (ball.x <= 105 && ball.x > 85 && position2 && !position1 )
				{
					x2=ball.x;
					y2=ball.y;
					position1 = true;
					
					
					minorState= choose(y2);
					majorState = majorState + Math.pow(minorState, 1);
				}
				
				if (position1) 
					{
						state = (int) majorState;
						
					}
				else
				{
					state=0;
					
				}
		
				
			
			}
				/*else
				{
					x1 = 0; x2 = 0; y1 = 0; y2 = 0;
					position1 = false;
					position2 = false;
				}*/
		
		
		////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		/////////////////////////////////////////////
		//////// CHOOSE MOVE //////////////////////// 
		/////////////////////////////////////////////
		if(position1 && !readyMove)
		{
			amountGreedyAction=0;
			maxQ=Q[state][0];
			for(int n=1;n<4;n++)
			{
				if( maxQ < Q[state][n])
				{
					maxQ = Q[state][n];
				}
			}
		
			for(int n=0;n<4;n++)
			{
				if (maxQ == Q[state][n])
				{
					amountGreedyAction ++;
				}
			}
			System.out.println("amountgreedyAction check "  + amountGreedyAction );
			greedyAction = new int [amountGreedyAction];
			int amountgreedyaction2 = 0;
			
			for(int n=0;n<4;n++)
			{
				
				if(maxQ == Q[state][n])
				{
					greedyAction[amountgreedyaction2 ] = n;
					System.out.println("greedyAction "  + greedyAction[amountgreedyaction2] );
					amountgreedyaction2 ++;
					
				}	
				
			}
			for(int i=0;i<amountGreedyAction;i++)
				System.out.println("greedyAction check "  + greedyAction[i] );
			
			if(random.nextDouble() > epsilon)
			{
				int c;
				c = greedyAction[ (int) Math.floor(random.nextDouble() * greedyAction.length)];
				action= c;
				System.out.println("Akcja: " + c );
				
			}
			else 
			{
				int c = A[(int) Math.floor(random.nextDouble()*(A.length))];
				action= c;
				System.out.println("Akcj2: " + c );
			}
			
			readyMove = true;
			System.out.println("Akcja3: " + action);
			System.out.println("Stan: " + state);
		}
		
		//////// MOVE ///////
		if (!botTeaching)
		{
			if (w)
			{
				player1.move(true);
			}
			if (s)
			{
				player1.move(false);
			}
		}
		else if(position1)// koncepcja z 1s (nr2) 
		{
			switch (action)
			{
			case 0: player1.y = 0;
				break;

			case 1: player1.y = 175;
				break;

			case 2: player1.y = 350;
				break;

			case 3: player1.y = 525;
				break;
			}
			
		}
			
		if(!bot)
		{
		
			if (up)
			{
				player2.move(true);
			}
			if (down)
			{
				player2.move(false);
			}
		}
		else
		{
			if(botCooldown > 0)
			{
				botCooldown--;
				if(botCooldown <= 0)
				{
					botMoves = 0;
				}
			}
			if( botMoves < 10)
			{
				if(player2.y + player2.height / 2 < ball.y)
				{
					player2.move(false);
					botMoves++;
				}
				if(player2.y + player2.height / 2  > ball.y)
				{
					player2.move(true);
					botMoves++;
				}
				if (botMoves >= 10)
				{
					if(botDifficulty == 0)
					{
						botCooldown=20;	
					}
					if(botDifficulty == 1)
					{
						botCooldown=15;	
					}
					if(botDifficulty == 2)
					{
						botCooldown=10;	
					}
				}
			}
		}
		
		ball.update(player1, player2);
		
		//// Q-LEARNING ///////////////////////////////
		///////////////////////////////////////////////
		if(botTeaching){
			
		/*
		//// koncepcja z 1 stanem (nr2) /////
		delta = prize - Q[state][action];
		Q[state][action] = Q[state][action] + beta*delta; 
		
		*/
		//////////////////////////////
		
		//// koncjecpja z wieloma stanami (nr1) ////
		/*
		 * 
		 */
		//////////////////////////////
		
				
				
				
		}
		///////////////////////////////////////////////	
	}
	
	/////////// TEACHING //////////////////////////////
	///////////////////////////////////////////////////
	
	private int choose (double y)
	{
		if (y <= 117 ) 
		{
			minorState = 1;
		}
		if (y > 117 && y <= 234) 
		{
			minorState  = 2;
		}
		if (y > 234 && y <= 351) 
		{
			minorState  = 3;
		}
		if (y > 351 && y<= 468) 
		{
			minorState  = 4;
		}
		if (y > 468 && y <= 585  ) 
		{
			minorState  = 5;
		}
		if (y > 585  ) 
		{
			minorState  = 6;
		}
		if (y > 585  ) 
		{
			minorState  = 7;
		}
		if (y > 585  ) 
		{
			minorState  = 8;
		}
		return minorState;
	}
	
	/*private double coefficientsA(int param1x, int param1y, int param2x, int param2y) 
	{
		double x;
		double y;
		double diagonal;
		double A=0;
		
		x= param1x - param2x;
		x = Math.abs(x);
		
		y = param1y - param2y;
		y= Math.abs(y);
			
		diagonal= Math.sqrt(((x*x)+(y*y)));
		A = diagonal/x;
		
		if (param1y < param2y)
		{
			A = A * (-1);
		}
		
		return A;
	}*/
	
////////////////////////////////////////////////////////

	public void render(Graphics g) throws FileNotFoundException, IOException 
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING ,RenderingHints.VALUE_ANTIALIAS_OFF );
		
		if(gameStatus == 0)
		{
			amountofGames=0;
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial" ,1, 50));
			
			g.drawString("PONG", width / 2 - 70 , 50);
			
			if(!selectDifficulty)
			{
				g.setFont(new Font("Arial" ,1, 25));
				g.drawString("Press Space to play", width / 2 - 150 , height / 2 - 70);
				g.drawString("Press Shift to play with Bot", width / 2 - 150 , height / 2 - 25);
				g.drawString("Press P to play with intelligent Bot", width / 2 - 150 , height / 2 + 25);
				g.drawString("Press T to teach intelligent Bot", width / 2 - 150 , height / 2 + 75);
				g.drawString(">>Score Limit:  " + scorelimit, width / 2 - 150 , height / 2 + 125);
			}
		}
		
		if(selectDifficulty)
		{
			String string = botDifficulty == 0 ? "Easy" : (botDifficulty == 1 ? "Medium" : "Hard" );
			
			g.setFont(new Font("Arial" ,1, 25));
			g.drawString("Bot difficulty: " + string, width / 2 - 150 , height / 2 - 25);
			g.drawString("Press Space to play", width / 2 - 150 , height / 2 + 25);
			
		}
		
		
		if(gameStatus == 2 || gameStatus == 1)
		{
			theend=false;
			g.setColor(Color.WHITE);
			//g.setStroke(new BasicStroke(10)); // thicker line on the middle
			g.drawLine(width / 2, 0 , width / 2 , height);
			g.setFont(new Font("Arial" ,1, 50));
			
			g.drawString(String.valueOf(player1.score), width / 2 - 70 , 50);
			g.drawString(String.valueOf(player2.score), width / 2 + 70 , 50);
			g.setFont(new Font("Arial" ,1, 30));
			g.drawString("Numer rozgrywki: "+amountofGames, width / 2 - 130 , height / 2 + 340);
			
			player1.render(g);
			player2.render(g);
		
			ball.render(g);
		}
		
		if(gameStatus == 1)
		{
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial" ,1, 50));
			g.drawString("PAUSED", width / 2 - 105 , height / 2 - 105);
			g.setFont(new Font("Arial" ,1, 20));
			g.drawString("Amount of games: " + amountofGames, width / 2 - 97 , height / 2 - 55 );
			g.drawString("Press R to spawn the ball", width / 2 - 97, height / 2 - 30 );
		}
		
		if(gameStatus == 3)
		{
			//selectDifficulty = false;
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial" ,1, 50));
			
			g.drawString("PONG", width / 2 - 70 , 50);
			
			if (bot && playerWon == 2 )
			{
				g.drawString("Winner: Bot " , width / 2 - 190 , height / 2 - 170 );
			}
			else if(!botTeaching )
			{
				g.drawString("Winner: Player " + playerWon, width / 2 - 190 , height / 2 - 170 );
			}
			
			else if(botTeaching && playerWon == 1)
			{
				g.drawString("Winner: AI "  , width / 2 - 190 , height / 2 - 170 );
			}
			else
			{
				g.drawString("Winner: Player " + playerWon, width / 2 - 190 , height / 2 - 170 );
			}
			
			g.setFont(new Font("Arial" ,1, 25));
			g.drawString("Press Space to play Again", width / 2 - 170 , height / 2 - 25);
			g.drawString("Press Escape for Menu", width / 2 - 150 , height / 2 + 25);
			
			///TEACHING///
			if(botTeaching && bot)
			{
				g.setFont(new Font("Arial" ,1, 15));
				g.drawString("Amount of games: " + amountofGames, width / 2 - 130 , height / 2 + 75);
				g.drawString("Amount of hits: " + ball.amountOfHits2, width / 2 - 130 , height / 2 + 100);
				g.drawString("Win1: hits " + ball.amountOfHitsW1 + " games " + win1,width / 2 - 130, height / 2 + 120);
				g.drawString("Win2: hits " + ball.amountOfHitsW2 + " games " + win2,width / 2 - 130, height / 2 + 140);
				g.drawString("Win3: hits " + ball.amountOfHitsW3 + " games " + win3,width / 2 - 130, height / 2 + 160);
				g.drawString("Win3: hits " + ball.amountOfHitsW4 + " games " + win4,width / 2 - 130, height / 2 + 180);
				if(!theend)
				{
					amountofGames++;
					theend=true;
					if (playerWon == 1)
						wins ++;
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Q.ser"));
					out.writeObject(Q);
					out.flush();
					out.close();
		
				}
				if(playerWon == 2 )
				{
					player1.score = 0;
					player1.y = this.height /2 - player1.height /2  ;
					player2.score = 0;
					player2.y=this.height /2 - player2.height /2 ;
					gameStatus = 2;
					
				}
				
				else
				{
				
					if(wins == 1)
					{
						win1 = amountofGames;
						ball.amountOfHitsW1 = ball.amountOfHits2;
					}
					if(wins == 2)
					{
						win2 = amountofGames - win1;
						ball.amountOfHitsW2 = ball.amountOfHits2 - ball.amountOfHitsW1;
					}
					if(wins == 3)
					{
						win3 = amountofGames - win2 - win1;
						ball.amountOfHitsW3 = ball.amountOfHits2 - ball.amountOfHitsW1- ball.amountOfHitsW2;
					}
					if(wins == 4)
					{
						win4 = amountofGames - win3 - win2 - win1;
						ball.amountOfHitsW4 = ball.amountOfHits2- ball.amountOfHitsW3 - ball.amountOfHitsW1- ball.amountOfHitsW2;
					}
					if (wins <4)
					{
						player1.score = 0;
						player1.y = this.height /2 - player1.height /2  ;
						player2.score = 0;
						player2.y=this.height /2 - player2.height /2 ;
						gameStatus=2;
					}
				}
					
				
			}
			//////////////
			
		}
		
	}

	
	@Override
	public void actionPerformed(ActionEvent e)
	{	
		
		if (gameStatus == 2)
		{
			update();
		}
		
		renderer.repaint();
		
		
	}

		
	public static void main (String[] args) throws FileNotFoundException, IOException, ClassNotFoundException
	{ 
			pong= new PONG_INZ();
	}


	@Override
	public void keyPressed(KeyEvent e)
	{
		int id= e.getKeyCode();
		
		if(id == KeyEvent.VK_W )
		{
			w = true;
		}
		else if(id == KeyEvent.VK_S )
		{
			s = true;
		}
		else if(id == KeyEvent.VK_UP )
		{
			up = true;
		}
		else if(id == KeyEvent.VK_DOWN )
		{
			down = true;
		}
		else if(id == KeyEvent.VK_RIGHT)
		{
			if(selectDifficulty)
			{
				if(botDifficulty < 2)
				{
					botDifficulty ++;					
				}
				else
				{
					botDifficulty = 0;
				}
			}
			else if (gameStatus == 0)
			{
				scorelimit ++;
			}
				
			
		}
		else if(id == KeyEvent.VK_LEFT)
		{
			if(selectDifficulty)
			{
				if(botDifficulty > 0)
				{
					botDifficulty --;
				}
				else
				{
					botDifficulty = 2;
				}
			}
			else if (gameStatus == 0 && scorelimit > 1)
			{
				scorelimit --;
			}
			
		}
		else if(id == KeyEvent.VK_ESCAPE && (gameStatus == 2 || gameStatus == 3 ))
		{
			gameStatus = 0;
			selectDifficulty = false;
			player1.score = 0;
			player2.score = 0;
		}
		else if(id == KeyEvent.VK_SHIFT && gameStatus == 0)
		{
			
			selectDifficulty = true;
		}
		
		else if(id == KeyEvent.VK_P && gameStatus == 0)
		{
			bot = false;
			botTeaching = true;
			gameStatus = 2;
			ObjectInputStream in;
			try {
				in = new ObjectInputStream(new FileInputStream("Q.ser"));
				Q = (double[][]) in.readObject();
				in.close();
			} catch (IOException | ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			start();
			
		}
		
		else if (id == KeyEvent.VK_T && gameStatus == 0)
		{
			botTeaching = true;
			selectDifficulty = true;
			scorelimit=20;
			wins=0;
			
		}
		
		
		else if(id == KeyEvent.VK_SPACE )
		{
			if( gameStatus == 0)
			{
				
				if(selectDifficulty)
				{
					bot = true;
				}
				else
				{
					bot = false;
					selectDifficulty = false;
				}
			    start();
				 
			    
			}
			else if( gameStatus == 1)
			{
				gameStatus = 2;
			}
			else if( gameStatus == 2)
			{
				gameStatus = 1;
			}
			else if( gameStatus == 3)
			{
				player1.score = 0;
				player1.y = this.height /2 - player1.height /2  ;
				player2.score = 0;
				player2.y=this.height /2 - player2.height /2 ;
				gameStatus = 2;
				
			}
			else if(id == KeyEvent.VK_R && gameStatus == 1)
			{
				gameStatus = 2 ;
				ball.spawn();
				
				
			}
			else if(id == KeyEvent.VK_S && gameStatus == 1)
			{
				ObjectOutputStream out;
				try {
					out = new ObjectOutputStream(new FileOutputStream("Q.ser"));
					out.writeObject(Q);
					out.flush();
					out.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
								
			}
			else if(id == KeyEvent.VK_L && gameStatus == 1)
			{
				ObjectInputStream in;
				try {
					in = new ObjectInputStream(new FileInputStream("Q.ser"));
					Q = (double[][]) in.readObject();
					in.close();
				} catch (IOException | ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(id == KeyEvent.VK_ESCAPE && gameStatus == 1)
			{
				gameStatus=0;
				wins=0;
				
			}
		}
		
		
		
	}


	@Override
	public void keyReleased(KeyEvent e)
	{
		int id= e.getKeyCode();
		
		if(id == KeyEvent.VK_W )
		{
			w = false;
		}
		else if(id == KeyEvent.VK_S )
		{
			s = false;
		}
		else if(id == KeyEvent.VK_UP )
		{
			up = false;
		}
		else if(id == KeyEvent.VK_DOWN )
		{
			down = false;
		}
		
		
		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}





}

package code;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable,KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//FIELDS
	public static int WIDTH=800,HEIGHT=600;
	private Thread thread;
	private boolean running;
	private BufferedImage image;
	private Graphics2D g;
	private int FPS=30;

	public static Player player;
	public static ArrayList<Bullet>bullets;
	public static ArrayList<Enemy>enemies;
	public static ArrayList<PowerUp>powerUps;
	public static ArrayList<Explosion>explosions;
	public static ArrayList<Text>texts;

	private long waveStartTimer,waveStartTimerDiff;
	private int waveNumber;
	private boolean waveStart;
	private int waveDelay=2000;
	private ClassLoader loader= GamePanel.class.getClassLoader();
	private Image backgroundImage;
	private long slowDownTimer,slowDownTimerDiff;
	private int slowDownLength=6000;
	
	//COnstructor
	public GamePanel() throws IOException{
		super();
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setFocusable(true);
		requestFocus();
		 backgroundImage = ImageIO.read(loader.getResource("Background.png"));
	}
	
	//FUNCTIONS
	public synchronized void addNotify(){
		super.addNotify();
		if(thread==null){
			thread=new Thread(this);
			thread.start();
		}
		addKeyListener(this);
	}
	public void run(){
		running=true;
		
		image=new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g=(Graphics2D)image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		
		long startTime,URDTimeMillis,waitTime,targetTime=1000/FPS;
		int frameCount=0;
		int maxFrameCount=30;
		player=new Player();
		bullets=new ArrayList<Bullet>();
		enemies=new ArrayList<Enemy>();

		powerUps=new ArrayList<PowerUp>();
		explosions=new ArrayList<Explosion>();
		texts=new ArrayList<Text>();
		
		waveStartTimer=0;
		waveStartTimerDiff=0;
		waveStart=true;
		waveNumber=0;
		
		
		
		//Game loop
		while(running){
			
			startTime=System.nanoTime();
			
			gameUpdate();
			try {
				gameRender();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			gameDraw();
			
			URDTimeMillis=(System.nanoTime()-startTime)/1000000;
			
			waitTime=targetTime-URDTimeMillis;
			try{
				Thread.sleep(waitTime);
			}catch(Exception e){
				//Do nothing
			}
			
			frameCount++;
			if(frameCount==maxFrameCount){
				frameCount=0;
			}
			
		}
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.RED);
		g.setFont(new Font("Tahoma",Font.PLAIN,20));
		String gameOver="G A M E 	O V E R";
		int length=(int)g.getFontMetrics().getStringBounds(gameOver,g).getWidth();
		String score = "SCORE: "+player.getScore();
		g.drawString(gameOver, (WIDTH-length)/2, HEIGHT/2);
		length=(int)g.getFontMetrics().getStringBounds(score,g).getWidth();
		g.drawString(score, (WIDTH-length)/2, HEIGHT/2+50);
		gameDraw();
		
	}
	private void gameUpdate(){
		//new wave
		if(waveStartTimer==0&&enemies.size()==0){
			waveNumber++;
			waveStart=false;
			waveStartTimer=System.nanoTime();
			
		}else{
			waveStartTimerDiff=(System.nanoTime()-waveStartTimer)/1000000;
			if(waveStartTimerDiff>waveDelay){
				waveStart=true;
				waveStartTimer=0;
				waveStartTimerDiff=0;
			}
		}
		//create enemies
		if(waveStart&&enemies.size()==0){
			createNewEnemies();
		}
		
		
		
		
		//player Update
		player.update();
		
		//Bullet update
		for(int i=0;i<bullets.size();i++){
			boolean remove=bullets.get(i).update();
			if(remove){
				bullets.remove(i);
				i--;
			}
		}
		//Enemy update
		for(int i=0;i<enemies.size();i++){
			enemies.get(i).update();
		}
	
		//PowerUp update
		for(int i=0;i<powerUps.size();i++){
		boolean remove=	powerUps.get(i).update();
			if(remove){
				powerUps.remove(i);
				i--;
			}
		}
		//Explosion update
		for(int i=0;i<explosions.size();i++){
			boolean remove=explosions.get(i).update();
			if(remove){
				explosions.remove(i);
				i--;
			}
		}
		//Text Update
			for(int i=0;i<texts.size();i++){
				boolean remove= texts.get(i).update();
				if(remove){
					texts.remove(i);
					i--;
				}
			}
		
		//Bullet - Enemy Collision
		for(int i=0;i<bullets.size();i++){
			Bullet b=bullets.get(i);
			double bX=b.getX();
			double bY=b.getY();
			double bR=b.getR();
			for(int j=0;j<enemies.size();j++){
				Enemy e = enemies.get(j);
				double eX=e.getX();
				double eY=e.getY();
				double eR=e.getR();
				
				double dx=bX-eX;
				double dy=bY-eY;
				double distance=Math.sqrt(dx*dx+dy*dy);
				if(distance<bR+eR){
					e.hit();
					bullets.remove(i);
					i--;
					break;
					
				}
			}
		}
		
		//check dead enemies
		for(int i=0;i<enemies.size();i++){
			if(enemies.get(i).isDead()){
				Enemy e = enemies.get(i);
				//  powerUp chance
				double rand=Math.random()*100;
			if(rand<5)powerUps.add(new PowerUp(1,e.getX(),e.getY()));
				else if(rand<10)powerUps.add(new PowerUp(3,e.getX(),e.getY()));
				else if(rand<15)powerUps.add(new PowerUp(2,e.getX(),e.getY()));
				else if(rand<20)powerUps.add(new PowerUp(4,e.getX(),e.getY()));
				
				//add score to player
				player.addScore(e.getScore());
				enemies.remove(i);
				i--;
				e.explode();
				explosions.add(new Explosion(e.getX(),e.getY()));
			}
		}
	
	/*	
		for(int i=0;i<turrets.size();i++){
			if(turrets.get(i).isDead()){
				Turret e = turrets.get(i);
				//add score to player
				player.addScore(e.getScore());
				turrets.remove(i);
				i--;
				e.explode();
				explosions.add(new Explosion(e.getX(),e.getY(),e.getR(),e.getR()+30));
			}
		}
		*/
		
		
		
		//check Player - enemy Collision
		if(!player.isRecovering()){
			int px=player.getX();
			int py=player.getY();
			int pr=player.getR();
			for(int i=0;i<enemies.size();i++){
				Enemy e=enemies.get(i);
				double ex=e.getX();
				double ey=e.getY();
				double er=e.getR();
				
				double dx=px-ex;
				double dy=py-ey;
				double distance=Math.sqrt(dx*dx+dy*dy);
				if(distance<pr+er){
					player.loseLife();
					
				}
				
				
			}

		}
		//check dead player
		if(player.isDead()){
			running=false;
		}
		//Player-powerUp collision
		int px= player.getX();
		int py= player.getY();
		int pr= player.getR();
		for(int i=0;i<powerUps.size();i++){
			PowerUp p = powerUps.get(i);
			double x=p.getX();
			double y=p.getY();
			double r=p.getR();
			double dx=px-x;
			double dy=py-y;
			double distance =Math.sqrt(dx*dx+dy*dy);
			//collected powerUps
			if(distance<pr+r){
				int type=p.getType();
				if(type==1){
					player.gainLife();
					texts.add(new Text(player.getX(),player.getY(),2000,"Life Up"));
				}
				if(type==2){
					player.increasePower(1);
					texts.add(new Text(player.getX(),player.getY(),2000,"bullets"));
				}
				if(type==3){
					player.increasePower(2);
					texts.add(new Text(player.getX(),player.getY(),2000,"bullets"));
				}
				if(type==4){
				slowDownTimer=System.nanoTime();
				for(int j=0;j<enemies.size();j++ ){
					enemies.get(j).setSlow(true);
					}
	
				texts.add(new Text(player.getX(),player.getY(),2000,"Slow Down"));
				}
				player.addScore(powerUps.get(i).getScore());
				powerUps.remove(i);
				i--;
			}
			
		}
		if(slowDownTimer!=0){
			slowDownTimerDiff=(System.nanoTime()-slowDownTimer)/1000000;
			if(slowDownTimerDiff>slowDownLength){
				slowDownTimer=0; 
				for(int j=0;j<enemies.size();j++ ){
					enemies.get(j).setSlow(false);
					}
			
			}
		}
		
		
	}


	private void gameRender() throws IOException {
	
		

		
		//Draw BackGround
		if(waveNumber%3==0)this.setBackgroundImage("Background3.png");
		else if(waveNumber%3==2)this.setBackgroundImage("Background2.png");
		else this.setBackgroundImage("Background.png");
		g.drawImage(backgroundImage, 0, 0, null);

		
		//Draw Player
		player.draw(g);
		
		
		//draw Bullet
		for(int i=0;i<bullets.size();i++){
			bullets.get(i).draw(g);
		}
		//draw SlowDown screen 
		if(slowDownTimer!=0){
		g.setColor(new Color(255,255,255,64));
		g.fillRect(0, 0, WIDTH, HEIGHT);  
		}
		
		
		//Draw enemy
		for(int i=0;i<enemies.size();i++){
			enemies.get(i).draw(g);
		}

	
		//Draw PowerUp
		for(int i=0;i<powerUps.size();i++){
			powerUps.get(i).draw(g);
		}
		
		//Draw wave number
		if(waveStartTimer!=0){
			g.setFont(new Font("Century Gothic",Font.PLAIN,40));
			String s ="- S T A G E "+waveNumber+" -";
			String s2 ="PRESS 'Z' TO SHOOT";
			int length=(int)g.getFontMetrics().getStringBounds(s, g).getWidth();
			int alpha=(int)(255*Math.sin(3.141592*waveStartTimerDiff/waveDelay));
			if(alpha>255)alpha=255;
			g.setColor(new Color(255,255,255,alpha));
			g.drawString(s, WIDTH/2-length/2, HEIGHT/2-100);
			g.setColor(Color.ORANGE);
			g.drawString(s2, WIDTH/2-length+60, HEIGHT/2);
		}
		//Draw Player lives
		for(int i=0;i<player.getLives();i++){
			g.drawImage(Resource.heart,20+(20*i),20 ,null );
		
		}
		//Draw player score
		g.setColor(Color.WHITE);
		g.setFont(new Font("Century Gothic",Font.PLAIN,30));
		g.drawString("Score: "+player.getScore(), WIDTH-200, 50);
		
		// Draw Player power
			g.setColor(Color.YELLOW);
			g.fillRect(20, 60, player.getPower()*10, 10);//size of Power block is 10*10
			g.setColor(Color.YELLOW.darker());
			g.setStroke(new BasicStroke(2));
			for(int i=0;i<player.getRequiredPower();i++){
				g.drawRect(20+10*i, 60,10,10);
			}
			g.setStroke(new BasicStroke(1));
			//Draw Explosion
			for(int i=0;i<explosions.size();i++){
				explosions.get(i).draw(g);
				
			}
			//Draw slowdown meter
			 if(slowDownTimer!=0){
				 g.setColor(Color.WHITE);
				 g.drawRect(20, 60,100, 8);
				 g.fillRect(20, 60,(int) (100-100.0*slowDownTimerDiff/slowDownLength), 8);
			 }
	//Draw Text
		for(int i=0;i<texts.size();i++){
			texts.get(i).draw(g);
			
		}
	}
	
	
	
	private void gameDraw(){
		Graphics g2=this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		
		
	}
	
	private void createNewEnemies() {
		enemies.clear();
		if(waveNumber == 1) {
	        for(int i = 0; i < 4*waveNumber; i++) {
	          enemies.add(new Enemy(1, 1));
	        }
	      }
	      if(waveNumber == 2) {
	        for(int i = 0; i < 8; i++) {
	          enemies.add(new Enemy(1, 1));
	        }
	      }
	      if(waveNumber == 3) {
	          for(int i = 0; i < 4*waveNumber; i++) {
		          enemies.add(new Enemy(1, 1));
		        }
	        enemies.add(new Enemy(1, 2));
	        enemies.add(new Enemy(1, 2));
	      }
	      if(waveNumber == 4) {
	        enemies.add(new Enemy(1, 3));
	        enemies.add(new Enemy(1, 4));
	        for(int i = 0; i < 4; i++) {
	          enemies.add(new Enemy(2, 1));
	        }
	      }
	      if(waveNumber == 5) {
	          for(int i = 0; i < 3*waveNumber; i++) {
		          enemies.add(new Enemy(1, 1));
		        }
	        enemies.add(new Enemy(1, 4));
	        enemies.add(new Enemy(1, 3));
	        enemies.add(new Enemy(2, 3));
	      }
	      if(waveNumber == 6) {
	        enemies.add(new Enemy(1, 3));
	        for(int i = 0; i < 4; i++) {
	          enemies.add(new Enemy(2, 1));
	          enemies.add(new Enemy(3, 1));
	        }
	      }
	      if(waveNumber == 7) {
	          for(int i = 0; i < 3*waveNumber; i++) {
		          enemies.add(new Enemy(1, 1));
		        }
	        enemies.add(new Enemy(1, 3));
	        enemies.add(new Enemy(2, 3));
	        enemies.add(new Enemy(3, 3));
	      }
	      if(waveNumber == 8) {
	          for(int i = 0; i < 3*waveNumber; i++) {
		          enemies.add(new Enemy(1, 1));
		        }
	        enemies.add(new Enemy(1, 4));
	        enemies.add(new Enemy(2, 3));
	        enemies.add(new Enemy(3, 4));
	      }
	      if(waveNumber == 9) {
	          for(int i = 0; i < 3*waveNumber; i++) {
		          enemies.add(new Enemy(1, 1));
		        }
	        enemies.add(new Enemy(2, 3));
	        enemies.add(new Enemy(2, 3));
	        enemies.add(new Enemy(3, 4));
	        enemies.add(new Enemy(3, 4));
	        
	      }
	      if(waveNumber == 10) {
	          for(int i = 0; i < 3*waveNumber; i++) {
		          enemies.add(new Enemy(1, 1));
		        }
	        enemies.add(new Enemy(2, 3));
	        enemies.add(new Enemy(2, 3));
	        enemies.add(new Enemy(3, 4));
	        enemies.add(new Enemy(3, 4));
	        enemies.add(new Enemy(3, 4));
	        enemies.add(new Enemy(3, 4));
	      }
	      if(waveNumber == 11) {
	    	  running=false;
	      }
	}	
	
//KEY LISTENER
	@Override
	public void keyPressed(KeyEvent key) {
		int keyCode=key.getKeyCode();
		if(keyCode==KeyEvent.VK_LEFT){
			player.setLeft(true);
		}
		if(keyCode==KeyEvent.VK_RIGHT){
			player.setRight(true);
		}
		if(keyCode==KeyEvent.VK_UP){
			player.setUp(true);
		}
		if(keyCode==KeyEvent.VK_DOWN){
			player.setDown (true);
		}
		if(keyCode==KeyEvent.VK_Z){
			player.setFiring(true);
		}
		if(keyCode==KeyEvent.VK_X&&player.getPower()>=4&&player.getPowerLevel()>=4){
			player.setUltimate(true);
		}
	}

	@Override
	public void keyReleased(KeyEvent key) {
		int keyCode=key.getKeyCode();
		if(keyCode==KeyEvent.VK_LEFT){
			player.setLeft(false);
		}
		if(keyCode==KeyEvent.VK_RIGHT){
			player.setRight(false);
		}
		if(keyCode==KeyEvent.VK_UP){
			player.setUp(false);
		}
		if(keyCode==KeyEvent.VK_DOWN){
			player.setDown(false);
		}
		if(keyCode==KeyEvent.VK_Z){
			player.setFiring(false);
		}
		if(keyCode==KeyEvent.VK_X){
			player.setUltimate(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent key) {
		// TODO Auto-generated method stub
		}

	public void setBackgroundImage(String str) throws IOException {
		this.backgroundImage = ImageIO.read(loader.getResource(str));
		
	}
	
}
 
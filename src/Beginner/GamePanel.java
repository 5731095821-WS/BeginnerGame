package Beginner;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable,KeyListener {
	//FIELDS
	public static int WIDTH=800,HEIGHT=600;
	private Thread thread;
	private boolean running;
	private BufferedImage image;
	private Graphics2D g;
	private int FPS=30;
	private double averageFPS;
	public static Player player;
	public static ArrayList<Bullet>bullets;
	public static ArrayList<Enemy>enemies;
	public static ArrayList<PowerUp>powerUps;
	private long waveStartTimer,waveStartTimerDiff;
	private int waveNumber;
	private boolean waveStart;
	private int waveDelay=2000;
	private ClassLoader loader= GamePanel.class.getClassLoader();
	private Image backgroundImage;

	
	
	//COnstructor
	public GamePanel() throws IOException{
		super();
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setFocusable(true);
		requestFocus();
		 backgroundImage = ImageIO.read(loader.getResource("Background.png"));
	}
	
	//FUNCTIONS
	public void addNotify(){
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
		
		
		long startTime,URDTimeMillis,waitTime,totalTime=0,targetTime=1000/FPS;
		int frameCount=0;
		int maxFrameCount=30;
		player=new Player();
		bullets=new ArrayList<Bullet>();
		enemies=new ArrayList<Enemy>();
		powerUps=new ArrayList<PowerUp>();
		
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
			
			totalTime+=System.nanoTime()-startTime;
			frameCount++;
			if(frameCount==maxFrameCount){
				averageFPS=1000.0/((totalTime/frameCount)/1000000);
				frameCount=0;
				totalTime=0;
			}
			
		}
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
				double rand=Math.random();
				if(rand<0.3)powerUps.add(new PowerUp(1,e.getX(),e.getY()));
				else if(rand<0.5)powerUps.add(new PowerUp(3,e.getX(),e.getY()));
				else if(rand<1)powerUps.add(new PowerUp(2,e.getX(),e.getY()));
				
				
				//add score to player
				player.addScore(e.getScore());
				enemies.remove(i);
				i--;
				e.explode();
			}
		}
		
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
			//collected
			if(distance<pr+r){
				int type=p.getType();
				if(type==1){
					player.gainLife();
				}
				if(type==2){
					player.increasePower(1);
				}
				if(type==3){
					player.increasePower(2);
				}
				
				powerUps.remove(i);
				i--;
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
			String s ="- S T A G E "+waveNumber+" - ";
			int length=(int)g.getFontMetrics().getStringBounds(s, g).getWidth();
			int alpha=(int)(255*Math.sin(3.141592*waveStartTimerDiff/waveDelay));
			if(alpha>255)alpha=255;
			g.setColor(new Color(255,255,255,alpha));
			g.drawString(s, WIDTH/2-length/2, HEIGHT/2-100);
		}
		//Draw Player lives
		for(int i=0;i<player.getLives();i++){
			g.setColor(Color.WHITE);
			g.fillOval(20+(20*i),20 , player.getR()*3, player.getR()*3);
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.WHITE.darker());
			g.drawOval(20+(20*i),20 , player.getR()*3, player.getR()*3);
			g.setStroke(new BasicStroke(1));
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
	}
	
	
	
	private void gameDraw(){
		Graphics g2=this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		
		
	}
	
	private void createNewEnemies() {
		enemies.clear();
		Enemy e;
		if(waveNumber==1){ 
			for(int i=0;i<4;i++){
				enemies.add(new Enemy(1,1));
			}
		}
		if(waveNumber==2){ 
			for(int i=0;i<8;i++){
				enemies.add(new Enemy(1,1));
				
			}
			enemies.add(new Enemy(1,2));
			enemies.add(new Enemy(1,2));
		}
		if(waveNumber==3){ 
			for(int i=0;i<4;i++){
				enemies.add(new Enemy(2,1));//Type 2 enemies
			}
			for(int i=0;i<4;i++){
				enemies.add(new Enemy(3,1));//Type 3 enemies
			}
			enemies.add(new Enemy(1,3));
			enemies.add(new Enemy(1,3));
			enemies.add(new Enemy(1,4));
			enemies.add(new Enemy(1,4));
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
	}

	@Override
	public void keyTyped(KeyEvent key) {
		// TODO Auto-generated method stub
		}

	public void setBackgroundImage(String str) throws IOException {
		this.backgroundImage = ImageIO.read(loader.getResource(str));
		
	}
	
}
 
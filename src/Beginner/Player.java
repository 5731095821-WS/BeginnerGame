package Beginner;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Player {
//fields
	private int x,y,r,width,height;
	private int dx,dy,speed;
	private int lives,maxLives;
	private boolean left,right,up,down,firing,ultimate;
	private long firingTimer,firingDelay;
	private boolean recovering;
	private long recoveryTimer;
	private int score;
	private int powerLevel,power;
	private int[] requiredPower={1,2,3,4,5};
	
	private Animation animation;
	private BufferedImage[] idleSprites;
	private BufferedImage[] walkingSprites;
	private BufferedImage[] hurtingSprites;
	ClassLoader loader=Player.class.getClassLoader();
	
	
	//constructor
	public Player(){
		x=GamePanel.WIDTH/2;
		y=GamePanel.HEIGHT/2;
		r=10;
		dx=0;dy=0;
		speed=7;
		lives=3;
		maxLives=10;
		firing=false;
		firingTimer=System.nanoTime();
		firingDelay=200;
		score=0;
		width=64;
		height=64;
		
		recovering=false;
		recoveryTimer=0;
		
	try {
			
			idleSprites = new BufferedImage[1];
			idleSprites[0] = ImageIO.read(loader.getResource("player/doctor_stand_0001.png"));
			walkingSprites = new BufferedImage[4];
			hurtingSprites= new BufferedImage[2];
			//BufferedImage image = ImageIO.read(loader.getResource("graphics/player/kirbywalk.gif"));
			for(int i = 0; i < walkingSprites.length; i++) {
				walkingSprites[i] = ImageIO.read(loader.getResource("player/doctor_move_0"+(i+1)+".png"));
				
			}
			for(int i = 0; i < hurtingSprites.length; i++) {
				hurtingSprites[i] = ImageIO.read(loader.getResource("player/doctor_summon_0"+(i+1)+".png"));
				
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
			}
	//functions
public void update(){
	if(left)dx-=speed;
	if(right)dx+=speed;
	if(up){
		dy-=speed;
	
	}
	if(down){
		dy+=speed;
	}
	x+=dx;
	y+=dy;
	if(x<r)x=r;
	if(y<r)y=r;
	if(x>GamePanel.WIDTH-r)x=GamePanel.WIDTH-r;
	if(y>GamePanel.HEIGHT-r)y=GamePanel.HEIGHT-r; 
	dx=0;
	dy=0; 
	
	//firing
	if(firing){
		 long elapsed=(System.nanoTime()-firingTimer)/1000000;
		 
		 
		 
		 if(elapsed>firingDelay){
			firingTimer=System.nanoTime();
			if(powerLevel<2){
				 GamePanel.bullets.add(new Bullet(270,x,y));
			}
			else if(powerLevel<4){
				 GamePanel.bullets.add(new Bullet(270,x+5,y));
				 GamePanel.bullets.add(new Bullet(270,x-5,y));
			}
			else {
				 GamePanel.bullets.add(new Bullet(270,x,y));
				 GamePanel.bullets.add(new Bullet(275,x+5,y));
				 GamePanel.bullets.add(new Bullet(265,x-5,y));
				
			}
		 }
	}
	//Sprites
	if(up||down){
		animation.setFrames(walkingSprites);
		animation.setDelay(100);
	}else{
		animation.setFrames(idleSprites);
		animation.setDelay(-1);
	}
	animation.update();
	//recovering
	long elapsed=(System.nanoTime()-recoveryTimer)/1000000;
	if(elapsed>2000){
		recovering=false;
		recoveryTimer=0;
	}
}
public void loseLife(){
	lives--;
	decreasePower();
	recovering=true;
	recoveryTimer=System.nanoTime();
	 
}
public void draw(Graphics2D g){
	if(recovering){
		animation.setFrames(hurtingSprites);
		animation.setDelay(100);
		g.drawImage(animation.getImage(),x-width/2,y-height/2,null);
	}
	else{
	g.drawImage(animation.getImage(),x-width/2,y-height/2,null);
			}
}
public void setLeft(boolean left) {
	this.left = left;
}
public void setRight(boolean right) {
	this.right = right;
}
public void setUp(boolean up) {
	this.up = up;
}
public void setDown(boolean down) {
	this.down = down;
}
public void setUltimate(boolean ultimate) {
	this.ultimate = ultimate;
}
public void setFiring(boolean b) {
	 firing=b;
	
}
public int getLives(){
	return lives;
}
public int getX() {
	return x;
}
public int getY() {
	return y;
}
public int getR() {
	return r;
}
public boolean isRecovering() {
	return recovering;
}
public int getScore(){
	return score;
}
public void addScore(int i){
	score+=i;
}
public void gainLife() {
	lives++;
	if(lives>maxLives)lives=maxLives;//Lives is limited to maxLives
}
public int getPowerLevel(){
	return powerLevel;
}
public int getPower(){
	return power;
}
public int getRequiredPower(){
	return requiredPower[powerLevel];
}
public void increasePower(int i){
    power += i;
    if(powerLevel == 4) {
      if(power > requiredPower[powerLevel]) {
        power = requiredPower[powerLevel];
      }
      return;
    }
    if(power >= requiredPower[powerLevel]) {
      power -= requiredPower[powerLevel];
      powerLevel++;
    }
}
public void decreasePower(){
    powerLevel--;
    power=0;
    if(powerLevel<=0)powerLevel=0;
}
public void ultimate(){
	if(power>=4||powerLevel>=4){
		powerLevel=0;
		power=0;
	}
}
public boolean isDead() {
	return lives<=0;
}
}

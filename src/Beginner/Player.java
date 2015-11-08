package Beginner;
import java.awt.*;


public class Player {
//fields
	private int x,y,r;
	private int dx,dy,speed;
	private int lives;
	private boolean left,right,up,down,firing;
	private Color color1,color2;
	private long firingTimer,firingDelay;
	private boolean recovering;
	private long recoveryTimer;
	private int score;
	//constructor
	public Player(){
		x=GamePanel.WIDTH/2;
		y=GamePanel.HEIGHT/2;
		r=10;
		dx=0;dy=0;
		speed=7;
		lives=3;
		color1=Color.WHITE;
		color2=Color.RED;
		firing=false;
		firingTimer=System.nanoTime();
		firingDelay=200;
		score=0;
		
		
		recovering=false;
		recoveryTimer=0;
		
		
			}
	//functions
public void update(){
	if(left)dx-=speed;
	if(right)dx+=speed;
	if(up)dy-=speed;
	if(down)dy+=speed;
	x+=dx;
	y+=dy;
	if(x<r)x=r;
	if(y<r)y=r;
	if(x>GamePanel.WIDTH-r)x=GamePanel.WIDTH-r;
	if(y>GamePanel.HEIGHT-r)y=GamePanel.HEIGHT-r; 
	dx=0;
	dy=0; 
	if(firing){
		 long elapsed=(System.nanoTime()-firingTimer)/1000000;
		 if(elapsed>firingDelay){
			 GamePanel.bullets.add(new Bullet(270,x,y));
			 firingTimer=System.nanoTime();
		 }
	}
	long elapsed=(System.nanoTime()-recoveryTimer)/1000000;
	if(elapsed>2000){
		recovering=false;
		recoveryTimer=0;
	}
}
public void loseLife(){
	lives--;
	recovering=true;
	recoveryTimer=System.nanoTime();
	
}
public void draw(Graphics2D g){
	if(recovering){
		g.setColor(color2);
		g.fillOval(x-r, y-r, 2*r, 2*r);
		g.setStroke(new BasicStroke(3));
		g.setColor(color2.darker());
		g.drawOval(x-r, y-r, 2*r, 2*r);
		g.setStroke(new BasicStroke(1));
	}
	else{
	g.setColor(color1);
	g.fillOval(x-r, y-r, 2*r, 2*r);
	g.setStroke(new BasicStroke(3));
	g.setColor(color1.darker());
	g.drawOval(x-r, y-r, 2*r, 2*r);
	g.setStroke(new BasicStroke(1));
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
}

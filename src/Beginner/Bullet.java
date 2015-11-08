package Beginner;

import java.awt.*;

public class Bullet {
	//Fields
	private double x,y;
	private int r;
	private double rad;
	private double speed;
	private double dx,dy;
	private Color color1;
	public Bullet(double angle,int x,int y){
		this.x=x;
		this.y=y;
		r=3;
		speed=15;
		rad=Math.toRadians(angle);
		dx=Math.cos(rad)*speed;
		dy=Math.sin(rad)*speed;
		
		color1=Color.YELLOW;
		
				
	}
	//Functions
	public boolean update(){
		this.x+=dx;
		this.y+=dy;
		if(x<-r||x>GamePanel.WIDTH+r||y<-r||y>GamePanel.HEIGHT+r)return true;
		
		return false;
	}
	public void draw(Graphics2D g){
		g.setColor(color1);
		g.fillOval((int)(x-r), (int)(y-r), 2*r, 2*r);
	}
	
	public double getX(){return x;}
	public double getY(){return y;}
	public double getR(){return r;}
}

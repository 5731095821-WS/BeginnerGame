package Beginner;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Turret {
   //FIELDS
	private double x,y,dx,dy,rad,speed;
	private int r;
	private int health,type,rank;

	private Color color1;
	private boolean ready,dead;
	private boolean hit;
	private long hitTimer;
	private boolean slow;
	//sprites
	private Animation animation;
	private BufferedImage[] idleSprites;
	private BufferedImage[] walkingSprites;
	private BufferedImage[] hurtingSprites;
	ClassLoader loader=Player.class.getClassLoader();
	//
	public void setSlow(boolean slow) {
		this.slow = slow;
	}

	//Constructor	
	public Turret(int type) {
		super();
		this.type=type;
		if(type==1){
			
		}
		if(type==2){
			
		}
		if(type==3){
			
		}
		
					
		hit=false;
		hitTimer=0;
		
		
		x=Math.random()*GamePanel.WIDTH/2+GamePanel.WIDTH/4;
		y=-r;
		double angle= Math.random()*140+20;
		rad=Math.toRadians(angle);
		dx=Math.cos(rad)*speed;
		dy=Math.sin(rad)*speed;
		ready=false;
		dead=false;
		
		
	}
	
//Functions
		//Getters Setters
		public double getX(){return x;}
		public double getY(){return y;}
		public int getR(){return r;}
		public boolean isDead(){return dead;}
		public int getType() {
			return type;
		}

		public int getRank() {
			return rank;
		}

		public int getScore() {
			return type+rank;
		}
		
		
		
		
	public void hit(){
		health--;
		if(health<0)dead=true;
		hit=true;
		hitTimer=System.nanoTime();
	}
	
	public void explode(){
		if(rank>1){
			int amount=0;
			if(type==1){
				amount=3;
			}
			if(type==2){
				amount=3;
			}
			if(type==3){
				amount=3;
			}
			if(type==4){
				amount=4;
			}
			for(int i=0;i<amount;i++){
				Turret t= new Turret(getType());
				t.setSlow(slow);
				t.x=this.x;t.y=this.y;
				double angle=0;
				if(ready){
					angle=Math.random()*140+20;
				}
				else{
					angle =Math.random()*360;
				}
				t.rad=Math.toRadians(angle);
				GamePanel.turrets.add(t);
				
			}
		}
	}
	public void update(){
		if(!ready){
			if(x>r&& x<GamePanel.WIDTH-r&&y>r&&y<GamePanel.HEIGHT-r){
				ready=true;
				}
		}
		if(x<r&&dx<0)dx=-dx;
		if(y<r&&dy<0)dy=-dy;
		if(x>GamePanel.WIDTH-r&&dx>0)dx=-dx;
		if(y>GamePanel.HEIGHT-r&&dy>0)dy=-dy;
		
		
		if(hit){
			long elapsed=(System.nanoTime()-hitTimer)/1000000;
			if(elapsed>50){
				hit=false;
				hitTimer=0;
			}
		}
	}
	public void draw(Graphics2D g){
		if(hit){
			g.setPaint(Color.WHITE);
			g.fillOval((int)x-r, (int)y-r, 2*r, 2*r);
		
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.WHITE.darker());
			g.fillOval((int)x-r, (int)y-r, 2*r, 2*r);
			g.setStroke(new BasicStroke(1));
		}
		else{
		g.setPaint(color1);
		g.fillOval((int)x-r, (int)y-r, 2*r, 2*r);
	
		g.setStroke(new BasicStroke(3));
		g.setColor(color1.darker());
		g.fillOval((int)x-r, (int)y-r, 2*r, 2*r);
		g.setStroke(new BasicStroke(1));
		}
	}

}
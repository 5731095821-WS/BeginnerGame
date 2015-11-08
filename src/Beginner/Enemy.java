package Beginner;

import java.awt.*;

public class Enemy {
   //FIELDS
	private double x,y,dx,dy,rad,speed;
	private int r;
	private int health,type,rank,score;

	private Color color1;
	private boolean ready,dead;
	
	
	
	//Constructor	
	public Enemy(int type,int rank) {
		super();
		this.type=type;
		this.rank=rank;
		//default Enemy
		if(type==1){
			color1=Color.BLUE;
			if(rank==1){
				speed=3;
				r=8;
				health=1;
			}
		}
		//faster Default
			if(type==2){
				color1=Color.RED;
				if(rank==1){
					speed=4;
					r=8;
					health=2;
				}
			}
		//slow but hard to kill
			if(type==3){
				color1=Color.GREEN;
				if(rank==1){
					speed=2;
					r=10;
					health=5;
				}
			}
		
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
		public double getR(){return r;}
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
	}
	
	
	public void update(){
		x+=dx;
		y+=dy;
		if(!ready){
			if(x>r&& x<GamePanel.WIDTH-r&&y>r&&y<GamePanel.HEIGHT-r){
				ready=true;
				}
		}
		if(x<r&&dx<0)dx=-dx;
		if(y<r&&dy<0)dy=-dy;
		if(x>GamePanel.WIDTH-r&&dx>0)dx=-dx;
		if(y>GamePanel.HEIGHT-r&&dy>0)dy=-dy;
	}
	public void draw(Graphics2D g){
		g.setPaint(color1);
		g.fillOval((int)x-r, (int)y-r, 2*r, 2*r);
	
		g.setStroke(new BasicStroke(3));
		g.setColor(color1.darker());
		g.fillOval((int)x-r, (int)y-r, 2*r, 2*r);
		g.setStroke(new BasicStroke(1));
	}

}
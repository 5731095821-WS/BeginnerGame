package Beginner;

import java.awt.*;

public class Enemy {
   //FIELDS
	private double x,y,dx,dy,rad,speed;
	private int r;
	private int health,type,rank,score;

	private Color color1;
	private boolean ready,dead;
	private boolean hit;
	private long hitTimer;
	private boolean slow;
	
	public void setSlow(boolean slow) {
		this.slow = slow;
	}

	//Constructor	
	public Enemy(int type,int rank) {
		super();
		this.type=type;
		this.rank=rank;
		//default Enemy
		if(type==1){
		//	color1=Color.BLUE;
			color1=new Color(0,0,255,128);
			if(rank==1){
				speed=3;
				r=8;
				health=1;
			}
			if(rank==2){
				speed=4;
				r=10;
				health=2;
			}
			if(rank==3){
				speed=5;
				r=20;
				health=3;
			}
			if(rank==4){
				speed=5;
				r=30;
				health=4;
			}
		}
		//faster Default
			if(type==2){
				//color1=Color.RED;
				color1=new Color(255,0,0,128);
				if(rank==1){
					speed=4;
					r=8;
					health=2;
				}
				if(rank==2){
					speed=4+this.rank;
					r=8+this.rank;
					health=2+this.rank;
				}
				if(rank==3){
					speed=4;
					r=20;
					health=2+this.rank;
				}
				if(rank==3){
					speed=4;
					r=30;
					health=2+this.rank;
				}
			}
		//slow but hard to kill
			if(type==3){
				//color1=Color.GREEN;
				color1=new Color(0,255,0,128);
				if(rank==1){
					speed=2;
					r=7;
					health=5;
				}
				if(rank==2){
					speed=2;
					r=10;
					health=6;
				}
				if(rank==3){
					speed=2;
					r=25;
					health=7;
				}
				if(rank==4){
					speed=2;
					r=45;
					health=10;
				}
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
				Enemy e= new Enemy(getType(),getRank()-1);
				e.setSlow(slow);
				e.x=this.x;e.y=this.y;
				double angle=0;
				if(ready){
					angle=Math.random()*140+20;
				}
				else{
					angle =Math.random()*360;
				}
				e.rad=Math.toRadians(angle);
				GamePanel.enemies.add(e);
				
			}
		}
	}
	public void update(){
		if(slow){
			x+=dx*0.3;
			y+=dy*0.3;
		}
		else{
		x+=dx;
		y+=dy;
		}
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
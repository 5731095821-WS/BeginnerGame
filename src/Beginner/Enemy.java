package Beginner;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.sun.org.apache.regexp.internal.RESyntaxException;

public class Enemy {
   //FIELDS
	private double x,y,dx,dy,rad,speed;
	private int r;
	private int health,type,rank;
	private int width,height;
	private Color color1;
	private boolean ready,dead;
	private boolean hit;
	private long hitTimer;
	private boolean slow;
	//sprites
	private Animation animation;
	private BufferedImage[] walkingSprites;
	private BufferedImage[] hurtingSprites;
	ClassLoader loader=Player.class.getClassLoader();


	//
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
			color1=Color.RED;
		
			if(rank==1){
				walkingSprites=Resource.kidWalkingSprites;
				hurtingSprites=Resource.kidHurtingSprites;
				speed=3;
				r=16;
				health=1;
			}
			if(rank==2){
				walkingSprites=Resource.zombieMove;
				hurtingSprites=Resource.zombieHurt;
			
				speed=4;
				r=20;
				health=2;
			}
			if(rank==3){
				walkingSprites=Resource.deerMove;
				hurtingSprites=Resource.deerHurt;
				speed=5;
				r=35;
				health=3;
			}
			if(rank==4){
				walkingSprites=Resource.fatSoMove;
				hurtingSprites=Resource.fatSoHurt;
				speed=5;
				r=45;
				health=4;
			}
		}
		//faster Default
			if(type==2){
				color1=new Color(255,0,0,128);
				if(rank==1){
					walkingSprites=Resource.crowMove;
					hurtingSprites=Resource.crowMove;
					speed=4;
					r=16;
					health=2;
				}
				if(rank==2){
					walkingSprites=Resource.vomitMove;
					hurtingSprites=Resource.vomitHurt;
					speed=4+this.rank;
					r=20+this.rank;
					health=2+this.rank;
				}
				if(rank==3){
					walkingSprites=Resource.witchMove;
					hurtingSprites=Resource.witchHurt;
					speed=4;
					r=35;
					health=2+this.rank;
				}
				if(rank==3){
					walkingSprites=Resource.fatSoAttack;
					hurtingSprites=Resource.fatSoHurt;
					speed=4;
					r=45;
					health=2+this.rank;
				}

			}
		//slow but hard to kill
			if(type==3){
				//color1=Color.GREEN;
				color1=new Color(0,255,0,128);
				if(rank==1){
					walkingSprites=Resource.kidChainsawSprites;
					hurtingSprites=Resource.kidHurtingSprites;
					speed=2;
					r=18;
					health=5;
				}
				if(rank==2){
					walkingSprites=Resource.dogMove;
					hurtingSprites=Resource.dogHurt;
					speed=2;
					r=25;
					health=6;
				}
				if(rank==3){
					walkingSprites=Resource.crowAttack;
					hurtingSprites=Resource.crowAttack;
					speed=2;
					r=35;
					health=7;
				}
				if(rank==4){
					walkingSprites=Resource.witchMove;
					hurtingSprites=Resource.witchHurt;
					speed=2;
					r=50;
					health=10;
				}
			}
		
		hit=false;
		hitTimer=0;
		animation=new Animation();
		
		x=Math.random()*GamePanel.WIDTH/2+GamePanel.WIDTH/4;
		y=-r;
		double angle= Math.random()*140+20;
		rad=Math.toRadians(angle);
		dx=Math.cos(rad)*speed;
		dy=Math.sin(rad)*speed;
		ready=false;
		dead=false;
		width=2*r;
		height=2*r;
		
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
		animation.setFrames(walkingSprites);
		animation.setDelay(100);
		animation.update();
	}
	public void draw(Graphics2D g){
		if(hit){
			animation.setFrames(hurtingSprites);
			animation.setDelay(100);
			g.drawImage(animation.getImage(),(int)(x-2*r),(int)(y-2*r),null);
			
		}
		else{
			animation.setFrames(walkingSprites);
		g.drawImage(animation.getImage(),(int)(x-2*r),(int)(y-2*r),null);
				

		}

	}

}
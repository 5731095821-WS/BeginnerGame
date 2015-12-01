package code;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class PowerUp {
//FIELDS
private	double x,y;
private int width=40,height=40;
private	int r;
private	int type;
private BufferedImage image;
private int score=7;
ClassLoader loader= PowerUp.class.getClassLoader();
	// 1 is +1Life
	// 2 is +1 power
	// 3 is +2 power 
	// 4 slow Down
	// 5 ultimate

	//Constructor
	public PowerUp(int type,double x,double y){
		this.type=type;
		this.x=x;
		this.y=y;
		try{
	
		if(type==1)image=ImageIO.read(loader.getResource("powerups/health.png"));
		if(type==2)image=ImageIO.read(loader.getResource("powerups/bullets.png"));
		if(type==3)image=ImageIO.read(loader.getResource("powerups/ammobox.png"));
		
		if(type==4)image=ImageIO.read(loader.getResource("powerups/battery.png"));
		}catch(Exception e){
			e.printStackTrace();
		}
		new Animation();
		r=20;
	}
//Funtions

	public boolean update(){
		y+=3;
		if(y>GamePanel.HEIGHT+r)return true;
			return false;
	}
	public void draw(Graphics2D g){
		g.drawImage(image,(int)x-width,(int)y-height,null);

	}
	
	
	
	
	
	
	
	
	
	//getters

	public double getX() {
		return x;
	}


	public double getY() {
		return y;
	}


	public int getR() {
		return r;
	}


	public int getType() {
		return type;
	}
	public int getScore(){
		return score;
	}
	
}

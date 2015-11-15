package Beginner;

import java.awt.*;

public class PowerUp {
//FIELDS
private	double x,y;
private	int r;
private	int type;
private Color color1;
	// 1 is +1Life
	// 2 is +1 power
	// 3 is +2 power 
	// 4 slow Down
	
	//Constructor
	public PowerUp(int type,double x,double y){
		this.type=type;
		this.x=x;
		this.y=y;
		if(type==1)color1=Color.PINK;
		if(type==2||type==3)color1=Color.YELLOW;
		r=6;
		if(type==4){
			color1=Color.WHITE;
			r=6;
		}
	}
//Funtions

	public boolean update(){
		y+=3;
		if(y>GamePanel.HEIGHT+r)return true;
			return false;
	}
	public void draw(Graphics2D g){
		g.setColor(color1);
		g.fillRect((int)x-r, (int)y-r, 2*r, r*2);
		g.setStroke(new BasicStroke(3));
		g.setColor(color1.darker());
		g.drawRect((int)x-r, (int)y-r, 2*r, r*2);
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
	
	
}

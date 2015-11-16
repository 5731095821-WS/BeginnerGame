package Beginner;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Explosion {
	//FIELDS
	private double x,y;
	private int r;
	private int maxRadius;
	private Animation animation;
	private BufferedImage[] explosion;
	private int width;
	private int height;
	//Constructor
	public Explosion(double x,double y){
		animation=new Animation();
		this.x=x;
		this.y=y;
		this.r=8;
		this.maxRadius=38;
		width=2*r;
		height=2*r;
		explosion=Resource.blood;
		animation.setFrames(explosion);
	}
	
	public boolean update(){
		r++;
		if(r>=maxRadius){
			return true;
		}
		animation.setDelay(200);
		animation.update();
		return false;

	}
	public void draw(Graphics2D g){
		g.drawImage(animation.getImage(),(int)x-2*width,(int)y-height,null);

	}
}

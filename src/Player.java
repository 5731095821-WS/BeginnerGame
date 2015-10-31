import java.awt.*;


public class Player {
//fields
	private int x,y,r;
	private int dx,dy,speed;
	private int lives;
	private boolean left,right,up,down;
	private Color color1,color2;
	//constructor
	public Player(){
		x=GamePanel.WIDTH/2;
		y=GamePanel.HEIGHT/2;
		r=5;
		dx=0;dy=0;
		speed=5;
		lives=3;
		color1=Color.WHITE;
		color2=Color.RED;
			}
	//functions
public void update(){
	if(left)dx-=speed;
	if(right)dx+=speed;
	if(up)dy+=speed;
	if(down)dy-=speed;
	x+=dx;
	y+=dy;
	if(x<r)x=r;
	if(y<r)y=r;
	if(x>GamePanel.WIDTH-r)x=GamePanel.WIDTH-r;
	if(y>GamePanel.HEIGHT-r)y=GamePanel.HEIGHT-r; 
	dx=0;
	dy=0;
}
public void draw(Graphics2D g){
	g.setColor(color1);
	g.fillOval(x-r, y-r, 2*r, 2*r);
	g.setStroke(new BasicStroke(3));
	g.setColor(color1.darker());
	g.drawOval(x-r, y-r, 2*r, 2*r);
	g.setStroke(new BasicStroke(1));
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

}

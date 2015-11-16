package Beginner;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;



public class Resource {
	public static final Font standardFont=new Font("Tahoma", Font.BOLD, 30);
	public static BufferedImage heart;
	public static BufferedImage turret;
	public static AffineTransform at=new AffineTransform();
	public static BufferedImage[] kidWalkingSprites = new BufferedImage[4];
	public static BufferedImage[] kidHurtingSprites = new BufferedImage[1];
	public static BufferedImage[] kidChainsawSprites = new BufferedImage[2];
	public static BufferedImage[] blood = new BufferedImage[6];
	public static BufferedImage[] crowMove = new BufferedImage[4];
	public static BufferedImage[] crowAttack = new BufferedImage[2];
	public static BufferedImage[] fatSoMove = new BufferedImage[4];
	public static BufferedImage[] fatSoAttack = new BufferedImage[2];
	public static BufferedImage[] fatSoHurt = new BufferedImage[2];
	public static BufferedImage[] zombieMove = new BufferedImage[4];
	public static BufferedImage[] zombieHurt = new BufferedImage[2];
	public static BufferedImage[] vomitHurt = new BufferedImage[2];
	public static BufferedImage[] vomitMove = new BufferedImage[4];
	public static BufferedImage[] deerMove = new BufferedImage[4];
	public static BufferedImage[] deerHurt = new BufferedImage[2];
	public static BufferedImage[] dogHurt = new BufferedImage[2];
	public static BufferedImage[] dogMove = new BufferedImage[4];
	public static BufferedImage[] witchMove = new BufferedImage[4];
	public static BufferedImage[] witchHurt = new BufferedImage[1];
	//public static AudioClip coinSound;
	
	static {
			try{
				
				ClassLoader loader = Resource.class.getClassLoader();
				heart = ImageIO.read(loader.getResource("powerups/heart.png"));


					for(int i = 0; i < kidWalkingSprites.length; i++) {
						kidWalkingSprites[i] = ImageIO.read(loader.getResource("enemy/kid/kid_move_000"+(i+1)+".png"));	
					}
					for(int i = 0; i < kidHurtingSprites.length; i++) {
						kidHurtingSprites[i] = ImageIO.read(loader.getResource("enemy/kid/kid_pain_0001.png"));		
					}
					for(int i = 0; i < blood.length; i++) {
						blood[i] = ImageIO.read(loader.getResource("blood/blood_a_000"+(i+1)+".png"));	
					}
					for(int i = 0; i < kidChainsawSprites.length; i++) {
						kidChainsawSprites[i] = ImageIO.read(loader.getResource("enemy/kid/kid_chainsaw_000"+(i+1)+".png"));		
					}
					for(int i = 0; i < crowMove.length; i++) {
						crowMove[i] = ImageIO.read(loader.getResource("enemy/crow/move/crow_move_000"+(i+1)+".png"));		
					}
					for(int i = 0; i < crowAttack.length; i++) {
						crowAttack[i] = ImageIO.read(loader.getResource("enemy/crow/attack/crow_attack_000"+(i+1)+".png"));		
					}
					
					for(int i = 0; i < fatSoHurt.length; i++) {
						fatSoHurt[i] = ImageIO.read(loader.getResource("enemy/fatso/spawn/fatso_spawn_000"+(i+1)+".png"));	
					}
					
					for(int i = 0; i < zombieMove.length; i++) {
						zombieMove[i] = ImageIO.read(loader.getResource("enemy/zombie/move/zombie_move_000"+(i+1)+".png"));	
					}
					for(int i = 0; i < zombieHurt.length; i++) {
						zombieHurt[i] = ImageIO.read(loader.getResource("enemy/zombie/spawn/zombie_spawn_000"+(i+1)+".png"));	
					}
					for(int i = 0; i < vomitHurt.length; i++) {
						vomitHurt[i] = ImageIO.read(loader.getResource("enemy/vomit/spawn/vomit_spawn_000"+(i+1)+".png"));	
					}
					for(int i = 0; i < vomitMove.length; i++) {
						vomitMove[i] = ImageIO.read(loader.getResource("enemy/vomit/move/vomit_move_000"+(i+1)+".png"));	
					}
					
					
					for(int i = 0; i < deerHurt.length; i++) {
						deerHurt[i] = ImageIO.read(loader.getResource("enemy/moose/spawn/moose_spawn_000"+(i+1)+".png"));	
					}
					for(int i = 0; i < deerMove.length; i++) {
						deerMove[i] = ImageIO.read(loader.getResource("enemy/moose/move/moose_move_000"+(i+1)+".png"));	
					}
		
					for(int i = 0; i < fatSoMove.length; i++) {
						fatSoMove[i] = ImageIO.read(loader.getResource("enemy/fatso/move/fatso_move_000"+(i+1)+".png"));	
					}
					for(int i = 0; i < fatSoAttack.length; i++) {
						fatSoAttack[i] = ImageIO.read(loader.getResource("enemy/fatso/attack/fatso_attack_000"+(i+1)+".png"));	
					}	
					for(int i = 0; i < dogMove.length; i++) {
						dogMove[i] = ImageIO.read(loader.getResource("enemy/rott/move/rott_move_000"+(i+1)+".png"));	
					}
					for(int i = 0; i < dogHurt.length; i++) {
						dogHurt[i] = ImageIO.read(loader.getResource("enemy/rott/spawn/rott_spawn_000"+(i+1)+".png"));	
					}
					for(int i = 0; i < witchHurt.length; i++) {
						witchHurt[i] = ImageIO.read(loader.getResource("enemy/witch/witch_pain.png"));
					}
					for(int i = 0; i < witchMove.length; i++) {
						witchMove[i] = ImageIO.read(loader.getResource("enemy/witch/witch_fire_000"+(i+1)+".png"));	
					}

	
			}
			catch (IOException e){
				e.printStackTrace();
				//coinSound=null;
				heart=null;
				
			}
	}
}
	

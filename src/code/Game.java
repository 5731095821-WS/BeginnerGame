//This is Main

package code;

import java.io.IOException;

import javax.swing.*;
public class Game {
	public static void main(String[] args) throws IOException{
		JFrame window= new JFrame("Kill'em All");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane(new GamePanel());
		window.pack();
		window.setVisible(true);
		
		
	}
}
 
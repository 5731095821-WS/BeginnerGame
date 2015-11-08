//This is Main

package Beginner;

import java.io.IOException;

import javax.swing.*;
public class Game {
	public static void main(String[] args) throws IOException{
		JFrame window= new JFrame("Mad man with guns");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane(new GamePanel());
		window.pack();
		window.setVisible(true);
		
		
	}
}
 
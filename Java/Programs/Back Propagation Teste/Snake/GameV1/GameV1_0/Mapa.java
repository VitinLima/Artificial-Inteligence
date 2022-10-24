import java.awt.*; 
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mapa extends JFrame implements KeyListener{
	public int[][] Grid;
	public int Size_Of_Map;
	private Snake snake1;
	private Snake snake2;
	
	private JButton button_normal;
	
	public Mapa(){
		Size_Of_Map = 10;
		Grid = new int[Size_Of_Map][Size_Of_Map];
		snake1 = new Snake(1, this, Size_Of_Map);
		snake2 = new Snake(2, this, Size_Of_Map);
		
		button_normal.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				opt = 1;
				Get_Options_cobra = false;
			}
		});
	}
	public void keyTyped(KeyEvent e) {
		//System.out.println("keyTyped");
	}
	public void keyPressed(KeyEvent e) {
		//System.out.println("keyPressed");
	}
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			setDirection(1);
		else if(e.getKeyCode() == KeyEvent.VK_LEFT)
			setDirection(2);
		else if(e.getKeyCode() == KeyEvent.VK_DOWN)
			setDirection(3);
		else if(e.getKeyCode() == KeyEvent.VK_UP)
			setDirection(4);
	}
}
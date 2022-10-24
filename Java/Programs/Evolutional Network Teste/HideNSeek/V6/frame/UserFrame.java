package frame;

import main.Sync;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserFrame extends JFrame implements KeyListener{
	private int vertical = 0;
	private int horizontal = 0;
	
	public UserFrame(String name){
		setName(name);
		setSize(new Dimension(200,200));
		addKeyListener(this);
		setFocusable(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBackground(Color.black);
		setVisible(true);
	}
	public void keyTyped(KeyEvent e) {
		//System.out.println("keyTyped");
	}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			if(horizontal < 1) horizontal = 1;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT){
			if(horizontal > -1) horizontal = -1;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN){
			if(vertical < 1) vertical = 1;
		}else if(e.getKeyCode() == KeyEvent.VK_UP){
			if(vertical > -1) vertical = -1;
		}
	}
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			horizontal = 0;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT){
			horizontal = 0;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN){
			vertical = 0;
		}else if(e.getKeyCode() == KeyEvent.VK_UP){
			vertical = 0;
		}
	}
	public int getVertical(){
		return vertical;
	}
	public int getHorizontal(){
		return horizontal;
	}
	public void read(){
	}
}
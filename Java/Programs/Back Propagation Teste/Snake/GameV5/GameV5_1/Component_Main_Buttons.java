import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Component_Main_Buttons extends JComponent implements KeyListener{
	
	public Component_Main_Buttons(Object exit_obj, Object pause_obj, Object hide_obj, Object spy_refresh_obj){
		Dimension buttonDimension = new Dimension(80,40);
		
		JLabel title = new JLabel("Options");
		title.setLocation(0,0);
		title.setSize(buttonDimension);
		add(title);
		
		JButton tempbutton;
		
		tempbutton = new JButton("Refresh Spy");
		tempbutton.setLocation(0,40);
		tempbutton.setSize(buttonDimension);
		tempbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				synchronized(spy_refresh_obj){
					spy_refresh_obj.notify();
				}
			}
		});
		add(tempbutton);
		
		tempbutton = new JButton("Hide");
		tempbutton.setLocation(0,80);
		tempbutton.setSize(buttonDimension);
		tempbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				synchronized(hide_obj){
					hide_obj.notify();
				}
			}
		});
		add(tempbutton);
		
		tempbutton = new JButton("Pause");
		tempbutton.setLocation(0,120);
		tempbutton.setSize(buttonDimension);
		tempbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				synchronized(pause_obj){
					pause_obj.notifyAll();
				}
			}
		});
		add(tempbutton);
		
		tempbutton = new JButton("Exit");
		tempbutton.setLocation(0,160);
		tempbutton.setSize(buttonDimension);
		tempbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				synchronized(exit_obj){
					exit_obj.notifyAll();
				}
			}
		});
		add(tempbutton);
		
		addKeyListener(this);
		setPreferredSize(new Dimension(80, 200));
	}
	public void keyTyped(KeyEvent e) {
		//System.out.println("keyTyped");
	}
	public void keyPressed(KeyEvent e) {
		//System.out.println("keyPressed");
	}
	public void keyReleased(KeyEvent e) {
		/*if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			System.out.println("Right key pressed!");
		else if(e.getKeyCode() == KeyEvent.VK_LEFT)
			System.out.println("Left key pressed!");
		else if(e.getKeyCode() == KeyEvent.VK_DOWN)
			System.out.println("Down key pressed!");
		else if(e.getKeyCode() == KeyEvent.VK_UP)
			System.out.println("Up key pressed!");*/
	}
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Component_Main_Buttons extends JPanel{
	
	public Component_Main_Buttons(Object exit_obj, Object pause_obj, Object hide_obj){
		Dimension buttonDimension = new Dimension(80,40);
		
		JLabel title = new JLabel("Options");
		title.setLocation(0,0);
		title.setSize(buttonDimension);
		add(title);
		
		JButton tempbutton;
		
		tempbutton = new JButton("Pause");
		tempbutton.setLocation(0,40);
		tempbutton.setSize(buttonDimension);
		tempbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				synchronized(pause_obj){
					pause_obj.notifyAll();
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
		
		tempbutton = new JButton("Exit");
		tempbutton.setLocation(0,120);
		tempbutton.setSize(buttonDimension);
		tempbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				synchronized(exit_obj){
					exit_obj.notifyAll();
				}
			}
		});
		add(tempbutton);
		
		setPreferredSize(new Dimension(80,160));
	}
}
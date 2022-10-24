import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Component_Main_Buttons extends JPanel{
	private boolean is_hidden;
	private boolean is_paused;
	private boolean exit;
	
	public Component_Main_Buttons(){
		is_hidden = false;
		is_paused = false;
		exit = false;
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
				is_paused = !is_paused;
			}
		});
		add(tempbutton);
		
		tempbutton = new JButton("Hide");
		tempbutton.setLocation(0,80);
		tempbutton.setSize(buttonDimension);
		tempbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				is_hidden = !is_hidden;
			}
		});
		add(tempbutton);
		
		tempbutton = new JButton("Exit");
		tempbutton.setLocation(0,120);
		tempbutton.setSize(buttonDimension);
		tempbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				exit = true;
			}
		});
		add(tempbutton);
		
		setPreferredSize(new Dimension(80,160));
	}
	public boolean fis_hidden(){
		return is_hidden;
	}
	public boolean fis_paused(){
		return is_paused;
	}
	public boolean fis_exit(){
		return exit;
	}
}
import javax.swing.*;
import java.lang.Thread;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window_Buttons extends JComponent implements KeyListener{
	private Window_Spy window_spy;
	private Object pause_obj;
	private Object hide_obj;
	
	public Window_Buttons(Snake[] snakes, Object game_obj, Object master_obj, Object new_pause_obj, Object new_hide_obj){
		pause_obj = new_pause_obj;
		hide_obj = new_hide_obj;
		Dimension buttonDimension = new Dimension(80,40);
		
		JLabel title = new JLabel("Options");
		title.setLocation(0,0);
		title.setSize(buttonDimension);
		add(title);
		
		JButton tempbutton;
		tempbutton = new JButton("Spy");
		tempbutton.setLocation(0,40);
		tempbutton.setSize(buttonDimension);
		tempbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				window_spy = new Window_Spy(game_obj, master_obj, snakes);
				window_spy.addWindowListener(new WindowAdapter(){
					@Override
					public void windowClosing(WindowEvent e){
						window_spy.fend_thread();
						window_spy.dispose();
						window_spy = null;
					}
				});
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
				System.exit(0);
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
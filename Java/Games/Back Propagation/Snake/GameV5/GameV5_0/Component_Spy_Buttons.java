import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Component_Spy_Buttons extends JComponent{
	private int id;
	
	public Component_Spy_Buttons(Snake[] snakes, Object spy_obj){
		id = -1;
		
		JLabel title = new JLabel("snakes");
		title.setSize(new Dimension(80,40));
		add(title);
		
		JButton temp_button;
		int last_x = 0;
		int last_y = 40;
		
		temp_button = new JButton("Gen. Info");
		temp_button.setSize(new Dimension(80, 40));
		temp_button.setLocation(last_x, last_y);
		temp_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				synchronized(spy_obj){
					id = -1;
					spy_obj.notify();
				}
			}
		});
		add(temp_button);
		
		for(Snake snake:snakes){
			temp_button = new JButton("Snake "+ snake.fget_id());
			temp_button.setSize(new Dimension(80, 40));
			temp_button.setLocation(last_x, last_y);
			temp_button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					synchronized(spy_obj){
						id = snake.fget_id();
						spy_obj.notify();
					}
				}
			});
			add(temp_button);
			last_y += 40;
		}
		last_y += 40;
		
		setPreferredSize(new Dimension(80, last_y));
	}
	
	public int fget_id(){
		return id;
	}
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Component_Info_Buttons extends JPanel{
	private int id;
	private boolean display;
	private boolean display_switch;
	private boolean refresh;
	
	private JComboBox<String> box;
	
	public Component_Info_Buttons(Snake[] snakes){
		id = -1;
		display = true;
		display_switch = true;
		refresh = true;
		
		JLabel title = new JLabel("snakes");
		title.setSize(new Dimension(80,40));
		add(title);
		
		JButton temp_button;
		
		temp_button = new JButton("Switch Display");
		temp_button.setSize(new Dimension(80, 40));
		temp_button.setLocation(0,0);
		temp_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(display)
					display = !display;
				else{
					if(display_switch)
						display_switch = !display_switch;
					else{
						display_switch = !display_switch;
						display = !display;
					}
				}
				refresh = true;
			}
		});
		add(temp_button);
		
		String[] list = new String[snakes.length + 1];
		list[0] = "Gen. Info";
		for(int i = 0; i < snakes.length; i++){
			list[i+1] = "snake " + snakes[i].getId();
		}
		
		box = new JComboBox<>(list);
		box.setSize(new Dimension(80,40));
		box.setLocation(0,40);
		box.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				id = box.getSelectedIndex() - 1;
				refresh = true;
			}
		});
		add(box);
		
		setPreferredSize(new Dimension(80,80));
	}
	
	public int fget_id(){
		return id;
	}
	
	public boolean fget_display(){
		return display;
	}
	
	public boolean fget_display_switch(){
		return display_switch;
	}
	
	public boolean fget_refresh(){
		if(display) return true;
		if(!refresh) return false;
		refresh = false;
		return true;
	}
}
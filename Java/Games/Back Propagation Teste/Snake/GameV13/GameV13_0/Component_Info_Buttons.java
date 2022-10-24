import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Component_Info_Buttons extends JPanel{
	private int id;
	private int rel_to;
	private boolean display;
	private boolean display_switch;
	private boolean refresh;
	
	private JComboBox<String> box1;
	private JComboBox<String> box2;
	
	Snake[] snakes;
	
	public Component_Info_Buttons(Snake[] new_snakes){
		snakes = new_snakes;
		id = -1;
		rel_to = -1;
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
				if(display){
					display = !display;
					fmodify_box_1();
				} else{
					if(display_switch){
						display_switch = !display_switch;
						box1.setVisible(false);
						box2.setVisible(false);
					} else{
						display_switch = !display_switch;
						display = !display;
						box1.setVisible(true);
						fmodify_box_1();
					}
				}
				refresh = true;
			}
		});
		add(temp_button);
		
		box2 = new JComboBox<>(new String[0]);
		box2.setSize(new Dimension(80,40));
		box2.setLocation(0,40);
		box2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				rel_to = box2.getSelectedIndex()-1;
				refresh = true;
			}
		});
		box2.setVisible(false);
		
		box1 = new JComboBox<>(new String[0]);
		box1.setSize(new Dimension(80,40));
		box1.setLocation(0,80);
		box1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				id = box1.getSelectedIndex() - 1;
				box2.setVisible(false);
				if(id > -1 && !display && display_switch)
					fmodify_box_2();
				refresh = true;
			}
		});
		box1.setVisible(true);
		fmodify_box_1();
		add(box1);
		add(box2);
		
		setPreferredSize(new Dimension(80,120));
	}
	
	private void fmodify_box_1(){
		String[] list = new String[snakes.length + 1];
		list[0] = "Gen. Info";
		for(int i = 0; i < snakes.length; i++){
			list[i+1] = "snake " + snakes[i].getId();
		}
		
		box1.removeAllItems();
		for(String s:list)
			box1.addItem(s);
	}
	
	private void fmodify_box_2(){
		String[] list = new String[snakes[id].fget_number_of_neurons() + 1];
		rel_to = -1;
		list[0] = "NN";
		box2.setVisible(true);
		for(int i = 1; i < list.length; i++)
			list[i] = "neuron " + (i-1);
		box2.removeAllItems();
		for(String s:list)
			box2.addItem(s);
	}
	
	public int fget_id(){
		return id;
	}
	
	public int fget_rel_to(){
		return rel_to;
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
	public void fask_refresh(){
		refresh = true;
	}
}
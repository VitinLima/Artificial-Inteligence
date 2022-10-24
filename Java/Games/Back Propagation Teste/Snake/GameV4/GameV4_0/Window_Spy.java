import javax.swing.*;
import java.lang.Thread;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window_Spy extends JFrame{
	private Object game_obj;
	private Object master_obj;
	private Object spy_obj;
	
	private Snake[] snakes;
	private int snake_id;
	private JTextArea text;
	private JScrollPane scroll;
	private Refresh refresh;
	
	public Window_Spy(Snake[] new_snakes, Object new_game_obj, Object new_master_obj, Object new_spy_obj){
		snakes = new_snakes;
		game_obj = new_game_obj;
		master_obj = new_master_obj;
		spy_obj = new_spy_obj;
		
		Window_Spy_Buttons options = new Window_Spy_Buttons();
		setLayout(new FlowLayout());
		add(options);
		text = new JTextArea();
		scroll = new JScrollPane(text);
		add(scroll);
		pack();
		
		refresh = new Refresh();
		refresh.start();
		setVisible(true);
	}
	
	public void fend_thread(){
		refresh.fend_thread();
	}
	
	private void fdisplay(int id){
		scroll.setPreferredSize(new Dimension(1000,300));
		text.setText(snakes[id].fget_information());
		pack();
	}
	
	private class Refresh extends Thread{
		private int id;
		private boolean running;
		public Refresh(){
			running = true;
			id = -1;
		}
		public void run(){
			while(running){
				synchronized(spy_obj){
					try{
						spy_obj.wait();
					} catch(Exception e){
						System.out.println(e);
					}
				}
				if(id >= 0)
					fdisplay(id);
			}
		}
		public void fend_thread(){
			running = false;
		}
		public void fset_id(int new_id){
			id = new_id;
		}
	}

	private class Window_Spy_Buttons extends JComponent{
		public Window_Spy_Buttons(){
			JLabel title = new JLabel("snakes");
			title.setSize(new Dimension(80,40));
			add(title);
			JButton temp_button;
			int last_x = 0;
			int last_y = 40;
			for(Snake snake:snakes){
				temp_button = new JButton("Snake "+ snake.fget_id());
				temp_button.setSize(new Dimension(80, 40));
				temp_button.setLocation(last_x, last_y);
				temp_button.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						refresh.fset_id(snake.fget_id());
						synchronized(spy_obj){
							spy_obj.notify();
						}
					}
				});
				add(temp_button);
				last_y += 40;
			}
			setPreferredSize(new Dimension(80, last_y));
		}
	}
}
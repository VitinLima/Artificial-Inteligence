import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
	
public class Frame extends JFrame{
	public Frame(){
		Object exit_obj = new Object();
		Game_Object game_obj = new Game_Object();
		Object master_obj = new Object();
		
		Thread_Handler thread_handler = new Thread_Handler(exit_obj, game_obj, master_obj);
		
		Component_Main component_main = new Component_Main(thread_handler.fget_map(), exit_obj, game_obj, master_obj);
		add(component_main);
		
		Component_Info component_info = new Component_Info(thread_handler.fget_snakes(), exit_obj, game_obj, master_obj, this);
		add(component_info);
		
		setTitle("Snake");
		setLayout(new FlowLayout());
		pack();
		setFocusable(true);
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				synchronized(exit_obj){
					exit_obj.notifyAll();
				}
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setVisible(true);
		thread_handler.fstart();
		dispose();
	}
}
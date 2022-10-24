import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
	
public class Component_Info extends JPanel{
	private Frame frame;
	private Object exit_obj;
	private Game_Object game_obj;
	private Object master_obj;
	
	private Component_Info_Buttons component_info_buttons;
	private Component_Info_Display component_info_display;
	
	private boolean is_to_exit;
	
	public Component_Info(Snake[] snakes, Object new_exit_obj, Game_Object new_game_obj, Object new_master_obj, Frame new_frame){
		frame = new_frame;
		
		exit_obj = new_exit_obj;
		game_obj = new_game_obj;
		master_obj = new_master_obj;
		
		is_to_exit = false;
		
		Dimension frames_dim = new Dimension(500,500);
		Dimension buttons_dim = new Dimension(80,500);
		
		component_info_buttons = new Component_Info_Buttons(snakes);
		add(component_info_buttons);
		
		component_info_display = new Component_Info_Display(snakes);
		add(component_info_display);
		
		Exit_Handle exit_handle = new Exit_Handle();
		exit_handle.start();
		
		Info_Handle info_handle = new Info_Handle();
		info_handle.start();
	}
	
	private class Info_Handle extends Thread{
		public void run(){
			setName("Info handler");
			while(!is_to_exit){
				game_obj.fwait();
				if(component_info_buttons.fget_refresh())
					component_info_display.frefresh(component_info_buttons.fget_id(), component_info_buttons.fget_display());
				synchronized(master_obj){
					master_obj.notify();
				}
				frame.pack();
			}
		}
	}
	
	private class Exit_Handle extends Thread{
		public void run(){
			setName("Exit for Frame handler");
			synchronized(exit_obj){
				try{
					exit_obj.wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
			is_to_exit = true;
		}
	}
}
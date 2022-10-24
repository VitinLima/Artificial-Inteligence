import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
	
public class Component_Main extends JPanel{
	private Object exit_obj;
	private Game_Object game_obj;
	private Object master_obj;
	private Object pause_obj;
	private Object hide_obj;
	
	private Component_Main_Buttons component_main_buttons;
	private Component_Main_Map component_main_map;
	
	private boolean is_to_exit;
	private boolean is_hidden;
	
	private Pause_Handle pause_handle;
	private Hide_Handle hide_handle;
	
	public Component_Main(Map map, Object new_exit_obj, Game_Object new_game_obj, Object new_master_obj){
		exit_obj = new_exit_obj;
		game_obj = new_game_obj;
		master_obj = new_master_obj;
		pause_obj = new Object();
		hide_obj = new Object();
		
		is_to_exit = false;
		is_hidden = false;
		
		Dimension frames_dim = new Dimension(500,500);
		Dimension buttons_dim = new Dimension(80,500);
		
		component_main_buttons = new Component_Main_Buttons(exit_obj, pause_obj, hide_obj);
		add(component_main_buttons);
		
		component_main_map = new Component_Main_Map(map.fget_grid());
		add(component_main_map);
		
		Exit_Handle exit_handle = new Exit_Handle();
		Main_Handle main_handle = new Main_Handle();
		pause_handle = new Pause_Handle();
		hide_handle = new Hide_Handle();
		exit_handle.start();
		main_handle.start();
		pause_handle.start();
		hide_handle.start();
	}
	
	private class Main_Handle extends Thread{
		public void run(){
			setName("Main handler");
			while(!is_to_exit){
				game_obj.fwait();
				if(!is_hidden){
					component_main_map.frefresh();
					try{
						sleep(100);
					} catch(Exception e){
						System.out.println(e);
					}
				}
				synchronized(master_obj){
					master_obj.notify();
				}
			}
		}
	}
	
	private class Pause_Handle extends Thread{
		public void run(){
			setName("Pause handler");
			while(faction());
		}
		private boolean faction(){
			synchronized(pause_obj){
				try{
					pause_obj.wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
			if(is_to_exit) return false;
			game_obj.fwait();
			synchronized(pause_obj){
				try{
					pause_obj.wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
			synchronized(master_obj){
				master_obj.notify();
			}
			if(is_to_exit) return false;
			return true;
		}
	}
	
	private class Hide_Handle extends Thread{
		public void run(){
			setName("Hide handler");
			while(faction());
		}
		private boolean faction(){
			synchronized(hide_obj){
				try{
					hide_obj.wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
			if(is_to_exit) return false;
			is_hidden = !is_hidden;
			return true;
		}
	}
	
	private class Exit_Handle extends Thread{
		public void run(){
			setName("Exit for Main handler");
			synchronized(exit_obj){
				try{
					exit_obj.wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
			is_to_exit = true;
			while(pause_handle.getState() != Thread.State.valueOf(new String("WAITING")));
			synchronized(pause_obj){
				pause_obj.notify();
			}
			while(hide_handle.getState() != Thread.State.valueOf(new String("WAITING")));
			synchronized(hide_obj){
				hide_obj.notify();
			}
		}
	}
}
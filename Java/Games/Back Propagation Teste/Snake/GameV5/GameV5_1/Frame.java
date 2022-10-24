import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
	
public class Frame extends JFrame{
	private Object exit_obj;
	private Game_Object game_obj;
	private Object master_obj;
	private Object pause_obj;
	private Object hide_obj;
	private Object spy_obj;
	private Object spy_refresh_obj;
	
	private Component_Main_Buttons component_main_buttons;
	private Component_Main_Map component_main_map;
	private Component_Spy_Buttons component_spy_buttons;
	private Component_Spy_Text component_spy_text;
	
	private boolean is_to_exit;
	private boolean is_paused;
	private boolean is_hidden;
	private boolean is_spy_refresh;
	
	private Pause_Handle pause_handle;
	private Hide_Handle hide_handle;
	private Spy_Handle spy_handle;
	private Spy_Refresh_Handle spy_refresh_handle;
	
	public Frame(Map map, Snake[] snakes, Fruit[] fruits, Object new_exit_obj, Game_Object new_game_obj, Object new_master_obj){
		exit_obj = new_exit_obj;
		game_obj = new_game_obj;
		master_obj = new_master_obj;
		pause_obj = new Object();
		hide_obj = new Object();
		spy_obj = new Object();
		spy_refresh_obj = new Object();
		
		is_to_exit = false;
		is_paused = false;
		is_hidden = false;
		is_spy_refresh = true;
		
		Dimension frames_dim = new Dimension(500,500);
		Dimension buttons_dim = new Dimension(80,500);
		
		component_main_buttons = new Component_Main_Buttons(exit_obj, pause_obj, hide_obj, spy_refresh_obj);
		add(component_main_buttons);
		
		component_main_map = new Component_Main_Map(map.fget_grid());
		add(component_main_map);
		
		component_spy_buttons = new Component_Spy_Buttons(snakes, spy_obj);
		add(component_spy_buttons);
		
		component_spy_text = new Component_Spy_Text(snakes);
		add(component_spy_text);
		
		setTitle("Snake");
		setLayout(new FlowLayout());
		pack();
		setFocusable(true);
		setLocationRelativeTo(null);
		
		Exit_Handle exit_handle = new Exit_Handle();
		Frame_Handle frame_handle = new Frame_Handle();
		pause_handle = new Pause_Handle();
		hide_handle = new Hide_Handle();
		spy_handle = new Spy_Handle();
		spy_refresh_handle = new Spy_Refresh_Handle();
		exit_handle.start();
		frame_handle.start();
		pause_handle.start();
		hide_handle.start();
		spy_handle.start();
		spy_refresh_handle.start();
		
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
	}
	
	private class Frame_Handle extends Thread{
		public void run(){
			setName("Frame handler");
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
				if(is_spy_refresh)
					component_spy_text.frefresh();
				synchronized(master_obj){
					master_obj.notify();
				}
			}
			dispose();
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
			is_paused = true;
			synchronized(pause_obj){
				try{
					pause_obj.wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
			is_paused = false;
			synchronized(master_obj){
				master_obj.notify();
			}
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
	
	private class Spy_Handle extends Thread{
		public void run(){
			setName("Spy handler");
			while(faction());
		}
		private boolean faction(){
			synchronized(spy_obj){
				try{
					spy_obj.wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
			if(is_to_exit) return false;
			component_spy_text.fset_id(component_spy_buttons.fget_id());
			if(is_paused)
				component_spy_text.frefresh();
			return true;
		}
	}
	
	private class Spy_Refresh_Handle extends Thread{
		public void run(){
			setName("Spy refresh handler");
			while(faction());
		}
		private boolean faction(){
			synchronized(spy_refresh_obj){
				try{
					spy_refresh_obj.wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
			if(is_to_exit) return false;
			is_spy_refresh = !is_spy_refresh;
			return true;
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
			while(pause_handle.getState() != Thread.State.valueOf(new String("WAITING")));
			synchronized(pause_obj){
				pause_obj.notify();
			}
			while(hide_handle.getState() != Thread.State.valueOf(new String("WAITING")));
			synchronized(hide_obj){
				hide_obj.notify();
			}
			while(spy_handle.getState() != Thread.State.valueOf(new String("WAITING")));
			synchronized(spy_obj){
				spy_obj.notify();
			}
			while(spy_refresh_handle.getState() != Thread.State.valueOf(new String("WAITING")));
			synchronized(spy_refresh_obj){
				spy_refresh_obj.notify();
			}
		}
	}
}
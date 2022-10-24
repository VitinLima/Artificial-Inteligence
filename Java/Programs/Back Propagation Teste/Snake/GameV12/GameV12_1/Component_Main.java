import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
	
public class Component_Main extends JPanel{
	private Game_Object obj;
	private Frame frame;
	
	private Component_Main_Buttons component_main_buttons;
	private Component_Main_Map component_main_map;
	
	private Component_Info_Buttons component_info_buttons;
	private Component_Info_Display component_info_display;
	
	public Component_Main(Game_Object new_obj, Frame new_frame){
		obj = new_obj;
		frame = new_frame;
		
		Dimension frames_dim = new Dimension(500,500);
		Dimension buttons_dim = new Dimension(80,500);
		
		component_main_buttons = new Component_Main_Buttons();
		add(component_main_buttons);
		
		component_main_map = new Component_Main_Map(obj.fget_map().fget_grid());
		add(component_main_map);
		
		component_info_buttons = new Component_Info_Buttons(obj.fget_snakes());
		add(component_info_buttons);
		
		component_info_display = new Component_Info_Display(obj.fget_snakes(), obj.fget_map());
		add(component_info_display);
		
		(new Main_Handle()).start();
		(new Pause_Handle()).start();
		(new Hide_Handle()).start();
	}
	
	private class Main_Handle extends Thread{
		public void run(){
			setName("Main handler");
			boolean temp = true;
			while(temp){
				synchronized(obj){
					obj.notifyAll();
					if(!component_main_buttons.fis_hidden() && !component_main_buttons.fis_paused()){
						component_main_map.frefresh();
						if(component_info_buttons.fget_refresh())
							component_info_display.frefresh(component_info_buttons.fget_id(), component_info_buttons.fget_rel_to(), component_info_buttons.fget_display(), component_info_buttons.fget_display_switch());
						try{
							sleep(100);
						} catch(Exception e){
							System.out.println(e);
						}
					}
					if(component_main_buttons.fis_exit())
						obj.fexit();
					temp = obj.fis_running();
					try{
						obj.wait();
					} catch(Exception e){
						System.out.println(e);
					}
				}
			}
			synchronized(obj){
				obj.notifyAll();
			}
		}
	}
	private class Pause_Handle extends Thread{
		public void run(){
			setName("Pause handler");
			boolean temp = true;
			while(temp){
				synchronized(obj){
					obj.notifyAll();
					if(!component_main_buttons.fis_paused()){
						temp = obj.fis_running();
						try{
							obj.wait();
						} catch(Exception e){
							System.out.println(e);
						}
						continue;
					}
					while(component_main_buttons.fis_paused() && !component_main_buttons.fis_exit() && obj.fis_running()){
						if(component_info_buttons.fget_refresh())
							component_info_display.frefresh(component_info_buttons.fget_id(), component_info_buttons.fget_rel_to(), component_info_buttons.fget_display(), component_info_buttons.fget_display_switch());
						try{
							sleep(100);
						} catch(Exception e){
							System.out.println(e);
						}
					}
					temp = obj.fis_running();
					try{
						obj.wait();
					} catch(Exception e){
						System.out.println(e);
					}
				}
			}
			synchronized(obj){
				obj.notifyAll();
			}
		}
	}
	private class Hide_Handle extends Thread{
		public void run(){
			setName("Hide handler");
			boolean temp = true;
			while(temp){
				synchronized(obj){
					obj.notifyAll();
					if(!component_main_buttons.fis_hidden() || component_main_buttons.fis_paused()){
						temp = obj.fis_running();
						try{
							obj.wait();
						} catch(Exception e){
							System.out.println(e);
						}
						continue;
					}
					if(component_info_buttons.fget_refresh())
						component_info_display.frefresh(component_info_buttons.fget_id(), component_info_buttons.fget_rel_to(), component_info_buttons.fget_display(), component_info_buttons.fget_display_switch());
					temp = obj.fis_running();
					try{
						obj.wait();
					} catch(Exception e){
						System.out.println(e);
					}
				}
				try{
					sleep(1000);
				} catch(Exception e){
					System.out.println(e);
				}
			}
			synchronized(obj){
				obj.notifyAll();
			}
		}
	}
}
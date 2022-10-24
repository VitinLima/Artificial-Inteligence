import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
	
public class Component_Main extends JPanel{
	private Game_Object obj;
	
	private Component_Main_Buttons component_main_buttons;
	private Component_Main_Map component_main_map;
	
	public Component_Main(Map map, Game_Object new_obj){
		obj = new_obj;
		
		Dimension frames_dim = new Dimension(500,500);
		Dimension buttons_dim = new Dimension(80,500);
		
		component_main_buttons = new Component_Main_Buttons();
		add(component_main_buttons);
		
		component_main_map = new Component_Main_Map(map.fget_grid());
		add(component_main_map);
		
		Main_Handle main_handle = new Main_Handle();
		
		main_handle.start();
	}
	
	private class Main_Handle extends Thread{
		public void run(){
			setName("Main handler");
			boolean temp = true;
			while(temp){
				synchronized(obj){
					obj.notifyAll();
					if(!component_main_buttons.fis_hidden()){
						component_main_map.frefresh();
						try{
							sleep(100);
						} catch(Exception e){
							System.out.println(e);
						}
					}
					while(component_main_buttons.fis_paused() && !component_main_buttons.fis_exit() && obj.fis_running());
					if(component_main_buttons.fis_exit())
						obj.fexit();
					if(!obj.fis_running())
						temp = false;
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
			System.out.println("Thread " + Thread.currentThread() + " closed");
		}
	}
}
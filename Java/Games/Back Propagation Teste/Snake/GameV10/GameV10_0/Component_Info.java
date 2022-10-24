import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
	
public class Component_Info extends JPanel{
	private Frame frame;
	private Game_Object obj;
	
	private Component_Info_Buttons component_info_buttons;
	private Component_Info_Display component_info_display;
	
	public Component_Info(Game_Object new_obj, Frame new_frame){
		frame = new_frame;
		
		obj = new_obj;
		
		Dimension frames_dim = new Dimension(500,500);
		Dimension buttons_dim = new Dimension(80,500);
		
		component_info_buttons = new Component_Info_Buttons(obj.fget_snakes());
		add(component_info_buttons);
		
		component_info_display = new Component_Info_Display(obj.fget_snakes());
		add(component_info_display);
		
		Info_Handle info_handle = new Info_Handle();
		info_handle.start();
	}
	
	private class Info_Handle extends Thread{
		public void run(){
			setName("Info handler");
			boolean temp = true;
			while(temp){
				synchronized(obj){
					obj.notifyAll();
					if(component_info_buttons.fget_refresh())
						component_info_display.frefresh(component_info_buttons.fget_id(), component_info_buttons.fget_display(), component_info_buttons.fget_display_switch());
					frame.pack();
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
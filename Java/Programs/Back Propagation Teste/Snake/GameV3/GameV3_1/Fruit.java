import javax.swing.*;
import java.lang.Thread;
import java.awt.*;

public class Fruit extends Entity{
	private int timer;
	private int max_time;
	public Fruit(int new_id, Color new_color, Object new_game_obj, Object new_master_obj){
		fset_id(new_id);
		fset_color(new_color);
		fset_obj(new Object());
		fset_game_obj(new_game_obj);
		fset_master_obj(new_master_obj);
		
		max_time = 20;
	}
	public void frespawn(){
		timer = max_time;
		int x = 0;
		int y = 0;
		boolean temp = true;
		while(temp){
			temp = false;
			x = (int)((float)Math.random()*((float)fget_map().fget_size_x() - 1.0f));
			y = (int)((float)Math.random()*((float)fget_map().fget_size_y() - 1.0f));
			if(fget_map().fget_pixel(x,y) != Color.black)
				temp = true;
		}
		fset_coordinates(new int[][]{new int[]{x, y}});
	}
	public void fmove_timer(){
		timer--;
		if(timer <= 0)
			frespawn();
	}
	public void run(){
		frespawn();
		while(true){
			synchronized(fget_game_obj()){
				try{
					fget_game_obj().wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
			//System.out.println("Working..fruit " + fget_id());
			fmove_timer();
			synchronized(fget_master_obj()){
				fget_master_obj().notify();
			}
		}
	}
}
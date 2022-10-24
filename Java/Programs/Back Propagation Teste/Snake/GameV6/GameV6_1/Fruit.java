import javax.swing.*;
import java.lang.Thread;
import java.awt.*;

public class Fruit extends Entity{
	private int timer;
	private int max_time;
	public Fruit(int new_id, Color new_color, Object new_exit_obj, Game_Object new_game_obj, Object new_master_obj){
		fset_id(new_id);
		fset_color(new_color);
		fset_obj(new Object());
		fset_exit_obj(new_exit_obj);
		fset_game_obj(new_game_obj);
		fset_master_obj(new_master_obj);
		fset_fruit();
		
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
	
	@Override
	public void faction(){
		if(fget_coordinates() == null)
			frespawn();
		timer--;
		if(timer <= 0)
			frespawn();
	}
}
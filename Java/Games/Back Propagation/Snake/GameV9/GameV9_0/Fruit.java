import javax.swing.*;
import java.lang.Thread;
import java.awt.*;

public class Fruit extends Entity{
	public Fruit(int new_id, Color new_color, Map new_map, Game_Object new_obj){
		fset_id(new_id);
		fset_color(new_color);
		fset_map(new_map);
		fset_obj(new_obj);
		fspawn();
	}
	public void fspawn(){
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
	public boolean faction(){
		if(fget_coordinates().length == 0)
			return false;
		return true;
	}
}
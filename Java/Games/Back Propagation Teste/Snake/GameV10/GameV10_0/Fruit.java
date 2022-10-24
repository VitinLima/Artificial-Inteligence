import javax.swing.*;
import java.lang.Thread;
import java.awt.*;

public class Fruit extends Entity{
	public Fruit(Color new_color, Game_Object new_obj){
		fset_color(new_color);
		fset_obj(new_obj);
		setName("fruit " + getId());
		fspawn();
	}
	public void fspawn(){
		int x = 0;
		int y = 0;
		boolean temp = true;
		while(temp){
			temp = false;
			x = (int)((float)Math.random()*((float)fget_obj().fget_map().fget_size_x() - 1.0f));
			y = (int)((float)Math.random()*((float)fget_obj().fget_map().fget_size_y() - 1.0f));
			if(fget_obj().fget_map().fget_pixel(x,y) != Color.black)
				temp = true;
		}
		fset_coordinates(new int[][]{new int[]{x, y}});
	}
	
	@Override
	public boolean faction(){
		if(fget_obj().fget_map().fget_id(fget_coordinates()[0][0], fget_coordinates()[0][1]) != getId()){
			fset_coordinates(new int[0][0]);
			return false;
		}
		return true;
	}
}
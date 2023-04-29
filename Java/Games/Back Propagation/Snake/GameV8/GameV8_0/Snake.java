import javax.swing.*;
import java.lang.Thread;
import java.awt.*;

public class Snake extends Entity{
	private AI ai;
	private int opt;
	private int field_of_view;
	private int movements_left;
	private int max_movements;
	private int deaths;
	private int scored;
	private int score;
	private int max_score;
	private float[] last_input;
	private int last_output;
	public Snake(int new_id, Color new_color, Object new_exit_obj, Game_Object new_game_obj, Object new_master_obj){
		fset_id(new_id);
		fset_color(new_color);
		fset_obj(new Object());
		fset_exit_obj(new_exit_obj);
		fset_game_obj(new_game_obj);
		fset_master_obj(new_master_obj);
		
		max_movements = 100;
		deaths = 0;
		scored = 0;
		max_score = 0;
		
		field_of_view = 1;
		ai = new AI(new int[]{2*(2*field_of_view+1)*(2*field_of_view+1), 16, 4}, 2*field_of_view);
		
		last_output = 0;
		last_input = null;
	}
	
	@Override
	public void faction(){
		if(fget_coordinates() == null)
			frespawn();
		
		last_input = fget_surrounding();
		ai.fset_input(last_input);
		int choice = ai.fstart();
		last_output = choice;
		
		int temp_x = fget_coordinates()[0][0];
		int temp_y = fget_coordinates()[0][1];
		switch(choice){
			case 0:
				temp_x++;
				break;
			case 1:
				temp_y++;
				break;
			case 2:
				temp_x--;
				break;
			case 3:
				temp_y--;
				break;
		}
		
		if(temp_x < 0 || temp_x == fget_map().fget_size_x() || temp_y < 0 || temp_y == fget_map().fget_size_y()){
			fkill();
			return;
		}
		if(fget_map().fget_pixel(temp_x, temp_y) != Color.black){
			boolean temp;
			int object_id = 0;
			temp = true;
			for(Fruit fruit:fget_fruits())
				if(fruit.fget_color() == fget_map().fget_pixel(temp_x, temp_y)){
					object_id = fruit.fget_id();
					temp = false;
				}
			if(temp){
				fkill();
				return;
			}
			else{
				fgrow();
				fget_fruits()[object_id].frespawn();
			}
		}
		
		int[][] new_coordinates = new int[fget_coordinates().length][2];
		for(int i = new_coordinates.length-1; i > 0; i--)
			new_coordinates[i] = fget_coordinates()[i-1];
		new_coordinates[0][0] = temp_x;
		new_coordinates[0][1] = temp_y;
		fset_coordinates(new_coordinates);
		
		movements_left--;
		if(movements_left == 0)
			fkill();
	}
	public void fkill(){
		ai.fback_propagate(false);
		
		deaths++;
		frespawn();
	}
	public void fgrow(){
		ai.fback_propagate(true);
		
		int[][] new_coordinates = new int[fget_coordinates().length+1][2];
		for(int i = 0; i < new_coordinates.length-1; i++)
			new_coordinates[i] = fget_coordinates()[i];
		new_coordinates[new_coordinates.length-1] = new_coordinates[new_coordinates.length-2];
		fset_coordinates(new_coordinates);
		
		scored++;
		score++;
		movements_left = max_movements;
	}
	public void frespawn(){
		ai.ferase_memories();
		movements_left = max_movements;
		if(score > max_score)
			max_score = score;
		score = 0;
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
	public void fset_gif(JPanel panel){
		ai.fset_gif(panel);
	}
	public void fdisable_gif(){
		ai.fdisable_gif();
	}
	private float[] fget_surrounding(){
		Color[][] temp_grid = new Color[(2*field_of_view+1)][(2*field_of_view+1)];
		int x = fget_coordinates()[0][0];
		int y = fget_coordinates()[0][1];
		int m = 0;
		int n = 0;
		
		for(int i = x - field_of_view; i <= x + field_of_view; i++){
			for(int j = y - field_of_view; j <= y + field_of_view; j++)
				if(i < 0 || i >= fget_map().fget_size_x() || j < 0 || j >= fget_map().fget_size_y())
					temp_grid[m][n++] = Color.white;
				else
					temp_grid[m][n++] = fget_map().fget_pixel(i, j);
			m++;
			n = 0;
		}
		
		boolean temp;
		float[] input = new float[(2*field_of_view+1)*(2*field_of_view+1)*2];
		int k = 0;
		
		for(int i = 0; i < (2*field_of_view+1); i++) for(int j = 0; j < (2*field_of_view+1); j++){
			input[(2*field_of_view+1)*(2*field_of_view+1)+k] = 0.0f;
			input[k] = 0.0f;
			if(temp_grid[i][j] == Color.black){
				k++;
			} else{
				if(temp_grid[i][j] == Color.white){
					input[k++] = 1.0f;
				} else{
					temp = true;
					for(Fruit fruit:fget_fruits())
						if(fruit.fget_color() == temp_grid[i][j]) temp = false;
					if(temp) input[k++] = 1.0f;
					else input[(2*field_of_view+1)*(2*field_of_view+1)+(k++)] = 1.0f;
				}
			}
		}
		
		return input;
	}
	public String fget_information(){
		String data = new String();
		
		data += "Displaying information of snake " + fget_id() + ":\n";
		data += "Color: " + fget_color() + '\n';
		data += "Size: " + fget_coordinates().length + "\n";
		data += "Deaths: " + deaths + "\n";
		data += "Total scored: " + scored + "\n";
		data += "Score: " + score + "\n";
		data += "Movements left: " + movements_left + "\n";
		data += "Max score: " + max_score + "\n";
		data += "Position of the head:\nx " + fget_coordinates()[0][0] + "\ny " + fget_coordinates()[0][1] + "\n\n";
		data += "Last input:\n";
		
		int k = 0;
		float[][] walls = new float[(2*field_of_view+1)][(2*field_of_view+1)];
		float[][] foods = new float[(2*field_of_view+1)][(2*field_of_view+1)];
		if(last_input != null){
			for(int i = 0; i < (2*field_of_view+1); i++) for(int j = 0; j < (2*field_of_view+1); j++){
					foods[i][j] = last_input[(2*field_of_view+1)*(2*field_of_view+1) + k];
					walls[i][j] = last_input[k++];
			}
			for(int i = 0; i < (2*field_of_view+1); i++){
				for(int j = 0; j < (2*field_of_view+1); j++)
					data += walls[i][j] + " ";
				data += "   ";
				for(int j = 0; j < (2*field_of_view+1); j++)
					data += foods[i][j] + " ";
				data += '\n';
			}
			data += "\n";
		}
		
		data += "Last output: " + last_output + "\n\n";
		
		data += "Data about it's neural network:\n";
		data += ai.fget_data();
		
		return data;
	}
	public int fget_scored(){
		return scored;
	}
	public int fget_max_score(){
		return max_score;
	}
	public int fget_deaths(){
		return deaths;
	}
}
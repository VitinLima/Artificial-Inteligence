import javax.swing.*;
import java.lang.Thread;
import java.awt.*;

public class Snake extends Entity{
	private AI_Mem_Draw ai;
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
	public Snake(Color new_color, Game_Object new_obj){
		fset_color(new_color);
		fset_obj(new_obj);
		setName("snake " + getId());
		
		max_movements = 100;
		deaths = 0;
		scored = 0;
		max_score = 0;
		
		field_of_view = 2;
		ai = new AI_Mem_Draw(new int[]{2*(2*field_of_view+1)*(2*field_of_view+1), 16, 4}, field_of_view);
		
		last_output = -1;
		last_input = null;
		
		fspawn();
	}
	
	@Override
	public boolean faction(){
		if(!is_ready()) return true;
		
		float[] input = fget_surrounding();
		ai.fset_input(input);
		int choice = ai.fstart();
		
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
		
		if(fdeath_event(temp_x, temp_y)){
			fset_coordinates(new int[0][0]);
			return true;
		}
		
		fmove(temp_x, temp_y);
		last_input = input;
		last_output = choice;
		return true;
	}
	private boolean is_ready(){
		if(fget_coordinates().length == 0){
			fkill();
			return false;
		}
		return true;
	}
	private void fkill(){
		ai.fback_propagate(false);
		fspawn();
		deaths++;
	}
	private boolean fdeath_event(int x, int y){
		if(x < 0 || x == fget_obj().fget_map().fget_size_x() || y < 0 || y == fget_obj().fget_map().fget_size_y())
			return true;
		
		long cur_id = fget_obj().fget_map().fget_id(x, y);
		
		if(cur_id == 0) return false;
		if(cur_id > 0){
			Snake[] ref = fget_obj().fget_snakes();
			for(int i = 0; i < ref.length; i++){
				if(ref[i].getId() == cur_id){
					if(ref[i].fget_score() < fget_score()){
						ref[i].fset_fruits();
						fgrow();
						return false;
					} else{
						ref[i].fgrow();
						return true;
					}
				}
			}
		}
		fgrow();
		return false;
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
		if(getPriority() > 1)
			setPriority(getPriority()-1);
	}
	private void fmove(int x, int y){
		int[][] new_coordinates = new int[fget_coordinates().length][2];
		for(int i = new_coordinates.length-1; i > 0; i--)
			new_coordinates[i] = fget_coordinates()[i-1];
		new_coordinates[0][0] = x;
		new_coordinates[0][1] = y;
		fset_coordinates(new_coordinates);
		
		movements_left--;
		if(movements_left == 0)
			fset_coordinates(new int[0][0]);
	}
	private void fspawn(){
		ai.ferase_memories();
		last_input = null;
		last_output = -1;
		movements_left = max_movements;
		if(score > max_score)
			max_score = score;
		score = 0;
		int x = 0;
		int y = 0;
		setPriority(10);
		
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
	private float[] fget_surrounding(){
		int x = fget_coordinates()[0][0];
		int y = fget_coordinates()[0][1];
		float[] input = new float[(2*field_of_view+1)*(2*field_of_view+1)*2];
		long cur_id;
		int k = 0;
		Snake[] ref = fget_obj().fget_snakes();
		
		for(int i = y - field_of_view; i <= y + field_of_view; i++){
			for(int j = x - field_of_view; j <= x + field_of_view; j++){
				if(i < 0 || i >= fget_obj().fget_map().fget_size_x() || j < 0 || j >= fget_obj().fget_map().fget_size_y()){
					input[(2*field_of_view+1)*(2*field_of_view+1) + k] = 0.0f;
					input[k++] = 1.0f;
				} else{
					cur_id = fget_obj().fget_map().fget_id(j, i);
					if(cur_id == 0){
						input[(2*field_of_view+1)*(2*field_of_view+1) + k] = 0.0f;
						input[k++] = 0.0f;
					} else{
						if(cur_id < 0){
							input[(2*field_of_view+1)*(2*field_of_view+1) + k] = 1.0f;
							input[k++] = 0.0f;
						} else{
							for(Snake s:ref){
								if(s.getId() == cur_id){
									if(s.fget_score() < fget_score()){
										input[(2*field_of_view+1)*(2*field_of_view+1) + k] = 1.0f;
										input[k++] = 0.0f;
									} else{
										input[(2*field_of_view+1)*(2*field_of_view+1) + k] = 0.0f;
										input[k++] = 1.0f;
									}
									break;
								}
							}
						}
					}
				}
			}
		}
		
		boolean temp;
		
		return input;
	}
	public String fget_information(){
		String data = new String();
		data += "Displaying information of snake " + getId() + ":\n";
		String red = Integer.toHexString(fget_color().getRed());
		String green = Integer.toHexString(fget_color().getGreen());
		String blue = Integer.toHexString(fget_color().getBlue());
		String s = new String("#" + 
			(red.length() == 1? "0" + red : red) +
			(green.length() == 1? "0" + green : green) +
			(blue.length() == 1? "0" + blue : blue));
		s = "<font size='5' color=" + s + ">" + fget_color() + " -> \u25A0" + "</font>";
		data += "Color: " + s + "\n";
		data += "Size: " + fget_coordinates().length + "\n";
		data += "Score: " + score + "\n";
		if(deaths > 0)
			data += "Medium: " + max_score/deaths + "\n";
		else
			data += "Medium: " + max_score + "\n";
		data += "Max score: " + max_score + "\n";
		data += "Total scored: " + scored + "\n";
		data += "Movements left: " + movements_left + "\n";
		data += "Deaths: " + deaths + "\n";
		if(fget_coordinates().length > 0)
			data += "Position of the head:\nx " + fget_coordinates()[0][0] + "\ny " + fget_coordinates()[0][1] + "\n\n";
		else
			data += "Position of the head:\nx " + "null" + "\ny " + "null" + "\n\n";
		
		data += "Current input:\n\n";
		if(fget_coordinates().length > 0){
			float[] field = fget_surrounding();
			int k = 0;
			int l = 0;
			for(int i = 0; i < 2*field_of_view+1; i++){
				for(int j = 0; j < 2*field_of_view+1; j++)
					data += field[k++] + " ";
				data += "| ";
				for(int j = 0; j < 2*field_of_view+1; j++)
					data += field[(2*field_of_view+1)*(2*field_of_view+1) + l++] + " ";
				data += '\n';
			}
		} else{
			for(int i = 0; i < 2*field_of_view+1; i++){
				for(int j = 0; j < 2*field_of_view+1; j++)
					data += "null ";
				data += "| ";
				for(int j = 0; j < 2*field_of_view+1; j++)
					data += "null ";
				data += '\n';
			}
		}
		//data += "\n\n";
		
		//data += "Data about it's neural network:\n";
		//data += ai.fget_data();
		
		return data;
	}
	public int fget_score(){
		return score;
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
	public void fset_gif(JPanel panel, int source){
		ai.fset_gif(panel, source);
	}
	public void fdisable_gif(){
		ai.fdisable_gif();
	}
	public int fget_number_of_neurons(){
		return ai.fget_number_of_neurons();
	}
}
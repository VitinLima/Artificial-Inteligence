import javax.swing.*;
import java.lang.Thread;
import java.awt.*;

public class Snake extends Entity{
	private NeuralNetwork neural_network;
	private int opt;
	private int field_of_view;
	private int movements_left;
	private int max_movements;
	private int deaths;
	private int scored;
	private int score;
	private int max_score;
	private float[] last_input;
	private float[] last_output;
	public Snake(int new_id, Color new_color, Object new_game_obj, Object new_master_obj){
		fset_id(new_id);
		fset_color(new_color);
		fset_obj(new Object());
		fset_game_obj(new_game_obj);
		fset_master_obj(new_master_obj);
		
		max_movements = 36;
		deaths = 0;
		scored = 0;
		max_score = 0;
		
		field_of_view = 1;
		neural_network = new NeuralNetwork(2*(2*field_of_view+1)*(2*field_of_view+1), 4);
		/*neural_network = new NeuralNetwork(new int[]{2*(2*field_of_view+1)*(2*field_of_view+1), 4});
		neural_network.neurons[18].fadd_dentrite(18);
		neural_network.neurons[19].fadd_dentrite(18);
		neural_network.neurons[18].fadd_dentrite(19);
		neural_network.neurons[20].fadd_dentrite(18);*/
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
			fmove();
			//System.out.println("Working..snake " + fget_id());
			synchronized(fget_master_obj()){
				fget_master_obj().notify();
			}
		}
	}
	public void fmove(){
		last_input = fget_surrounding();
		//last_output = new float[]{1.0f, 0.0f, 0.0f, 0.0f};
		System.out.println("starting neural network");
		last_output = neural_network.fget_output(last_input);
		System.out.println("closing neural network");
		try{
			sleep(5000);
		} catch(Exception e){
			System.out.println(e);
		}
		float[] output = last_output;
		int choice = 0;
		for(int i = 1; i < output.length; i++)
			if(output[i] > output[choice])
				choice = i;
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
			fkill(choice);
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
					//System.out.println("Fruit eaten!");
				}
			if(temp){
				fkill(choice);
				return;
			}
			else{
				fgrow(choice);
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
			fkill(choice);
	}
	public void fkill(){
		deaths++;
		frespawn();
	}
	public void fkill(int choice){
		float[] incentive = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
		incentive[choice] = 0.0f;
		neural_network.fmodify(incentive);
		deaths++;
		frespawn();
	}
	public void fgrow(){
		int[][] new_coordinates = new int[fget_coordinates().length+1][2];
		for(int i = 0; i < new_coordinates.length-1; i++)
			new_coordinates[i] = fget_coordinates()[i];
		new_coordinates[new_coordinates.length-1] = new_coordinates[new_coordinates.length-2];
		fset_coordinates(new_coordinates);
		scored++;
		score++;
		movements_left = max_movements;
	}
	public void fgrow(int choice){
		float[] incentive = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
		incentive[choice] = 1.0f;
		neural_network.fmodify(incentive);
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
		//System.out.println("Respawning snake " + fget_id());
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
		if(last_output != null){
			data += "Last output:\n";
			for(float f:last_output)
				data += f + " ";
			data += "\n\n";
		}
		
		float[] input = fget_surrounding();
		//float[] output = new float[]{1.0f, 0.0f, 0.0f, 0.0f};//
		float[] output = neural_network.fget_output(input);
		data += "current input:\n";
		k = 0;
		for(int i = 0; i < (2*field_of_view+1); i++) for(int j = 0; j < (2*field_of_view+1); j++){
				foods[i][j] = input[(2*field_of_view+1)*(2*field_of_view+1) + k];
				walls[i][j] = input[k++];
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
		data += "current output:\n";
		for(float f:output)
			data += f + " ";
		data += "\n\n";
		data += "Data about it's neural network:\n";
		data += neural_network.fprint_network_information();
		return data;
	}
}
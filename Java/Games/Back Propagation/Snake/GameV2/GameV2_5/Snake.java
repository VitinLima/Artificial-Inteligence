import java.awt.*;

public class Snake{
	private int id;
	private Color color;
	private int[][] coordinates;
	private NeuralNetwork neural;
	private float[] surrounding;
	private int opt;
	private int field_of_view;
	private int movements_left;
	private int max_movements;
	private Color[] snake_keys;
	private Color[] fruit_keys;
	private int deaths;
	private int scored;
	private int score;
	private int max_score;
	public Snake(int new_id, Color new_color, Color[][] grid){
		id = new_id;
		color = new_color;
		coordinates = new int[1][2];
		boolean temp = true;
		while(temp){
			temp = false;
			coordinates[0][0] = (int)((float)Math.random()*(float)(grid.length-1));
			coordinates[0][1] = (int)((float)Math.random()*(float)(grid[0].length-1));
			if(grid[coordinates[0][0]][coordinates[0][1]] != Color.black)
				temp = true;
		}
		field_of_view = 3;
		surrounding = new float[(2*field_of_view+1)*(2*field_of_view+1)*3*2];
		neural = new NeuralNetwork();
		neural.Basic_Constructor_1(new int[]{(2*field_of_view+1)*(2*field_of_view+1)*3*2, 4});
		
		max_movements = 36;
		movements_left = max_movements;
		deaths = 0;
		scored = 0;
		max_score = 0;
		score = 0;
	}
	public int fget_id(){
		return id;
	}
	public Color fget_color(){
		return color;
	}
	public int[][] fget_coordinates(){
		return coordinates;
	}
	public Color fmove(Color[][] grid){
		fget_surrounding(grid);
		float[] choice = neural.fget_output(surrounding);
		int temp_x = coordinates[0][0];
		int temp_y = coordinates[0][1];
		opt = 0;
		for(int i = 0; i < choice.length; i++)
			if(choice[i] > choice[opt])
				opt = i;
		switch(opt){
			case 0:
				temp_x++;
				break;
			case 1:
				temp_x--;
				break;
			case 2:
				temp_y++;
				break;
			case 3:
				temp_y--;
				break;
		}
		Color event;
		if(temp_x < 0 || temp_x >= grid.length || temp_y < 0 || temp_y >= grid[0].length || movements_left == -1)
			return Color.white;
		else
			event = grid[temp_x][temp_y];
		for(int i = coordinates.length-1; i > 0; i--){
			coordinates[i][0] = coordinates[i-1][0];
			coordinates[i][1] = coordinates[i-1][1];
		}
		coordinates[0][0] = temp_x;
		coordinates[0][1] = temp_y;
		movements_left--;
		return event;
	}
	public void fkill(Color[][] grid){
		deaths++;
		if(score>max_score)
			max_score = score;
		score = 0;
		float[] incentive = new float[]{1.0f,1.0f,1.0f,1.0f};
		incentive[opt] = 0.0f;
		neural.fmodify(incentive);
		
		for(int[] l:coordinates)
			grid[l[0]][l[1]] = Color.black;
		coordinates = new int[1][2];
		boolean temp = true;
		while(temp){
			temp = false;
			coordinates[0][0] = (int)((float)Math.random()*(float)(grid.length-1));
			coordinates[0][1] = (int)((float)Math.random()*(float)(grid[0].length-1));
			if(grid[coordinates[0][0]][coordinates[0][1]] != Color.black)
				temp = true;
		}
		movements_left = max_movements;
	}
	public void fgrow(){
		scored++;
		score++;
		float[] incentive = new float[]{0.0f,0.0f,0.0f,0.0f};
		incentive[opt] = 1.0f;
		neural.fmodify(incentive);
		int[][] temp = coordinates.clone();
		coordinates = new int[coordinates.length+1][2];
		int k = 0;
		for(int[] l:temp)
			coordinates[k++] = l.clone();
		coordinates[coordinates.length-1] = coordinates[coordinates.length-2].clone();
		movements_left = max_movements;
	}
	private void fget_surrounding(Color[][] grid){
		Color[][] snake_surrounding_grid = new Color[(2*field_of_view+1)][(2*field_of_view+1)];
		Color[][] fruit_surrounding_grid = new Color[(2*field_of_view+1)][(2*field_of_view+1)];
		boolean temp;
		int n = 0;
		int m;
		for(int i = coordinates[0][0]-field_of_view; i <= coordinates[0][0]+field_of_view; i++){
			m = 0;
			for(int j = coordinates[0][1]-field_of_view; j <= coordinates[0][1]+field_of_view; j++){
				if(i < 0 || i >= grid.length || j < 0 || j >= grid[0].length){
					snake_surrounding_grid[n][m] = Color.white;
					fruit_surrounding_grid[n][m++] = Color.black;
				} else{
					if(grid[i][j] == Color.black){
						snake_surrounding_grid[n][m] = Color.black;
						fruit_surrounding_grid[n][m++] = Color.black;
					} else{
						temp = false;
						for(Color c:fruit_keys) if(c == grid[i][j]) temp = true;
						if(temp){
							snake_surrounding_grid[n][m] = Color.black;
							fruit_surrounding_grid[n][m++] = grid[i][j];
						} else{
							snake_surrounding_grid[n][m] = grid[i][j];
							fruit_surrounding_grid[n][m++] = Color.black;
						}
					}
				}
			}
			n++;
		}
		int k = 0;
		for(int i = 0; i < (2*field_of_view+1); i++) for(int j = 0; j < (2*field_of_view+1); j++){
			surrounding[k++] = (float)snake_surrounding_grid[i][j].getRed();
			surrounding[k++] = (float)snake_surrounding_grid[i][j].getGreen();
			surrounding[k++] = (float)snake_surrounding_grid[i][j].getBlue();
			surrounding[k++] = (float)fruit_surrounding_grid[i][j].getRed();
			surrounding[k++] = (float)fruit_surrounding_grid[i][j].getGreen();
			surrounding[k++] = (float)fruit_surrounding_grid[i][j].getBlue();
		}
		for(int i = 0; i < surrounding.length; i++)
			surrounding[i] /= 255.0f;
	}
	public String fget_information(){
		String data = new String();
		data += "Displaying information of snake " + id + ":\n";
		data += "Size: " + coordinates.length + "\n";
		data += "Deaths: " + deaths + "\n";
		data += "Total scored: " + scored + "\n";
		data += "Score: " + score + "\n";
		data += "Max score: " + max_score + "\n";
		data += "Position of the head:\nx " + coordinates[0][0] + "\ny " + coordinates[0][1] + "\n";
		data += "Data about it's neural network:\n";
		data += neural.fprint_network_information();
		data += "Last input:\n";
		float[][] snake_grid_red, snake_grid_green, snake_grid_blue, fruit_grid_red, fruit_grid_green, fruit_grid_blue;
		snake_grid_red = new float[(2*field_of_view+1)][(2*field_of_view+1)];
		snake_grid_green = new float[(2*field_of_view+1)][(2*field_of_view+1)];
		snake_grid_blue = new float[(2*field_of_view+1)][(2*field_of_view+1)];
		fruit_grid_red = new float[(2*field_of_view+1)][(2*field_of_view+1)];
		fruit_grid_green = new float[(2*field_of_view+1)][(2*field_of_view+1)];
		fruit_grid_blue = new float[(2*field_of_view+1)][(2*field_of_view+1)];
		int k = 0;
		for(int i = 0; i < (2*field_of_view+1); i++)
			for(int j = 0; j < (2*field_of_view+1); j++){
				snake_grid_red[i][j] = surrounding[k++];
				snake_grid_green[i][j] = surrounding[k++];
				snake_grid_blue[i][j] = surrounding[k++];
				fruit_grid_red[i][j] = surrounding[k++];
				fruit_grid_green[i][j] = surrounding[k++];
				fruit_grid_blue[i][j] = surrounding[k++];
			}
		for(int i = 0; i < (2*field_of_view+1); i++){
			for(int j = 0; j < (2*field_of_view+1); j++)
				data += (float)((int)(snake_grid_red[i][j]*100.0f))/100.0f + " ";
			data += " ";
			for(int j = 0; j < (2*field_of_view+1); j++)
				data += (float)((int)(snake_grid_green[i][j]*100.0f))/100.0f + " ";
			data += " ";
			for(int j = 0; j < (2*field_of_view+1); j++)
				data += (float)((int)(snake_grid_blue[i][j]*100.0f))/100.0f + " ";
			data += " ";
			for(int j = 0; j < (2*field_of_view+1); j++)
				data += (float)((int)(fruit_grid_red[i][j]*100.0f))/100.0f + " ";
			data += " ";
			for(int j = 0; j < (2*field_of_view+1); j++)
				data += (float)((int)(fruit_grid_green[i][j]*100.0f))/100.0f + " ";
			data += " ";
			for(int j = 0; j < (2*field_of_view+1); j++)
				data += (float)((int)(fruit_grid_blue[i][j]*100.0f))/100.0f + " ";
			data += '\n';
		}
		return data;
	}
	public void fset_snake_keys(Color[] new_snake_keys){
		snake_keys = new_snake_keys.clone();
	}
	public void fset_fruit_keys(Color[] new_fruit_keys){
		fruit_keys = new_fruit_keys.clone();
	}
}
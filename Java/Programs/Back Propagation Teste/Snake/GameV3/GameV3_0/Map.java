import java.awt.*; 
	
public class Map{
	private Color[][] grid;
	
	public Map(Object new_game_obj, Object new_master_obj){
		grid = new Color[5][5];
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[0].length; j++)
				grid[i][j] = Color.black;
	}
	
	public int fget_size_x(){
		return grid.length;
	}
	
	public int fget_size_y(){
		return grid[0].length;
	}
	
	public Color[][] fget_grid(){
		return grid.clone();
	}
	
	public Color fget_pixel(int Px, int Py){
		return grid[Px][Py];
	}
	
	public void fset_pixel(Color new_color, int px, int py){
		grid[px][py] = new_color;
	}
	
	public void frefresh_map(Color color, int[][] coordinates){
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[0].length; j++)
				if(grid[i][j] == color) grid[i][j] = Color.black;
		for(int[] xy:coordinates)
			grid[xy[0]][xy[1]] = color;
	}
}
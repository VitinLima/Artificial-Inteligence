import java.awt.*; 
	
public class Map extends Thread{
	private Color[][] grid;
	
	public Map(int map_size_x, int map_size_y){
		grid = new Color[map_size_x][map_size_y];
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
		return grid;
	}
	
	public Color fget_pixel(int Px, int Py){
		return grid[Px][Py];
	}
	
	public void fset_pixel(Color new_color, int px, int py){
		grid[px][py] = new_color;
	}
	
	public void fupdate(Color color, int[][] coordinates){
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[0].length; j++)
				if(grid[i][j] == color) grid[i][j] = Color.black;
		for(int[] n:coordinates)
			grid[n[0]][n[1]] = color;
	}
}
import java.awt.*; 
	
public class Map{
	private Color[][] grid;
	private long[][] id;
	
	public Map(){
		int map_size = 28;
		grid = new Color[map_size][map_size];
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[0].length; j++)
				grid[i][j] = Color.black;
		id = new long[map_size][map_size];
		for(int i = 0; i < id.length; i++)
			for(int j = 0; j < id[0].length; j++)
				id[i][j] = 0;
	}
	
	public int fget_size_x(){
		return id.length;
	}
	
	public int fget_size_y(){
		return id[0].length;
	}
	
	public Color[][] fget_grid(){
		return grid.clone();
	}
	
	public Color fget_pixel(int x, int y){
		return grid[x][y];
	}
	
	public long fget_id(int x, int y){
		return id[x][y];
	}
	
	public void fadd(long n, Color color, int[][] coordinates){
		for(int i = 0; i < id.length; i++)
			for(int j = 0; j < id[0].length; j++)
				if(id[i][j] == n){
					id[i][j] = 0;
					grid[i][j] = Color.black;
				}
		for(int[] c:coordinates){
			id[c[0]][c[1]] = n;
			grid[c[0]][c[1]] = color;
		}
	}
}
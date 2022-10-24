package game;

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
		return grid;
	}
	
	public Color fget_pixel(int x, int y){
		return grid[y][x];
	}
	
	public long[][] fget_ids(){
		return id.clone();
	}
	
	public long fget_id(int x, int y){
		return id[y][x];
	}
	
	public void fadd(long n, Color color, int[][] coordinates){
		for(int i = 0; i < id.length; i++)
			for(int j = 0; j < id[0].length; j++)
				if(id[i][j] == n){
					id[i][j] = 0;
					grid[i][j] = Color.black;
				}
		for(int[] c:coordinates){
			id[c[1]][c[0]] = n;
			grid[c[1]][c[0]] = color;
		}
	}
	
	public void fadd_fruit(int x, int y){
		id[y][x] = -1;
		grid[y][x] = Color.white;
	}
}
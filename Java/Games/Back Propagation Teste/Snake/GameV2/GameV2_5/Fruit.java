import java.awt.*;

public class Fruit{
	private int id;
	private Color color;
	private int[][] coordinates;
	private Color[] snake_keys;
	private Color[] fruit_keys;
	private int timer;
	private int max_time;
	public Fruit(int new_id, Color new_color, Color[][] grid){
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
		max_time = 20;
		timer = max_time;
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
	public void frespawn(Color[][] grid){
		boolean temp = true;
		while(temp){
			temp = false;
			coordinates[0][0] = (int)((float)Math.random()*(float)(grid.length-1));
			coordinates[0][1] = (int)((float)Math.random()*(float)(grid[0].length-1));
			if(grid[coordinates[0][0]][coordinates[0][1]] != Color.black)
				temp = true;
		}
		timer = max_time;
	}
	public void fset_snake_keys(Color[] new_snake_keys){
		snake_keys = new_snake_keys;
	}
	public void fset_fruit_keys(Color[] new_fruit_keys){
		fruit_keys = new_fruit_keys;
	}
	public void fmove_timer(Color[][] grid){
		timer--;
		if(timer <= 0)
			frespawn(grid);
	}
}
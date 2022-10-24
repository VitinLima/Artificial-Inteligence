import java.awt.*;

public class Fruit{
	private int id;
	private Color color;
	private int[][] coordinates;
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
	}
	public int fget_if(){
		return id;
	}
	public Color fget_color(){
		return color;
	}
	public int[][] fget_coordinates(){
		return coordinates;
	}
	public void fset_coordinates(int[][] new_coordinates){
		coordinates = new_coordinates;
	}
}
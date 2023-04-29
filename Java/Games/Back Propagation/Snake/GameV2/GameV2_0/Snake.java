import java.awt.*;

public class Snake{
	private int id;
	private Color color;
	private int[][] coordinates;
	private NeuralNetwork neural;
	private float[] surrounding;
	private int opt;
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
		surrounding = new float[9*3];
		neural = new NeuralNetwork();
		//neural.Basic_Constructor_1(new int[]{9*3, 6, 6, 4});
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
		Color event = grid[temp_x][temp_y];
		for(int i = coordinates.length-1; i > 0; i--){
			coordinates[i][0] = coordinates[i-1][0];
			coordinates[i][1] = coordinates[i-1][1];
		}
		return event;
	}
	public void fkill(){
		float[] incentive = neural.fget_output(surrounding);
		incentive[opt] = 0.0f;
		neural.fmodify(incentive);
	}
	public void fgrow(){
		float[] incentive = neural.fget_output(surrounding);
		incentive[opt] = 1.0f;
		neural.fmodify(incentive);
	}
	private void fget_surrounding(Color[][] grid){
		Color[][] surrounding_grid = new Color[3][3];
		int n = 0;
		int m;
		for(int i = coordinates[0][0]-1; i < coordinates[0][0]+1; i++){
			m = 0;
			for(int j = coordinates[0][1]-1; j < coordinates[0][1]+1; j++){
				if(i < 0 || i >= grid.length || j < 0 || j >= grid[0].length)
					surrounding_grid[n][m++] = Color.white;
				else
					surrounding_grid[n][m++] = grid[i][j];
				}
			n++;
		}
		int k = 0;
		for(Color[] ci:surrounding_grid) for(Color cj:ci){
			surrounding[k++] = (float)cj.getRed();
			surrounding[k++] = (float)cj.getGreen();
			surrounding[k++] = (float)cj.getBlue();
		}
	}
}
import java.awt.*;

public class Entity extends Thread{
	private Object obj;
	private Object exit_obj;
	private Game_Object game_obj;
	private Object master_obj;
	
	private boolean is_to_exit = false;
	private boolean is_snake = true;
	private int id;
	private Color color;
	private int[][] coordinates;
	
	private Snake[] snakes;
	private Fruit[] fruits;
	private Map map;
	
	private Exit_Handle exit_handle;
	
	public int fget_id(){
		return id;
	}
	public void fset_id(int new_id){
		id = new_id;
	}
	public Color fget_color(){
		return color;
	}
	public void fset_color(Color new_color){
		color = new_color;
	}
	public int[][] fget_coordinates(){
		return coordinates;
	}
	public void fset_coordinates(int[][] new_coordinates){
		coordinates = new_coordinates;
		map.frefresh_map(color, coordinates);
	}
	public void fset_snakes(Snake[] new_snakes){
		snakes = new_snakes;
	}
	public Snake[] fget_snakes(){
		return snakes;
	}
	public void fset_fruits(Fruit[] new_fruits){
		fruits = new_fruits;
	}
	public Fruit[] fget_fruits(){
		return fruits;
	}
	public void fset_map(Map new_map){
		map = new_map;
	}
	public Map fget_map(){
		return map;
	}
	public Object fget_obj(){
		return obj;
	}
	public void fset_obj(Object new_obj){
		obj = new_obj;
	}
	public Object fget_exit_obj(){
		return exit_obj;
	}
	public void fset_exit_obj(Object new_exit_obj){
		exit_obj = new_exit_obj;
	}
	public Game_Object fget_game_obj(){
		return game_obj;
	}
	public void fset_game_obj(Game_Object new_game_obj){
		game_obj = new_game_obj;
	}
	public Object fget_master_obj(){
		return master_obj;
	}
	public void fset_master_obj(Object new_master_obj){
		master_obj = new_master_obj;
	}
	public void fset_fruit(){
		is_snake = false;
	}
	public void faction(){
	}
	
	public void run(){
		exit_handle = new Exit_Handle();
		exit_handle.start();
		while(!is_to_exit){
			game_obj.fwait();
			faction();
			synchronized(master_obj){
				master_obj.notify();
			}
		}
	}
	
	private class Exit_Handle extends Thread{
		public void run(){
			if(is_snake)
				setName("Exit for snake " + id);
			else
				setName("Exit for fruit " + id);
			synchronized(fget_exit_obj()){
				try{
					fget_exit_obj().wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
			is_to_exit = true;
		}
	}
}
import java.awt.*;

public class Entity extends Thread{
	private int id;
	private Color color;
	private int[][] coordinates;
	
	private Game_Object obj;
	
	private Snake[] snakes;
	private Fruit[] fruits;
	private Map map;
	
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
	public void fset_obj(Game_Object new_obj){
		obj = new_obj;
	}
	public boolean faction(){
		return false;
	}
	
	public void run(){
		boolean temp =  true;
		while(temp){
			synchronized(obj){
				obj.notifyAll();
				if(!faction() || !obj.fis_running())
					temp = false;
				try{
					obj.wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
		}
		synchronized(obj){
			obj.notifyAll();
		}
		System.out.println("Thread " + Thread.currentThread() + " closed");
	}
}
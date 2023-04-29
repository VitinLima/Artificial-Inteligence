import javax.swing.*;
import java.lang.Thread;
import java.awt.*;
import java.util.Random;

public class Thread_Handler{
	private Game_Object obj;
	
	private Map map;
	private Snake[] snakes;
	private Fruit[] fruits;
	
	public Thread_Handler(Game_Object new_obj){
		obj = new_obj;
		
		int snake_count = 5;
		int fruit_count = 15;
		
		Random rand = new Random();
		
		map = new Map();
		
		snakes = new Snake[snake_count];
		for(int i = 0; i < snakes.length; i++){
			snakes[i] = new Snake(i, new Color(rand.nextInt(253) + 1, rand.nextInt(253) + 1, rand.nextInt(253) + 1), map, obj);
		}
		
		fruits = new Fruit[fruit_count];
		for(int i = 0; i < fruits.length; i++){
			fruits[i] = new Fruit(i, new Color(rand.nextInt(253) + 1, rand.nextInt(253) + 1, rand.nextInt(253) + 1), map, obj);
		}
		
		int k = 0;
		for(int i= 0; i < snakes.length; i++){
			snakes[i].fset_snakes(snakes);
			snakes[i].fset_fruits(fruits);
			snakes[i].setName("Snake " + i);
			snakes[i].start();
		}
		for(int i= 0; i < fruits.length; i++){
			fruits[i].fset_snakes(snakes);
			fruits[i].fset_fruits(fruits);
			fruits[i].fset_map(map);
			fruits[i].setName("Fruit " + i);
			fruits[i].start();
		}
	}
	public Map fget_map(){
		return map;
	}
	public Snake[] fget_snakes(){
		return snakes;
	}
	public Fruit[] fget_fruits(){
		return fruits;
	}
	public void fclose(){
		System.out.println("Shutting down...");
		System.out.println("\nWait for threads:\n");
		Thread[] th = new Thread[Thread.activeCount()];
		Thread.enumerate(th);
		for(int i = 0; i < th.length; i++)
			System.out.println("thread number " + i + ": " + th[i] + " state: " + th[i].getState());
	}
}
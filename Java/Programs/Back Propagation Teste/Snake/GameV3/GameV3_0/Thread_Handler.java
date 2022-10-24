import javax.swing.*;
import java.lang.Thread;
import java.awt.*;
import java.util.Random;

public class Thread_Handler extends Thread{
	private Object obj;
	private Object game_obj;
	private Object master_obj;
	private Map map;
	private Snake[] snakes;
	private Fruit[] fruits;
	
	private Game_Notify game_notify;
	private Master_Notify master_notify;
	
	public Thread_Handler(){
		int snake_count = 1;
		int fruit_count = 1;
		
		Random rand = new Random();
		obj = new Object();
		game_obj = new Object();
		master_obj = new Object();
		
		game_notify = new Game_Notify();
		master_notify = new Master_Notify();
		
		map = new Map(game_obj, master_obj);
		
		snakes = new Snake[snake_count];
		for(int i = 0; i < snakes.length; i++){
			snakes[i] = new Snake(i, new Color(rand.nextInt(253) + 1, rand.nextInt(253) + 1, rand.nextInt(253) + 1), game_obj, master_obj);
		}
		
		fruits = new Fruit[fruit_count];
		for(int i = 0; i < fruits.length; i++){
			fruits[i] = new Fruit(i, new Color(rand.nextInt(253) + 1, rand.nextInt(253) + 1, rand.nextInt(253) + 1), game_obj, master_obj);
		}
		for(int i= 0; i < snakes.length; i++){
			snakes[i].fset_snakes(snakes);
			snakes[i].fset_fruits(fruits);
			snakes[i].fset_map(map);
			snakes[i].start();	
		}
		for(int i= 0; i < fruits.length; i++){
			fruits[i].fset_snakes(snakes);
			fruits[i].fset_fruits(fruits);
			fruits[i].fset_map(map);
			fruits[i].start();
		}
		
		game_notify.start();
		master_notify.start();
		start();
	}
	public void run(){
		System.out.println("Starting thread_handler");
		while(true){
			synchronized(obj){
				try{
					obj.wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
			//System.out.println("Working..Thread handler");
			while(master_notify.getState() != Thread.State.valueOf(new String("WAITING")));
			while(game_notify.getState() != Thread.State.valueOf(new String("WAITING")));
			synchronized(game_obj){
				try{
					game_obj.notify();
				} catch(Exception e){
					System.out.println(e);
				}
			}
		}
	}
	public Object fget_game_obj(){
		return game_obj;
	}
	public Object fget_master_obj(){
		return master_obj;
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
	private class Master_Notify extends Thread{
		public void run(){
			System.out.println("Starting master_notify");
			while(true){
				while(Thread_Handler.this.getState() != Thread.State.valueOf(new String("WAITING")));
				synchronized(obj){
					obj.notify();
				}
				synchronized(master_obj){
					try{
						master_obj.wait();
					} catch(Exception e){
						System.out.println(e);
					}
				}
			}
		}
	}
	private class Game_Notify extends Thread{
		public void run(){
			System.out.println("Starting game_notify");
			while(true){
				synchronized(game_obj){
					try{
						game_obj.wait();
					} catch(Exception e){
						System.out.println(e);
					}
				}
				synchronized(master_obj){
					master_obj.notify();
				}
			}
		}
	}
}
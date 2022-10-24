import javax.swing.*;
import java.lang.Thread;
import java.awt.*;
import java.util.Random;

public class Thread_Handler extends Thread{
	private Object obj;
	private Object exit_obj;
	private Game_Object game_obj;
	private Object master_obj;
	
	private boolean is_to_exit;
	
	private Map map;
	private Snake[] snakes;
	private Fruit[] fruits;
	
	private Master_Notify master_notify;
	
	public Thread_Handler(){
		int snake_count = 1;
		int fruit_count = 5;
		
		Random rand = new Random();
		obj = new Object();
		exit_obj = new Object();
		game_obj = new Game_Object();
		master_obj = new Object();
		
		map = new Map();
		
		snakes = new Snake[snake_count];
		for(int i = 0; i < snakes.length; i++){
			snakes[i] = new Snake(i, new Color(rand.nextInt(253) + 1, rand.nextInt(253) + 1, rand.nextInt(253) + 1), exit_obj, game_obj, master_obj);
		}
		
		fruits = new Fruit[fruit_count];
		for(int i = 0; i < fruits.length; i++){
			fruits[i] = new Fruit(i, new Color(rand.nextInt(253) + 1, rand.nextInt(253) + 1, rand.nextInt(253) + 1), exit_obj, game_obj, master_obj);
		}
		for(int i= 0; i < snakes.length; i++){
			snakes[i].fset_snakes(snakes);
			snakes[i].fset_fruits(fruits);
			snakes[i].fset_map(map);
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
		
		
		is_to_exit = false;
		
		Exit_Handle exit_handle = new Exit_Handle();
		exit_handle.start();
		start();
	}
	public void run(){
		setName("Thread Handler");
		while(!is_to_exit || game_obj.fget_count() != 0){
			master_notify = new Master_Notify();
			while(master_notify.getState() != Thread.State.valueOf(new String("NEW")));
			master_notify.start();
			while(master_notify.getState() != Thread.State.valueOf(new String("WAITING")));
			
			game_obj.fnotify();
			
			try{
				master_notify.join();
			} catch(Exception e){
				System.out.println(e);
			}
		}
		System.out.println("Shutting down...");
		System.out.println("\nWait for threads:\n");
		Thread[] th = new Thread[Thread.activeCount()];
		Thread.enumerate(th);
		for(int i = 0; i < Thread.activeCount(); i++)
			System.out.println("thread number " + i + ": " + th[i]);
	}
	public Game_Object fget_game_obj(){
		return game_obj;
	}
	public Object fget_master_obj(){
		return master_obj;
	}
	public Object fget_exit_obj(){
		return exit_obj;
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
			setName("Master object");
			synchronized(master_obj){
				try{
					master_obj.wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
		}
	}
	
	private class Exit_Handle extends Thread{
		public void run(){
			setName("Exit for Thread Handler");
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
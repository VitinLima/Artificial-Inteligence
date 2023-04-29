import javax.swing.*;
import java.lang.Thread;
import java.awt.*;
import java.util.Random;

public class Thread_Handler{
	private Object obj;
	private Object exit_obj;
	private Game_Object game_obj;
	private Object master_obj;
	
	private boolean is_to_exit;
	
	private Map map;
	private Snake[] snakes;
	private Fruit[] fruits;
	
	private Master_Notify master_notify;
	private Thread[] threads;
	private String[] thread_names;
	
	public Thread_Handler(Object new_exit_obj, Game_Object new_game_obj, Object new_master_obj){
		int snake_count = 5;
		int fruit_count = 15;
		
		thread_ids = new String[snake_count + fruit_count];
		
		Random rand = new Random();
		obj = new Object();
		exit_obj = new_exit_obj;
		game_obj = new_game_obj;
		master_obj = new_master_obj;
		
		map = new Map();
		
		snakes = new Snake[snake_count];
		for(int i = 0; i < snakes.length; i++){
			snakes[i] = new Snake(i, new Color(rand.nextInt(253) + 1, rand.nextInt(253) + 1, rand.nextInt(253) + 1), exit_obj, game_obj, master_obj);
		}
		
		fruits = new Fruit[fruit_count];
		for(int i = 0; i < fruits.length; i++){
			fruits[i] = new Fruit(i, new Color(rand.nextInt(253) + 1, rand.nextInt(253) + 1, rand.nextInt(253) + 1), exit_obj, game_obj, master_obj);
		}
		
		int k = 0;
		for(int i= 0; i < snakes.length; i++){
			snakes[i].fset_snakes(snakes);
			snakes[i].fset_fruits(fruits);
			snakes[i].fset_map(map);
			snakes[i].setName("Snake " + i);
			snakes[i].start();
			thread_names[k++] = snakes[i].getName();
		}
		for(int i= 0; i < fruits.length; i++){
			fruits[i].fset_snakes(snakes);
			fruits[i].fset_fruits(fruits);
			fruits[i].fset_map(map);
			fruits[i].setName("Fruit " + i);
			fruits[i].start();
			thread_names[k++] = fruits[i].getName();
		}
		
		
		is_to_exit = false;
		
		Exit_Handle exit_handle = new Exit_Handle();
		exit_handle.start();
	}
	public void fstart(){
		while(frun());
		
		System.out.println("Shutting down...");
		System.out.println("\nWait for threads:\n");
		thread = new Thread[Thread.activeCount()];
		Thread.enumerate(th);
		for(int i = 0; i < Thread.activeCount(); i++)
			System.out.println("thread number " + i + ": " + thread[i] + " state: " + thread[i].getState());
	}
	public boolean frun(){
		threads = new Thread[Thread.activeCount()];
		Thread.enumerate(threads);
		
		master_notify = new Master_Notify();
		while(master_notify.getState() != Thread.State.valueOf(new String("NEW")));
		master_notify.start();
		while(master_notify.getState() != Thread.State.valueOf(new String("WAITING")));
		
		boolean temp = true;
		while(temp){
			for(Thread th:threads)
				if(th.getState() == Thread.State.valueOf(new String("WAITING")))
					for(String s:thread_names)
						if(s.equals(th.getName())) temp = false;
		}
		game_obj.fnotify();
		
		try{
			master_notify.join();
		} catch(Exception e){
			System.out.println(e);
		}
		return true;
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
			synchronized(exit_obj){
				try{
					exit_obj.wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
			is_to_exit = true;
		}
	}
}
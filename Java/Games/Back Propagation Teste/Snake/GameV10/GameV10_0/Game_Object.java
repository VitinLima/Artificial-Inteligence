import javax.swing.*;
import java.lang.Thread;
import java.awt.*;
import java.util.Random;

public class Game_Object extends Thread{
	private boolean running = true;
	private boolean exit = false;
	
	private int max_fruits = 30;
	private int fruit_qtt = 0;
	
	private Map map;
	private Snake[] snakes;
	
	public Game_Object(){
		int snake_count = 15;
		
		Random rand = new Random();
		
		map = new Map();
		
		snakes = new Snake[snake_count];
		for(int i = 0; i < snakes.length; i++){
			snakes[i] = new Snake(new Color(rand.nextInt(253) + 1, rand.nextInt(253) + 1, rand.nextInt(253) + 1), this);
			snakes[i].start();
		}
	}
	
	public boolean fis_running(){
		return running;
	}
	public void fexit(){
		running = false;
	}
	public void factivate(){
		boolean temp = true;
		while(fspawn_fruit());
		start();
		temp = true;
		while(temp){
			synchronized(this){
				notifyAll();
				if(Thread.activeCount() == 3)
					temp = false;
				try{
					wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
		}
		exit = true;
		synchronized(this){
			notifyAll();
		}
		System.out.println("Shutting down...");
		System.out.println("\nWait for threads:\n");
		Thread[] th = new Thread[Thread.activeCount()];
		Thread.enumerate(th);
		for(int i = 0; i < th.length; i++)
			System.out.println("thread number " + i + ": " + th[i] + " state: " + th[i].getState());
	}
	public boolean fspawn_fruit(){
		synchronized(this){
			notifyAll();
			if(!running)
				return false;
			if(fruit_qtt < max_fruits){
				(new Fruit(new Color(250, 250, 250), this)).start();
				fruit_qtt++;
			}
			try{
				wait();
			} catch(Exception e){
				System.out.println(e);
			}
		}
		return true;
	}
	public void run(){
		while(!exit)
			synchronized(this){
				notifyAll();
				try{
					wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
	}
	public Map fget_map(){
		return map;
	}
	public Snake[] fget_snakes(){
		return snakes;
	}
	public Thread fget_thread(long id){
		Thread[] th = new Thread[Thread.activeCount()];
		Thread.enumerate(th);
		for(Thread t:th) if(t.getId() == id) return t;
		return null;
	}
	public void fremove_fruit(){
		fruit_qtt--;
	}
}
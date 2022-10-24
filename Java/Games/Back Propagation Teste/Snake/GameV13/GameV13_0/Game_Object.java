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
		int snake_count = 5;
		
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
			fruit_qtt = 0;
			long[][] ids = map.fget_ids();
			for(int i = 0; i < ids.length; i++)
				for(int j = 0; j < ids[0].length; j++)
					if(ids[i][j] < 0) fruit_qtt++;
			if(fruit_qtt < max_fruits){
				fspawn_new_fruit();
			}
			try{
				wait();
			} catch(Exception e){
				System.out.println(e);
			}
		}
		return true;
	}
	private void fspawn_new_fruit(){
		int x = 0;
		int y = 0;
		boolean temp = true;
		while(temp){
			temp = false;
			x = (int)((float)Math.random()*((float)map.fget_size_x() - 1.0f));
			y = (int)((float)Math.random()*((float)map.fget_size_y() - 1.0f));
			if(map.fget_pixel(x,y) != Color.black)
				temp = true;
		}
		map.fadd_fruit(x, y);
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
	
	private Object frame_obj = new Object();
	private Object snake_obj = new Object();
	private int snakes_in_wait = 0;
	public void fnotify_snake(){
	}
	public void fwait_snake(){
	}
	public void fnotify_frame(){
	}
	public void fwait_frame(){
	}
}
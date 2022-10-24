import javax.swing.*;
import java.lang.Thread;
import java.awt.*;
	
public class Window_Map extends JComponent{
	private Object obj;
	private Object game_obj;
	private Object master_obj;
	private Object pause_obj;
	private Object hide_obj;
	
	private Color[][] grid;
	private int pixel_size;
	
	private boolean is_hidden;
	
	private Refresh refresh;
	
	public Window_Map(Color[][] new_grid, Object new_game_obj, Object new_master_obj, Object new_pause_obj, Object new_hide_obj){
		grid = new_grid;
		obj = new Object();
		game_obj = new_game_obj;
		master_obj = new_master_obj;
		pause_obj = new_pause_obj;
		hide_obj = new_hide_obj;
		
		pixel_size = 10;
		setPreferredSize(new Dimension(grid.length*pixel_size, grid[0].length*pixel_size));
		
		is_hidden = false;
		
		refresh = new Refresh();
		refresh.start();
		Hide_Thread hide_thread = new Hide_Thread();
		hide_thread.start();
		Pause_Thread pause_thread = new Pause_Thread();
		pause_thread.start();
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[0].length; j++)
				fDrawPixel(i,j,g);
		while(refresh.getState() != Thread.State.valueOf(new String("WAITING")));
		synchronized(obj){
			obj.notify();
		}
	}
	
	private void fDrawPixel(int px, int py, Graphics g){
		g.setColor(grid[px][py]);
		for(int i = px*pixel_size; i < px*pixel_size + pixel_size; i++)
			for(int j = py*pixel_size; j < py*pixel_size + pixel_size; j++)
				g.drawLine(i, j, i, j);
	}
	
	private class Refresh extends Thread{
		public void run(){
			while(true){
				synchronized(game_obj){
					try{
						game_obj.wait();
					} catch(Exception e){
						System.out.println(e);
					}
				}
				if(!is_hidden){
					repaint();
					synchronized(obj){
						try{
							obj.wait();
						} catch(Exception e){
							System.out.println(e);
						}
					}
					try{
						sleep(100);
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
	private class Pause_Thread extends Thread{
		public void run(){
			while(true){
				synchronized(pause_obj){
					try{
						pause_obj.wait();
						System.out.println("Paused pressed..");
					} catch(Exception e){
						System.out.println(e);
					}
					synchronized(game_obj){
						try{
							game_obj.wait();
						} catch(Exception e){
							System.out.println(e);
						}
					}
					try{
						pause_obj.wait();
						System.out.println("Paused pressed..");
					} catch(Exception e){
						System.out.println(e);
					}
					synchronized(master_obj){
						master_obj.notify();
					}
				}
			}
		}
	}
	private class Hide_Thread extends Thread{
		public void run(){
			while(true){
				synchronized(hide_obj){
					try{
						hide_obj.wait();
						System.out.println("Hidden pressed..");
					} catch(Exception e){
						System.out.println(e);
					}
					is_hidden = !is_hidden;
				}
			}
		}
	}
}
import javax.swing.*;
import java.lang.Thread;
import java.awt.*;
	
public class Component_Main_Map extends JPanel{
	private Object obj;
	
	private Color[][] grid;
	private int pixel_size;
	
	public Component_Main_Map(Color[][] new_grid){
		grid = new_grid;
		
		obj = new Object();
		
		pixel_size = 10;
		setPreferredSize(new Dimension(grid.length*pixel_size, grid[0].length*pixel_size));
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[0].length; j++)
				fDrawPixel(i,j,g);
		synchronized(obj){
			obj.notify();
		}
	}
	
	private void fDrawPixel(int py, int px, Graphics g){
		g.setColor(grid[py][px]);
		for(int i = px*pixel_size; i < px*pixel_size + pixel_size; i++)
			for(int j = py*pixel_size; j < py*pixel_size + pixel_size; j++)
				g.drawLine(i, j, i, j);
	}
	
	public void frefresh(){
		repaint();
		synchronized(obj){
			try{
				obj.wait();
			} catch(Exception e){
				System.out.println(e);
			}
		}
	}
}
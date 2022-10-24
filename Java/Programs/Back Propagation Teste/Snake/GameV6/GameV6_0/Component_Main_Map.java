import javax.swing.*;
import java.lang.Thread;
import java.awt.*;
	
public class Component_Main_Map extends JScrollPane{
	private Map_Picture map_picture;
	
	public Component_Main_Map(Color[][] new_grid){
		
		map_picture = new Map_Picture(new_grid);
		if(map_picture.getPreferredSize().getWidth() < 500 && map_picture.getPreferredSize().getHeight() < 500){
			setColumnHeaderView(map_picture);
			setPreferredSize(map_picture.getPreferredSize());
		} else{
			setViewportView(map_picture);
			setPreferredSize(new Dimension(500, 500));
		}
	}
	
	public void frefresh(){
		map_picture.frefresh();
	}
	
	private class Map_Picture extends JComponent{
		private Object obj;
		
		private Color[][] grid;
		private int pixel_size;
		
		public Map_Picture(Color[][] new_grid){
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
		
		private void fDrawPixel(int px, int py, Graphics g){
			g.setColor(grid[px][py]);
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
}
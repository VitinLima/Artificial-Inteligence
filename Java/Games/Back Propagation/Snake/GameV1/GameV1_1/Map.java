import java.awt.*; 
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Thread;
	
	public class Map extends Thread{
		private int MapSizeX;
		private int MapSizeY;
		private Color[][] MapGrid;
		private boolean isToClose;
		private boolean show;
		private GameWindow gameWindow;
		private Snake[] snakes;
		private Object locksnakes;
		private Object lockwindow;
		
		public Map(GameWindow newgameWindow, Snake[] newsnakes, Object newlocksnakes, Object newlockwindow){
			gameWindow = newgameWindow;
			snakes = newsnakes;
			locksnakes = newlocksnakes;
			lockwindow = newlockwindow;
			show = false;
			MapGrid = new Color[10][10];
			MapSizeX = MapGrid.length;
			MapSizeY = MapGrid[0].length;
			isToClose = false;
			for(int i = 0; i < MapGrid.length; i++)
				for(int j = 0; j < MapGrid[0].length; j++)
					MapGrid[i][j] = Color.black;
		}
		
		public int getMapSizeX(){
			return MapSizeX;
		}
		
		public int getMapSizeY(){
			return MapSizeY;
		}
		
		public Color[][] getGrid(){
			return MapGrid;
		}
		
		public Color getPixel(int Px, int Py){
			return MapGrid[Px][Py];
		}
		
		public void setPixel(Color newcolor, int Px, int Py){
			MapGrid[Px][Py] = newcolor;
		}
		
		public boolean isToClose(){
			return isToClose;
		}
		
		public void switchShow(){
			show = !show;
		}
		
		public void begin(){
			while(true){
				turn();
			}
		}
		
		private void turn(){
			System.out.println(snakes.length);
			/*for(Snake snake:snakes)
				if(!snake.isready()) return;
			if(show){
				try{
					Thread.sleep(100);
				} catch(Exception e){
					System.out.println(e);
				}
				gameWindow.repaint();
				synchronized(lockwindow){
					try{
						lockwindow.wait();
					} catch(Exception e){
						System.out.println(e);
					}
				}
			}
			synchronized(locksnakes){
				locksnakes.notifyAll();
			}*/
		}
	}
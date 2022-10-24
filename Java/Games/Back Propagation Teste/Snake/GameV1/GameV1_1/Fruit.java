import java.awt.*; 
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Thread;
	
	public class Fruit extends Thread{
		private int PosX;
		private int PosY;
		private int Type;
		private int Fruit_Id;
		private Color color;
		private Map map;
		
		public Fruit(int newFruit_Id, Color newcolor, Map newmap){
			Fruit_Id = newFruit_Id;
			color = newcolor;
			map = newmap;
			spawn();
		}
		
		public int getFruit_Id(){
			return Fruit_Id;
		}
		
		public Color getColor(){
			return color;
		}
		
		public void spawn(){
			boolean temp = true;
			while(temp){
				temp = false;
				PosX = (int)(Math.random()*(map.getMapSizeX()-1));
				PosY = (int)(Math.random()*(map.getMapSizeY()-1));
				if(map.getPixel(PosX, PosY) != Color.black)
					temp = true;
			}
			map.setPixel(color, PosX, PosY);
		}
		
		public int getPosX(){
			return PosX;
		}
		
		public int getPosY(){
			return PosY;
		}
		
		public void run(){
			
		}
	}
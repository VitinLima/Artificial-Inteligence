import java.awt.*; 
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Thread;

public class Game{
	private Map map;
	private GameWindow gameWindow;
	private Snake[] snakes;
	private Fruit[] fruits;
	
	private Object locksnakes;
	private Object lockwindow;
	
	public Game(){
		map = new Map(gameWindow, snakes, locksnakes, lockwindow);
		snakes = new Snake[1];
		fruits = new Fruit[1];
		locksnakes = new Object();
		lockwindow = new Object();
		
		Color tempColor = null;
		boolean tempFlag;
		
		for(int i = 0; i < snakes.length; i++){
			tempFlag = true;
			while(tempFlag){
				tempColor = new Color((int)(55 + Math.random()*145), (int)(55 + Math.random()*145), (int)(55 + Math.random()*145));
				tempFlag = false;
				for(int j = 0; j < i; j++)
					if(tempColor == snakes[j].getColor())
						tempFlag = true;
			}
			snakes[i] = new Snake(i, tempColor, map, gameWindow, snakes, fruits, locksnakes);
		}
		for(int i = 0; i < fruits.length; i++){
			tempFlag = true;
			while(tempFlag){
				tempColor = new Color((int)(55 + Math.random()*145), (int)(55 + Math.random()*145), (int)(55 + Math.random()*145));
				tempFlag = false;
				for(Snake snake:snakes)
					if(tempColor == snake.getColor())
						tempFlag = true;
				for(int j = 0; j < i; j++)
					if(tempColor == fruits[j].getColor())
						tempFlag = true;
			}
			fruits[i] = new Fruit(i, tempColor, map);
		}
		gameWindow = new GameWindow(10, map, snakes, lockwindow);
		
		for(int i = 0; i < snakes.length; i++)
			snakes[i].start();
		//map.begin();
	}
}
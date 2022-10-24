import java.awt.*; 
import java.lang.Thread;

public class Game extends Thread{
	Map map;
	Snake[] snakes;
	Fruit[] fruits;
	GameWindow game_window;
	boolean is_show;
	public static void main(String[] args){
		Game game = new Game();
	}
	public Game(){
		is_show = true;
		
		int snake_count = 1;
		int fruit_count = 1;
		int map_size_x = 28;
		int map_size_y = 28;
		Color temp_color = null;
		boolean temp_flag;
		
		map = new Map(map_size_x, map_size_y);
		
		snakes = new Snake[snake_count];
		for(int i = 0; i < snakes.length; i++){
			temp_flag = true;
			while(temp_flag){
				temp_flag = false;
				temp_color = new Color((int)((float)Math.random()*255.0f), (int)((float)Math.random()*255.0f), (int)((float)Math.random()*255.0f));
				for(int j = 0; j < i; j++)
					if(snakes[j].fget_color() == temp_color)
						temp_flag = false;
			}
			snakes[i] = new Snake(i, temp_color, map.fget_grid());
			map.fupdate(snakes[i].fget_color(), snakes[i].fget_coordinates());
		}
		
		fruits = new Fruit[fruit_count];
		for(int i = 0; i < fruits.length; i++){
			temp_flag = true;
			while(temp_flag){
				temp_flag = false;
				temp_color = new Color((int)((float)Math.random()*255.0f), (int)((float)Math.random()*255.0f), (int)((float)Math.random()*255.0f));
				for(Snake snake:snakes)
					if(snake.fget_color() == temp_color)
						temp_flag = false;
				for(int j = 0; j < i; j++)
					if(fruits[j].fget_color() == temp_color)
						temp_flag = false;
			}
			fruits[i] = new Fruit(i, temp_color, map.fget_grid());
			map.fupdate(fruits[i].fget_color(), fruits[i].fget_coordinates());
		}
		
		game_window = new GameWindow(map.fget_grid());
		start();
	}
	public void run(){
		Color event;
		while(true){
			is_show = game_window.fis_show();
			for(int i = 0; i < snakes.length; i++){
				event = snakes[i].fmove(map.fget_grid());
				map.fupdate(snakes[i].fget_color(), snakes[i].fget_coordinates());
			}
			if(is_show){
				try{
					Thread.sleep(100);
				} catch(Exception e){
					System.out.println(e);
				}
				game_window.fupdate(map.fget_grid());
			}
		}
	}
}
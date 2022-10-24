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
		int map_size_x = 5;
		int map_size_y = 5;
		Color temp_color = null;
		boolean temp_flag;
		
		map = new Map(map_size_x, map_size_y);
		
		snakes = new Snake[snake_count];
		for(int i = 0; i < snakes.length; i++){
			temp_flag = true;
			while(temp_flag){
				temp_flag = false;
				temp_color = new Color((int)((float)Math.random()*254.0f + 1.0f), (int)((float)Math.random()*254.0f + 1.0f), (int)((float)Math.random()*254.0f + 1.0f));
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
				temp_color = new Color((int)((float)Math.random()*254.0f + 1.0f), (int)((float)Math.random()*254.0f + 1.0f), (int)((float)Math.random()*254.0f + 1.0f));
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
		Keys keys = new Keys();
		for(int i = 0; i < snakes.length; i++){
			snakes[i].fset_snake_keys(keys.fget_snake_keys());
			snakes[i].fset_fruit_keys(keys.fget_fruit_keys());
		}
		for(int i = 0; i < fruits.length; i++){
			fruits[i].fset_snake_keys(keys.fget_snake_keys());
			fruits[i].fset_fruit_keys(keys.fget_fruit_keys());
		}
		start();
	}
	public void run(){
		Color event;
		boolean temp_flag;
		while(true){
			game_window.fpause();
			is_show = game_window.fis_show();
			for(int i = 0; i < snakes.length; i++){
				event = snakes[i].fmove(map.fget_grid());
				if(event != Color.black){
					if(event == Color.white){
						snakes[i].fkill(map.fget_grid());
						map.fupdate(snakes[i].fget_color(), snakes[i].fget_coordinates());
					}
					else{
						temp_flag = true;
						for(Fruit fruit:fruits) if(fruit.fget_color() == event){
							temp_flag = false;
							fruits[fruit.fget_id()].frespawn(map.fget_grid());
							map.fupdate(fruits[i].fget_color(), fruits[i].fget_coordinates());
							snakes[i].fgrow();
							break;
						}
						if(temp_flag){
							snakes[i].fkill(map.fget_grid());
							map.fupdate(snakes[i].fget_color(), snakes[i].fget_coordinates());
						}
					}
				}
				map.fupdate(snakes[i].fget_color(), snakes[i].fget_coordinates());
			}
			if(game_window.fhas_spy()){
				if(game_window.fhas_snakes())
					game_window.fupdate_snakes();
				else{
					game_window.fset_snakes(snakes);
					game_window.fupdate_snakes();
				}
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
	private class Keys{
		private Color[] snake_keys;
		private Color[] fruit_keys;
		public Keys(){
			snake_keys = new Color[snakes.length];
			fruit_keys = new Color[fruits.length];
			for(Snake snake:snakes)
				snake_keys[snake.fget_id()] = snake.fget_color();
			for(Fruit fruit:fruits)
				fruit_keys[fruit.fget_id()] = fruit.fget_color();
		}
		public Color[] fget_snake_keys(){
			return snake_keys.clone();
		}
		public Color[] fget_fruit_keys(){
			return fruit_keys.clone();
		}
	}
}
public class Game_Object{
	private int count;
	private Object game_obj;
	
	public Game_Object(){
		game_obj = new Object();
		count = 0;
	}
	
	public void fwait(){
		count++;
		synchronized(game_obj){
			try{
				game_obj.wait();
			} catch(Exception e){
				System.out.println(e);
			}
		}
	}
	
	public void fnotify(){
		count--;
		synchronized(game_obj){
			game_obj.notify();
		}
	}
	
	public int fget_count(){
		return count;
	}
}
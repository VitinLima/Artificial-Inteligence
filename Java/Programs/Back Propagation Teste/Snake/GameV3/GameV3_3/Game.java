public class Game{
	public static void main(String[] args){
		Thread_Handler thread_handler = new Thread_Handler();
		Window window = new Window(thread_handler.fget_map(), thread_handler.fget_snakes(), thread_handler.fget_fruits(), thread_handler.fget_game_obj(), thread_handler.fget_master_obj());
	}
}
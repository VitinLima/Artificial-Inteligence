public class Game_Object extends Thread{
	private boolean running = true;
	private boolean exit = false;
	
	public boolean fis_running(){
		return running;
	}
	public void fexit(){
		running = false;
	}
	public void activate(){
		start();
		boolean temp = true;
		while(temp){
			synchronized(this){
				if(!running)
					temp = false;
			}
		}
		temp = true;
		while(temp){
			synchronized(this){
				notifyAll();
				if(Thread.activeCount() == 3)
					temp = false;
				try{
					wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
		}
		exit = true;
		synchronized(this){
			notifyAll();
		}
	}
	public void run(){
		while(!exit)
			synchronized(this){
				notifyAll();
				try{
					wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
	}
}
public class Test extends Thread{
	public int i;
	public Test(){
		i = 0;
		start();
	}
	@Override
	public void run(){
		while(true){
			synchronized(this){
				notifyAll();
				i++;
				if(i != 1){
					System.out.println("error" + i);
					return;
				}
				System.out.println(Thread.currentThread().getId());
				i--;
				try{
					wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
		}
	}
}
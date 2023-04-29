public class Game{
	public static void main(String[] args){
		Frame frame = new Frame();
		/*Test test = new Test();
		while(true){
			synchronized(test){
				test.notifyAll();
				test.i++;
				if(test.i != 1){
					System.out.println("error" + test.i);
					return;
				}
				System.out.println(Thread.currentThread().getId());
				test.i--;
				try{
					test.wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
		}*/
	}
}	
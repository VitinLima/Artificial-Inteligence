import ai.*;

public class Test{
	public Test(){
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run(){
			}
		});
		thread.start();
		try{
			thread.join();
			thread.join();
			System.out.println("hi");
		} catch(Exception e){
			System.out.println(e);
		}
	}
}

/*
		AIConstructor ai;
		ai = new AIConstructor(new int[]{1,3});
		//System.out.println(ai.getData());
		for(int i = 0; i < 100; i++){
			ai.setInput(new float[]{0.0f});
			if(ai.getAns() == 0)
				ai.backPropagate(true);
			else
				ai.backPropagate(false);
			ai.setInput(new float[]{0.5f});
			if(ai.getAns() == 1)
				ai.backPropagate(true);
			else
				ai.backPropagate(false);
			ai.setInput(new float[]{1.0f});
			if(ai.getAns() == 2)
				ai.backPropagate(true);
			else
				ai.backPropagate(false);
		}
		System.out.println(ai.getData());
		for(int i = 0; i < 3; i++){
			ai.setInput(new float[]{0.0f});
			System.out.println(ai.getAns());
			ai.setInput(new float[]{0.5f});
			System.out.println(ai.getAns());
			ai.setInput(new float[]{1.0f});
			System.out.println(ai.getAns());
		}
*/
/*
		Parent p = new Child();
		((Child)p).printChildInfo();
	private class Parent{
		public void printInfo(){
			System.out.println("Parent");
		}
	}
	private class Child extends Parent{
		public void printChildInfo(){
			System.out.println("Child");
		}
	}
*/
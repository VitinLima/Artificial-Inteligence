package main;

import entity.Entity;
import entity.Hider;
import entity.Seeker;
import ai.EvolutionalAI;
import frame.Frame;

public class Sync implements Runnable{
	private int attached = -1;
	private int timer;
	
	private boolean running = false;
	private boolean finished = false;
	
	private Object waitingObj = new Object();
	private Frame frame = null;
	
	private Entity[] entitys = new Entity[0];
	
	public Sync(int timer){
		this.timer = timer;
	}
	
	public void attach(){
		synchronized(this){
			if(this.attached == -1) this.attached++;
			this.attached++;
		}
	}
	
	public void attach(Entity caller){
		synchronized(this){
			if(caller instanceof Hider) System.out.println("attaching hider");
			else System.out.println("attaching seeker");
			
			if(this.attached == -1) this.attached++;
			this.attached++;
			
			Entity temp[] = new Entity[entitys.length+1];
			for(int i = 0; i < entitys.length; i++)
				temp[i] = entitys[i];
			temp[entitys.length] = caller;
			entitys = temp;
		}
	}
	
	public void detach(){
		synchronized(this){
			this.attached--;
		}
	}
	
	public void detach(Entity caller){
		synchronized(this){
			if(caller instanceof Hider) System.out.println("detaching hider");
			else System.out.println("detaching seeker");
			
			this.attached--;
			
			Entity temp[] = new Entity[entitys.length-1];
			int n = 0;
			for(int i = 0; i < entitys.length; i++){
				if(entitys[i] == caller) continue;
				temp[n++] = entitys[i];
			}
			entitys = temp;
		}
	}
	
	public float[] getRelativeView(int definition, Entity caller){
		float[] view = new float[definition];
		for(int i = 0; i < view.length; i++)
			view[i] = 0.0f;
		
		float[] temp = new float[definition];
		for(int i = 0; i < temp.length; i++)
			temp[i] = 500.0f;
		
		float[] limits = new float[definition+1];
		for(int i = 0; i < limits.length; i++)
			limits[i] = (float)(2.0*Math.PI*((double)i + 0.5)/(double)(limits.length-1));
		
		float x = caller.getX();
		float y = caller.getY();
		
		for(int i = 0; i < entitys.length; i++){
			if(caller == entitys[i]) continue;
			
			float deltaX = entitys[i].getX() - x;
			float deltaY = entitys[i].getY() - y;
			
			float angle = (float)Math.atan2(deltaY, deltaX) + (float)Math.PI;
			float distance = (float)Math.sqrt((double)deltaX*(double)deltaX + (double)deltaY*(double)deltaY);
			
			for(int j = 0; j < view.length; j++){
				if(limits[j] <= angle && angle < limits[j+1]){
					if(distance < temp[j]){
						temp[j] = distance;
						view[j] = distance;
						break;
					}
				}
			}
		}
		
		return view;
	}
	
	public void handleEvents(Entity caller){
	}
	
	public boolean isRunning(){
		return this.running;
	}
	
	public boolean isFinished(){
		return this.finished;
	}
	
	public void exit(){
		this.running = false;
	}
	
	public void setFrame(Frame frame){
		synchronized(this){
			this.frame = frame;
		}
	}
	
	@Override
	public void run(){
		this.running = true;
		
		(new Thread(new Runnable(){
			@Override
			public void run(){
				while(!isFinished()){
					synchronized(Sync.this){
						Sync.this.notify();
						try{
							Sync.this.wait();
						} catch(Exception e){
							System.out.println(e);
						}
					}
				}
				synchronized(Sync.this){
					Sync.this.notify();
				}
			}
		})).start();
		
		while(!isFinished()){
			synchronized(this){
				if(attached == 0 || (!running && attached == -1)) this.finished = true;
				notify();
				
				for(int i = 0; i < entitys.length; i++)
					if(!entitys[i].isInGame()) entitys[i].spawn();
				
				if(frame != null){
					frame.refresh(entitys);
					if(frame.isExit()) this.exit();
				}
				//if(this.timer == 0) this.exit();
				//else if(this.timer > 0) this.timer--;
				
				try{
					wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
		}
		if(frame != null)
			frame.dispose();
		synchronized(waitingObj){
			waitingObj.notifyAll();
		}
	}
	
	public void waitForFinish(){
		synchronized(waitingObj){
			try{
				waitingObj.wait();
			} catch(Exception e){
				System.out.println(e);
			}
		}
	}
}
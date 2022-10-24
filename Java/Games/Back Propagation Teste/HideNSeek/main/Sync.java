package main;

import entity.Entity;
import frame.Frame;

public class Sync implements Runnable{
	private int connected = -1;
	private boolean running = true;
	private boolean finished = false;
	private Entity[] etts = new Entity[0];
	private Frame frame;
	
	public Sync(){
		
	}
	
	@Override
	public void run(){
		(new Thread( new Runnable(){
			@Override
			public void run(){
				while(!Sync.this.finished){
					synchronized(Sync.this){
						Sync.this.notify();
						Sync.this.auxiliaryThreadMethod();
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
		while(!finished){
			synchronized(this){
				notify();
				mainThreadMethod();
				try{
					wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
		}
		if(this.frame != null) frame.dispose();
		System.out.println("finished");
	}
	
	public void mainThreadMethod(){
		if(connected == 0) finished = true;
		if(!running && connected == -1) finished = true;
		if(finished) return;
		if(frame != null){
			frame.refresh(etts);
			running = frame.isRunning();
		}
		for(int i = 0; i < etts.length; i++)
			if(!etts[i].isRunning()) etts[i].spawn();
	}
	
	public void auxiliaryThreadMethod(){
		
	}
	
	public Entity[] getRelativeView(Entity caller, int definition){
		Entity[] view = new Entity[definition];
		for(int i = 0; i < view.length; i++)
			view[i] = null;
		
		float[] temp = new float[definition];
		for(int i = 0; i < temp.length; i++)
			temp[i] = 100.0f;
		
		float x = caller.getX();
		float y = caller.getY();
		
		for(int i = 0; i < etts.length; i++){
			if(caller == etts[i]) continue;
			
			float deltaX = etts[i].getX() - x;
			float deltaY = etts[i].getY() - y;
			
			float angle = (float)Math.atan2(-(double)deltaY, (double)deltaX) + (float)Math.PI + (float)Math.PI/(float)definition;
			float distance = (float)Math.sqrt((double)(deltaX*deltaX) + (double)deltaY*(deltaY));
			
			int n = (int)((float)definition*angle/(float)(2.0*Math.PI)) - definition/2;
			n += definition;
			n -= definition*(int)((float)n/(float)definition);
			if(distance < temp[n]){
				temp[n] = distance;
				view[n] = etts[i];
			}
		}
		
		return view;
	}
	
	public void connect(Entity ett){
		synchronized(this){
			System.out.println("connecting " + connected);
			connected++;
			if(connected == 0) connected++;
			
			Entity[] temp = new Entity[etts.length + 1];
			for(int i = 0; i < etts.length; i++)
				temp[i] = etts[i];
			temp[etts.length] = ett;
			etts = temp;
			System.out.println("connected " + connected);
		}
	}
	
	public void disconnect(Entity ett){
		synchronized(this){
			System.out.println("disconnecting " + connected);
			connected--;
			if(!running && connected == 0) connected--;
			
			exist:{
				for(int i = 0; i < etts.length; i++)
					if(etts[i] == ett) break exist;
				return;
			}
			
			Entity[] temp = new Entity[etts.length - 1];
			int n = 0;
			for(int i = 0; i < etts.length; i++){
				if(etts[i] == ett) continue;
				temp[n++] = etts[i];
			}
			etts = temp;
			System.out.println("disconnected " + connected);
		}
	}
	
	public void setFrame(Frame frame){
		synchronized(this){
			this.frame = frame;
		}
	}
	
	public boolean isRunning(){
		return running;
	}
}
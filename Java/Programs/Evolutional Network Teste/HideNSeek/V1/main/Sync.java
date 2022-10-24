package main;

import entity.Entity;
import frame.GameFrame;

public class Sync implements Runnable{
	private int connected = -1;
	private boolean isRunning = false;
	private boolean isClosed = true;
	private Entity[] etts = new Entity[0];
	private GameFrame frame;
	private int timer;
	
	public void open(){
		isClosed = false;
		isRunning = true;
	}
	
	public void close(){
		isRunning = false;
	}
	
	public boolean isClosed(){
		return isClosed;
	}
	
	public void setFrame(GameFrame frame){
		synchronized(this){
			this.frame = frame;
		}
	}
	
	public boolean isRunning(){
		return isRunning;
	}
	
	public void setTimer(int timer){
		this.timer = timer;
	}
	
	@Override
	public void run(){
		if(this.timer == -1 && frame == null){
			System.out.println("No timer or window defined");
			return;
		}
		(new Thread( new Runnable(){
			@Override
			public void run(){
				while(!Sync.this.isClosed){
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
		while(!isClosed){
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
		if(frame != null){
			frame.dispose();
			frame = null;
		}
	}
	
	public void mainThreadMethod(){
		if(connected == 0) isClosed = true;
		if(!isRunning && connected == -1) isClosed = true;
		if(isClosed) return;
		
		if(this.timer == 0) isRunning = false;
		if(this.timer > 0) this.timer--;
		if(frame != null){
			frame.refresh(etts);
			if(!frame.isRunning()) isRunning = false;
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
			connected++;
			if(connected == 0) connected++;
			
			Entity[] temp = new Entity[etts.length + 1];
			for(int i = 0; i < etts.length; i++)
				temp[i] = etts[i];
			temp[etts.length] = ett;
			etts = temp;
		}
	}
	
	public void disconnect(Entity ett){
		synchronized(this){
			connected--;
			if(isRunning && connected == 0) connected--;
			
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
		}
	}
}
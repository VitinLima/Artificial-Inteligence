package main;

import entity.Entity;
import frame.GameFrame;
import javax.swing.*;
import java.awt.*;

public class Sync{
	private boolean isClosed = false;
	private Entity[] etts = new Entity[0];
	private GameFrame frame;
	private long timer;
	private long con = 0;
	
	private final void initializeAuxiliaryThreadRoutine(){
		(new Thread( new Runnable(){
			@Override
			public void run(){
				while(!Sync.this.isClosed){
					synchronized(Sync.this){
						Sync.this.notifyAll();
						try{
							Sync.this.wait();
						} catch(Exception e){
							System.out.println(e);
						}
					}
				}
				isClosed = false;
			}
		})).start();
	}
	
	private final void mainThreadRoutine(){
		synchronized(this){
			System.out.println("started");
			while(!isClosed){
				for(int i = 0; i < etts.length; i++)
					if(!etts[i].exist()) etts[i].spawn((float)Math.random()*460.0f + 20.0f, (float)Math.random()*460.0f + 20.0f);
				
				notify();
				try{
					wait();
				} catch(Exception e){
					System.out.println(e);
				}
				
				if(frame != null){
					try{
						Thread.sleep(100);
					} catch(Exception e){
						System.out.println(e);
					}
					if(frame.isClosed()) close();
				}
				if(this.timer == 0) close();
				if(this.timer > 0)
					this.timer--;
			}
			notifyAll();
		}
		
		for(int i = 0; i < etts.length; i++)
			etts[i].close();
		
		if(frame != null){
			frame.dispose();
			frame = null;
		}
		System.out.println("finished");
	}
	
	public void run(long timer){
		this.timer = timer;
		initializeAuxiliaryThreadRoutine();
		mainThreadRoutine();
	}
	
	public void run(GameFrame frame){
		this.timer = -1;
		this.frame = frame;
		for(int i = 0; i < etts.length; i++)
			etts[i].setPanel(this.frame.panel);
		initializeAuxiliaryThreadRoutine();
		mainThreadRoutine();
	}
	
	public void run(long timer, GameFrame frame){
		this.timer = timer;
		this.frame = frame;
		for(int i = 0; i < etts.length; i++)
			etts[i].setPanel(this.frame.panel);
		initializeAuxiliaryThreadRoutine();
		mainThreadRoutine();
	}
	
	public Entity[] getRelativeView(Entity caller){
		Entity[] view = new Entity[8];
		for(int i = 0; i < view.length; i++)
			view[i] = null;
		
		float[] temp = new float[8];
		for(int i = 0; i < temp.length; i++)
			temp[i] = 100.0f;
		
		float x = caller.getX();
		float y = caller.getY();
		
		for(int i = 0; i < etts.length; i++){
			if(caller == etts[i]) continue;
			if(!etts[i].isVisible) continue;
			
			float deltaX = etts[i].getX() - x;
			float deltaY = etts[i].getY() - y;
			float angle = (float)Math.atan2(-(double)deltaY, (double)deltaX) + (float)Math.PI + (float)Math.PI/8.0f;
			float distance = (float)Math.sqrt((double)(deltaX*deltaX) + (double)deltaY*(deltaY));
			
			int n = (int)(8.0f*angle/(float)(2.0*Math.PI)) + 4;
			n -= 8*(int)((float)n/8.0f);
			
			if(distance < temp[n]){
				temp[n] = distance;
				view[n] = etts[i];
			}
		}
		
		return view;
	}	
	
	public final void addEntity(Entity ett){
		synchronized(this){
			Entity[] temp = new Entity[etts.length + 1];
			for(int i = 0; i < etts.length; i++)
				temp[i] = etts[i];
			temp[etts.length] = ett;
			etts = temp;
			if(frame != null)
				ett.setPanel(frame.panel);
		}
	}
	
	public final void removeEntity(Entity ett){
		synchronized(this){
			Entity[] temp = new Entity[etts.length - 1];
			int n = 0;
			for(int i = 0; i < etts.length; i++){
				if(etts[i] == ett) continue;
				temp[n++] = etts[i];
			}
			etts = temp;
		}
	}
	
	public final void close(){
		synchronized(this){
			isClosed = true;
		}
	}
}
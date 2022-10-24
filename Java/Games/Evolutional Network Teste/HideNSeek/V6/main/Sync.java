package main;

import entity.*;
import frame.GameFrame;
import javax.swing.*;
import java.awt.*;

public class Sync{
	private boolean isClosed = false;
	private Entity[] etts = new Entity[0];
	private GameFrame frame;
	private long timer;
	private Map map;
	
	public void run(int mapOpt, long timer){
		this.timer = timer;
		map = new Map(mapOpt, this);
		for(int i = 0; i < etts.length; i++)
			if(etts[i] instanceof SeekerNPC || etts[i] instanceof SeekerPlayer)
				etts[i].setHeadStart(timer/3);
		mainThreadRoutine();
	}
	
	public void run(int mapOpt, GameFrame frame){
		this.timer = -1;
		this.frame = frame;
		map = new Map(mapOpt, this);
		for(int i = 0; i < etts.length; i++)
			if(etts[i] instanceof SeekerNPC || etts[i] instanceof SeekerPlayer)
				etts[i].setHeadStart(50);
		for(int i = 0; i < etts.length; i++)
			etts[i].setPanel(this.frame.panel);
		mainThreadRoutine();
	}
	
	public void run(int mapOpt, long timer, GameFrame frame){
		this.timer = timer;
		this.frame = frame;
		map = new Map(mapOpt, this);
		for(int i = 0; i < etts.length; i++)
			if(etts[i] instanceof SeekerNPC || etts[i] instanceof SeekerPlayer)
				etts[i].setHeadStart(timer/3);
		for(int i = 0; i < etts.length; i++)
			etts[i].setPanel(this.frame.panel);
		mainThreadRoutine();
	}
	
	private final void mainThreadRoutine(){
		synchronized(this){
			while(!isClosed){
				notifyAll();
				try{
					wait();
				} catch(Exception e){
					System.out.println("Sync ln 42 " + e);
				}
				for(int i = 0; i < etts.length; i++){
					if(!etts[i].exist()){
						if(etts[i] instanceof Wall){
							etts[i].spawn((float)(Math.random()*map.wallSpawnArea.getWidth() + map.wallSpawnPoint.getX()),
							(float)(Math.random()*map.wallSpawnArea.getHeight() + map.wallSpawnPoint.getY()),
							false);
						}else if(etts[i] instanceof SeekerNPC || etts[i] instanceof SeekerPlayer){
							etts[i].spawn((float)(Math.random()*map.seekerSpawnArea.getWidth() + map.seekerSpawnPoint.getX()),
							(float)(Math.random()*map.seekerSpawnArea.getHeight() + map.seekerSpawnPoint.getY()),
							false);
						}else if(etts[i] instanceof HiderNPC || etts[i] instanceof HiderPlayer){
							etts[i].spawn((float)(Math.random()*map.hiderSpawnArea.getWidth() + map.hiderSpawnPoint.getX()),
							(float)(Math.random()*map.hiderSpawnArea.getHeight() + map.hiderSpawnPoint.getY()),
							false);
						}
					}
				}
				if(timer == 0) isClosed = true;
				if(timer > 0) timer--;
				if(frame != null){
					try{
						Thread.sleep(100);
					} catch(Exception e){
						System.out.println("Sync ln 52 " + e);
					}
					if(frame.isClosed()) isClosed = true;
				}
			}
			for(int i = 0; i < etts.length; i++)
				etts[i].close();
			notifyAll();
		}
		for(int i = 0; i < etts.length; i++){
			etts[i].waitForClosed();
			i--;
		}
		if(frame != null) frame.dispose();
	}
	
	public Entity[] getRelativeView(Entity caller){
		int def = caller.def;
		Entity[] view = new Entity[def];
		for(int i = 0; i < view.length; i++)
			view[i] = null;
		
		float[] temp = new float[def];
		for(int i = 0; i < temp.length; i++)
			temp[i] = caller.fov;
		
		float x = caller.getX();
		float y = caller.getY();
		
		for(int i = 0; i < etts.length; i++){
			if(caller == etts[i]) continue;
			if(!etts[i].isVisible) continue;
			
			float deltaX = etts[i].getX() - x;
			float deltaY = etts[i].getY() - y;
			float angle = (float)Math.atan2(-(double)deltaY, (double)deltaX) + (float)Math.PI + (float)Math.PI/(float)def;
			float distance = (float)Math.sqrt((double)(deltaX*deltaX) + (double)deltaY*(deltaY));
			
			int n = (int)((float)def*angle/(float)(2.0*Math.PI)) + def/2;
			n -= def*(int)((float)n/(float)def);
			
			if(distance < temp[n]){
				temp[n] = distance;
				view[n] = etts[i];
			}
		}
		
		return view;
	}
	
	public final void addEntity(Entity ett) throws Exception{
		synchronized(this){
			if(isClosed) throw new Exception("Simulation Closed");
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
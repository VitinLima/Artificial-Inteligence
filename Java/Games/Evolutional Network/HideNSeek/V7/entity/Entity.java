package entity;

import main.Sync;
import javax.swing.*;
import java.awt.*;

public abstract class Entity implements Runnable{
	private float x;
	private float y;
	
	private JPanel panel;
	
	private boolean exist = false;
	private boolean isClosed = false;
	private long headStart = 0;
	
	private Entity owner = null;
	
	public final boolean isPushable;
	public final boolean isMovable;
	public final boolean isSolid;
	public final boolean isVisible;
	public final boolean isAlive;
	public final String name;
	public final Color color;
	public final float size;
	public final float fov;
	public final int def = 32;
	
	private Sync sync;
	private Thread thread;
	
	public Entity(boolean isPushable, boolean isMovable, boolean isSolid, boolean isVisible, boolean isAlive, String name, Color color, float size, float fov){
		this.isPushable = isPushable;
		this.isMovable = isMovable;
		this.isSolid = isSolid;
		this.isVisible = isVisible;
		this.isAlive = isAlive;
		this.name = name;
		this.color = color;
		this.size = size;
		this.fov = fov;
	}
	
	public final void start(Sync sync){
		this.sync = sync;
		
		fireStarted();
		try{
			sync.addEntity(this);
		} catch(Exception e){
			System.out.println(e);
			return;
		}
		
		thread = new Thread(this);
		thread.start();
		
		//synchronized(sync){
		//	try{
		//		sync.wait();
		//	} catch(Exception e){
		//		System.out.println(e);
		//	}
		//}
	}
	
	public final void close(){
		isClosed = true;
	}
	
	public final void waitForClosed(){
		try{
			thread.join();
		} catch(Exception e){
			System.out.println("Entity ln 50 " + e);
		}
		fireClosed();
		sync.removeEntity(this);
		exist = false;
		sync = null;
		thread = null;
		panel = null;
		isClosed = false;
	}
	
	@Override
	public void run(){
		if(!isAlive) return;
		synchronized(sync){
			while(!isClosed){
				sync.notifyAll();
				if(exist){
					stepping:{
						if(headStart > 0){
							headStart--;
							break stepping;
						}
						firePreStep();
						step();
						firePosStep();
					}
				}
				try{
					sync.wait();
				} catch(Exception e){
					System.out.println("Entity ln 76 " + e);
				}
			}
		}
	}
	
	public final void move(double angle, int velocity){
		if(!isMovable) return;
		
		for(int i = 0; i < velocity; i++){
			float lastX = this.x;
			float lastY = this.y;
			float tempX = this.x + (float)Math.cos(angle);
			float tempY = this.y + (float)Math.sin(angle);
			
			Entity[] etts = getRelativeView();
			
			boolean xBlocked = false;
			boolean yBlocked = false;
			for(int j = 0; j < etts.length; j++){
				if(etts[j] == null) continue;
				if(!etts[j].isSolid) continue;
				
				float xi = etts[j].getX();
				float yi = etts[j].getY();
				float deltaX0 = xi-tempX;
				float deltaY0 = yi-tempY;
				float deltaX1 = xi-this.x;
				float deltaY1 = yi-this.y;
				
				if((float)Math.sqrt((double)(deltaX0*deltaX0 + deltaY1*deltaY1)) < (this.size + etts[j].size)/2.0f){
					xBlocked = true;
					if(etts[j].isPushable) etts[j].push(this, angle, velocity);
				}
				if((float)Math.sqrt((double)(deltaX1*deltaX1 + deltaY0*deltaY0)) < (this.size + etts[j].size)/2.0f){
					yBlocked = true;
					if(etts[j].isPushable) etts[j].push(this, angle, velocity);
				}
			}
			
			if(!xBlocked)
				this.x = tempX;
			if(!yBlocked)
				this.y = tempY;
			if(panel != null) redraw(lastX, lastY);
		}
	}
	
	public final void spawn(float x, float y, boolean ignoreColision){
		this.x = x;
		this.y = y;
		if(ignoreColision){
			if(panel != null) redraw(this.x, this.y);
			fireSpawned();
			exist = true;
			return;
		}
		Entity[] etts = getRelativeView();
		
		for(Entity e:etts){
			if(e == null) continue;
			if(!e.isSolid) continue;
			
			float xi = e.getX();
			float yi = e.getY();
				
			if((float)Math.sqrt((double)((x-xi)*(x-xi) + (y-yi)*(y-yi))) < (this.size + e.size)/2.0f)
				return;
		}
		
		if(panel != null) redraw(this.x, this.y);
		
		fireSpawned();
		
		exist = true;
	}
	
	public void push(Entity e, double angle, int velocity){
		if(!isPushable) return;
		if(e.isPushable) return;
		permitted:{
			if(owner == null) break permitted;
			if((e instanceof HiderNPC || e instanceof HiderPlayer) && (owner instanceof HiderNPC || owner instanceof HiderPlayer)) break permitted;
			if((e instanceof SeekerNPC || e instanceof SeekerPlayer) && (owner instanceof SeekerNPC || e instanceof SeekerPlayer)) break permitted;
			return;
		}
		owner = e;
		move(angle, velocity);
	}
	
	public Entity getOwner(){
		return owner;
	}
	
	public void redraw(float lastX, float lastY){
		Graphics g = panel.getGraphics();
		if(exist){
			g.setColor(Color.black);
			g.fillOval((int)lastX-(int)size/2,(int)lastY-(int)size/2,(int)size,(int)size);
		}
		
		g.setColor(color);
		g.fillOval((int)x-(int)size/2,(int)y-(int)size/2,(int)size,(int)size);
		if(owner != null){
			if(owner instanceof SeekerNPC || owner instanceof SeekerPlayer)
				g.setColor(Color.red);
			if(owner instanceof HiderNPC || owner instanceof HiderPlayer)
				g.setColor(Color.blue);
			g.fillOval((int)x-(int)size/4,(int)y-(int)size/4,(int)size/2,(int)size/2);
		}
	}
	
	public abstract void step();
	public abstract String getInfo();
	public void fireStarted(){}
	public void fireSpawned(){}
	public void firePreStep(){}
	public void firePosStep(){}
	public void fireClosed(){}
	
	public final float getX(){
		return x;
	}
	public final float getY(){
		return y;
	}
	public final Entity[] getRelativeView(){
		return sync.getRelativeView(this);
	}
	public final void setPanel(JPanel panel){
		this.panel = panel;
	}
	public final boolean exist(){
		return this.exist;
	}
	public final void setHeadStart(long headStart){
		this.headStart = headStart;
	}
	public final boolean isHeadStart(){
		if(headStart > 0) return true;
		return false;
	}
}
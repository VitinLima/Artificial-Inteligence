package entity;

import main.Sync;
import javax.swing.*;
import java.awt.*;

public abstract class Entity implements Runnable{
	private float x;
	private float y;
	private float size = 2.0f;
	
	protected JPanel panel;
	
	private boolean exist = false;
	private boolean isClosed = false;
	
	public final boolean isLockable;
	public final boolean isMovable;
	public final boolean isSolid;
	public final boolean isVisible;
	public final boolean isAlive;
	public final String name;
	
	private Sync sync;
	private Thread thread;
	
	public Entity(boolean isLockable, boolean isMovable, boolean isSolid, boolean isVisible, boolean isAlive, String name){
		this.isLockable = isLockable;
		this.isMovable = isMovable;
		this.isSolid = isSolid;
		this.isVisible = isVisible;
		this.isAlive = isAlive;
		this.name = name;
	}
	
	public final void start(Sync sync){
		this.sync = sync;
		thread = new Thread(this);
		thread.start();
	}
	
	private final void runAnimatedThread(){
		synchronized(sync){
			while(!isClosed){ 
				if(this.exist){
					firePreStep();
					step();
					firePosStep();
				}
				sync.notify();
				try{
					sync.wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
		}
	}
	
	public final void close(){
		isClosed = true;
		try{
			thread.join();
		} catch(Exception e){
			System.out.println(e);
		}
		
		sync.removeEntity(this);
		exist = false;
		fireFinished();
		System.out.println("finished ett");
		
		sync = null;
		thread = null;
		panel = null;
	}
	
	@Override
	public void run(){
		if(sync == null){
			System.out.println("null sync given");
			return;
		}
		fireStarted();
		System.out.println("started ett");
		sync.addEntity(this);
		
		if(isAlive) runAnimatedThread();
	}
	
	public final void move(double angle, int velocity) throws Exception{
		if(!isMovable) throw new Exception("This entity cannot be moved");
		
		float lastX = this.x;
		float lastY = this.y;
		for(int i = 0; i < velocity; i++){
			float tempX = this.x + (float)Math.cos(angle);
			float tempY = this.y + (float)Math.sin(angle);
			
			Entity[] etts = getRelativeView();
			
			boolean xBlocked = false;
			boolean yBlocked = false;
			for(Entity e:etts){
				if(e == null) continue;
				if(!e.isSolid) continue;
				
				float xi = e.getX();
				float yi = e.getY();
				float deltaX0 = xi-tempX;
				float deltaY0 = yi-tempY;
				float deltaX1 = xi-this.x;
				float deltaY1 = yi-this.y;
				
				if((float)Math.sqrt((double)(deltaX0*deltaX0 + deltaY1*deltaY1)) < (getSize() + e.getSize())/2.0f)
					xBlocked = true;
				if((float)Math.sqrt((double)(deltaX1*deltaX1 + deltaY0*deltaY0)) < (getSize() + e.getSize())/2.0f)
					yBlocked = true;
				
				if(xBlocked && yBlocked) return;
			}
			
			if(!xBlocked)
				this.x = tempX;
			if(!yBlocked)
				this.y = tempY;
		}
		
		if(panel != null) redraw(lastX, lastY, this.x, this.y, size, panel);
	}
	
	public final void spawn(float x, float y){
		float lastX = this.x;
		float lastY = this.y;
		this.x = x;
		this.y = y;
		
		exist = true;
		
		if(panel != null) redraw(lastX, lastY, this.x, this.y, size, panel);
		
		fireSpawned();
	}
	
	public abstract void step();
	public abstract String getInfo();
	public abstract void redraw(float f0, float f1, float f2, float f3, float f4, JPanel p0);
	public void fireStarted(){}
	public void fireSpawned(){}
	public void firePreStep(){}
	public void firePosStep(){}
	public void fireFinished(){}
	public void setNetId(int i0){}
	
	public void switchLock(Entity e0) throws Exception{
		throw new Exception("This entity cannot be locked");
	}
	public void pickUp(Entity e0, double f1) throws Exception{
		throw new Exception("This entity cannot be picked");
	}
	public Entity getOwner() throws Exception{
		throw new Exception("This entity cannot be owned");
	}
	
	public final float getX(){
		return x;
	}
	public final float getY(){
		return y;
	}
	public final void setSize(float size){
		this.size = size;
	}
	public final float getSize(){
		return size;
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
}
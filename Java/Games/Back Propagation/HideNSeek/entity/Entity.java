package entity;

import main.Sync;

public abstract class Entity{
	private float x;
	private float y;
	private float minX;
	private float maxX;
	private float minY;
	private float maxY;
	private float velocity = 0.0f;
	private float maxVelocity = 10.0f;
	private float angle = 0.0f;
	private float size = 2.0f;
	private boolean running = false;
	private Sync sync;
	
	public Entity(float minX, float maxX, float minY, float maxY){
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}
	
	public void start(Sync sync){
		this.sync = sync;
		(new Thread(new Runnable(){
			@Override
			public void run(){
				Entity.this.run();
			}
		})).start();
	}
	
	private void run(){
		System.out.println("attempting to connect");
		sync.connect(this);
		
		fireStarted();
		while(sync.isRunning()){
			synchronized(sync){
				try{
					sync.wait();
				} catch(Exception e){
					System.out.println(e);
				}
				
				if(this.running){
					firePreStep();
					step();
					firePosStep();
					
					firePreMove();
					move();
					firePosMove();
					
					if(x < 0.0f || x > 500.0f || y < 0.0f || y > 500.0f)
						spawn();
				}
				
				sync.notify();
			}
		}
		
		sync.disconnect(this);
		fireFinished();
		running = false;
	}
	
	public void move(){
		x += velocity*maxVelocity*(float)Math.cos((double)angle);
		y += velocity*maxVelocity*(float)Math.sin((double)angle);
	}
	
	public void spawn(){
		x = minX+(float)Math.random()*(maxX-minX);
		y = minY+(float)Math.random()*(maxY-minY);
		running = true;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public void setSize(float size){
		this.size = size;
	}
	
	public float getSize(){
		return size;
	}
	
	public void setVelocity(float velocity){
		if(velocity > 1.0f) return;
		this.velocity = velocity;
	}
	
	public void setAngle(float angle){
		this.angle = angle;
	}
	
	public void setRunning(boolean running){
		this.running = running;
	}
	
	public boolean isRunning(){
		return this.running;
	}
	
	public Entity[] getRelativeView(int d){
		return sync.getRelativeView(this, d);
	}
	
	public abstract void step();
	
	public void switchLock(Entity e0){}
	
	public void pickUp(Entity e0, float v0, float a0){}
	
	public Entity getOwner(){
		return null;
	}
	
	public void fireStarted(){}
	
	public void firePreStep(){}
	
	public void firePosStep(){}
	
	public void firePreMove(){}
	
	public void firePosMove(){}
	
	public void fireFinished(){}
}
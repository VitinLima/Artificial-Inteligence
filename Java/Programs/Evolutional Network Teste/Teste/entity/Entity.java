package entity;

import main.Sync;

public abstract class Entity implements Runnable{
	private Sync sync;
	
	private float x;
	private float y;
	private float angle = 0.0f;
	private float velocity = 0.0f;
	private float maxVelocity = 10.0f;
	private float size = 10.0f;
	private boolean inGame = false;
	
	public void connect(Sync sync){
		this.sync = sync;
		Thread thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run(){
		sync.attach(this);
		while(sync.isRunning()){
			synchronized(sync){
				try{
					sync.wait();
				} catch(Exception e){
					System.out.println(e);
				}
				if(inGame){
					firePreStep();
					step();
					move();
					firePosStep();
					sync.handleEvents(this);
				}
				if(this.size >= 100 || x < -20 || y < -20 || x > 520 || y > 520)
					inGame = false;
				sync.notify();
			}
		}
		sync.detach(this);
	}
	
	public void move(){
		x = this.x + maxVelocity*this.velocity*(float)Math.cos((double)this.angle);
		y = this.y + maxVelocity*this.velocity*(float)Math.sin((double)this.angle);
	}
	
	public void spawn(){
		this.velocity = 0.0f;
		this.x = (float)Math.random()*500.0f;
		this.y = (float)Math.random()*500.0f;
		this.inGame = true;
	}
	
	public void setVelocity(float angle, float velocity){
		this.angle = angle;
		this.velocity = velocity;
	}
	
	public float getX(){
		return this.x;
	}
	
	public float getY(){
		return this.y;
	}
	
	public float getSize(){
		return this.size;
	}
	
	public void setSize(float size){
		this.size = size;
	}
	
	public float[] getRelativeView(int definition){
		return sync.getRelativeView(definition, this);
	}
	
	public boolean isInGame(){
		return this.inGame;
	}
	
	public void firePreStep(){};
	
	public void firePosStep(){};
	
	public abstract void step();
	
	public abstract void beeingSeen(Entity e);
	
	public abstract void seeing(Entity e);
}
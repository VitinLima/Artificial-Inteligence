package entity;

import main.Sync;

public abstract class Entity{
	private float x;
	private float y;
	private boolean moving = false;
	private float angle = 0.0f;
	private float size = 2.0f;
	private int def = 8;
	private boolean running = false;
	private Sync sync;
	private String name = new String("");
	
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
					
					if(x < 0.0f || x > 500.0f || y < 0.0f || y > 500.0f){
						fireOutOfMap();
						spawn();
					}
				}
				
				sync.notify();
			}
		}
		
		sync.disconnect(this);
		fireFinished();
		running = false;
	}
	
	public void move(){
		for(int i = 0; i < velocity; i++){
			float tempX = this.x + (float)Math.cos((double)angle);
			float tempY = this.y + (float)Math.sin((double)angle);
			Entity[] etts = getRelativeView(def);
			boolean xBlocked = false;
			boolean yBlocked = false;
			for(Entity e:etts){
				if(!(e instanceof Solid)) continue;
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
	}
	
	public void spawn(){
		x = minX+(float)Math.random()*(maxX-minX);
		y = minY+(float)Math.random()*(maxY-minY);
		running = true;
		fireSpawned();
	}
	
	public void spawn(float x, float y){
		this.x = x;
		this.y = y;
		running = true;
		fireSpawned();
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
	
	public void pickUp(Entity e0, float f0, float f1){}
	
	public Entity getOwner(){
		return null;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getInfo(){
		return new String("");
	}
	
	public void setNetId(int i0){}
	
	public void fireStarted(){}
	
	public void fireOutOfMap(){}
	
	public void fireSpawned(){}
	
	public void firePreStep(){}
	
	public void firePosStep(){}
	
	public void firePreMove(){}
	
	public void firePosMove(){}
	
	public void fireFinished(){}
}
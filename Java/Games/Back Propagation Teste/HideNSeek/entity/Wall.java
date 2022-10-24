package entity;

public class Wall extends Lockable{
	private boolean locked = false;
	private boolean carry = false;
	private Entity owner = null;
	
	public Wall(){
		super(0.0f, 500.0f, 0.0f, 500.0f);
	}
	
	@Override
	public void step(){
		super.setVelocity(0.0f);
		super.setAngle(0.0f);
	};
	
	@Override
	public void switchLock(Entity e){
		if(locked == false){
			locked = true;
			owner = e;
			return;
		}
		if(e instanceof Hider && owner instanceof Hider){
			locked = false;
			owner = null;
			return;
		}
		if(e instanceof Seeker && owner instanceof Seeker){
			locked = false;
			owner = null;
			return;
		}
	}
	
	@Override
	public void pickUp(Entity e, float velocity, float angle){
		permitted:{
			if(!locked) break permitted;
			if(e instanceof Hider && owner instanceof Hider) break permitted;
			if(e instanceof Seeker && owner instanceof Seeker) break permitted;
			return;
		}
		owner = e;
		locked = true;
		super.setVelocity(velocity);
		super.setAngle(angle);
		super.move();
	}
	
	@Override
	public Entity getOwner(){
		return owner;
	}
}
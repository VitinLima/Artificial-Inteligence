package entity;

public class Wall extends Entity{
	private boolean locked = false;
	private boolean carry = false;
	private Entity owner = null;
	
	public Wall(){
		super.setLockable();
		super.setSolid();
		super.setName("Wall");
	}
	
	@Override
	public void step(){
		super.setMoving(false);
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
	public void pickUp(Entity e, boolean isMoving, float angle){
		permitted:{
			if(!locked) break permitted;
			if(e instanceof Hider && owner instanceof Hider) break permitted;
			if(e instanceof Seeker && owner instanceof Seeker) break permitted;
			return;
		}
		owner = e;
		locked = true;
		super.setAngle(angle);
		super.setMoving(isMoving);
		super.move();
	}
	
	@Override
	public Entity getOwner(){
		return owner;
	}
}
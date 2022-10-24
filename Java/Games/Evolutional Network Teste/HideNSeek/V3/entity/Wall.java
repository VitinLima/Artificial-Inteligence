package entity;

public class Wall extends Entity{
	private boolean locked = false;
	private boolean carry = false;
	private Entity owner = null;
	
	public Wall(){
		super.setLockable();
		super.setSolid();
		super.setVisible();
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
		if((e instanceof HiderNPC || e instanceof HiderPlayer) && (owner instanceof HiderNPC || owner instanceof HiderPlayer)){
			locked = false;
			owner = null;
			return;
		}
		if((e instanceof SeekerNPC || e instanceof SeekerPlayer) && (owner instanceof SeekerNPC || e instanceof SeekerPlayer)){
			locked = false;
			owner = null;
			return;
		}
	}
	
	@Override
	public void pickUp(Entity e, boolean isMoving, float angle){
		permitted:{
			if(!locked) break permitted;
			if((e instanceof HiderNPC || e instanceof HiderPlayer) && (owner instanceof HiderNPC || owner instanceof HiderPlayer)) break permitted;
			if((e instanceof SeekerNPC || e instanceof SeekerPlayer) && (owner instanceof SeekerNPC || e instanceof SeekerPlayer)) break permitted;
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
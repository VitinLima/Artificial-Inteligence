package entity;

public abstract class Hider extends Entity{
	public Hider(){
		super.setSize(10.0f);
		super.setSolid();
		super.setCallable();
	}
}
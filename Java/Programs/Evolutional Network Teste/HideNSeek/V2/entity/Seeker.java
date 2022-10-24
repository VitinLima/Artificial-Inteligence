package entity;

public abstract class Seeker extends Entity{
	public Seeker(){
		super.setSize(10.0f);
		super.setSolid();
		super.setCallable();
	}
}
package entity;

public abstract class Solid extends Entity{
	public Solid(float p0, float p1, float p2, float p3){
		super(p0,p1,p2,p3);
		super.setSize(10.0f);
	}
}
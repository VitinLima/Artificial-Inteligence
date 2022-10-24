package entity;

public class Wall extends Solid{
	private boolean locked = false;
	private boolean carry = false;
	private Entity owner = null;
	
	public Wall(float p0, float p1, float p2, float p3){
		super(p0,p1,p2,p3);
		super.setName("Border");
	}
	
	@Override
	public void step(){}
}
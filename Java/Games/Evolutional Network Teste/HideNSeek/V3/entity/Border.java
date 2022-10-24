package entity;

public class Border extends Entity{
	public Border(){
		super.setSolid();
		super.setVisible();
		super.setName("Border");
	}
	
	@Override
	public void step(){}
}
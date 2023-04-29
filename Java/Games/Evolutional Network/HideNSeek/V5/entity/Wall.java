package entity;

import javax.swing.*;
import java.awt.*;

public class Wall extends Entity{
	private boolean locked = false;
	private boolean carry = false;
	private Entity owner = null;
	
	public Wall(){
		super(true, true, true, true, false, "Wall");
	}
	
	@Override
	public void step(){}
	
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
	public void pickUp(Entity e, double angle){
		permitted:{
			if(!locked) break permitted;
			if((e instanceof HiderNPC || e instanceof HiderPlayer) && (owner instanceof HiderNPC || owner instanceof HiderPlayer)) break permitted;
			if((e instanceof SeekerNPC || e instanceof SeekerPlayer) && (owner instanceof SeekerNPC || e instanceof SeekerPlayer)) break permitted;
			return;
		}
		owner = e;
		locked = true;
		try{
			super.move(angle, 5);
		} catch(Exception ex){
			System.out.println(ex);
		}
	}
	
	@Override
	public Entity getOwner(){
		return owner;
	}
	
	@Override
	public String getInfo(){
		return "Wall";
	}
	
	@Override
	public void redraw(float lastX, float lastY, float x, float y, float size, JPanel panel){
		Graphics g = panel.getGraphics();
		
		g.setColor(Color.black);
		g.fillOval((int)lastX-(int)size/2,(int)lastY-(int)size/2,(int)size,(int)size);
		
		g.setColor(Color.white);
		g.fillOval((int)x-(int)size/2,(int)y-(int)size/2,(int)size,(int)size);
	}
}
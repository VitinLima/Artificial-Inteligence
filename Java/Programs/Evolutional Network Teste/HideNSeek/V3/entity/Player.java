package entity;

import frame.UserFrame;
import javax.swing.*;
import java.awt.*;

public abstract class Player extends Entity{
	private UserFrame frame;
	private double[] angles;
	
	public Player(){
		angles = new double[super.getDef()];
		for(int i = 0; i < angles.length; i++)
			angles[i] = -2.0*Math.PI*(double)i/(double)super.getDef();
		super.setSize(10.0f);
		super.setSolid();
		super.setCallable();
		super.setVisible();
	}
	
	public double[] getAngles(){
		return angles;
	}
	
	public UserFrame getFrame(){
		return frame;
	}
	
	@Override
	public void fireStarted(){
		frame = new UserFrame();
		if(this instanceof SeekerPlayer)
			frame.setName("Seeker");
		else
			frame.setName("Hider");
	}
	
	@Override
	public void step(){
		Entity[] view = super.getRelativeView();
		int x = frame.getHorizontal();
		int y = frame.getVertical();
		
		boolean isMoving = false;
		float angle = (float)Math.atan2((double)y, (double)x);
		super.setAngle(angle);
		if(x != 0 || y != 0) isMoving = true;
		super.setMoving(isMoving);
		
		for(int i = 0; i < super.getDef(); i++){
			if(view[i] == null) continue;
			if(view[i].isLockable()){
				if(frame.isToPickUp())
					view[i].pickUp(this, isMoving, angle);
				if(frame.isToSwitchLock())
					view[i].switchLock(this);
			}
		}
		frame.read();
	}
	
	@Override
	public void fireFinished(){
		frame.dispose();
	}
}
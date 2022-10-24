package entity;

import frame.UserFrame;
import javax.swing.*;
import java.awt.*;

public abstract class Player extends Entity{
	private UserFrame frame;
	private final String frameName;
	private final double[] angles;
	
	public Player(String frameName){
		super(false, true, true, true, true, "Player");
		super.setSize(10.0f);
		this.frameName = frameName;
		angles = new double[8];
		for(int i = 0; i < angles.length; i++)
			angles[i] = -2.0*Math.PI*(double)i/8.0;
	}
	
	public double[] getAngles(){
		return angles;
	}
	
	public UserFrame getFrame(){
		return frame;
	}
	
	@Override
	public void fireStarted(){
		frame = new UserFrame(Player.this.frameName);
	}
	
	@Override
	public void step(){
		Entity[] view = super.getRelativeView();
		int x = frame.getHorizontal();
		int y = frame.getVertical();
		
		if(x != 0 || y != 0){
			double angle = Math.atan2((double)y, (double)x);
			try{
				super.move(angle, 5);
			} catch(Exception e){
				System.out.println(e);
			}
			try{
				for(int i = 0; i < 8; i++){
					if(view[i] == null) continue;
					if(view[i].isLockable){
						if(frame.isToPickUp())
							view[i].pickUp(this, angle);
						if(frame.isToSwitchLock())
							view[i].switchLock(this);
					}
				}
			} catch(Exception e){
				System.out.println(e);
			}
		}
		frame.read();
	}
	
	@Override
	public void fireFinished(){
		frame.dispose();
	}
}
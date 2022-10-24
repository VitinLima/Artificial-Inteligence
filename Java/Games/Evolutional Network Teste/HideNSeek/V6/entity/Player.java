package entity;

import frame.UserFrame;
import javax.swing.*;
import java.awt.*;

public abstract class Player extends Entity{
	private UserFrame frame;
	private final String frameName;
	private final double[] angles;
	
	public Player(String frameName, Color color){
		super(false, true, true, true, true, "Player", color, 20.0f, 500.0f);
		this.frameName = frameName;
		angles = new double[super.def];
		for(int i = 0; i < angles.length; i++)
			angles[i] = -2.0*Math.PI*(double)i/(double)super.def;
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
			super.move(angle, 10);
		}
		frame.read();
	}
	
	@Override
	public void fireClosed(){
		frame.dispose();
	}
}
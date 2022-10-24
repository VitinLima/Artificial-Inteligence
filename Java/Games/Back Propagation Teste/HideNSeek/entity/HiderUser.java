package entity;

import frame.UserFrame;
import javax.swing.*;
import java.awt.*;

public class HiderUser extends Hider{
	private UserFrame frame;
	private boolean beeingSeen;
	private double[] angles;
	private int definition = 8;
	
	public HiderUser(UserFrame frame){
		super(0.0f, 500.0f, 0.0f, 500.0f);
		this.frame = frame;
		angles = new double[definition];
		for(int i = 0; i < angles.length; i++)
			angles[i] = -2.0*Math.PI*(double)i/(double)definition;
	}
	
	@Override
	public void step(){
		Entity[] view = super.getRelativeView(definition);
		float[] input = new float[definition];
		for(int i = 0; i < definition; i++){
			input[i] = 0.0f;
			if(view[i] != null){
				if(view[i] instanceof Seeker)
					input[i] = -1.0f;
				else if(view[i] instanceof Hider)
					input[i] = 1.0f;
				else if(view[i] instanceof Wall){
					if(view[i].getOwner() instanceof Seeker)
						input[i] = -0.5f;
					else
						input[i] = 0.5f;
				}
			}
		}
		float x = (float)frame.getHorizontal()/10.0f;
		float y = (float)frame.getVertical()/10.0f;
		
		float angle = (float)Math.atan2((double)y, (double)x);
		super.setAngle(angle);
		float velocity = (float)Math.sqrt((double)(x*x+ y*y));
		if(velocity > 1.0f)
			super.setVelocity(1.0f);
		else
			super.setVelocity(velocity);
		
		for(int i = 0; i < definition; i++){
			if(view[i] instanceof Wall){
				if(frame.isToPickUp())
					view[i].pickUp(this, velocity, angle);
				if(frame.isToSwitchLock())
					view[i].switchLock(this);
			}
		}
		frame.read();
	}
	
	@Override
	public void firePreStep(){
		beeingSeen = false;
	}
	
	@Override
	public void firePosMove(){
		Graphics g = frame.getGraphics();
		
		g.setColor(Color.black);
		for(int i = 0; i < 200; i++)
			g.drawLine(i,0,i,200);
		
		int n = 0;
		for(Entity ett:super.getRelativeView(definition)){
			if(ett instanceof Seeker){
				beeingSeen = true;
				g.setColor(Color.red);
				g.fillOval(95 + (int)(40.0*Math.cos(angles[n])), 95 + (int)(40.0*Math.sin(angles[n])), 10, 10);
			} else if(ett instanceof Hider){
				beeingSeen = true;
				g.setColor(Color.blue);
				g.fillOval(95 + (int)(40.0*Math.cos(angles[n])), 95 + (int)(40.0*Math.sin(angles[n])), 10, 10);
			} else if(ett instanceof Wall){
				g.setColor(Color.white);
				g.fillOval(95 + (int)(40.0*Math.cos(angles[n])), 95 + (int)(40.0*Math.sin(angles[n])), 10, 10);
			}
			n++;
		}
		
		if(beeingSeen)
			g.setColor(Color.red);
		else
			g.setColor(Color.blue);
		
		g.fillOval(95,95,10,10);
		
		try{
			Thread.sleep(100);
		} catch(Exception e){
			System.out.println(e);
		}
	}
	
	@Override
	public void fireFinished(){
		frame.dispose();
	}
}
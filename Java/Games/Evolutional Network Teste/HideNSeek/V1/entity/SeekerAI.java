package entity;

import frame.UserFrame;
import ai.Network;
import javax.swing.*;
import java.awt.*;

public class SeekerAI extends Seeker{
	private Network net;
	private int netId = -1;
	private boolean seeing;
	private int definition = 8;
	
	public SeekerAI(float p0, float p1, float p2, float p3, Network net){
		super(p0,p1,p2,p3);
		this.net = net;
	}
	
	public SeekerAI(float p0, float p1, float p2, float p3, Network net, int netId){
		super(p0,p1,p2,p3);
		this.net = net;
		this.netId = netId;
		super.setName(new String("Seeker AI " + this.netId));
	}
	
	@Override
	public void setNetId(int netId){
		this.netId = netId;
		super.setName(new String("Seeker AI " + this.netId));
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
		net.setInput(input, netId);
		float[] output = net.getOutput(netId);
		float x = output[0] - 0.5f;
		float y = output[1] - 0.5f;
		
		float angle = (float)Math.atan2((double)y, (double)x);
		super.setAngle(angle);
		float velocity = (float)Math.sqrt((double)(x*x + y*y));
		if(velocity < 0.1f)
			super.setVelocity(0);
		else
			super.setVelocity(5);
		
		for(int i = 0; i < definition; i++){
			if(view[i] instanceof Wall){
				if(output[2] > 0.5f)
					view[i].pickUp(this, velocity, angle);
				if(output[3] > 0.5f)
					view[i].switchLock(this);
			}
		}
	}
	
	@Override
	public void firePreStep(){
		seeing = false;
	}
	
	@Override
	public void firePosMove(){
		for(Entity ett:super.getRelativeView(definition)){
			if(ett instanceof Hider){
				seeing = true;
				break;
			}
		}
		
		if(seeing)
			net.addEff(true, netId);
		else
			net.addEff(false, netId);
	}
	
	@Override
	public String getInfo(){
		if(netId < 0) return new String("This AI has not been implemented yet.");
		return "Current effectiness:\n\n" + net.getEff(netId) + "\n\n" + net.access(netId).getData();
	}
}
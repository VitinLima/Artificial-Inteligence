package entity;

import frame.UserFrame;
import ai.Network;
import ai.AIConstructor;
import javax.swing.*;
import java.awt.*;

public class HiderAI extends Hider{
	private Network net;
	private int netId = -1;
	private boolean beeingSeen;
	private int definition = 8;
	
	public HiderAI(Network net){
		this.net = net;
	}
	
	public HiderAI(Network net, int netId){
		this.net = net;
		this.netId = netId;
		super.setName(new String("Hider AI " + this.netId));
	}
	
	@Override
	public void setNetId(int netId){
		this.netId = netId;
		super.setName(new String("Hider AI " + this.netId));
	}
	
	@Override
	public void step(){
		if(netId == -1) return;
		Entity[] view = super.getRelativeView(definition);
		float[] input = new float[definition];
		for(int i = 0; i < definition; i++){
			input[i] = 0.0f;
			if(view[i] == null) continue;
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
		net.setInput(input, netId);
		float[] output = net.getOutput(netId);
		float x = output[0] - 0.5f;
		float y = output[1] - 0.5f;
		
		boolean isMoving = false;
		float angle = (float)Math.atan2((double)y, (double)x);
		super.setAngle(angle);
		float velocity = (float)Math.sqrt((double)(x*x + y*y));
		if(velocity > 0.1f) isMoving = true;
		super.setMoving(isMoving);
		
		//System.out.println(x + " " + y + " " + output[0] + " " + output[1] + " " + angle);
		
		for(int i = 0; i < definition; i++){
			if(view[i] == null) continue;
			if(view[i].isLockable()){
				if(output[2] > 0.5f)
					view[i].pickUp(this, isMoving, angle);
				if(output[3] > 0.5f)
					view[i].switchLock(this);
			}
		}
	}
	
	@Override
	public void firePreStep(){
		beeingSeen = false;
	}
	
	@Override
	public void firePosMove(){
		for(Entity ett:super.getRelativeView(definition)){
			if(ett instanceof Seeker){
				beeingSeen = true;
				break;
			}
		}
		
		if(beeingSeen)
			net.addEff(false, netId);
		else
			net.addEff(true, netId);
		//System.out.println("beeing seen " + beeingSeen);
	}
	
	@Override
	public String getInfo(){
		if(netId < 0) return new String("This AI has not been implemented yet.");
		return "Current effectiness:\n\n" + net.getEff(netId) + "\n\n" + net.access(netId).getData();
	}
}
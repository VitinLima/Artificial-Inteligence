package entity;

import ai.Network;
import ai.AIConstructor;

public abstract class NPC extends Entity{
	Network net;
	int netId = -1;
	
	public NPC(Network net){
		this.net = net;
		super.setSize(10.0f);
		super.setSolid();
		super.setCallable();
		super.setVisible();
	}
	
	public NPC(Network net, int netId){
		this.net = net;
		this.netId = netId;
	}
	
	@Override
	public void setNetId(int netId){
		this.netId = netId;
		if(this instanceof SeekerNPC)
			super.setName(new String("Seeker AI " + this.netId));
		else
			super.setName(new String("Hider AI " + this.netId));
	}
	
	@Override
	public void step(){
		Entity[] view = super.getRelativeView();
		float[] input = new float[super.getDef()*5];
		for(int i = 0; i < super.getDef(); i++){
			input[i] = 0.0f;
			if(view[i] == null) continue;
			if(view[i] instanceof SeekerNPC || view[i] instanceof SeekerPlayer)
				input[i] = 1.0f;
			else if(view[i] instanceof HiderNPC || view[i] instanceof HiderPlayer)
				input[super.getDef()+i] = 1.0f;
			else if(view[i] instanceof Wall){
				if(view[i].getOwner() instanceof SeekerNPC || view[i].getOwner() instanceof SeekerPlayer)
					input[2*super.getDef()+i] = 1.0f;
				else
					input[3*super.getDef()+i] = 1.0f;
			}else if(view[i] instanceof Border){
				input[4*super.getDef()+i] = 1.0f;
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
		
		for(int i = 0; i < super.getDef(); i++){
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
	public String getInfo(){
		if(netId < 0) return new String("This AI has not been implemented yet.");
		return "Current effectiness:\n\n" + net.getEff(netId) + "\n\n" + net.access(netId).getData();
	}
}
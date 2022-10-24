package entity;

import ai.Network;
import ai.AIConstructor;

public abstract class NPC extends Entity{
	Network net;
	int netId = -1;
	
	public NPC(Network net, String name){
		super(false, true, true, true, true, name);
		super.setSize(10.0f);
		this.net = net;
	}
	
	public NPC(Network net, int netId, String name){
		super(false, true, true, true, true, name);
		super.setSize(10.0f);
		this.net = net;
		this.netId = netId;
	}
	
	@Override
	public void setNetId(int netId){
		this.netId = netId;
	}
	
	@Override
	public void step(){
		Entity[] view = super.getRelativeView();
		float[] input = new float[8*5];
		for(int i = 0; i < 8; i++){
			input[i] = 0.0f;
			if(view[i] == null) continue;
			if(view[i] instanceof SeekerNPC || view[i] instanceof SeekerPlayer)
				input[i] = 1.0f;
			else if(view[i] instanceof HiderNPC || view[i] instanceof HiderPlayer)
				input[8+i] = 1.0f;
			else if(view[i] instanceof Wall){
				try{
					if(view[i].getOwner() instanceof SeekerNPC || view[i].getOwner() instanceof SeekerPlayer)
						input[2*8+i] = 1.0f;
					else
						input[3*8+i] = 1.0f;
				} catch(Exception e){
					System.out.println(e);
				}
			}else if(view[i] instanceof Border){
				input[4*8+i] = 1.0f;
			}
		}
		net.setInput(input, netId);
		float[] output = net.getOutput(netId);
		float x = output[0] - 0.5f;
		float y = output[1] - 0.5f;
		//for(float f:output)
		//	System.out.print(f + " ");
		//System.out.println("");
		
		float velocity = (float)Math.sqrt((double)(x*x + y*y));
		if(velocity > 0.1f){
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
						if(output[2] > 0.5f)
							view[i].pickUp(this, angle);
						if(output[3] > 0.5f)
							view[i].switchLock(this);
					}
				}
			} catch(Exception e){
				System.out.println(e);
			}
		}
	}
	
	@Override
	public String getInfo(){
		if(netId < 0) return new String("This AI has not been implemented yet.");
		return "Current effectiness:\n\n" + net.getEff(netId) + "\n\n" + net.access(netId).getData();
	}
}
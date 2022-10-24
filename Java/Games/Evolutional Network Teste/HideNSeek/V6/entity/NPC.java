package entity;

import ai.Network;
import ai.AIConstructor;
import javax.swing.*;
import java.awt.*;

public abstract class NPC extends Entity{
	Network net;
	int netId = -1;
	boolean pushing = false;
	
	public NPC(Network net, String name, Color color){
		super(false, true, true, true, true, name, color, 20.0f, 500.0f);
		this.net = net;
	}
	
	public NPC(Network net, int netId, String name, Color color){
		super(false, true, true, true, true, name, color, 20.0f, 500.0f);
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
		float[] input = new float[super.def*5];
		for(int i = 0; i < super.def*5; i++)
			input[i] = 0.0f;
		for(int i = 0; i < super.def; i++){
			if(view[i] == null) continue;
			float x = super.getX();
			float y = super.getY();
			float xi = view[i].getX();
			float yi = view[i].getY();
			float d = (float)Math.sqrt((double)((x-xi)*(x-xi) + (y-yi)*(y-yi)));
			if(view[i] instanceof SeekerNPC || view[i] instanceof SeekerPlayer)
				input[i] = super.fov-d;
			else if(view[i] instanceof HiderNPC || view[i] instanceof HiderPlayer)
				input[super.def+i] = super.fov-d;
			else if(view[i] instanceof Wall){
				if(view[i].getOwner() instanceof SeekerNPC || view[i].getOwner() instanceof SeekerPlayer)
					input[2*super.def+i] = super.fov-d;
				else
					input[3*super.def+i] = super.fov-d;
			}else if(view[i] instanceof Border){
				input[4*super.def+i] = super.fov-d;
			}
		}
		for(int i = 0; i < input.length; i++)
			input[i] /= super.fov;
		net.setInput(input, netId);
		int ans = net.getAns(netId);
		float x = 0.0f;
		float y = 0.0f;
		switch(ans){
			case 0:
				x = 1.0f;
				break;
			case 1:
				x = 1.0f;
				y = 1.0f;
				break;
			case 2:
				y = 1.0f;
				break;
			case 3:
				x = -1.0f;
				y = 1.0f;
				break;
			case 4:
				x = -1.0f;
				break;
			case 5:
				x = -1.0f;
				y = -1.0f;
				break;
			case 6:
				y = -1.0f;
				break;
			case 7:
				x = 1.0f;
				y = -1.0f;
				break;
		}
		float velocity = (float)Math.sqrt((double)(x*x + y*y));
		if(velocity > 0.1f){
			double angle = Math.atan2(y, x);
			super.move(angle, 10);
		}
	}
	
	@Override
	public String getInfo(){
		if(netId < 0) return new String("This AI has not been implemented yet.");
		return "Current effectiness:\n\n" + net.getEff(netId) + "\n\n" + net.access(netId).getData();
	}
}
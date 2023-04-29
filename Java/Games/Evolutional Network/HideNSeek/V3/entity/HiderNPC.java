package entity;

import ai.Network;

public class HiderNPC extends NPC{
	private boolean beeingSeen;
	
	public HiderNPC(Network net){
		super(net);
	}
	
	public HiderNPC(Network net, int netId){
		super(net, netId);
	}
	
	@Override
	public void firePreStep(){
		beeingSeen = false;
	}
	
	@Override
	public void firePosMove(){
		for(Entity ett:super.getRelativeView()){
			if(ett instanceof SeekerNPC || ett instanceof SeekerPlayer){
				beeingSeen = true;
				break;
			}
		}
		
		if(beeingSeen)
			super.net.addEff(false, super.netId);
		else
			super.net.addEff(true, super.netId);
		//System.out.println("beeing seen " + beeingSeen);
	}
}
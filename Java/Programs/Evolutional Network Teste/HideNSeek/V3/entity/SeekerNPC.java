package entity;

import ai.Network;

public class SeekerNPC extends NPC{
	private boolean seeing;
	
	public SeekerNPC(Network net){
		super(net);
	}
	
	public SeekerNPC(Network net, int netId){
		super(net, netId);
	}
	
	@Override
	public void firePreStep(){
		seeing = false;
	}
	
	@Override
	public void firePosMove(){
		for(Entity ett:super.getRelativeView()){
			if(ett instanceof HiderNPC || ett instanceof HiderPlayer){
				seeing = true;
				break;
			}
		}
		
		if(seeing)
			super.net.addEff(true, super.netId);
		else
			super.net.addEff(false, super.netId);
		//System.out.println("seeing " + seeing);
	}
}
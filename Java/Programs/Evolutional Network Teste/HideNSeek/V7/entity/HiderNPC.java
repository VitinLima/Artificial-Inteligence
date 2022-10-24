package entity;

import ai.Network;
import javax.swing.*;
import java.awt.*;

public class HiderNPC extends NPC{
	private boolean beeingSeen;
	private boolean lastBeeingSeen = false;
	
	public HiderNPC(Network net){
		super(net, "Hider NPC", Color.blue);
	}
	
	@Override
	public void firePreStep(){
		beeingSeen = false;
	}
	
	@Override
	public void firePosStep(){
		for(Entity ett:super.getRelativeView()){
			if(ett instanceof SeekerNPC || ett instanceof SeekerPlayer){
				if(!ett.isHeadStart())
					beeingSeen = true;
				break;
			}
		}
		if(beeingSeen != lastBeeingSeen){
			if(beeingSeen)
				super.net.addEff(false, super.netId);
			else
				super.net.addEff(true, super.netId);
		}
		lastBeeingSeen = beeingSeen;
		//System.out.println("beeing seen " + beeingSeen);
	}
}
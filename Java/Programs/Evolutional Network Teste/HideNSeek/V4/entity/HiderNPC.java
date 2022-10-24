package entity;

import ai.Network;
import javax.swing.*;
import java.awt.*;

public class HiderNPC extends NPC{
	private boolean beeingSeen;
	
	public HiderNPC(Network net){
		super(net, "Hider NPC");
	}
	
	public HiderNPC(Network net, int netId){
		super(net, netId, "Hider NPC");
	}
	
	@Override
	public void firePreStep(){
		beeingSeen = false;
	}
	
	@Override
	public void firePosStep(){
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
	
	@Override
	public void redraw(float lastX, float lastY, float x, float y, float size, JPanel panel){
		Graphics g = panel.getGraphics();
		
		g.setColor(Color.black);
		g.fillOval((int)lastX-(int)size/2,(int)lastY-(int)size/2,(int)size,(int)size);
		
		g.setColor(Color.blue);
		g.fillOval((int)x-(int)size/2,(int)y-(int)size/2,(int)size,(int)size);
	}
}
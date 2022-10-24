package entity;

import ai.Network;
import javax.swing.*;
import java.awt.*;

public class SeekerNPC extends NPC{
	private boolean seeing;
	
	public SeekerNPC(Network net){
		super(net, "Seeker NPC");
	}
	
	public SeekerNPC(Network net, int netId){
		super(net, netId, "Seeker NPC");
	}
	
	@Override
	public void firePreStep(){
		seeing = false;
	}
	
	@Override
	public void firePosStep(){
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
	
	@Override
	public void redraw(float lastX, float lastY, float x, float y, float size, JPanel panel){
		Graphics g = panel.getGraphics();
		
		g.setColor(Color.black);
		g.fillOval((int)lastX-(int)size/2,(int)lastY-(int)size/2,(int)size,(int)size);
		
		g.setColor(Color.red);
		g.fillOval((int)x-(int)size/2,(int)y-(int)size/2,(int)size,(int)size);
	}
}
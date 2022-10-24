package entity;

import frame.UserFrame;
import javax.swing.*;
import java.awt.*;

public class HiderPlayer extends Player{
	private boolean beeingSeen;
	
	public HiderPlayer(){
		super("Hider", Color.blue);
	}
	
	@Override
	public void firePreStep(){
		beeingSeen = false;
	}
	
	@Override
	public void firePosStep(){
		Graphics g = super.getFrame().getGraphics();
		
		g.setColor(Color.black);
		for(int i = 0; i < 200; i++)
			g.drawLine(i,0,i,200);
		
		int n = 0;
		for(Entity ett:super.getRelativeView()){
			if(ett instanceof SeekerNPC || ett instanceof SeekerPlayer){
				if(!ett.isHeadStart())
					beeingSeen = true;
				g.setColor(Color.red);
				g.fillOval(95 + (int)(40.0*Math.cos(super.getAngles()[n])), 95 + (int)(40.0*Math.sin(super.getAngles()[n])), 10, 10);
			} else if(ett instanceof HiderNPC || ett instanceof HiderPlayer){
				g.setColor(Color.blue);
				g.fillOval(95 + (int)(40.0*Math.cos(super.getAngles()[n])), 95 + (int)(40.0*Math.sin(super.getAngles()[n])), 10, 10);
			} else if(ett instanceof Wall){
				g.setColor(Color.white);
				g.fillOval(95 + (int)(40.0*Math.cos(super.getAngles()[n])), 95 + (int)(40.0*Math.sin(super.getAngles()[n])), 10, 10);
			} else if(ett instanceof Border){
				g.setColor(Color.yellow);
				g.fillOval(95 + (int)(40.0*Math.cos(super.getAngles()[n])), 95 + (int)(40.0*Math.sin(super.getAngles()[n])), 10, 10);
			}
			n++;
		}
		
		if(beeingSeen)
			g.setColor(Color.red);
		else
			g.setColor(Color.white);
		g.drawOval(94,94,12,12);
		
		g.setColor(Color.blue);
		g.fillOval(95,95,10,10);
	}
	
	@Override
	public String getInfo(){
		return "Hider Player";
	}
}
package entity;

import frame.UserFrame;
import javax.swing.*;
import java.awt.*;

public class SeekerPlayer extends Player{
	private boolean seeing;
	private boolean lastSeeing = false;
	
	public SeekerPlayer(){
		super("Seeker", Color.red);
	}
	
	@Override
	public void firePreStep(){
		seeing = false;
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
				g.setColor(Color.red);
				g.fillOval(95 + (int)(40.0*Math.cos(super.getAngles()[n])), 95 + (int)(40.0*Math.sin(super.getAngles()[n])), 10, 10);
			} else if(ett instanceof HiderNPC || ett instanceof HiderPlayer){
				seeing = true;
				g.setColor(Color.blue);
				g.fillOval(95 + (int)(40.0*Math.cos(super.getAngles()[n])), 95 + (int)(40.0*Math.sin(super.getAngles()[n])), 10, 10);
			} else if(ett instanceof Wall || ett instanceof Border){
				g.setColor(Color.white);
				g.fillOval(95 + (int)(40.0*Math.cos(super.getAngles()[n])), 95 + (int)(40.0*Math.sin(super.getAngles()[n])), 10, 10);
			}
			n++;
		}
		if(seeing != lastSeeing){
			if(seeing)
				g.setColor(Color.white);
			else
				g.setColor(Color.blue);
		}
		lastSeeing = seeing;
		g.drawOval(94,94,12,12);
		
		g.setColor(Color.red);
		g.fillOval(95,95,10,10);
	}
	
	@Override
	public String getInfo(){
		return "Seeker Player";
	}
}
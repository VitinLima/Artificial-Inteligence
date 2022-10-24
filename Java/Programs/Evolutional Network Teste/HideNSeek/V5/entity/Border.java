package entity;

import javax.swing.*;
import java.awt.*;

public class Border extends Entity{
	public Border(){
		super(false, false, true, true, false, "Border Wall");
	}
	
	@Override
	public void step(){}
	
	@Override
	public String getInfo(){
		return "Border Wall";
	}
	
	@Override
	public void redraw(float lastX, float lastY, float x, float y, float size, JPanel panel){
		Graphics g = panel.getGraphics();
		
		g.setColor(Color.black);
		g.fillOval((int)lastX-(int)size/2,(int)lastY-(int)size/2,(int)size,(int)size);
		
		g.setColor(Color.yellow);
		g.fillOval((int)x-(int)size/2,(int)y-(int)size/2,(int)size,(int)size);
	}
}
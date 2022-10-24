package frame;

import main.Sync;
import entity.Entity;
import entity.Hider;
import entity.Seeker;

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel{
	public MapPanel(){
		setPreferredSize(new Dimension(500,500));
	}
	
	public void refresh(Entity[] entitys){
		Graphics g = getGraphics();
		
		g.setColor(Color.black);
		for(int i = 0; i < 500; i++)
			g.drawLine(i, 0, i, 500);
		
		for(Entity e:entitys){
			g.setColor(Color.white);
			if(e instanceof Hider)
				g.setColor(Color.blue);
			if(e instanceof Seeker)
				g.setColor(Color.red);
			g.fillOval((int)e.getX()-(int)e.getSize()/2, (int)e.getY()-(int)e.getSize()/2, (int)e.getSize(), (int)e.getSize());
		}
		
		try{
			Thread.sleep(100);
		} catch(Exception e){
			System.out.println(e);
		}
	}
}
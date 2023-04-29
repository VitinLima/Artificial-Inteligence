package frame;

import main.Sync;
import entity.Entity;
import entity.Hider;
import entity.Seeker;
import entity.Wall;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

public class Frame extends JFrame{
	private boolean running = true;
	
	public Frame(Sync sync){
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				Frame.this.running = false;
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setSize(new Dimension(500,500));
		setVisible(true);
		
		sync.setFrame(this);
		
		UserFrame child1 = new UserFrame("hider");
		child1.start(sync);
		UserFrame child2 = new UserFrame("seeker");
		child2.start(sync);
		Entity child3 = new Wall();
		child3.start(sync);
		System.out.println("childs created");
	}
	
	public void refresh(Entity[] es){
		Graphics g = getGraphics();
		
		g.setColor(Color.black);
		for(int i = 0; i < 500; i++)
			g.drawLine(i, 0, i, 500);
		
		for(Entity e:es){
			if(e instanceof Hider)
				g.setColor(Color.blue);
			else if(e instanceof Seeker)
				g.setColor(Color.red);
			else if(e instanceof Wall)
				g.setColor(Color.white);
			g.fillOval((int)e.getX()-(int)e.getSize()/2, (int)e.getY()-(int)e.getSize()/2, (int)e.getSize(), (int)e.getSize());
			if(e.getOwner() instanceof Hider){
				g.setColor(Color.blue);
				g.drawLine((int)e.getX()-(int)e.getSize()/2, (int)e.getY(), (int)e.getX()+(int)e.getSize()/2, (int)e.getY());
				g.drawLine((int)e.getX(), (int)e.getY()-(int)e.getSize()/2, (int)e.getX(), (int)e.getY()+(int)e.getSize()/2);
			} else if(e.getOwner() instanceof Seeker){
				g.setColor(Color.red);
				g.drawLine((int)e.getX()-(int)e.getSize()/2, (int)e.getY(), (int)e.getX()+(int)e.getSize()/2, (int)e.getY());
				g.drawLine((int)e.getX(), (int)e.getY()-(int)e.getSize()/2, (int)e.getX(), (int)e.getY()+(int)e.getSize()/2);
			}
		}
	}
	
	public boolean isRunning(){
		return this.running;
	}
}
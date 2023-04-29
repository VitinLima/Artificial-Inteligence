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

public class GameFrame extends JFrame{
	private boolean running = true;
	private int sizeX = 500;
	private int sizeY = 500;
	
	public GameFrame(Sync sync){
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				GameFrame.this.running = false;
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setSize(new Dimension(500,500));
		setVisible(true);
		
		sync.setFrame(this);
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
package frame;

import main.Sync;
import entity.Entity;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

public class Frame extends JFrame{
	MapPanel mapPanel;
	ButtonPanel buttonPanel;
	private boolean exit = false;
	
	public Frame(Sync sync){
		buttonPanel = new ButtonPanel();
		add(buttonPanel);
		
		mapPanel = new MapPanel();
		add(mapPanel);
		
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				Frame.this.exit = true;
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setLayout(new FlowLayout());
		pack();
		setVisible(true);
		
		sync.setFrame(this);
	}
	
	public void refresh(Entity[] e){
		mapPanel.refresh(e);
		
		if(buttonPanel.isExit())
			this.exit = true;
	}
	
	public boolean isExit(){
		return this.exit;
	}
}
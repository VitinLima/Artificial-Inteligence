package frame;

import main.Sync;
import entity.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

public class GameFrame extends JFrame{
	private int sizeX = 500;
	private int sizeY = 500;
	
	public JPanel panel = new JPanel();
	private boolean isClosed = false;
	
	public GameFrame(){
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				GameFrame.this.isClosed = true;
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		panel.setPreferredSize(new Dimension(500,500));
		panel.setBackground(Color.black);
		add(panel);
		
		pack();
		setVisible(true);
	}
	
	public boolean isClosed(){
		return isClosed;
	}
}
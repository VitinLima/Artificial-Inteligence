package frame;

import main.Sync;
import entity.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

public class GameFrame extends JFrame{
	private boolean running = true;
	private int sizeX = 500;
	private int sizeY = 500;
	private JPanel panel = new JPanel();
	private float[][] pos = new float[0][3];
	private Entity[] etts = new Entity[0];
	private Sync sync;
	
	public GameFrame(Sync sync){
		this.sync = sync;
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				GameFrame.this.sync.close();
				dispose();
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		panel.setPreferredSize(new Dimension(500,500));
		panel.setBackground(Color.black);
		add(panel);
		
		pack();
		setVisible(true);
		this.sync.setPanel(panel);
	}
}
package frame;

import main.Sync;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonPanel extends JPanel{
	private boolean exit = false;
	
	public ButtonPanel(){
		add(new JButton("Exit"){{
			setSize(new Dimension(80,40));
			addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					ButtonPanel.this.exit = true;
				}
			});
		}});
	}
	
	public boolean isExit(){
		return this.exit;
	}
}
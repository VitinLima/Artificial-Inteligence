import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
	
public class Frame extends JFrame{
	public Frame(){
		Game_Object obj = new Game_Object();
		
		Component_Main component_main = new Component_Main(obj, this);
		add(component_main);
		
		setTitle("Snake");
		setLayout(new FlowLayout());
		pack();
		setFocusable(true);
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				obj.fexit();
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setVisible(true);
		
		obj.factivate();
		dispose();
	}
}
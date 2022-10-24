import javax.swing.*;
import java.lang.Thread;
import java.awt.*; 
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
	
public class Window extends JFrame{
	private Object pause_obj;
	private Object hide_obj;
	private Object spy_obj;
	
	private Window_Buttons window_buttons;
	private Window_Map window_map;
	public Window(Map map, Snake[] snakes, Fruit[] fruits, Object game_obj, Object master_obj){
		pause_obj = new Object();
		hide_obj = new Object();
		spy_obj = new Object();
		
		window_buttons = new Window_Buttons(snakes, game_obj, master_obj, pause_obj, hide_obj, spy_obj);
		window_map = new Window_Map(map.fget_grid(), game_obj, master_obj, pause_obj, hide_obj, spy_obj);
		add(window_buttons);
		add(window_map);
		
		setTitle("Snake");
		setLayout(new FlowLayout());
		pack();
		setFocusable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
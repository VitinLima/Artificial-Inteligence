import java.awt.*; 
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
	
public class GameWindow extends JFrame implements KeyListener{
	private Game_Window_Buttons game_window_buttons;
	private Game_Window_Map game_window_map;
	private boolean is_show;
	
	public GameWindow(Color[][] new_grid){
		game_window_buttons = new Game_Window_Buttons();
		game_window_map = new Game_Window_Map(new_grid);
		add(game_window_buttons);
		add(game_window_map);
		
		setTitle("Snake");
		addKeyListener(this);
		setLayout(new FlowLayout());
		pack();
		setFocusable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	public void fupdate(Color[][] new_grid){
		game_window_map.fupdate(new_grid);
	}
	public boolean fis_show(){
		return game_window_buttons.fis_show();
	}
	public void keyTyped(KeyEvent e) {
		//System.out.println("keyTyped");
	}
	public void keyPressed(KeyEvent e) {
		//System.out.println("keyPressed");
	}
	public void keyReleased(KeyEvent e) {
		/*if(e.getKeyCode() == KeyEvent.VK_RIGHT)
			System.out.println("Right key pressed!");
		else if(e.getKeyCode() == KeyEvent.VK_LEFT)
			System.out.println("Left key pressed!");
		else if(e.getKeyCode() == KeyEvent.VK_DOWN)
			System.out.println("Down key pressed!");
		else if(e.getKeyCode() == KeyEvent.VK_UP)
			System.out.println("Up key pressed!");*/
	}
	
	private class Game_Window_Map extends JComponent{
		private Color[][] grid;
		private int pixel_size;
		private Object obj;
		private Dimension preferred_size;
		public Game_Window_Map(Color[][] new_grid){
			grid = new_grid;
			pixel_size = 10;
			obj = null;
			preferred_size = new Dimension(grid.length*pixel_size, grid[0].length*pixel_size);
		}
		@Override
		public void paint(Graphics g){
			super.paint(g);
			for(int i = 0; i < grid.length; i++)
				for(int j = 0; j < grid[0].length; j++)
					fDrawPixel(i,j,g);
			if(obj != null)
				synchronized(obj){
					obj.notifyAll();
				}
		}
		@Override
		public Dimension getPreferredSize(){
			return preferred_size;
		}
		
		public void fupdate(Color[][] new_grid){
			grid = new_grid;
			preferred_size = new Dimension(grid.length*pixel_size, grid[0].length*pixel_size);
			obj = new Object();
			repaint();
			synchronized(obj){
				try{
					obj.wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
		}
		
		private void fDrawPixel(int px, int py, Graphics g){
			g.setColor(grid[px][py]);
			for(int i = px*pixel_size; i < px*pixel_size + pixel_size; i++)
				for(int j = py*pixel_size; j < py*pixel_size + pixel_size; j++)
					g.drawLine(i, j, i, j);
		}
	}
	
	private class Game_Window_Buttons extends JComponent{
		private boolean is_show;
		
		public Game_Window_Buttons(){
			is_show = true;
			Dimension buttonDimension = new Dimension(80,40);
			
			JLabel title = new JLabel("Options");
			title.setLocation(0,0);
			title.setSize(buttonDimension);
			add(title);
			
			JButton tempbutton;
			tempbutton = new JButton("Spy");
			tempbutton.setLocation(0,40);
			tempbutton.setSize(buttonDimension);
			tempbutton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//SpyWindow spyWindow = new SpyWindow();
				}
			});
			add(tempbutton);
			
			tempbutton = new JButton("Show");
			tempbutton.setLocation(0,80);
			tempbutton.setSize(buttonDimension);
			tempbutton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					is_show = !is_show;
				}
			});
			add(tempbutton);
			
			tempbutton = new JButton("Exit");
			tempbutton.setLocation(0,120);
			tempbutton.setSize(buttonDimension);
			tempbutton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
						System.exit(0);
				}
			});
			add(tempbutton);
			
			setPreferredSize(new Dimension(80, 160));
		}
		
		public boolean fis_show(){
			return is_show;
		}
	}

	private class SpyWindow extends JFrame{
		
		public SpyWindow(){
		}
	}
	
	private class SpyWindowButtons extends JComponent{
		
		public SpyWindowButtons(JTextArea textArea, JScrollPane scrollPane){
		}
	}
}
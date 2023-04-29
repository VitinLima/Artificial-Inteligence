import java.awt.*; 
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
	
public class GameWindow extends JFrame implements KeyListener{
	private Game_Window_Buttons game_window_buttons;
	private Game_Window_Map game_window_map;
	
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
	public boolean fhas_spy(){
		return game_window_buttons.fhas_spy();
	}
	public boolean fhas_snakes(){
		return game_window_buttons.fhas_snakes();
	}
	public void fupdate_snakes(){
		game_window_buttons.fupdate_snakes();
	}
	public void fset_snakes(Snake[] snakes){
		game_window_buttons.fset_snakes(snakes);
	}
	public void fpause(){
		game_window_buttons.fpause();
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
		public Game_Window_Map(Color[][] new_grid){
			grid = new_grid;
			pixel_size = 10;
			obj = null;
			setPreferredSize(new Dimension(grid.length*pixel_size, grid[0].length*pixel_size));
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
		
		public void fupdate(Color[][] new_grid){
			grid = new_grid;
			setPreferredSize(new Dimension(grid.length*pixel_size, grid[0].length*pixel_size));
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
		private SpyWindow spy_window;
		private boolean is_show;
		private boolean is_paused;
		private Object pause_obj;
		
		public Game_Window_Buttons(){
			spy_window = null;
			is_show = true;
			is_paused = false;
			pause_obj = new Object();
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
					spy_window = new SpyWindow();
					spy_window.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							spy_window.dispose();
							spy_window = null;
						}
				
					});
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
			
			tempbutton = new JButton("Pause");
			tempbutton.setLocation(0,120);
			tempbutton.setSize(buttonDimension);
			tempbutton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					is_paused = !is_paused;
					synchronized(pause_obj){
						pause_obj.notifyAll();
					}
				}
			});
			add(tempbutton);
			
			tempbutton = new JButton("Exit");
			tempbutton.setLocation(0,160);
			tempbutton.setSize(buttonDimension);
			tempbutton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					System.exit(0);
				}
			});
			add(tempbutton);
			
			setPreferredSize(new Dimension(80, 200));
		}
		
		public boolean fis_show(){
			return is_show;
		}
		
		public void fpause(){
			if(is_paused) synchronized(pause_obj){
				try{
					pause_obj.wait();
				} catch(Exception e){
					System.out.println(e);
				}
			}
		}
		
		public boolean fhas_spy(){
			if(spy_window == null)
				return false;
			return true;
		}
		
		public boolean fhas_snakes(){
			if(spy_window == null)
				return false;
			return spy_window.fhas_snakes();
		}
		
		public void fupdate_snakes(){
			if(spy_window == null)
				return;
			spy_window.fupdate_snakes();
		}
		
		public void fset_snakes(Snake[] snakes){
			if(spy_window == null)
				return;
			spy_window.fset_snakes(snakes);
		}
	}

	private class SpyWindow extends JFrame{
		private Snake[] snakes;
		private int snake_id;
		private JTextArea text;
		private JScrollPane scroll;
		
		public boolean fhas_snakes(){
			if(snakes == null)
				return false;
			return true;
		}
		public void fupdate_snakes(){
			if(snake_id == -1)
				return;
			text.setText(snakes[snake_id].fget_information());
			pack();
		}
		public void fset_snakes(Snake[] new_snakes){
			snakes = new_snakes;
			snake_id = -1;
			SpyWindowButtons options = new SpyWindowButtons(snakes);
			setLayout(new FlowLayout());
			add(options);
			text = new JTextArea();
			scroll = new JScrollPane(text);
			scroll.setPreferredSize(new Dimension(1000,200));
			add(scroll);
			pack();
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setVisible(true);
		}
	
		private class SpyWindowButtons extends JComponent{
			public SpyWindowButtons(Snake[] snakes){
				JLabel title = new JLabel("snakes");
				title.setSize(new Dimension(80,40));
				add(title);
				JButton temp_button;
				int last_x = 0;
				int last_y = 40;
				for(Snake snake:snakes){
					temp_button = new JButton("Snake "+ snake.fget_id());
					temp_button.setSize(new Dimension(80, 40));
					temp_button.setLocation(last_x, last_y);
					last_y += 40;
					temp_button.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							snake_id = snake.fget_id();
						}
					});
					add(temp_button);
				}
				setPreferredSize(new Dimension(80, last_y));
			}
		}
	}
}
import java.awt.*; 
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Thread;

		
	public class GameWindow extends JFrame implements KeyListener{
		private int PixelSize;
		private int MapLocationX;
		private int MapLocationY;
		private Map map;
		private Snake[] snakes;
		private Object lockwindow;
		
		public GameWindow(int newPixelSize, Map newmap, Snake[] newsnakes, Object newlockwindow){
			MapLocationX = 10;
			MapLocationY = 20 + 40;
			PixelSize = newPixelSize;
			map = newmap;
			snakes = newsnakes;
			lockwindow = newlockwindow;
			
			GameWindowButtons gameWindowButtons = new GameWindowButtons();
			add(gameWindowButtons);
			
			MapWindow mapWindow = new MapWindow();
			setBackground(Color.black);
			add(mapWindow);
			
			setTitle("Snake");
			addKeyListener(this);
			setLayout(new FlowLayout());
			pack();
			setFocusable(true);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocationRelativeTo(null);
			setVisible(true);
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
		
		private class MapWindow extends JComponent{
			
			@Override
			public void paint(Graphics g){
				super.paint(g);
				Color newGrid[][] = map.getGrid();
				for(int i = 0; i < map.getMapSizeX(); i++)
					for(int j = 0; j < map.getMapSizeY(); j++)
						DrawPixel(newGrid[i][j], i, j, g);
				synchronized(lockwindow){
					try{
						lockwindow.notify();
					} catch(Exception e){
						System.out.println(e);
					}
				}
			}
			
			private void DrawPixel(Color newcolor, int Px, int Py, Graphics g){
				g.setColor(newcolor);
				for(int i = Px*PixelSize; i < Px*PixelSize + PixelSize; i++)
					for(int j = Py*PixelSize; j < Py*PixelSize + PixelSize; j++)
						g.drawLine(i, j, i, j);
			}
			
			@Override
			public Dimension getPreferredSize(){
				return new Dimension(map.getMapSizeX()*PixelSize, map.getMapSizeY()*PixelSize);
			}
		}
		
		private class GameWindowButtons extends JComponent{
			
			public GameWindowButtons(){
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
						SpyWindow spyWindow = new SpyWindow();
					}
				});
				add(tempbutton);
				
				tempbutton = new JButton("Show");
				tempbutton.setLocation(0,80);
				tempbutton.setSize(buttonDimension);
				tempbutton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						map.switchShow();
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
		}
	
	private class SpyWindow extends JFrame{
		private boolean opt;
		
		public SpyWindow(){
			opt = true;
			
			JTextArea textArea = new JTextArea();
			JScrollPane scrollPane = new JScrollPane(textArea);
			scrollPane.setPreferredSize(new Dimension(0,0));
			
			SpyWindowButtons spyWindowButtons = new SpyWindowButtons(textArea, scrollPane);
			add(spyWindowButtons);
			add(scrollPane);
			setLayout(new FlowLayout());
			setLocationRelativeTo(null);
			pack();
			setVisible(true);
		}
		
		private class SpyWindowButtons extends JComponent{
			
			public SpyWindowButtons(JTextArea textArea, JScrollPane scrollPane){
				Dimension buttonDimension = new Dimension(80,40);
				
				JLabel title = new JLabel("Select snake to spy");
				title.setLocation(0,0);
				title.setSize(buttonDimension);
				add(title);
				
				Point temppoint = new Point(0,40);
				JButton tempbutton;
				for(Snake snake:snakes){
					tempbutton = new JButton("Snake " + snake.getSnake_Id());
					tempbutton.setLocation(temppoint);
					tempbutton.setSize(buttonDimension);
					tempbutton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							if(opt)
								textArea.setText("Spying on snake " + snake.getSnake_Id() + "\nX position: " + snake.getPos()[0][0] + "\nY position: " + snake.getPos()[0][1] + "\nPackCount: "+ snake.getMinding().getPackCount() + "\nMovements left: " + snake.getMovesLeft() + "\nShowing Weights and Biases:\n" + snake.getMind());
							else
								textArea.setText("Spying on snake " + snake.getSnake_Id() + "\nX position: " + snake.getPos()[0][0] + "\nY position: " + snake.getPos()[0][1] + "\nPackCount: "+ snake.getMinding().getPackCount() + "\nShowing Layers:\n" + snake.getNeuronsActivation());
							scrollPane.setPreferredSize(new Dimension(500,500));
							pack();
						}
					});
					add(tempbutton);
					temppoint.translate(0,40);
				}
				tempbutton = new JButton("Option");
				tempbutton.setLocation(temppoint);
				tempbutton.setSize(buttonDimension);
				tempbutton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						opt = !opt;
					}
				});
				add(tempbutton);
				temppoint.translate(0,40);
				setPreferredSize(new Dimension(80, temppoint.y));
			}
		}
	}
}
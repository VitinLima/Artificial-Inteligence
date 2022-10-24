import java.awt.*; 
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Thread;
	
	public class Snake extends Thread{
		private int[][] Pos;
		private int Snake_Id;
		private Color color;
		private boolean ready;
		private int InputSize;
		private int OutputSize;
		private int Moves;
		private int FieldofView;
		private Map map;
		private GameWindow gameWindow;
		private Snake[] snakes;
		private Fruit[] fruits;
		private Object locksnakes;
		
		private Minding mind;
		
		public Snake(int newSnake_Id, Color newcolor, Map newmap, GameWindow newgameWindow, Snake[] newsnakes, Fruit[] newfruits, Object newlocksnakes){
			Snake_Id = newSnake_Id;
			color = newcolor;
			map = newmap;
			gameWindow = newgameWindow;
			snakes = newsnakes;
			fruits = newfruits;
			locksnakes = newlocksnakes;
			ready = false;
			
			FieldofView = 1;
			InputSize = (FieldofView*2+1)*(FieldofView*2+1)*3;
			//InputSize = 4;
			OutputSize = 4;
			mind = new Minding(new int[]{InputSize, OutputSize}, 10);
			
			Moves = 30;
			
			boolean temp = true;
			Pos = new int[1][2];
			while(temp){
				temp = false;
				Pos[0][0] = (int)(Math.random()*(map.getMapSizeX()-1));
				Pos[0][1] = (int)(Math.random()*(map.getMapSizeX()-1));
				if(map.getPixel(Pos[0][0], Pos[0][1]) != Color.black)
					temp = true;
			}
			map.setPixel(color, Pos[0][0], Pos[0][1]);
			
			ready = true;
		}
		
		public int getSnake_Id(){
			return Snake_Id;
		}
		
		public Color getColor(){
			return color;
		}
		
		public boolean isready(){
			return ready;
		}
		
		public int[][] getPos(){
			return Pos;
		}
		
		public int getMovesLeft(){
			return Moves;
		}
		
		public String getNeuronsActivation(){
			String data = new String();
			for(int i = 0; i < mind.getSize(); i++){
				for(float l:mind.getLayers(i))
					data += l + " ";
				data += "\n\n";
			}
			return data;
		}
	
		public String getMind(){
			String data = new String();
			for(int i = 0; i < mind.getSize()-1; i++){
				for(float[] M:mind.getWeights(i)){
					for(float m:M)
						data += m + " ";
					data += '\n';
				}
				data += '\n';
				for(float v:mind.getBias(i))
					data += v + " ";
				data += "\n\n";
			}
			return data;
		}
		
		public Minding getMinding(){
			return mind;
		}
		
		public void run(){
			while(!map.isToClose()){
				synchronized(locksnakes){
					try{
						locksnakes.wait();
					} catch(Exception e){
						System.out.println(e);
					}
				}
				Process();
				ready = true;
			}
		}
		
		private void Process(){
			int opt = Choose();
			checkEvent(opt);
		}
		
		private int Choose(){
			float[] Input = getVision();
			float[] Output = mind.Process(Input);
			int opt = 0;
			for(int i = 1; i < OutputSize; i++)
				if(Output[i] > Output[opt])
					opt = i;
			return opt;
		}
		
		public float[] getVision(){
			float[] Input = new float[InputSize];
			/*Input[0] = (float)Pos[0][0]/(float)(map.getMapSizeX()-1);
			Input[1] = (float)Pos[0][1]/(float)(map.getMapSizeX()-1);
			Input[2] = 1.0f - Input[0];
			Input[3] = 1.0f - Input[1];*/
			int tempx;
			int tempy;
			boolean temp;
			for(int i = -FieldofView; i <= FieldofView; i++){
				for(int j = -FieldofView; j <= FieldofView; j++){
					tempx = Pos[0][0]+i;
					tempy = Pos[0][1]+j;
					Input[(2*FieldofView+1)*(i+FieldofView) + (j+FieldofView)] = 0.0f;
					Input[(2*FieldofView+1)*(2*FieldofView+1) + (2*FieldofView+1)*(i+FieldofView) + (j+FieldofView)] = 0.0f;
					Input[(2*FieldofView+1)*(2*FieldofView+1)*2 + (2*FieldofView+1)*(i+FieldofView) + (j+FieldofView)] = 0.0f;
					if(tempx < 0 || tempx >= map.getMapSizeX() || tempy < 0 || tempy >= map.getMapSizeY()){
						Input[3*(i+FieldofView) + (j+FieldofView)] = 1.0f;
					} else{
						if(map.getPixel(tempx, tempy) != Color.black){
							temp = true;
							for(Fruit fruit:fruits) if(tempx == fruit.getPosX() && tempy == fruit.getPosY()) temp = false;
							if(temp) Input[(2*FieldofView+1)*(2*FieldofView+1) + (2*FieldofView+1)*(i+FieldofView) + (j+FieldofView)] = 1.0f;
							else Input[(2*FieldofView+1)*(2*FieldofView+1)*2 + (2*FieldofView+1)*(i+FieldofView) + (j+FieldofView)] = 1.0f;
						}
					}
				}
			}
			return Input;
		}
		
		private void checkEvent(int opt){
			int tempPosX = Pos[0][0];
			int tempPosY = Pos[0][1];
			switch(opt){
				case 0:
					tempPosX++;
					break;
				case 1:
					tempPosX--;
					break;
				case 2:
					tempPosY++;
					break;
				case 3:
					tempPosY--;
					break;
			}
			if(tempPosX == -1 || tempPosX == map.getMapSizeX() || tempPosY == -1 || tempPosY == map.getMapSizeY() || Moves == 0){
				float[] Incentive = mind.getProcessed().clone();
				Incentive[opt] = 0.0f;
				mind.addPack(Incentive);
				respawn();
				return;
			}
			int Event = 0;
			int EventObjId = -1;
			for(Snake snake:snakes) for(int[] snakePos:snake.getPos()) if(tempPosX == snakePos[0] && tempPosY == snakePos[1]){
				Event = 1;
				EventObjId = snake.getSnake_Id();
			}
			if(Event == 1){
				float[] Incentive = mind.getProcessed().clone();
				Incentive[opt] = 0.0f;
				mind.addPack(Incentive);
				respawn();
				return;
			}
			for(Fruit fruit:fruits) if(tempPosX == fruit.getPosX() && tempPosY == fruit.getPosY()){
				Event = 2;
				EventObjId = fruit.getFruit_Id();
			}
			if(Event == 2){
				float[] Incentive = mind.getProcessed().clone();
				Incentive[opt] = 1.0f;
				mind.addPack(Incentive);
				fruits[EventObjId].spawn();
				addSize();
				Moves = 30;
			}
			Move(tempPosX, tempPosY, Event);
		}
		
		private void Move(int newPosX, int newPosY, int Event){
			if(Event != 2)
				map.setPixel(Color.black, Pos[Pos.length-1][0], Pos[Pos.length-1][1]);
			for(int i = Pos.length-1; i > 0; i--){
				Pos[i][0] = Pos[i-1][0];
				Pos[i][1] = Pos[i-1][1];
			}
			Pos[0][0] = newPosX;
			Pos[0][1] = newPosY;
			map.setPixel(color, Pos[0][0], Pos[0][1]);
			
			Moves--;
		}
		
		private void respawn(){
			for(int[] P:Pos)
				map.setPixel(Color.black, P[0], P[1]);
			boolean temp = true;
			Pos = new int[1][2];
			while(temp){
				temp = false;
				Pos[0][0] = (int)(Math.random()*(map.getMapSizeX()-1));
				Pos[0][1] = (int)(Math.random()*(map.getMapSizeX()-1));
				if(map.getPixel(Pos[0][0], Pos[0][1]) != Color.black)
					temp = true;
			}
			map.setPixel(color, Pos[0][0], Pos[0][1]);
			
			Moves = 30;
		}
		
		private void addSize(){
			int[][] temp = Pos.clone();
			Pos = new int[Pos.length + 1][2];
			int k = 0;
			for(int[] a:temp){
				Pos[k][0] = a[0];
				Pos[k++][1] = a[1];
			}
		}
	}
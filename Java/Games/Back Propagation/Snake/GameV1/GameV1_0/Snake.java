public class Snake extends Thread{
	private int[] x;
	private int[] y;
	private int Size;
	private int score;
	private int Id;
	private Map Janela;
	private Minding M;
	private int Size_Of_Map;
	private float[] Result;
	private int[] array;
	
	public Snake(int newId, Map newJanela, int newSize_Of_Map){
		Id = newId;
		Size_Of_Map = newSize_Of_Map;
		x = new int[Size_Of_Map*Size_Of_Map];
		y = new int[Size_Of_Map*Size_Of_Map];
		Size = 3;
		score = 0;
		Janela = newJanela;
		array = new int[3];
		array[0] = 8;
		array[1] = 16;
		array[2] = 4;
		M = new Minding(array);
		Result = new float[array[2]];
	}
	
	public void run(){
		Result = M.Process(getSurroundings());
		int D = 0;
		float temp = 0;
		for(i = 0; i < array[2]; i++)
			if(Result[i] > temp){
				D = i;
				temp = Result[i];
			}
		switch(D){
			case 0:
				x[0]++;
				break;
			case 1:
				x[0]--;
				break;
			case 2:
				y[0]++;
				break;
			case 3:
				y[0]--;
				break;
		}
		if(x[0] == -1 || y[0] == -1 || x[0] == Size_Of_Map || y[0] == Size_Of_Map){
			Janela.Restart_Game();
			M.addToBackPropagation(false, D);
		}
		if(Janela.Grid[x[0]][y[0]] > 0)
			Janela.Snake_Restart(Janela.Grid[x[0]][y[0]]);
		if(Janela.Grid[x[0]][y[0]] == -1){
			Size++;
			score++;
		}
	}
	
	public void setSize(int newSize){
		Size = newSize;
	}
	
	public void setscore(int newscore){
		score = newscore;
	}
	
	public void setX(int[] newX){
		for(int i = 0; i < Size; i++)
			x[i] = newX[i];
	}
	
	public void setY(int[] newY){
		for(int i = 0; i < Size; i++)
			y[i] = newY[i];
	}
	
	public int[] getX(){
		return x;
	}
	
	public int[] getY(){
		return y;
	}
	
	private void Restart_Snake(){
		
	}
	
	private float[] getSurroundings(){
		
	}
}
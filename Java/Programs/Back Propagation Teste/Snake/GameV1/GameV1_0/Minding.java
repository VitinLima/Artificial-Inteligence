import java.util.Random;

public class Minding{;
	private int Size;
	private int[] Indices;
	public Weights W;
	private Weights WC;
	public Bias B;
	private Bias BC;
	public Layers L;
	private Layers Z;
	private Layers temp;
	
	public Minding(int[] newIndices){
		Size = 0;
		for(int i:newIndices)
			Size++;
		Indices = new int[Size];
		for(int i = 0; i < Size; i++)
			Indices[i] = newIndices[i];
		Random gerador = new Random();
		W = new Weights(Size-1);
		WC = new Weights(Size-1);
		B = new Bias(Size-1);
		BC = new Bias(Size-1);
		L = new Layers(Size);
		temp = new Layers(Size);
		
		for(int i = 0; i < Size; i++){
			L.initiateLayer(i, Indices[i]);
			temp.initiateLayer(i, Indices[i]);
		}
		
		for(int i = 0; i < Size-1; i++){
			W.initiateWeight(i, Indices[i], Indices[i+1]);
			for(int j = 0; j < Indices[i]; j++)
				for(int k = 0; k < Indices[i+1]; k++)
					W.setElement(i, j, k, (gerador.nextFloat()-0.5f)*2);
			WC.initiateWeight(i, Indices[i], Indices[i+1]);
			B.initiateBias(i, Indices[i+1]);
			for(int j = 0; j < Indices[i+1]; j++)
				B.setElement(i, j, (gerador.nextFloat()-0.5f)*2);
			BC.initiateBias(i, Indices[i+1]);
		}
	}
	
	public void Process(float[] Signal){
		for(int i = 0; i < Indices[0]; i++)
			L.setElement(0, i, Signal[i]);
		for(int i = 0; i < Size-1; i++){
			for(int j = 0; j < Indices[i+1]; j++)
				L.setElement(i+1, j, 0);
			for(int j = 0; j < Indices[i]; j++)
				for(int k = 0; k < Indices[i+1]; k++)
					L.addToElement(i+1, k, L.getElement(i, j)*W.getElement(i, j, k));
			for(int j = 0; j < Indices[i+1]; j++){
				L.addToElement(i+1, j, B.getElement(i, j));
				L.setElement(i+1, j, Sigmoid(L.getElement(i+1, j)));
			}
		}
	}
	
	public void addToBackPropagation(Boolean Reinforcement, int Dif){
		for(int i = 0; i < Size-1; i++)
			for(int j = 0; j < Indices[i]; j++)
				temp.setElement(i, j, 0);
		
		if(Reinforcement){
			for(int i = 0; i < Indices[Size-1]; i++)
				temp.setElement(Size-1, i, 2*L.getElement(Size-1, i)*L.getElement(Size-1, i));
			temp.setElement(Size-1, Dif, 2*L.getElement(Size-1, Dif)*(L.getElement(Size-1, Dif) - 1));
		}
		else{
			for(int i = 0; i < Indices[Size-1]; i++)
				temp.setElement(Size-1, i, 2*L.getElement(Size-1, i)*(L.getElement(Size-1, i) - 1));
			temp.setElement(Size-1, Dif, 2*L.getElement(Size-1, Dif)*L.getElement(Size-1, Dif));
		}
		
		float zi;
		
		for(int n = Size-2; n >= 0; n--){
			for(int i = 0; i < Indices[n+1]; i++){
				zi = 0;
				for(int j = 0; j < Indices[n]; j++)
					zi += L.getElement(n, j)*W.getElement(n, j, i);
				zi += B.getElement(n, i);
				for(int j = 0; j < Indices[i]; j++){
					WC.addToElement(n, j, i, temp.getElement(n+1, i)*DSigmoid(zi)*L.getElement(n, j));
					temp.addToElement(n, j, temp.getElement(n+1, i)*DSigmoid(zi)*W.getElement(n, j, i));
				}
				BC.addToElement(n, i, temp.getElement(n+1, i)*DSigmoid(zi));
			}
		}
	}
	
	public void BackPropagation(){
		for(int i = 0; i < Size-1; i++){
			for(int j = 0; j < Indices[i+1]; j++){
				for(int k = 0; k < Indices[i]; k++){
					W.addToElement(i, k, j, -WC.getElement(i, k, j));
					WC.setElement(i, k, j, 0);
				}
				B.addToElement(i, j, -BC.getElement(i, j));
				BC.setElement(i, j, 0);
			}
		}
	}
	
	private float DSigmoid(float x){
		if(x*x > 36)
			return 0;
		float V = Sigmoid(x);
		V *= V;
		V *= exp(-x);
		return V;
	}
	
	private float Sigmoid(float x){
		if(x*x > 36){
			if(x > 0)
				return 1;
			else
				return 0;
		}
		return 1.0f/(1.0f+exp(-x));
	}
	
	private float exp(float x){
		return 1+exp(1, x, 1);
	}
	
	private float exp(float Last, float x, int a){
		if(a == 25)
			return 0;
		else
			return Last*x/a + exp(Last*x/a, x, a+1);
	}
	
	private class Weights{
		public float W[][];
		private int Size;
		private int Indice;
		private Weights weights;
		public Weights(int newSize){
			Weights(newSize, 0);
		}
		public Weights(int newSize, int newIndice){
			Weights(newSize, newIndice);
		}
		
		private void Weights(int newSize, int newIndice){
			Size = newSize;
			if(newIndice < newSize){
				Indice = newIndice;
				if(newIndice < newSize-1)
					weights = new Weights(newSize, newIndice+1);
			}
		}
		
		public void initiateWeight(int newIndice, int sizeLines, int sizeColumns){
			if(newIndice == Indice)
				W = new float[sizeLines][sizeColumns];
			else
				weights.initiateWeight(newIndice, sizeLines, sizeColumns);
		}
		
		public void setElement(int newIndice, int newLine, int newColumn, float newNumber){
			if(newIndice == Indice)
				W[newLine][newColumn] = newNumber;
			else
				weights.setElement(newIndice, newLine, newColumn, newNumber);
		}
		
		public void addToElement(int newIndice, int newLine, int newColumn, float newNumber){
			if(newIndice == Indice)
				W[newLine][newColumn] += newNumber;
			else
				weights.addToElement(newIndice, newLine, newColumn, newNumber);
		}
		
		public float getElement(int newIndice, int newLine, int newColumn){
			if(newIndice == Indice)
				return W[newLine][newColumn];
			else
				return weights.getElement(newIndice, newLine, newColumn);
		}
	}
	
	private class Bias{
		public float B[];
		private int Size;
		private int Indice;
		private Bias bias;
		public Bias(int newSize){
			Bias(newSize, 0);
		}
		public Bias(int newSize, int newIndice){
			Bias(newSize, newIndice);
		}
		
		private void Bias(int newSize, int newIndice){
			Size = newSize;
			if(newIndice < newSize){
				Indice = newIndice;
				if(newIndice < newSize-1)
					bias = new Bias(newSize, newIndice+1);
			}
		}
		
		public void initiateBias(int newIndice, int newSize){
			if(newIndice == Indice)
				B = new float[newSize];
			else
				bias.initiateBias(newIndice, newSize);
		}
		
		public void setElement(int newIndice, int newLine, float newNumber){
			if(newIndice == Indice)
				B[newLine] = newNumber;
			else
				bias.setElement(newIndice, newLine, newNumber);
		}
		
		public void addToElement(int newIndice, int newLine, float newNumber){
			if(newIndice == Indice)
				B[newLine] += newNumber;
			else
				bias.addToElement(newIndice, newLine, newNumber);
		}
		
		public float getElement(int newIndice, int newLine){
			if(newIndice == Indice)
				return B[newLine];
			else
				return bias.getElement(newIndice, newLine);
		}
	}
	
	private class Layers{
		public float L[];
		private int Size;
		private int Indice;
		private Layers layers;
		public Layers(int newSize){
			Layers(newSize, 0);
		}
		public Layers(int newSize, int newIndice){
			Layers(newSize, newIndice);
		}
		
		private void Layers(int newSize, int newIndice){
			Size = newSize;
			if(newIndice < newSize){
				Indice = newIndice;
				if(newIndice < newSize-1)
					layers = new Layers(newSize, newIndice+1);
			}
		}
		
		public void initiateLayer(int newIndice, int newSize){
			if(newIndice == Indice)
				L = new float[newSize];
			else
				layers.initiateLayer(newIndice, newSize);
		}
		
		public void setElement(int newIndice, int newLine, float newNumber){
			if(newIndice == Indice)
				L[newLine] = newNumber;
			else
				layers.setElement(newIndice, newLine, newNumber);
		}
		
		public void addToElement(int newIndice, int newLine, float newNumber){
			if(newIndice == Indice)
				L[newLine] += newNumber;
			else
				layers.addToElement(newIndice, newLine, newNumber);
		}
		
		public float getElement(int newIndice, int newLine){
			if(newIndice == Indice)
				return L[newLine];
			else
				return layers.getElement(newIndice, newLine);
		}
		
		public int getIndice(){
			return Indice;
		}
	}
	
	public void printSystem(){
		for(int i = 0; i < Size; i++)
			System.out.print(Indices[i] + " ");
		System.out.println();
		System.out.println();
		for(int i = 0; i < Size; i++){
			for(int j = 0; j < Indices[i]; j++){
				System.out.print(L.getElement(i, j) + " ");
			}
			System.out.println();
			System.out.println();
		}
		for(int i = 0; i < Size-1; i++){
			for(int j = 0; j < Indices[i]; j++){
				for(int k = 0; k < Indices[i+1]; k++)
					System.out.print(W.getElement(i, j, k) + " ");
				System.out.println();
			}
			System.out.println();
			System.out.println();
		}
		for(int i = 0; i < Size-1; i++){
			for(int j = 0; j < Indices[i+1]; j++){
				System.out.print(B.getElement(i, j) + " ");
			}
			System.out.println();
			System.out.println();
		}
	}
	
	public void printCSystem(){
		for(int i = 0; i < Size; i++)
			System.out.print(Indices[i] + " ");
		System.out.println();
		System.out.println();
		for(int i = 0; i < Size; i++){
			for(int j = 0; j < Indices[i]; j++){
				System.out.print(temp.getElement(i, j) + " ");
			}
			System.out.println();
			System.out.println();
		}
		for(int i = 0; i < Size-1; i++){
			for(int j = 0; j < Indices[i]; j++){
				for(int k = 0; k < Indices[i+1]; k++)
					System.out.print(WC.getElement(i, j, k) + " ");
				System.out.println();
			}
			System.out.println();
			System.out.println();
		}
		for(int i = 0; i < Size-1; i++){
			for(int j = 0; j < Indices[i+1]; j++){
				System.out.print(BC.getElement(i, j) + " ");
			}
			System.out.println();
			System.out.println();
		}
	}
	
	public float[] getProcess(){
		float[] array = new float[Indices[Size-1]];
		for(int i = 0; i < Indices[Size-1]; i++)
			array[i] = L.getElement(Size-1, i);
		return array;
	}
}
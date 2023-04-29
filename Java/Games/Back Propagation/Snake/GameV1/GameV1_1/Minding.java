import java.util.Random;

public class Minding{;
	private int Size;
	private int Pack;
	private int PackI;
	private int PackCount;
	private int[] Indices;
	private Matrix[] W;
	private Matrix[] Wc;
	private Vector[] B;
	private Vector[] Bc;
	private Vector[] L;
	private Vector[] Lt;
	private Vector[] temp;
	
	public Minding(int[] newIndices, int newPack){
		Size = 0;
		Pack = newPack;
		PackI = 0;
		PackCount = 0;
		for(int i:newIndices)
			Size++;
		Indices = newIndices;
		W = new Matrix[Size-1];
		Wc = new Matrix[Size-1];
		B = new Vector[Size-1];
		Bc = new Vector[Size-1];
		L = new Vector[Size];
		Lt = new Vector[Size-1];
		temp = new Vector[Size-1];
		
		for(int i = 0; i < Size; i++){
			L[i] = new Vector(Indices[i]);
		}
		
		for(int i = 0; i < Size-1; i++){
			W[i] = new Matrix(Indices[i+1], Indices[i]);
			Wc[i] = new Matrix(Indices[i+1], Indices[i]);
			B[i] = new Vector(Indices[i+1]);
			Bc[i] = new Vector(Indices[i+1]);
			temp[i] = new Vector(Indices[i+1]);
			Lt[i] = new Vector(Indices[i+1]);
		}
		
		Random gerador = new Random();
		
		for(int i = 0; i < Size-1; i++){
			for(int j = 0; j < W[i].getLines(); j++)
				for(int k = 0; k < W[i].getColumns(); k++){
					W[i].setElement(j, k, (gerador.nextFloat()-0.5f)*2);
					Wc[i].setElement(j, k, 0);
				}
			for(int j = 0; j < B[i].getLength(); j++){
				B[i].setElement(j, (gerador.nextFloat()-0.5f)*2);
				Bc[i].setElement(j, 0);
				Lt[i].setElement(j, 0);
			}
		}
	}
	
	public float[] Process(float[] Signal){
		for(int i = 0; i < L[0].getLength(); i++)
			L[0].setElement(i, Signal[i]);
		
		for(int i = 0; i < Size-1; i++){
			for(int j = 0; j < W[i].getLines(); j++){
				for(int k = 0; k < W[i].getColumns(); k++)
					Lt[i].addElement(j, W[i].getElement(j, k)*L[i].getElement(k));
				Lt[i].addElement(j, B[i].getElement(j));
				L[i+1].setElement(j, Sigmoid(Lt[i].getElement(j)));
			}
		}
		
		float[] Result = new float[L[Size-1].getLength()];
		for(int i = 0; i < L[Size-1].getLength(); i++)
			Result[i] = L[Size-1].getElement(i);
		
		return Result;
	}
	
	public float[] getProcessed(){
		return L[Size-1].getVector();
	}
	
	public void addPack(float[] Answer){
		for(int i = 0; i < L[Size-1].getLength(); i++){
			temp[Size-2].setElement(i, L[Size-1].getElement(i) - Answer[i]);
			temp[Size-2].setElement(i, 2*temp[Size-2].getElement(i));
		}
		for(int i = Size-2; i > 0; i--){
			for(int j = 0; j < W[i].getLines(); j++){
				for(int k = 0; k < W[i].getColumns(); k++){
					Wc[i].addElement(j, k, temp[i].getElement(j)*DSigmoid(Lt[i].getElement(j))*L[i].getElement(k)/Pack);
					temp[i-1].addElement(k, temp[i].getElement(j)*DSigmoid(Lt[i].getElement(j))*W[i].getElement(j, k));
				}
				Bc[i].addElement(j, temp[i].getElement(j)*DSigmoid(Lt[i].getElement(j))/Pack);
			}
		}
		for(int i = 0; i < W[0].getLines(); i++){
			for(int j = 0; j < W[0].getColumns(); j++)
				Wc[0].addElement(i, j, temp[0].getElement(i)*DSigmoid(Lt[0].getElement(i))*L[0].getElement(j)/Pack);
			Bc[0].addElement(i, temp[0].getElement(i)*DSigmoid(Lt[0].getElement(i))/Pack);
		}
		
		for(int i = 0; i < Size-1; i++){
			for(int j = 0; j < temp[i].getLength(); j++)
				temp[i].setElement(j, 0);
			for(int j = 0; j < temp[i].getLength(); j++)
				Lt[i].setElement(j, 0);
		}
		
		PackI++;
		if(PackI == Pack){
			resetPack();
			PackI = 0;
		}
	}
	
	public int getSize(){
		return Size;
	}
	
	public float[][] getWeights(int i){
		return W[i].getMatrix();
	}
	
	public float[] getBias(int i){
		return B[i].getVector();
	}
	
	public float[] getLayers(int i){
		return L[i].getVector();
	}
	
	public int getPackCount(){
		return PackCount;
	}
	
	private void resetPack(){
		PackCount++;
		//System.out.println(PackCount);
		for(int i = 0; i < Size-1; i++){
			for(int j = 0; j < W[i].getLines(); j++){
				for(int k = 0; k < W[i].getColumns(); k++){
					W[i].addElement(j, k, -Wc[i].getElement(j, k));
					Wc[i].setElement(j, k, 0);
				}
				B[i].addElement(j, -Bc[i].getElement(j));
				Bc[i].setElement(j, 0);
			}
		}
	}
	
	private class Vector{
		private int Length;
		private float[] V;
		
		public Vector(int newLength){
			Length = newLength;
			V = new float[Length];
		}
		
		public void setElement(int newIndice, float newElement){
			V[newIndice] = newElement;
		}
		
		public void addElement(int newIndice, float newElement){
			V[newIndice] = V[newIndice] + newElement;
		}
		
		public float getElement(int newIndice){
			return V[newIndice];
		}
		
		public int getLength(){
			return Length;
		}
		
		public float[] getVector(){
			return V;
		}
		
	}
	
	private class Matrix{
		private int Lines;
		private int Columns;
		private float[][] M;
		
		public Matrix(int newLines, int newColumns){
			Lines = newLines;
			Columns = newColumns;
			M = new float[Lines][Columns];
		}
		
		public void setElement(int newLine, int newColumn, float newElement){
			M[newLine][newColumn] = newElement;
		}
		
		public void addElement(int newLine, int newColumn, float newElement){
			M[newLine][newColumn] = M[newLine][newColumn] + newElement;
		}
		
		public float getElement(int newLine, int newColumn){
			return M[newLine][newColumn];
		}
		
		public int getLines(){
			return Lines;
		}
		
		public int getColumns(){
			return Columns;
		}
		
		public float[][] getMatrix(){
			return M;
		}
		
	}
	
	private float DSigmoid(float x){
		if(x*x > 36)
			return 0.01f;
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
	
	public void ChangeW(int i, int j, int k, float newElement){
		W[i].setElement(j, k, newElement);
	}
	
	public void ChangeB(int i, int j, float newElement){
		B[i].setElement(j, newElement);
	}
	
	public void Print_All(){
		System.out.println("W and B:");
		for(int i = 0; i < Size-1; i++){
			for(int j = 0; j < W[i].getLines(); j++){
				for(int k = 0; k < W[i].getColumns(); k++)
					System.out.print(W[i].getElement(j, k) + " ");
				System.out.println();
			}
			System.out.println();
			for(int j = 0; j < B[i].getLength(); j++)
				System.out.print(B[i].getElement(j) + " ");
			System.out.println();
			System.out.println();
		}
	}
	
	public void Printc_All(){
		System.out.println("Wc and Bc:");
		for(int i = 0; i < Size-1; i++){
			for(int j = 0; j < W[i].getLines(); j++){
				for(int k = 0; k < W[i].getColumns(); k++)
					System.out.print(Wc[i].getElement(j, k) + " ");
				System.out.println();
			}
			System.out.println();
			for(int j = 0; j < B[i].getLength(); j++)
				System.out.print(Bc[i].getElement(j) + " ");
			System.out.println();
			System.out.println();
		}
	}
	
	public void Printl_All(){
		System.out.println("L:");
		for(int i = 0; i < Size; i++){
			for(int j = 0; j < L[i].getLength(); j++)
				System.out.print(L[i].getElement(j) + " ");
			System.out.println();
			System.out.println();
		}
	}
	
	public void Printlt_All(){
		System.out.println("Lt:");
		for(int i = 0; i < Size-1; i++){
			for(int j = 0; j < Lt[i].getLength(); j++)
				System.out.print(Lt[i].getElement(j) + " ");
			System.out.println();
			System.out.println();
		}
	}
	
	public void Printtemp_All(){
		System.out.println("temp:");
		for(int i = 0; i < Size-1; i++){
			for(int j = 0; j < temp[i].getLength(); j++)
				System.out.print(temp[i].getElement(j) + " ");
			System.out.println();
			System.out.println();
		}
	}
}
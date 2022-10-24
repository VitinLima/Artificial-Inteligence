public class Array {
	private int Size;
	private int Number;
	private int Indice;
	private Array array;
	
	public Array(int newSize){
		Array(newSize, 0);
	}
	
	public Array(int newSize, int newIndice){
		Array(newSize, newIndice);
	}
	
	private void Array(int newSize, int newIndice){
		Size = newSize;
		if(newIndice < newSize){
			Indice = newIndice;
			array = new Array(newSize, newIndice+1);
		}
	}
	
	public void setElement(int newIndice, int newNumber){
		if(newIndice < 0)
			return;
		if(newIndice == Indice){
			Number = newNumber;
			return;
		}
		if(newIndice > Indice){
			if(Indice+1==Size)
				array = new Array(newIndice+1,Indice+1);
			array.setElement(newIndice, newNumber);
			Size = newIndice+1;
		}
	}
	
	public int getElement(int newIndice){
		if(newIndice < 0 || newIndice >= Size)
			return 0;
		if(newIndice == Indice)
			return Number;
		return array.getElement(newIndice);
	}
	
	public int getSize(){
		return Size;
	}
	
	public void removeElement(int newIndice){
		if(Indice >= newIndice-1){
			array = null;
			Size = newIndice;
			return;
		}
		Size = newIndice;
		array.removeElement(newIndice);
	}
}
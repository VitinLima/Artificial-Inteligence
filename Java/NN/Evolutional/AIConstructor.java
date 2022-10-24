public class AIConstructor extends AI{
	private int[][] ids;
	
	public AIConstructor(int[] ids){
		super();
		
		for(int i:ids)
			add(i);
		
		this.ids = new int[ids.length][];
		
		{
			int n = 0;
			int m = 0;
			for(int i:ids){
				this.ids[n] = new int[i];
				for(int j = 0; j < this.ids[n].length; j++)
					this.ids[n][j] = m++;
				n++;
			}
		}
		for(int i = 1; i < this.ids.length; i++)
			for(int j:this.ids[i])
				for(int k:this.ids[i-1])
					addConnection(j, k);
		
		setInputIds(this.ids[0]);
		setOutputIds(this.ids[this.ids.length-1]);
	}
	
	public void addLayerRecursiveConnection(int receiverLayer, int targetLayer){
		for(int i:ids[receiverLayer])
			for(int j:ids[targetLayer])
				addRecursiveConnection(i,j);
	}
	
	public void addLayerConnection(int receiverLayer, int targetLayer){
		for(int i:ids[receiverLayer])
			for(int j:ids[targetLayer])
				addConnection(i,j);
	}
	
	public void addLayer(int size){
		int[][] temp = new int[ids.length+1][];
		for(int i = 0; i < ids.length; i++)
			temp[i] = ids[i];
		
		temp[ids.length] = new int[size];
		
		int k = ids[ids.length-1][ids[ids.length-1].length-1];
		for(int i = 0; i < temp[ids.length].length; i++)
			temp[ids.length][i] = ++k;
		
		add(size);
		ids = temp;
	}
	
	public void addStraightRecursiveLayers(int qtt, int size){
		int[][] temp = new int[ids.length+qtt][];
		for(int i = 0; i < ids.length; i++)
			temp[i] = ids[i];
		
		int k = ids[ids.length-1][ids[ids.length-1].length-1];
		for(int i = ids.length; i < temp.length; i++){
			temp[i] = new int[size];
			for(int j = 0; j < temp[i].length; j++)
				temp[i][j] = ++k;
		}
		
		add(qtt*size);
		
		for(int i = ids.length+1; i < temp.length; i++){
			for(int j = 0; j < temp[i].length; j++)
				addRecursiveConnection(temp[i][j],temp[i-1][j]);
		}
		ids = temp;
	}
	
	public void removeLayerConnection(int receiverLayer, int targetLayer){
		if(!(receiverLayer < ids.length && targetLayer < ids.length)) return;
		
		for(int i:ids[receiverLayer])
			for(int j:ids[targetLayer])
				removeConnection(i, j);
	}
	
	public String getLayersData(){
		String data = new String("");
		for(int i = 0; i < ids.length; i++){
			for(int j = 0; j < ids[i].length; j++)
				data += ids[i][j] + " ";
			data += '\n';
		}
		return data;
	}
}
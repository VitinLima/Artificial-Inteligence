package ai;

public class AIConstructor extends AI{
	private int[][] ids = new int[0][0];
	private int[] singleIds = new int[0];
	
	public AIConstructor(int[] ids){
		super();
		
		for(int i:ids)
			super.add(i);
		
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
					super.addConnection(j, k);
		
		setInputIds(this.ids[0]);
		setOutputIds(this.ids[this.ids.length-1]);
	}
	
	public void addLayerRecursiveConnection(int receiverLayer, int targetLayer){
		if(!(receiverLayer < ids.length && targetLayer < ids.length) || receiverLayer < 0 || targetLayer < 0) return;
		
		for(int i:ids[receiverLayer])
			for(int j:ids[targetLayer])
				super.addRecursiveConnection(i,j);
	}
	
	public void addLayerConnection(int receiverLayer, int targetLayer){
		if(!(receiverLayer < ids.length && targetLayer < ids.length) || receiverLayer < 0 || targetLayer < 0) return;
		
		for(int i:ids[receiverLayer])
			for(int j:ids[targetLayer])
				super.addConnection(i,j);
	}
	
	public void addLayer(int size){
		if(size < 0) return;
		
		int[][] temp = new int[ids.length+1][];
		for(int i = 0; i < ids.length; i++)
			temp[i] = ids[i];
		
		temp[ids.length] = new int[size];
		
		int k = 0;
		for(int[] i:ids)
			k += i.length;
		k += singleIds.length;
		for(int i = 0; i < temp[ids.length].length; i++)
			temp[ids.length][i] = k++;
		
		super.add(size);
		ids = temp;
	}
	
	public void addStraightRecursiveLayers(int qtt, int size){
		if(qtt < 0 || size < 0) return;
		int[][] temp = new int[ids.length+qtt][];
		for(int i = 0; i < ids.length; i++)
			temp[i] = ids[i];
		
		int k = 0;
		for(int[] i:ids)
			k += i.length;
		k += singleIds.length;
		for(int i = ids.length; i < temp.length; i++){
			temp[i] = new int[size];
			for(int j = 0; j < temp[i].length; j++)
				temp[i][j] = k++;
		}
		
		super.add(qtt*size);
		
		for(int i = ids.length+1; i < temp.length; i++){
			for(int j = 0; j < temp[i].length; j++)
				super.addRecursiveConnection(temp[i][j],temp[i-1][j]);
		}
		ids = temp;
	}
	
	public void removeLayer(int id){
		if(!(id < ids.length) || id < 0) return;
		
		for(int i = 0; i < ids[id].length; i++){
			remove(ids[id][0]);
			i--;
		}
		
		int[][] temp = new int[ids.length-1][];
		int n = 0;
		for(int i = 0; i < ids.length; i++){
			if(i == id) continue;
			temp[n++] = ids[i];
		}
		ids = temp;
	}
	
	public void removeLayerConnection(int receiverLayer, int targetLayer){
		if(!(receiverLayer < ids.length && targetLayer < ids.length) || receiverLayer < 0 || targetLayer < 0) return;
		
		for(int i:ids[receiverLayer])
			for(int j:ids[targetLayer])
				super.removeConnection(i, j);
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
	
	@Override
	public void add(int qtt){
		if(qtt < 0) return;
		
		super.add(qtt);
		int[] temp = new int[singleIds.length + qtt];
		for(int i = 0; i < singleIds.length; i++)
			temp[i] = singleIds[i];
		int k = 0;
		for(int[] i:ids)
			k += i.length;
		k += singleIds.length;
		for(int i = singleIds.length; i < temp.length; i++)
			temp[i] = k++;
		singleIds = temp;
	}
	
	@Override
	public void add(){
		super.add();
		int[] temp = new int[singleIds.length + 1];
		for(int i = 0; i < singleIds.length; i++)
			temp[i] = singleIds[i];
		int k = 0;
		for(int[] i:ids)
			k += i.length;
		k += singleIds.length;
		for(int i = singleIds.length; i < temp.length; i++)
			temp[i] = k++;
		singleIds = temp;
	}
	
	@Override
	public void remove(int id){
		if(id < 0) return;
		
		int m = 0;
		for(int[] i:ids)
			m += i.length;
		m += singleIds.length;
		if(!(id < m)) return;
		
		super.remove(id);
		
		boolean existInLayers = true;
		for(int i:singleIds){
			if(i == id){
				existInLayers = false;
				break;
			}
		}
		
		if(existInLayers){
			for(int i = 0; i < ids.length; i++){
				for(int j = 0; j < ids[i].length; j++){
					if(ids[i][j] == id){
						int[] temp = new int[ids[i].length - 1];
						int n = 0;
						for(int k = 0; k < ids[i].length; k++){
							if(ids[i][k] == id) continue;
							temp[n++] = ids[i][k];
						}
						ids[i] = temp;
					}
				}
			}
		} else{
			int[] temp = new int[singleIds.length - 1];
			int n = 0;
			for(int i = 0; i < singleIds.length; i++){
				if(singleIds[i] == id) continue;
				temp[n++] = singleIds[i];
			}
		}
		
		for(int i = 0; i < ids.length; i++)
			for(int j = 0; j < ids[i].length; j++)
				if(ids[i][j] > id) ids[i][j]--;
		for(int i = 0; i < singleIds.length; i++)
			if(singleIds[i] > id) singleIds[i]--;
	}
}
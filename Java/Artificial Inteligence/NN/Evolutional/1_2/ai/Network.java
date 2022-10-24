package ai;

public class Network{
	private AI[] ais;
	private int[] eff;
	
	public Network(){
		ais = new AI[0];
		eff = new int[0];
	}
	
	public int add(AI ai){
		AI[] temp = new AI[ais.length+1];
		for(int i = 0; i < ais.length; i++)
			temp[i] = ais[i];
		temp[ais.length] = ai;
		ais = temp;
		
		int[] tempEff = new int[eff.length+1];
		for(int i = 0; i < eff.length; i++)
			tempEff[i] = eff[i];
		tempEff[eff.length] = 0;
		eff = tempEff;
		
		return eff.length - 1;
	}
	
	public void select(){
		if(ais.length < 2) return;
		int[] origIds = new int[ais.length/2];
		int[] subIds = new int[ais.length/2];
		organize(origIds, subIds);
		for(int i = 0; i < origIds.length; i++){
			ais[subIds[i]] = ais[origIds[i]].copy();
			ais[subIds[i]].randomize();
		}
	}
	
	private void organize(int[] origIds, int[] subIds){
		int[] temp = new int[ais.length];
		System.out.println(ais.length);
		for(int i = 0; i < ais.length; i++)
			temp[i] = i;
		for(int i = 0; i < ais.length-1; i++){
			if(i == -1) continue;
			if(eff[temp[i]] < eff[temp[i+1]]){
				temp[i] += temp[i+1];
				temp[i+1] = temp[i]-temp[i+1];
				temp[i] -= temp[i+1];
				i-=2;
			}
		}
		int n = 0;
		int m = 0;
		int k = 0;
		
		while(n < origIds.length && m < subIds.length){
			if( ((float)(temp.length-k)/(float)temp.length + (float)Math.random()-0.5f) > 0.5f) origIds[n++] = temp[k++];
			else subIds[m++] = temp[k++];
		}
		for(n = n; n < origIds.length; n++)
			origIds[n] = temp[k++];
		for(m = m; m < subIds.length; m++)
			subIds[m] = temp[k++];
		if(m + n < temp.length) subIds[--m] = temp[k];
		
		for(int i = 0; i < eff.length; i++)
			eff[i] = 0;
	}
	
	public void addEff(boolean signal, int id){
		
		if(signal) eff[id]++;
		else eff[id]--;
	}
	
	public void setInput(float[] input, int id){
		ais[id].setInput(input);
	}
	
	public float[] getOutput(int id){
		return ais[id].getOutput();
	}
	
	public int getAns(int id){
		return ais[id].getAns();
	}
}
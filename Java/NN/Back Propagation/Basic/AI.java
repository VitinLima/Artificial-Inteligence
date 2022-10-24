
import javax.swing.*;
import java.awt.*;

public class AI{
	private Neuron[] Ns;
	
	private int[] outputIds;
	private int[] inputIds;
	
	private float[] output;
	private int ans;
	
	public AI(){
		Ns = new Neuron[0];
		
		outputIds = new int[0];
		inputIds = new int[0];
		
		output = new float[0];
	}
	
	public void setInput(float[] input){
		int k = 0;
		for(int id:inputIds)
			Ns[id].setValue(input[k++]);
	}
	
	public int getAns(){
		start();
		return ans;
	}
	
	public float[] getOutput(){
		start();
		return output.clone();
	}
	
	private void start(){
		for(int i = 0; i < outputIds.length; i++){
			Ns[outputIds[i]].checkActive();
			
			while(!Ns[outputIds[i]].isReady());
			
			output[i] = Ns[outputIds[i]].getValue(true);
			for(int j = 0; j < Ns.length; j++)
				Ns[j].reset();
		}
		
		ans = 0;
		for(int i = 1; i < output.length; i++)
			if(output[i] > output[ans]) ans = i;
	}
	
	public void backPropagate(boolean incentive){
		float[] corrections = new float[outputIds.length];
		
		if(incentive){
			for(int j = 0; j < corrections.length; j++)
				corrections[j] = 0.0f;
			corrections[ans] = 1.0f;
		} else{
			for(int j = 0; j < corrections.length; j++)
				corrections[j] = 1.0f;
			corrections[ans] = 0.0f;
		}
		
		backPropagation(corrections);
		
		for(int j = 0; j < Ns.length; j++)
			Ns[j].settleCorrection();
	}
	
	public void backPropagation(float[] corrections){
		for(int i = 0; i < outputIds.length; i++){
			Ns[outputIds[i]].checkActive();
			
			while(!Ns[outputIds[i]].isReady());
			
			Ns[outputIds[i]].backPropagate(2*(Ns[outputIds[i]].getValue(false) - corrections[i]));
			
			for(int j = 0; j < Ns.length; j++)
				Ns[j].reset();
		}
	}
	
	public void setInputIds(int[] ids){
		for(int i:ids) if(i > Ns.length) return;
		inputIds = ids.clone();
		
		for(int i:inputIds)
			Ns[i].removeAllConnections();
	}
	
	public void setOutputIds(int[] ids){
		for(int i:ids) if(i > Ns.length) return;
		outputIds = ids.clone();
		
		output = new float[outputIds.length];
		for(int i = 0; i < output.length; i++)
			output[i] = 0.0f;
		ans = 0;
	}
	
	public void addConnection(int receiverId, int targetId){
		Ns[receiverId].addConnectionTo(Ns[targetId]);
	}
	
	public void addRecursiveConnection(int receiverId, int targetId){
		Ns[receiverId].addRecursiveConnectionTo(Ns[targetId]);
	}
	
	public void removeConnection(int receiverId, int targetId){
		Ns[receiverId].removeConnectionTo(targetId);
	}
	
	public void add(int qtt){
		Neuron[] temp = new Neuron[Ns.length+qtt];
		
		for(int i = 0; i < Ns.length; i++)
			temp[i] = Ns[i];
		for(int i = Ns.length; i < temp.length; i++)
			temp[i] = new Neuron(i);
		
		Ns = temp;
	}
	
	public void add(){
		Neuron[] temp = new Neuron[Ns.length+1];
		
		for(int i = 0; i < Ns.length; i++)
			temp[i] = Ns[i];
		temp[Ns.length] = new Neuron(Ns.length);
		
		Ns = temp;
	}
	
	public void remove(int id){
		if(!(id < Ns.length)) return;
		
		Neuron[] temp = new Neuron[Ns.length-1];
		
		int k = 0;
		for(int i = 0; i < Ns.length; i++){
			if(id == i) continue;
			temp[k++] = Ns[i];
		}
		
		Ns = temp;
	}
	
	/*UTIL*/
	public String getData(){
		if(Ns.length == 0) return "Neural Network empty\n";
		
		String data = new String("");
		for(Neuron n:Ns)
			data += n.getData();
		
		return data;
	}
}
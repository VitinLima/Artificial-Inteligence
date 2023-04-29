import javax.swing.*;
import java.awt.*;

public class AI{
	Neuron[] Ns;
	
	int[] output_ids;
	int[] input_ids;
	int[][] layers;
	int ans;
	
	public AI(int[] ids){
		int temp_int = 0;
		for(int i:ids)
			temp_int+=i;
		Ns = new Neuron[temp_int];
		
		for(int i = 0; i < Ns.length; i++)
			Ns[i] = new Neuron(i);
		
		layers = new int[ids.length][];
		temp_int = 0;
		for(int i = 0; i < ids.length; i++){
			layers[i] = new int[ids[i]];
			for(int j = 0; j < ids[i]; j++)
				layers[i][j] = temp_int++;
		}
		
		for(int i = 1; i < layers.length; i++)
			for(int j = 0; j < layers[i-1].length; j++)
				for(int k = 0; k < layers[i].length; k++)
					Ns[layers[i][k]].fconnect_to(Ns[layers[i-1][j]]);
		
		input_ids = layers[0];
		output_ids = layers[layers.length-1];
		
		for(int i = 0; i < Ns.length; i++)
			Ns[i].fset_random();
	}
	public void fset_input(float[] input){
		for(int i = 0; i < Ns.length; i++)
			Ns[i].freset_recursive();
		int k = 0;
		for(int id:input_ids)
			Ns[id].fset_value(input[k++]);
	}
	public int fstart(){
		float[] output = new float[output_ids.length];
		for(int i = 0; i < output_ids.length; i++){
			Ns[output_ids[i]].fready_up(true);
			while(!Ns[output_ids[i]].fready_up(false)[0]);
			output[i] = Ns[output_ids[i]].fstart();
			for(int j = 0; j < Ns.length; j++)
				Ns[j].freset_ready();
		}
		
		ans = 0;
		for(int i = 1; i < output_ids.length; i++)
			if(output[i] >= output[ans]) ans = i;
		return ans;
	}
	public void fback_propagate(boolean incentive){
		float[] corrections = new float[output_ids.length];
		
		if(incentive){
			for(int j = 0; j < corrections.length; j++)
				corrections[j] = 0.0f;
			corrections[ans] = 1.0f;
		} else{
			for(int j = 0; j < corrections.length; j++)
				corrections[j] = 1.0f;
			corrections[ans] = 0.0f;
		}
		
		finitiate_back_propagate(corrections);
		
		for(int j = 0; j < Ns.length; j++)
			Ns[j].fsettle_correction(1.0f);
	}
	public void finitiate_back_propagate(float[] corrections){
		for(int i = 0; i < output_ids.length; i++){
			Ns[output_ids[i]].fready_up(true);
			while(!Ns[output_ids[i]].fready_up(false)[0]);
			
			Ns[output_ids[i]].fback_propagate(2*(Ns[output_ids[i]].fget_value() - corrections[i]));
			
			for(int j = 0; j < Ns.length; j++)
				Ns[j].freset_ready();
		}
	}
	public void frecursive_layer(int from, int to){
		for(int i:layers[to])
			for(int j:layers[from])
				Ns[i].frecursive_connection_to(Ns[j]);
	}
	
	/*UTIL*/
	public String fget_data(){
		String data = new String("");
		for(Neuron n:Ns)
			data += n.fget_data();
		return data;
	}
}
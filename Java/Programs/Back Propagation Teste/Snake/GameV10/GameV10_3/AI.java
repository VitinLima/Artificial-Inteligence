import javax.swing.*;
import java.awt.*;

public class AI{
	Neuron[] Ns;
	
	int[] output_ids;
	int[] input_ids;
	
	int[] action;
	float[][] values;
	
	int trues = 1;
	int falses = 1;
	
	public AI(int[] ids){
		int temp_int = 0;
		for(int i:ids)
			temp_int+=i;
		Ns = new Neuron[temp_int];
		
		for(int i = 0; i < Ns.length; i++)
			Ns[i] = new Neuron(i);
		
		int[][] temp_ids = new int[ids.length][];
		temp_int = 0;
		for(int i = 0; i < ids.length; i++){
			temp_ids[i] = new int[ids[i]];
			for(int j = 0; j < ids[i]; j++)
				temp_ids[i][j] = temp_int++;
		}
		
		for(int i = 1; i < temp_ids.length; i++)
			for(int j = 0; j < temp_ids[i-1].length; j++)
				for(int k = 0; k < temp_ids[i].length; k++)
					Ns[temp_ids[i][k]].fconnect_to(Ns[temp_ids[i-1][j]]);
		
		input_ids = temp_ids[0];
		output_ids = temp_ids[temp_ids.length-1];
		
		values = new float[1][Ns.length];
		action = new int[1];
		
		for(int i = 0; i < Ns.length; i++)
			Ns[i].fset_random();
	}
	public void fset_input(float[] input){
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
				Ns[j].freset();
		}
		
		return action[0];
	}
	public void fback_propagate(boolean incentive){
		float[] corrections = new float[output_ids.length];
		
		for(int i = 0; i < values.length-1; i++){
			for(int j = 0; j < values[0].length; j++)
				Ns[j].fset_value(values[i][j]);
			
			if(incentive){
				trues++;
				for(int j = 0; j < corrections.length; j++)
					corrections[j] = 0.0f;
				corrections[action[i]] = 1.0f + (float)falses/(float)trues;
			} else{
				falses++;
				for(int j = 0; j < corrections.length; j++)
					corrections[j] = 1.0f;
				corrections[action[i]] = 0.0f - (float)trues/(float)falses;
			}
			
			finitiate_back_propagate(corrections);
		
			for(int j = 0; j < Ns.length; j++)
				Ns[j].fsettle_correction(1.0f);
		}
	}
	public void finitiate_back_propagate(float[] corrections){
		for(int i = 0; i < output_ids.length; i++){
			Ns[output_ids[i]].fready_up(true);
			while(!Ns[output_ids[i]].fready_up(false)[0]);
			
			Ns[output_ids[i]].fback_propagate(2*(Ns[output_ids[i]].fget_value() - corrections[i]));
			
			for(int j = 0; j < Ns.length; j++)
				Ns[j].freset();
		}
	}
}
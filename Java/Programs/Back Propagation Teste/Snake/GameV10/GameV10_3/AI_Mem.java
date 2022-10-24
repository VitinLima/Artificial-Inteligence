import javax.swing.*;
import java.awt.*;

public class AI_Mem extends AI{
	//int prop_counter = 0;
	//int mem_max = 1;
	//int mem_unlock = 1000;
	
	public AI_Mem(int[] ids, int n_mem){
		super(ids);
		if(n_mem < 1) n_mem = 1;
		
		values = new float[n_mem][Ns.length];
		action = new int[n_mem];
		ferase_memories();
	}
	public void ferase_memories(){
		for(int i = 0; i < values.length; i++){
			for(int j = 0; j < values[0].length; j++)
				values[i][j] = 0.0f;
			action[i] = -1;
		}
	}
	@Override
	public int fstart(){
		float[] output = new float[output_ids.length];
		for(int i = 0; i < output_ids.length; i++){
			Ns[output_ids[i]].fready_up(true);
			while(!Ns[output_ids[i]].fready_up(false)[0]);
			output[i] = Ns[output_ids[i]].fstart();
			for(int j = 0; j < Ns.length; j++)
				Ns[j].freset();
		}
		
		fadd_memorie(output);
		
		return action[0];
	}
	private void fadd_memorie(float[] output){
		for(int i = values.length-1; i > 0; i--){
			values[i] = values[i-1].clone();
			action[i] = action[i-1];
		}
		
		for(int i = 0; i < values[0].length; i++)
			values[0][i] = Ns[i].fget_value();
		action[0] = 0;
		for(int i = 1; i < output_ids.length; i++)
			if(output[i] >= output[action[0]]) action[0] = i;
	}
	@Override
	public void fback_propagate(boolean incentive){
		float[] corrections = new float[output_ids.length];
		float divider = 1.0f;
		/*if(mem_max != values.length){
			if(prop_counter == mem_unlock ){
				mem_max++;
				mem_unlock *= 2;
			}
			prop_counter++;
		}*/
		
		for(int i = 0; i < values.length && action[i] > -1; i++){
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
				Ns[j].fsettle_correction(divider);
			divider *= 2.0f;
		}
	}
}
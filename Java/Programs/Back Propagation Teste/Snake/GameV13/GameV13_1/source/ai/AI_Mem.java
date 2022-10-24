package ai;

import javax.swing.*;
import java.awt.*;

public class AI_Mem extends AI{
	int[] action;
	float[][] values;
	float[][] recursives;
	
	public AI_Mem(int[] ids, int n_mem){
		super(ids);
		if(n_mem < 1) n_mem = 1;
		
		values = new float[n_mem][Ns.length];
		recursives = new float[n_mem][Ns.length];
		action = new int[n_mem];
		ferase_memories();
	}
	public void ferase_memories(){
		for(int i = 0; i < values.length; i++){
			for(int j = 0; j < values[0].length; j++){
				values[i][j] = 0.0f;
				recursives[i][j] = 0.0f;
			}
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
				Ns[j].freset_ready();
		}
		
		fadd_memorie(output);
		
		return action[0];
	}
	public void fadd_memorie(float[] output){
		for(int i = values.length-1; i > 0; i--){
			recursives[i] = recursives[i-1].clone();
			values[i] = values[i-1].clone();
			action[i] = action[i-1];
		}
		
		for(int i = 0; i < values[0].length; i++){
			values[0][i] = Ns[i].fget_value();
			recursives[0][i] = Ns[i].fget_recursive();
		}
		action[0] = 0;
		for(int i = 1; i < output_ids.length; i++)
			if(output[i] >= output[action[0]]) action[0] = i;
	}
	@Override
	public void fback_propagate(boolean incentive){
		float[] corrections = new float[output_ids.length];
		float divider = 1.0f;
		
		float[] v = new float[Ns.length];
		float[] u = new float[Ns.length];
		for(int i = 0; i < Ns.length; i++){
			v[i] = Ns[i].fget_value();
			u[i] = Ns[i].fget_recursive();
		}
		
		for(int i = 0; i < values.length && action[i] > -1; i++){
			for(int j = 0; j < values[0].length; j++)
				Ns[j].fset_value(values[i][j]);
			
			if(incentive){
				for(int j = 0; j < corrections.length; j++)
					corrections[j] = 0.0f;
				corrections[action[i]] = 1.0f;
			} else{
				for(int j = 0; j < corrections.length; j++)
					corrections[j] = 1.0f;
				corrections[action[i]] = 0.0f;
			}
			
			finitiate_back_propagate(corrections);
		
			for(int j = 0; j < Ns.length; j++)
				Ns[j].fsettle_correction(divider);
			divider *= 2.0f;
		}
		
		for(int i = 0; i < Ns.length; i++){
			Ns[i].fset_value(v[i]);
			Ns[i].fset_recursive(u[i]);
		}
	}
}
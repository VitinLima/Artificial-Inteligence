import javax.swing.*;
import java.awt.*;

public class AI{
	Neuron[] Ns;
	
	int[] output_ids;
	int[] input_ids;
	
	int[] action;
	float[][] values;
	
	int prop_counter = 0;
	int mem_max = 1;
	int mem_unlock = 10000;
	
	boolean gif;
	boolean update;
	JPanel panel;
	int rel_to;
	
	public AI(int[] ids, int n_mem){
		if(n_mem < 1) n_mem = 1;
		
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
		
		values = new float[n_mem][Ns.length];
		action = new int[n_mem];
		ferase_memories();
		
		for(int i = 0; i < Ns.length; i++)
			Ns[i].fset_random();
		
		gif = false;
		update = false;
	}
	public void fset_input(float[] input){
		int k = 0;
		for(int id:input_ids)
			Ns[id].fset_value(input[k++]);
	}
	public int fstart(){
		if(gif){
			if(update){
				forganize();
				for(int i = 0; i < Ns.length; i++)
					Ns[i].fset_gif(panel.getGraphics());
				(panel.getGraphics()).setColor(Color.black);
				for(int i = 0; i < 500; i++)
					(panel.getGraphics()).drawLine(i,0,i,500);
				update = false;
			}
			if(rel_to == -1){
				for(Neuron N:Ns) N.fdraw(Color.white);
				for(int i:output_ids)
					Ns[i].fdraw(Color.blue);
				for(int i:input_ids)
					Ns[i].fdraw(Color.red);
			} else Ns[output_ids[rel_to]].fdraw_cons();
			
		}
		
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
	public void ferase_memories(){
		for(int i = 0; i < values.length; i++){
			for(int j = 0; j < values[0].length; j++)
				values[i][j] = 0.0f;
			action[i] = -1;
		}
	}
	public void fback_propagate(boolean incentive){
		float[] corrections = new float[output_ids.length];
		float divider = 1.0f;
		
		prop_counter++;
		if(prop_counter == mem_unlock){
			mem_max++;
			prop_counter = 0;
			mem_unlock *= 2;
		}
		
		for(int i = 0; i < values.length-1 && action[i] > -1 && i < mem_max; i++){
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
				Ns[j].fsettle_correction((float)divider);
			divider *= 2;
		}
	}
	private void finitiate_back_propagate(float[] corrections){
		for(int i = 0; i < output_ids.length; i++){
			Ns[output_ids[i]].fready_up(true);
			while(!Ns[output_ids[i]].fready_up(false)[0]);
			
			Ns[output_ids[i]].fback_propagate(2*(Ns[output_ids[i]].fget_value() - corrections[i]));
			
			for(int j = 0; j < Ns.length; j++)
				Ns[j].freset();
		}
	}
	public String fget_data(){
		String data = new String();
		data += fget_data_2();
		for(Neuron N:Ns) data += N.fget_data() + '\n';
		return data;
	}
	private String fget_data_2(){
		String data = new String();
		return data;
	}
	public void fprint_connections(){
		for(Neuron N:Ns) N.fprint_connections();
	}
	public void fset_gif(JPanel new_panel, int new_rel_to){
		panel = new_panel;
		gif = true;
		update = true;
		rel_to = new_rel_to;
	}
	public void fdisable_gif(){
		if(gif == false) return;
		for(int i = 0; i < Ns.length; i++)
			Ns[i].fdisable_gif();
		gif = false;
	}
	private void forganize(){
		int n = (int)Math.sqrt((double)(input_ids.length/2));
		int distance = (int)(500.0f/(float)n);
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++){
				Ns[n*i+j].fset_position(distance*i + distance/2, distance*j + distance/2);
				Ns[n*n + i*n + j].fset_position(distance*i + distance/4, distance*j + distance/4);
			}
	}
}
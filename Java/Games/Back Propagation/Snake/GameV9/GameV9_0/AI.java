import javax.swing.*;
import java.awt.*;

public class AI{
	Neuron[] Ns;
	
	int[] output_ids;
	int[] input_ids;
	
	int[] action;
	float[][] values;
	
	boolean gif;
	boolean update;
	JPanel panel;
	
	public AI(int[] ids, int n_mem){
		if(n_mem < 0) n_mem = 0;
		n_mem++;
		
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
				for(int i = 0; i < Ns.length; i++)
					Ns[i].fset_gif(panel.getGraphics());
				(panel.getGraphics()).setColor(Color.black);
				for(int i = 0; i < 500; i++)
					(panel.getGraphics()).drawLine(i,0,i,500);
				update = false;
			}
			for(Neuron N:Ns) N.fdraw(Color.white);
			for(int i:output_ids)
				Ns[i].fdraw(Color.blue);
			for(int i:input_ids)
				Ns[i].fdraw(Color.red);
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
		
		return action[action.length-1];
	}
	private void fadd_memorie(float[] output){
		for(int i = 1; i < values.length; i++){
			values[i-1] = values[i];
			action[i-1] = action[i];
		}
		for(int i = 0; i < values[0].length; i++)
			values[values.length-1][i] = Ns[i].fget_value();
		action[action.length-1] = 0;
		for(int i = 1; i < output_ids.length; i++)
			if(output[i] > output[action[action.length-1]]) action[action.length-1] = i;
	}
	public void ferase_memories(){
		for(int i = 0; i < values.length; i++){
			for(int j = 0; j < values[0].length; j++)
				values[i][j] = 0.0f;
			action[i] = 0;
		}
	}
	public void fback_propagate(boolean incentive){
		fback_propagate(incentive, 1);
	}
	public void fback_propagate(boolean incentive, float size){
		if(size < 0.0f) size = 1.0f;
		float[] corrections = new float[output_ids.length];
		
		for(int i = 0; i < values.length; i++){
			for(int j = 0; j < values[0].length; j++)
				Ns[i].fset_value(values[i][j]);
			
			if(incentive){
				for(int j = 0; j < corrections.length; j++)
					corrections[j] = 0.0f;
				corrections[action[i]] = 1.0f;
			} else{
				for(int j = 0; j < corrections.length; j++)
					corrections[j] = 1.0f;
				corrections[action[i]] = 0.0f;
			}
			
			finitiate_back_propagate(corrections, size);
		}
	}
	private void finitiate_back_propagate(float[] corrections, float size){
		for(int i = 0; i < output_ids.length; i++){
			Ns[output_ids[i]].fready_up(true);
			while(!Ns[output_ids[i]].fready_up(false)[0]);
			
			//Ns[output_ids[i]].fstart();
			Ns[output_ids[i]].fback_propagate(2*(Ns[output_ids[i]].fget_value() - corrections[i]), size);
			
			for(int j = 0; j < Ns.length; j++)
				Ns[j].freset();
		}
		
		for(int i = 0; i < Ns.length; i++)
			Ns[i].fsettle_correction();
	}
	public String fget_data(){
		String data = new String();
		for(Neuron N:Ns) data += N.fget_data() + '\n';
		return data;
	}
	public void fprint_connections(){
		for(Neuron N:Ns) N.fprint_connections();
	}
	public void fset_gif(JPanel new_panel){
		panel = new_panel;
		gif = true;
		update = true;
	}
	public void fdisable_gif(){
		if(gif == false) return;
		for(int i = 0; i < Ns.length; i++)
			Ns[i].fdisable_gif();
		gif = false;
	}
}
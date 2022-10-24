public class AI{
	Neuron[] Ns;
	
	int[] output_ids;
	int[][] input_ids;
	
	int action;
	float[] output;
	float[] values;
	
	public AI(int n_receivers, int n_mem, int n_transmitters){
		int k = 0;
		if(n_mem < 0) n_mem = 0;
		n_mem++;
		
		Ns = new Neuron[n_receivers*n_mem + n_transmitters];
		
		for(int i = 0; i < Ns.length; i++)
			Ns[i] = new Neuron(i);
		
		output_ids = new int[n_transmitters];
		input_ids = new int[n_mem][n_receivers];
		for(int i = 0; i < n_transmitters; i++) output_ids[i] = k++;
		for(int i = 0; i < n_mem; i++) for(int j = 0; j < n_receivers; j++) input_ids[i][j] = k++;
		
		for(int id_out:output_ids) for(int[] lines:input_ids) for(int id_in:lines)
			Ns[id_out].fconnect_to(Ns[id_in]);
		
		values = new float[Ns.length];
		for(int i = 0; i < Ns.length; i++)
			Ns[i].fset_random();
	}
	public void fset_input(float[] input){
		for(int i = 1; i < input_ids.length; i++) for(int j = 0; j < input_ids[0].length; j++)
			Ns[input_ids[i-1][j]].fset_value(Ns[input_ids[i][j]].fget_value());
		
		int k = 0;
		for(int id:input_ids[input_ids.length-1])
			Ns[id].fset_value(input[k++]);
	}
	public int fstart(){
		for(int i = 0; i < Ns.length; i++)
			values[i] = Ns[i].fget_value();
		
		output = new float[output_ids.length];
		for(int i = 0; i < output_ids.length; i++){
			Ns[output_ids[i]].fready_up(true);
			while(!Ns[output_ids[i]].fready_up(false)[0]);
			output[i] = Ns[output_ids[i]].fstart();
			for(int j = 0; j < Ns.length; j++)
				Ns[j].freset();
		}
		
		action = 0;
		for(int i = 1; i < output_ids.length; i++)
			if(output[i] > output[action]) action = i;
		
		return action;
	}
	public void fback_propagate(boolean incentive){
		fback_propagate(incentive, 1);
	}
	public void fback_propagate(boolean incentive, float size){
		if(size < 0.0f) size = 1.0f;
		
		for(int i = 0; i < Ns.length; i++)
			Ns[i].fset_value(values[i]);
		
		float[] corrections = new float[output_ids.length];
		if(incentive){
			for(int i = 0; i < corrections.length; i++)
				corrections[i] = 0.0f;
			corrections[action] = 1.0f;
		} else{
			for(int i = 0; i < corrections.length; i++)
				corrections[i] = 1.0f;
			corrections[action] = 0.0f;
		}
		
		for(int i = 0; i < output_ids.length; i++){
			Ns[output_ids[i]].fready_up(true);
			while(!Ns[output_ids[i]].fready_up(false)[0]);
			
			Ns[output_ids[i]].fback_propagate(2*(output[i] - corrections[i]), size);
			
			Ns[output_ids[i]].fstart();
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
}
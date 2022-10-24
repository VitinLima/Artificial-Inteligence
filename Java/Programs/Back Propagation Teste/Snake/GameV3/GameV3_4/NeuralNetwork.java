import java.lang.Thread;

public class NeuralNetwork{
	public Neuron[] neurons;
	public int[] input_ids;
	public int[] output_ids;
	private int[] inner;
	public NeuralNetwork(){
		inner = new int[0];
		
	}
	
	public float[] fget_output(float[] input){
		for(int i = 0; i < input_ids.length; i++)
			neurons[input_ids[i]].fset_value(input[i]);
		
		float[] output = new float[output_ids.length];
		for(int i = 0; i < output_ids.length; i++)
			output[i] = neurons[output_ids[i]].fget_value();
		
		return output;
	}
	
	public void fmodify(float[] correted_output){
		for(int i = 0; i < output_ids.length; i++)
			neurons[output_ids[i]].fmodify(2.0f*(neurons[output_ids[i]].fget_simple_value() - correted_output[i]));
		
		for(int i = 0; i < output_ids.length; i++)
			neurons[output_ids[i]].fback_propagation();
		
		for(int i = 0; i < neurons.length; i++)
			neurons[i].freset();
	}
	
	public class Neuron{
		private int id;
		private float value;
		private float zi;
		private float correction;
		private Dentrite[] dentrites;
		private float bias;
		private boolean is_active;
		private boolean is_receptor;
		
		public Neuron(int new_id){
			id = new_id;
			value = 0.0f;
			zi= 0.0f;
			dentrites = new Dentrite[0];
			bias = 0.0f;
			correction = 0.0f;
			is_active = false;
			is_receptor = false;
		}
		public void fset_receptor(boolean new_is_receptor){
			is_receptor = new_is_receptor;
		}
		public boolean fis_receptor(){
			return is_receptor;
		}
		public void freset(){
			is_active = false;
		}
		public int fget_id(){
			return id;
		}
		public void fset_value(float new_value){
			if(is_receptor)
				value = new_value;
		}
		public void fset_bias(float new_bias){
			bias = new_bias;
		}
		public float fget_bias(){
			return bias;
		}
		public void fset_dentrite_multiplier(int new_dentrite_id, float new_dentrite_multiplier){
			dentrites[new_dentrite_id].fset_dentrite_multiplier(new_dentrite_multiplier);
		}
		public float fget_dentrite_multiplier(int new_dentrite_id){
			return dentrites[new_dentrite_id].fget_dentrite_multiplier();
		}
		public int fget_dentrite_axon_id(int new_dentrite_id){
			return dentrites[new_dentrite_id].fget_dentrite_axon_id();
		}
		public int fget_number_of_dentrites(){
			return dentrites.length;
		}
		public float fget_value(){
			if(is_receptor || is_active) return value;
			is_active = true;
			
			int[] temp = new int[inner.length+1];
			for(int i = 0; i < inner.length; i++)
				temp[i] = inner[i];
			temp[inner.length] = id;
			inner = temp.clone();
			temp = null;
			for(int i = 0; i < inner.length; i++)
				System.out.print(inner[i] + " ");
			System.out.println("");
			try{
				Thread.sleep(100);
			} catch(Exception e){
				System.out.println(e);
			}
			
			zi = 0.0f;
			for(int i = 0; i < dentrites.length; i++)
				zi += dentrites[i].fget_value();
			zi += bias;
			value = Sigmoid(zi);
			
			temp = new int[inner.length-1];
			for(int i = 0; i < temp.length; i++)
				temp[i] = inner[i];
			inner = temp.clone();
			
			is_active = false;
			return value;
		}
		public float fget_simple_value(){
			return value;
		}
		public void fmodify(float increment){
			if(is_receptor || is_active) return;
			is_active = true;
			increment *= DSigmoid(zi);
			for(int i = 0; i < dentrites.length; i++)
				dentrites[i].fmodify(increment);
			correction -= increment;
			is_active = false;
		}
		public void fback_propagation(){
			if(is_receptor || is_active) return;
			is_active = true;
			bias += correction;
			correction = 0.0f;
			for(int i = 0; i < dentrites.length; i++)
				dentrites[i].fback_propagation();
		}
		public void fadd_dentrite(int new_axon_id){
			Dentrite[] temp = new Dentrite[dentrites.length+1];
			int k = 0;
			for(Dentrite dentrite:dentrites)
				temp[k++] = dentrite;
			temp[k] = new Dentrite(new_axon_id);
			dentrites = temp;
			is_active = false;
		}
		private class Dentrite{
			private int axon_id;
			private float multiplier;
			private float correction;
			public Dentrite(int new_axon_id){
				axon_id = new_axon_id;
				multiplier = 0.0f;
				correction = 0.0f;
			}
			public void fset_dentrite_multiplier(float new_dentrite_multiplier){
				multiplier = new_dentrite_multiplier;
			}
			public float fget_dentrite_multiplier(){
				return multiplier;
			}
			public int fget_dentrite_axon_id(){
				return axon_id;
			}
			public float fget_value(){
				return multiplier*neurons[axon_id].fget_value();
			}
			public float fget_simple_value(){
				return multiplier*neurons[axon_id].fget_simple_value();
			}
			public void fmodify(float increment){
				correction -= increment*neurons[axon_id].fget_simple_value();
				neurons[axon_id].fmodify(increment*multiplier);
			}
			public void fback_propagation(){
				multiplier += correction;
				correction = 0.0f;
				neurons[axon_id].fback_propagation();
			}
		}
	}
	private float DSigmoid(float x){
		if(x*x > 16)
			return 0.01f;
		float V = Sigmoid(x);
		V *= V;
		V *= exp(-x);
		return V;
	}
	private float Sigmoid(float x){
		if(x*x > 16){
			if(x > 0)
				return 1;
			else
				return 0;
		}
		return 1.0f/(1.0f+exp(-x));
	}
	private float exp(float x){
		return 1+exp(1, x, 1);
	}
	private float exp(float Last, float x, int a){
		if(a == 25)
			return 0;
		else
			return Last*x/a + exp(Last*x/a, x, a+1);
	}
	public NeuralNetwork(int number_of_neurons){
		inner = new int[0];
		
		neurons = new Neuron[number_of_neurons];
		int k = 0;
		for(Neuron neuron:neurons)
			neurons[k] = new Neuron(k++);
	}
	public NeuralNetwork(int[] newindexes){
		inner = new int[0];
		
		int[] indexes = newindexes.clone();
		for(int i = 1; i < indexes.length; i++)
			indexes[i] += indexes[i-1];
		neurons = new Neuron[indexes[indexes.length-1]];
		for(int i = 0; i < indexes[indexes.length-1]; i++)
			neurons[i] = new Neuron(i);
		for(int i = 0; i < indexes[0]; i++)
			neurons[i].fset_receptor(true);
		int n = 0;
		for(int i = 1; i < indexes.length; i++){
			for(int j = indexes[i-1]; j < indexes[i]; j++){
				for(int k = n; k < indexes[i-1]; k++)
					neurons[j].fadd_dentrite(k);
			}
			n = indexes[i-1];
		}
		int k = 0;
		for(int i = 0; i < indexes[0]; i++) k++;
		input_ids = new int[k];
		for(int i = 0; i < indexes[0]; i++)
			input_ids[i] = i;
		k = 0;
		for(int i = indexes[indexes.length-2]; i < indexes[indexes.length-1]; i++) k++;
		output_ids = new int[k];
		k = 0;
		for(int i = indexes[indexes.length-2]; i < indexes[indexes.length-1]; i++)
			output_ids[k++] = i;
		fset_random_values();
	}
	public NeuralNetwork(int number_of_inputs, int number_of_processors, int number_of_outputs){
		inner = new int[0];
		
		int number_of_neurons = number_of_inputs + number_of_processors + number_of_outputs;
		neurons = new Neuron[number_of_neurons];
		int k = 0;
		for(Neuron neuron:neurons)
			neurons[k] = new Neuron(k++);
		
		input_ids = new int[number_of_inputs];
		output_ids = new int[number_of_outputs];
		
		boolean temp;
		for(int i = 0; i < input_ids.length; i++){
			temp = true;
			while(temp){
				temp = false;
				input_ids[i] = (int)((float)Math.random()*(float)(number_of_neurons - 1));
				for(int j = 0; j < i; j++)
					if(input_ids[i] == input_ids[j]) temp = true;
			}
		}
		for(int i:input_ids)
			neurons[i].fset_receptor(true);
		
		for(int i = 0; i < output_ids.length; i++){
			temp = true;
			while(temp){
				temp = false;
				output_ids[i] = (int)((float)Math.random()*(float)(number_of_neurons - 1));
				if(neurons[output_ids[i]].fis_receptor()) temp = true;
				for(int j = 0; j < i; j++)
					if(output_ids[i] == output_ids[j]) temp = true;
			}
		}
		
		int temp_int_1;
		int temp_int_2;
		for(int i = 0; i < neurons.length; i++) if(!neurons[i].fis_receptor()){
			temp_int_1 = (int)((float)Math.random()*(float)(number_of_neurons - 1));
			for(int j = 0; j < temp_int_1; j++){
				temp_int_2 = (int)((float)Math.random()*(float)(number_of_neurons - 1));
				neurons[i].fadd_dentrite(temp_int_2);
			}
		}
		fset_random_values();
	}
	public void fset_random_values(){
		int k = 0;
		for(Neuron neuron:neurons){
			for(int j = 0; j < neurons[k].fget_number_of_dentrites(); j++)
				neurons[k].fset_dentrite_multiplier(j, ((float)Math.random()*2.0f)-1.0f);
			neurons[k++].fset_bias(((float)Math.random()*2.0f)-1.0f);
		}
	}
	public String fprint_network_information(){
		String data = new String("Displaying information about Neural Network:\n");
		for(Neuron neuron:neurons){
			data += '\n';
			if(!neuron.fis_receptor()){
				data += " Information about neuron " + neuron.fget_id() + ":\n";
				data += "  Has Bias equal to " + neuron.fget_bias() + "\n";
				if(neuron.fget_number_of_dentrites() == 0)
					data += "  Receives signal from no neuron\n";
				for(int i = 0; i < neuron.fget_number_of_dentrites(); i++)
					data += "  Receives signal from neuron " + neuron.fget_dentrite_axon_id(i) + " with weight of " + neuron.fget_dentrite_multiplier(i) + "\n";
			} else data += " Neuron " + neuron.fget_id() + " is a receptor\n";
		}
		return data;
	}
}
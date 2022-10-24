import java.lang.Thread;

public class NeuralNetwork{
	public int[] input_ids;
	public int[] output_ids;
	
	public Neuron[] neurons;
	
	public NeuralNetwork(){
		neurons = new Neuron[0];
		
		input_ids = new int[0];
		output_ids = new int[0];
	}
	
	public NeuralNetwork(int number_of_inputs, int intemp, int number_of_outputs, int memories){
		neurons = new Neuron[0];
		
		input_ids = new int[0];
		output_ids = new int[0];
	}
	
	public NeuralNetwork(int number_of_neurons){
		neurons = new Neuron[number_of_neurons];
		for(int i = 0; i < neurons.length; i++)
			neurons[i] = new Neuron(i);
		
		input_ids = new int[0];
		output_ids = new int[0];
	}
	
	public NeuralNetwork(int[] indexes){
		int number_of_neurons = 0;
		for(int i:indexes)
			number_of_neurons += i;
		neurons = new Neuron[number_of_neurons];
		
		input_ids = new int[indexes[0]];
		for(int i = 0; i < indexes[0]; i++)
			input_ids[i] = i;
		
		output_ids = new int[indexes[indexes.length-1]];
		int n = 0;
		for(int i = number_of_neurons - indexes[indexes.length-1]; i < number_of_neurons; i++)
			output_ids[n++] = i;
		
		for(int i = 0; i < neurons.length; i++)
			neurons[i] = new Neuron(i);
		
		for(int i = 0; i < indexes[0]; i++)
			neurons[i].fset_receptor();
		
		n = 0;
		for(int i = 1; i < indexes.length; i++){
			for(int j = n + indexes[i-1]; j < n + indexes[i-1] + indexes[i]; j++)
				for(int k = n; k < n + indexes[i-1]; k++)
					neurons[j].fadd_dentrite(neurons[k]);
			n += indexes[i-1];
		}
		
		fset_random();
	}
	
	public NeuralNetwork(int number_of_inputs, int intemp, int number_of_outputs){
		int number_of_neurons = number_of_inputs + intemp + number_of_outputs;
		
		neurons = new Neuron[number_of_neurons];
		for(int i = 0; i < neurons.length; i++)
			neurons[i] = new Neuron(i);
		
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
			neurons[i].fset_receptor();
		
		for(int i = 0; i < output_ids.length; i++){
			temp = true;
			while(temp){
				temp = false;
				output_ids[i] = (int)((float)Math.random()*(float)(number_of_neurons - 1));
				
				for(int j = 0; j < input_ids.length; j++)
					if(output_ids[i] == input_ids[j]) temp = true;
				for(int j = 0; j < i; j++)
					if(output_ids[i] == output_ids[j]) temp = true;
			}
		}
		
		int temp_int_1;
		int temp_int_2;
		for(int i = 0; i < neurons.length; i++){
			temp = true;
			for(int j:input_ids) if(i == j) temp = false;
			if(temp){
				temp_int_1 = (int)((float)Math.random()*(float)(number_of_neurons - 1));
				for(int j = 0; j < temp_int_1; j++){
					temp_int_2 = (int)((float)Math.random()*(float)(number_of_neurons - 1));
					neurons[i].fadd_dentrite(neurons[temp_int_2]);
				}
			}
		}
		
		fset_random();
	}
	
	public void fset_random(){
		for(int i = 0; i < neurons.length; i++)
			neurons[i].fset_random();
	}
	
	public float[] fget_output(float[] input){
		for(int i = 0; i < input_ids.length; i++)
			neurons[input_ids[i]].fset_value(input[i]);
		
		float[] output = new float[output_ids.length];
		for(int i = 0; i < output_ids.length; i++)
			output[i] = neurons[output_ids[i]].factivate();
		
		return output;
	}
	
	public void fback_propagation(float[] correted_output){
		for(int i = 0; i < output_ids.length; i++)
			neurons[output_ids[i]].fadd_back_propagation(2.0f*(neurons[output_ids[i]].fget_value() - correted_output[i]));
		
		for(int i = 0; i < neurons.length; i++)
			neurons[i].fset_back_propagation();
	}
	
	
	public String fget_data(){
		String data = new String("Displaying information about Neural Network:\n\n");
		for(Neuron neuron:neurons){
			data += neuron.fget_data();
			data += '\n';
		}
		return data;
	}
}
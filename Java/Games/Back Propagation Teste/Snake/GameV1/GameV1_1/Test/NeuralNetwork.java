public class NeuralNetwork{
	public Neuron[] neurons;
	public int[] transmissorneuronsIds;
	public NeuralNetwork(int number_of_neurons){
		neurons = new Neuron[number_of_neurons];
		int k = 0;
		for(Neuron neuron:neurons)
			neurons[k] = new Neuron(k++);
	}
	public void setRandomValues(){
		int k = 0;
		for(Neuron neuron:neurons){
			for(int j = 0; j < neurons[k].getNumberOfDentrites(); j++)
				neurons[k].setDentriteMultiplier(j, ((float)Math.random()*2.0f)-1.0f);
			neurons[k++].setBias(((float)Math.random()*2.0f)-1.0f);
		}
	}
	public void setReceptorNeuronsValues(float[] newreceptors){
		int n = 0;
		for(int m:getReceptorNeuronsIds())
			neurons[m].setValue(newreceptors[n++]);
	}
	public float[] getAnswer(){
		float[] answer = new float[transmissorneuronsIds.length];
		int n = 0;
		for(int m:transmissorneuronsIds)
			answer[n++] = neurons[m].getValue();
		return answer;
	}
	public void Modify(float[] modifications, int[] neurons_to_modify){
		int n = 0;
		for(int m:neurons_to_modify)
			neurons[m].Modify(2.0f*(neurons[m].getValue() - modifications[n++]));
	}
	public void Modify(float[] modifications){
		int n = 0;
		for(int m:transmissorneuronsIds)
			neurons[m].Modify(2.0f*(neurons[m].getValue() - modifications[n++]));
	}
	public class Neuron{
		private int Id;
		private float value;
		private Dentrite[] dentrites;
		private float bias;
		private boolean isactive;
		private boolean isreceptor;
		
		public Neuron(int newId){
			Id = newId;
			value = 0;
			dentrites = new Dentrite[0];
			bias = 0;
			isactive = false;
			isreceptor = false;
		}
		public void setReceptor(boolean newisreceptor){
			isreceptor = newisreceptor;
		}
		public boolean isReceptor(){
			return isreceptor;
		}
		public int getId(){
			return Id;
		}
		public void setValue(float newvalue){
			if(isreceptor) value = newvalue;
		}
		public void setBias(float newbias){
			bias = newbias;
		}
		public float getBias(){
			return bias;
		}
		public void setDentriteMultiplier(int newdentriteId, float newdentritemultiplier){
			dentrites[newdentriteId].setDentriteMultiplier(newdentritemultiplier);
		}
		public float getDentriteMultiplier(int newdentriteId){
			return dentrites[newdentriteId].getDentriteMultiplier();
		}
		public int getDentriteAxonId(int newdentriteId){
			return dentrites[newdentriteId].getDentriteAxonId();
		}
		public int getNumberOfDentrites(){
			return dentrites.length;
		}
		public void Modify(float increment){
			//System.out.println("Neuron " + Id + " is receptor: " + isreceptor);
			if(isreceptor || isactive) return;
			isactive = true;
			float zi = 0.0f;
			for(Dentrite dentrite:dentrites)
				zi += dentrite.getValue();
			zi += bias;
			increment *= DSigmoid(zi);
			for(int i = 0; i < dentrites.length; i++)
				dentrites[i].Modify(increment);
			bias -= increment;
			//System.out.println("Neuron " + Id + " has changed");
			isactive = false;
		}
		public void addDentrite(int newaxonId){
			Dentrite[] temp = new Dentrite[dentrites.length+1];
			int k = 0;
			for(Dentrite dentrite:dentrites)
				temp[k++] = dentrite;
			temp[k] = new Dentrite(newaxonId);
			dentrites = temp;
		}
		public float getValue(){
			if(isreceptor || isactive) return value;
			isactive = true;
			value = 0.0f;
			for(Dentrite dentrite:dentrites)
				value += dentrite.getValue();
			value += bias;
			value = Sigmoid(value);
			isactive = false;
			return value;
		}
		private class Dentrite{
			private int axonId;
			private float multiplier;
			public Dentrite(int newaxonId){
				axonId = newaxonId;
				multiplier = 0;
			}
			public void setDentriteMultiplier(float newdentritemultiplier){
				multiplier = newdentritemultiplier;
			}
			public float getDentriteMultiplier(){
				return multiplier;
			}
			public int getDentriteAxonId(){
				return axonId;
			}
			public float getValue(){
				return multiplier*neurons[axonId].getValue();
			}
			public void Modify(float increment){
				neurons[axonId].Modify(increment*multiplier);
				multiplier -= increment*neurons[axonId].getValue();
			}
		}
	}
	public String printNetworkInformation(){
		String data = new String("Displaying information about Neural Network:\n");
		for(Neuron neuron:neurons){
			data += '\n';
			if(!neuron.isReceptor()){
				data += " Information about neuron " + neuron.getId() + ":\n";
				data += "  Has Bias equal to " + neuron.getBias() + "\n";
				if(neuron.getNumberOfDentrites() == 0)
					data += "  Receives signal from no neuron\n";
				for(int i = 0; i < neuron.getNumberOfDentrites(); i++)
					data += "  Receives signal from neuron " + neuron.getDentriteAxonId(i) + " with weight of " + neuron.getDentriteMultiplier(i) + "\n";
			} else data += " Neuron " + neuron.getId() + " is a receptor\n";
		}
		return data;
	}
	public void Basic_Constructor_1(int[] newindexes){
		int[] indexes = newindexes.clone();
		for(int i = 1; i < indexes.length; i++)
			indexes[i] += indexes[i-1];
		neurons = new Neuron[indexes[indexes.length-1]];
		for(int i = 0; i < indexes[indexes.length-1]; i++)
			neurons[i] = new Neuron(i);
		for(int i = 0; i < indexes[0]; i++)
			neurons[i].setReceptor(true);
		int n = 0;
		for(int i = 1; i < indexes.length; i++){
			for(int j = indexes[i-1]; j < indexes[i]; j++){
				for(int k = n; k < indexes[i-1]; k++)
					neurons[j].addDentrite(k);
			}
			n = indexes[i-1];
		}
		transmissorneuronsIds = new int[indexes[indexes.length-1] - indexes[indexes.length-2]];
		int m = 0;
		for(int i = indexes[indexes.length-2]; i < indexes[indexes.length-1]; i++)
			transmissorneuronsIds[m++] = i;
	}
	public int[] getTransmissorNeuronsIds(){
		return transmissorneuronsIds.clone();
	}
	public void setTransmissorNeuronsIds(int[] newtransmissorneuronsIds){
		transmissorneuronsIds = newtransmissorneuronsIds.clone();
	}
	public int[] getReceptorNeuronsIds(){
		int[] receptorneuronsIds = new int[0];
		int[] temp;
		for(Neuron neuron:neurons) if(neuron.isReceptor()){
			temp = receptorneuronsIds;
			receptorneuronsIds = new int[receptorneuronsIds.length+1];
			receptorneuronsIds[0] = neuron.getId();
			int k = 1;
			for(int n:temp)
				receptorneuronsIds[k++] = n;
		}
		return receptorneuronsIds;
	}
	private float DSigmoid(float x){
		if(x*x > 36)
			return 0.01f;
		float V = Sigmoid(x);
		V *= V;
		V *= exp(-x);
		return V;
	}
	private float Sigmoid(float x){
		if(x*x > 36){
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
}
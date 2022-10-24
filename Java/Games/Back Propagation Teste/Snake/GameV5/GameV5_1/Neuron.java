public class Neuron{
	private int id;
	private boolean is_active;
	private boolean is_receptor;
	
	private float value;
	private float zi;
	
	private float bias;
	private float correction;
	
	private Dentrite[] dentrites;
	
	public Neuron(int new_id){
		id = new_id;
		is_active = false;
		is_receptor = false;
		value = 0.0f;
		zi= 0.0f;
		bias = 0.0f;
		correction = 0.0f;
		dentrites = new Dentrite[0];
	}
	public void fset_value(float new_value){
		value = new_value;
	}
	public float fget_value(){
		return value;
	}
	public void fset_receptor(){
		is_receptor = true;
	}
	public int fget_id(){
		return id;
	}
	public void fadd_dentrite(Neuron neuron){
		Dentrite[] temp = new Dentrite[dentrites.length+1];
		for(int i = 0; i < dentrites.length; i++)
			temp[i] = dentrites[i];
		temp[dentrites.length] = new Dentrite(neuron);
		dentrites = temp;
	}
	public float factivate(){
		if(is_receptor || is_active) return value;
		is_active = true;
		
		zi = 0.0f;
		for(int i = 0; i < dentrites.length; i++)
			zi += dentrites[i].factivate();
		zi += bias;
		value = Sigmoid(zi);
		
		is_active = false;
		return value;
	}
	public void fadd_back_propagation(float increment){
		if(is_receptor || is_active) return;
		is_active = true;
		
		increment *= DSigmoid(zi);
		
		for(int i = 0; i < dentrites.length; i++)
			dentrites[i].fadd_back_propagation(increment);
		correction -= increment;
		
		is_active = false;
	}
	public void fset_back_propagation(){
		if(is_receptor) return;
		bias += correction;
		correction = 0.0f;
		for(int i = 0; i < dentrites.length; i++)
			dentrites[i].fset_back_propagation();
	}
	public void fset_random(){
		for(int i = 0; i < dentrites.length; i++)
			dentrites[i].fset_random();
		bias = (float)Math.random()*2.0f - 1.0f;
	}
	public String fget_data(){
		String data = new String();
		if(is_receptor) data += "Neuron " + id + " is a receptor\n";
		else{
			data += "Information about neuron " + id + ":\n\n has bias of " + bias + " (correction " + correction + ")\n";
			if(dentrites.length == 0) data += " receives data from no other neuron\n";
			else for(Dentrite dentrite:dentrites) data += dentrite.fget_data();
		}
		return data;
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
}
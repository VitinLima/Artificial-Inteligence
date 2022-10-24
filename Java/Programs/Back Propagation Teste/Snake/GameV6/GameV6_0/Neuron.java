public class Neuron{
	private int id;
	private boolean flag;
	private boolean ready;
	
	private float value;
	private float zi;
	
	private float bias;
	private float correction;
	
	private Dentrite[] Ds;
	
	public Neuron(int new_id){
		id = new_id;
		flag = false;
		ready = false;
		value = 0.0f;
		zi= 0.0f;
		bias = 0.0f;
		correction = 0.0f;
		Ds = new Dentrite[0];
	}
	public boolean[] fready_up(boolean check){
		if(check){
			if(!flag){
				flag = true;
				return new boolean[]{false, true};
			}
			else return new boolean[]{true, false};
		}
		
		ready = true;
		for(int i = 0; i < Ds.length; i++)
			if(!Ds[i].fready_up()) ready = false;
		
		return new boolean[]{ready, false};
	}
	public float fstart(){
		if(Ds.length == 0) return value;
		
		zi = 0.0f;
		for(int i = 0; i < Ds.length; i++)
			zi += Ds[i].fstart();
		zi += bias;
		value = Sigmoid(zi);
		
		return value;
	}
	public float fget_value(){
		return value;
	}
	public void fset_value(float new_value){
		value = new_value;
	}
	public void freset(){
		flag = false;
		ready = false;
		for(int i = 0; i < Ds.length; i++) Ds[i].freset();
	}
	public void fback_propagate(float incentive, float size){
		if(Ds.length == 0) return;
		
		incentive *= size*DSigmoid(zi);
		
		for(int i = 0; i < Ds.length; i++)
			Ds[i].fback_propagate(incentive, size);
		
		correction += incentive;
	}
	public void fsettle_correction(){
		bias -= correction;
		correction = 0.0f;
		for(int i = 0; i < Ds.length; i++)
			Ds[i].fsettle_correction();
	}
	public void fconnect_to(Neuron neuron){
		Dentrite[] temp = new Dentrite[Ds.length+1];
		for(int i = 0; i < Ds.length; i++)
			temp[i] = Ds[i];
		temp[Ds.length] = new Dentrite(neuron, fget_id());
		Ds = temp;
	}
	public void fset_random(){
		for(int i = 0; i < Ds.length; i++)
			Ds[i].fset_random();
		bias = (float)Math.random()*2.0f - 1.0f;
	}
	public int fget_id(){
		return id;
	}
	public String fget_data(){
		String data = new String();
		data += "Information about neuron " + id + ":\n current value is " + value + '\n';
		if(Ds.length == 0) data += " is a receiver\n";
		else{
			data += " has bias of " + bias + " (correction " + correction + ")\n";
			for(Dentrite dentrite:Ds) data += dentrite.fget_data();
		}
		return data;
	}
	public void fprint_connections(){
		for(Dentrite D:Ds) D.fprint_connections();
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
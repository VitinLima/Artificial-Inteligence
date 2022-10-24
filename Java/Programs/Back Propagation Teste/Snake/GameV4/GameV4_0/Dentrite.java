public class Dentrite{
	private float multiplier;
	private float correction;
	
	private Neuron neuron;
	
	public Dentrite(Neuron new_neuron){
		neuron = new_neuron;
		
		multiplier = 0.0f;
		correction = 0.0f;
	}
	public float factivate(){
		return multiplier*neuron.factivate();
	}
	public void fadd_back_propagation(float increment){
		correction -= increment*neuron.fget_value();
		neuron.fadd_back_propagation(increment*multiplier);
	}
	public void fset_back_propagation(){
		multiplier += correction;
		correction = 0.0f;
	}
	public void fset_random(){
		multiplier = (float)Math.random()*2.0f - 1.0f;;
	}
	public String fget_data(){
		return new String(" receives data from neuron " + neuron.fget_id() + " with weight of " + multiplier + " (correction " + correction + ")\n");
	}
}
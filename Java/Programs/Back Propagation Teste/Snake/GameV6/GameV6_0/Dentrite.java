public class Dentrite{
	private boolean flag;
	private boolean check;
	
	private int owner;
	private float multiplier;
	private float correction;
	
	private Neuron neuron;
	
	public Dentrite(Neuron new_neuron, int new_owner){
		neuron = new_neuron;
		owner = new_owner;
		
		flag = false;
		check = true;
		
		multiplier = 0.0f;
		correction = 0.0f;
	}
	public boolean fready_up(){
		if(check){
			check = false;
			boolean[] temp = neuron.fready_up(true);
			flag = temp[1];
			return temp[0];
		}
		
		if(!flag) return true;
		return neuron.fready_up(false)[0];
	}
	public float fstart(){
		if(flag) return neuron.fstart()*multiplier;
		return neuron.fget_value()*multiplier;
	}
	public void freset(){
		flag = false;
		check = true;
	}
	public void fback_propagate(float incentive, float size){
		correction += incentive*neuron.fget_value();
		if(flag) neuron.fback_propagate(incentive*multiplier, size);
	}
	public void fsettle_correction(){
		multiplier -= correction;
		correction = 0.0f;
	}
	public void fprint_connections(){
		if(flag) System.out.println("neuron " + owner + " activates neuron " + neuron.fget_id());
	}
	public void fset_random(){
		multiplier = (float)Math.random()*2.0f - 1.0f;;
	}
	public String fget_data(){
		return new String(" receives data from neuron " + neuron.fget_id() + " with weight of " + multiplier + " (correction " + correction + ")\n");
	}
}
public class Axon{
	private int[] axon_connections;
	private float multiplier;
	private float correction;
	public Axon(){
		axon_connection int[0];
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
	public void fadd_back_propagation(float increment){
		correction -= increment*neurons[axon_id].fget_simple_value();
		neurons[axon_id].fmodify(increment*multiplier);
	}
	public void fset_back_propagation(){
		multiplier += correction;
		correction = 0.0f;
		neurons[axon_id].fback_propagation();
	}
}
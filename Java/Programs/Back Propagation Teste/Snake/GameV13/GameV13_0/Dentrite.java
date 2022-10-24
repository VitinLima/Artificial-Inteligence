import javax.swing.*;
import java.awt.*;

public class Dentrite{
	private boolean flag;
	private boolean check;
	private boolean recursive;
	
	private Neuron owner;
	private float multiplier;
	private float correction;
	
	private Neuron neuron;
	
	public Dentrite(Neuron new_neuron, boolean is_recursive, Neuron new_owner){
		neuron = new_neuron;
		recursive = is_recursive;
		owner = new_owner;
		
		flag = false;
		check = true;
		
		multiplier = 0.0f;
		correction = 0.0f;
	}
	public boolean fready_up(){
		if(check){
			check = false;
			if(recursive) return true;
			boolean[] temp = neuron.fready_up(true);
			flag = temp[1];
			return temp[0];
		}
		
		if(!flag) return true;
		return neuron.fready_up(false)[0];
	}
	public float fstart(){
		if(recursive) return neuron.fget_recursive()*multiplier;
		if(flag) return neuron.fstart()*multiplier;
		return neuron.fget_value()*multiplier;
	}
	public void freset(){
		flag = false;
		check = true;
	}
	public void fback_propagate(float incentive){
		if(recursive)
			correction += incentive*neuron.fget_recursive();
		else
			correction += incentive*neuron.fget_value();
		if(flag) neuron.fback_propagate(incentive*multiplier);
	}
	public void fsettle_correction(float divider){
		multiplier -= correction/divider;
		correction = 0.0f;
	}
	
	/*UTIL*/
	
	public void fset_random(){
		multiplier = (float)Math.random()*2.0f - 1.0f;;
	}
	public float fget_multiplier(){
		return multiplier;
	}
	public int fget_id(){
		return neuron.fget_id();
	}
	public boolean fis_flag(){
		return flag;
	}
	public String fget_data(){
		if(recursive)
			return "is connected to neuron " + neuron.fget_id() + " with weight of " + multiplier + " (recursive)\n";
		else
			return "is connected to neuron " + neuron.fget_id() + " with weight of " + multiplier + '\n';
	}
}
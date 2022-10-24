
import javax.swing.*;
import java.awt.*;

public class Dentrite{
	private boolean isActivating;
	private boolean isChecked;
	
	protected float multiplier;
	protected float correction;
	
	protected Neuron neuron;
	
	public Dentrite(Neuron neuron){
		this.neuron = neuron;
		isChecked = false;
		
		isActivating = false;
		
		multiplier = (float)Math.random()*2.0f - 1.0f;
		correction = 0.0f;
	}
	
	public boolean isReady(){
		if(isChecked)
			return neuron.isReady();
		
		isChecked = true;
		isActivating = neuron.checkActive();
		return false;
	}
	
	public float getValue(){
		if(isActivating) return neuron.getValue(true)*multiplier;
		return neuron.getValue(false)*multiplier;
	}
	
	public void backPropagate(float incentive){
		correction += incentive*neuron.getValue(false);
		if(isActivating)
			neuron.backPropagate(incentive*multiplier);
	}
	
	public void settleCorrection(){
		multiplier -= correction;
		correction = 0.0f;
	}
	
	public void reset(){
		isActivating = false;
		isChecked = false;
	}
	
	/*UTIL*/
	
	public int getId(){
		return neuron.getId();
	}
	public String getData(){
		if(isActivating)
			return "is activating neuron " + neuron.getId() + " with weight of " + multiplier + '\n';
		else
			return "is reading neuron " + neuron.getId() + " with weight of " + multiplier + '\n';
	}
}
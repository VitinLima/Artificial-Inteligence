package ai;

import javax.swing.*;
import java.awt.*;

public class RecursiveDentrite extends Dentrite{
	public RecursiveDentrite(Neuron neuron){
		super(neuron);
	}
	
	@Override
	public boolean isReady(){
		return true;
	}
	
	@Override
	public float getValue(){
		return neuron.getRecursiveValue()*multiplier;
	}
	
	@Override
	public void backPropagate(float incentive){
		correction += incentive*neuron.getRecursiveValue();
	}
	
	@Override
	public void reset(){
	}
	
	/*UTIL*/
	
	@Override
	public String getData(){
		return "is reading neuron " + neuron.getId() + " with weight of " + multiplier + " (is recursive)\n";
	}
}
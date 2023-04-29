import javax.swing.*;
import java.awt.*;

public class Dentrite{
	private boolean flag;
	private boolean check;
	
	private Neuron owner;
	private float multiplier;
	private float correction;
	
	private Neuron neuron;
	
	private boolean gif;
	private Graphics panel;
	
	public Dentrite(Neuron new_neuron, Neuron new_owner){
		neuron = new_neuron;
		owner = new_owner;
		
		flag = false;
		check = true;
		
		multiplier = 0.0f;
		correction = 0.0f;
		
		gif = false;
	}
	public boolean fready_up(){
		if(check){
			check = false;
			boolean[] temp = neuron.fready_up(true);
			flag = temp[1];
			if(flag && gif)
				fdraw(Color.gray, Color.gray);
			else
				if(gif)
					fdraw(Color.white, Color.white);
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
	public void fback_propagate(float incentive){
		correction += incentive*neuron.fget_value();
		if(flag) neuron.fback_propagate(incentive*multiplier);
	}
	public void fsettle_correction(float divider){
		multiplier -= correction/divider;
		correction = 0.0f;
	}
	public void fprint_connections(){
		if(flag) System.out.println("neuron " + owner.fget_id() + " activates neuron " + neuron.fget_id());
	}
	public void fset_random(){
		multiplier = (float)Math.random()*2.0f - 1.0f;;
	}
	public String fget_data(){
		return new String(" receives data from neuron " + neuron.fget_id() + " with weight of " + multiplier + " (correction " + correction + ")\n");
	}
	public void fset_gif(Graphics new_panel){
		panel = new_panel;
		gif = true;
	}
	public void fdisable_gif(){
		gif = false;
	}
	public void fdraw(Color outter_color, Color inner_color){
		panel.setColor(outter_color);
		for(int i = -1; i < 2; i++)
			for(int j = -1; j < 2; j++)
				panel.drawLine(owner.fget_x()+i, owner.fget_y()+j, neuron.fget_x()+i, neuron.fget_y()+j);
		panel.setColor(inner_color);
		panel.drawLine(owner.fget_x(), owner.fget_y(), neuron.fget_x(), neuron.fget_y());
	}
}
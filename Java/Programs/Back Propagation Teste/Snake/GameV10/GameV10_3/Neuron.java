import javax.swing.*;
import java.awt.*;

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
	public void freset(){
		flag = false;
		ready = false;
		for(int i = 0; i < Ds.length; i++) Ds[i].freset();
	}
	public void fback_propagate(float incentive){
		if(Ds.length == 0) return;
		
		incentive *= DSigmoid(zi);
		
		for(int i = 0; i < Ds.length; i++)
			Ds[i].fback_propagate(incentive);
		
		correction += incentive;
	}
	public void fsettle_correction(float divider){
		bias -= correction/divider;
		correction = 0.0f;
		for(int i = 0; i < Ds.length; i++)
			Ds[i].fsettle_correction(divider);
	}
	public void fconnect_to(Neuron neuron){
		Dentrite[] temp = new Dentrite[Ds.length+1];
		for(int i = 0; i < Ds.length; i++)
			temp[i] = Ds[i];
		temp[Ds.length] = new Dentrite(neuron, this);
		Ds = temp;
	}
	
	/*UTIL*/
	
	public int fget_id(){
		return id;
	}
	public int[] fget_connection_ids(){
		int[] connection_ids = new int[Ds.length];
		for(int i = 0; i < Ds.length; i++) connection_ids[i] = Ds[i].fget_id();
		return connection_ids;
	}
	public Float fget_multiplier(int target_id){
		for(Dentrite d:Ds) if(d.fget_id() == target_id) return (Float)d.fget_multiplier();
		return null;
	}
	public float fget_value(){
		return value;
	}
	public void fset_value(float new_value){
		value = new_value;
	}
	public void fset_random(){
		for(int i = 0; i < Ds.length; i++)
			Ds[i].fset_random();
		bias = (float)Math.random()*2.0f - 1.0f;
	}
	private float DSigmoid(float x){
		if(x*x > 16)
			return 0.0f;
		float V = Sigmoid(x);
		V *= V;
		V *= exp(-x);
		return V;
	}
	public float Sigmoid(float x){
		if(x*x > 16){
			if(x > 0)
				return 1.0f;
			else
				return 0.0f;
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
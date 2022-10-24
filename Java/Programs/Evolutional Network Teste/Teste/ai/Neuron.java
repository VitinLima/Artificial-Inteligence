package ai;

import javax.swing.*;
import java.awt.*;

public class Neuron{
	private int id;
	private boolean isActive;
	private boolean ready;
	
	private float value;
	private float recursiveValue;
	private float zi;
	
	private float bias;
	private float correction;
	
	private Dentrite[] Ds;
	
	public Neuron(int id){
		this.id = id;
		isActive = false;
		ready = false;
		value = 0.0f;
		recursiveValue = 0.0f;
		zi = 0.0f;
		bias = (float)Math.random()*2.0f - 1.0f;
		correction = 0.0f;
		Ds = new Dentrite[0];
	}
	
	public void backPropagate(float incentive){
		if(Ds.length == 0) return;
		
		incentive *= dSigmoid(zi);
		correction += incentive;
		
		for(int i = 0; i < Ds.length; i++)
			Ds[i].backPropagate(incentive);
	}
	
	public boolean checkActive(){
		if(isActive) return true;
		isActive = true;
		return false;
	}
	
	public boolean isReady(){
		for(int i = 0; i < Ds.length; i++)
			if(!Ds[i].isReady()) return false;
		
		return true;
	}
	
	public float getValue(boolean toActivate){
		if(!toActivate || Ds.length == 0) return value;
		
		recursiveValue = value;
		
		zi = 0.0f;
		for(int i = 0; i < Ds.length; i++)
			zi += Ds[i].getValue();
		zi += bias;
		value = sigmoid(zi);
		
		return value;
	}
	
	public float getRecursiveValue(){
		return recursiveValue;
	}
	
	public void settleCorrection(){
		bias -= correction;
		correction = 0.0f;
		for(int i = 0; i < Ds.length; i++)
			Ds[i].settleCorrection();
	}
	
	public void addConnectionTo(Neuron neuron){
		Dentrite[] temp = new Dentrite[Ds.length+1];
		
		for(int i = 0; i < Ds.length; i++)
			temp[i] = Ds[i];
		temp[Ds.length] = new Dentrite(neuron);
		
		Ds = temp;
	}
	
	public void addRecursiveConnectionTo(Neuron neuron){
		Dentrite[] temp = new Dentrite[Ds.length+1];
		
		for(int i = 0; i < Ds.length; i++)
			temp[i] = Ds[i];
		temp[Ds.length] = new RecursiveDentrite(neuron);
		
		Ds = temp;
	}
	
	public void removeConnectionTo(int id){
		idFound:{
			for(int i= 0; i < Ds.length; i++)
				if(Ds[i].getId() == id) break idFound;
			return;
		}
		
		Dentrite[] temp = new Dentrite[Ds.length-1];
		
		int k = 0;
		boolean flag = false;
		for(int i = 0; i < Ds.length; i++){
			if(Ds[i].getId() == id) continue;
			temp[k++] = Ds[i];
		}
		
		Ds = temp;
	}
	
	public void removeAllConnections(){
		Ds = new Dentrite[0];
	}
	
	public void setValue(float value){
		recursiveValue = this.value;
		this.value = value;
	}
	
	public void reset(){
		isActive = false;
		for(int i = 0; i < Ds.length; i++)
			Ds[i].reset();
	}
	
	/*UTIL*/
	
	public int getId(){
		return id;
	}
	public int[] getConnectedIds(){
		int[] connection_ids = new int[Ds.length];
		for(int i = 0; i < Ds.length; i++) connection_ids[i] = Ds[i].getId();
		return connection_ids;
	}
	public String getData(){
		String data = new String("neuron " + id);
		if(Ds.length == 0) return data + " has no dentrites\n\n";
		data += " has bias of " + bias + '\n';
		for(Dentrite d:Ds)
			data += d.getData();
		return data + '\n';
	}
	public void randomize(){
		bias *= (float)Math.random()*2.0f;
		for(int i = 0; i < Ds.length; i++)
			Ds[i].randomize();
	}
	public Neuron copy(){
		return new Neuron(id,isActive,ready,value,recursiveValue,zi,bias,correction,Ds);
	}
	public Neuron(int id, boolean isActive, boolean ready, float value, float recursiveValue, float zi, float bias, float correction, Dentrite[] Ds){
		this.id = id;
		this.isActive = isActive;
		this.ready = ready;
		this.value = value;
		this.recursiveValue = recursiveValue;
		this.zi = zi;
		this.bias = bias;
		this.correction = correction;
		this.Ds = new Dentrite[Ds.length];
		for(int i = 0; i < Ds.length; i++)
			this.Ds[i] = Ds[i].copy();
	}
	public void updateDentrites(Neuron[] Ns){
		for(int i = 0; i < Ds.length; i++)
			Ds[i].updateDentrite(Ns);
	}
	private float dSigmoid(float x){
		if(x*x > 16)
			return 0.0f;
		float d = sigmoid(x);
		d *= d;
		d *= exp(-x);
		return d;
	}
	private float sigmoid(float x){
		if(x*x > 16){
			if(x > 0)
				return 1.0f;
			else
				return 0.0f;
		}
		return 1.0f/(1.0f+exp(-x));
	}
	private float exp(float x){
		return 1+expSeries(x, 1.0f);
	}
	private float expSeries(float x, float n){
		if((int)n == 20)
			return 0;
		else
			return 1.0f + (x / n) * expSeries(x, ++n);
	}
}
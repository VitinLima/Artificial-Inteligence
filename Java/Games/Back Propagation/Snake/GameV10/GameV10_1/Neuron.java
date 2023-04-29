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
	
	private int x;
	private int y;
	private boolean gif;
	private Graphics panel;
	
	public Neuron(int new_id){
		id = new_id;
		flag = false;
		ready = false;
		value = 0.0f;
		zi= 0.0f;
		bias = 0.0f;
		correction = 0.0f;
		Ds = new Dentrite[0];
		x = -1;
		y = -1;
	}
	public boolean[] fready_up(boolean check){
		if(check){
			if(!flag){
				flag = true;
				if(gif){
					panel.setColor(Color.white);
					panel.drawOval(x-8,y-8,16,16);
					try{
						Thread.sleep(10);
					} catch(Exception e){
						System.out.println(e);
					}
				}
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
		if(gif){
			if(correction > 0.0f)
				panel.setColor(Color.blue);
			else
				panel.setColor(Color.red);
			panel.drawOval(x-8,y-8,16,16);
		}
	}
	public void fconnect_to(Neuron neuron){
		Dentrite[] temp = new Dentrite[Ds.length+1];
		for(int i = 0; i < Ds.length; i++)
			temp[i] = Ds[i];
		temp[Ds.length] = new Dentrite(neuron, this);
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
	public void fset_gif(Graphics new_panel){
		if(x == -1 || y == -1) return;
		panel = new_panel;
		for(int i = 0; i < Ds.length; i++)
			Ds[i].fset_gif(panel);
		gif = true;
	}
	public void fdisable_gif(){
		for(int i = 0; i < Ds.length; i++)
			Ds[i].fdisable_gif();
		gif = false;
	}
	public void fdraw(Color color){
		if(!gif) return;
		for(Dentrite d:Ds)
			d.fdraw(Color.black, Color.white);
		panel.setColor(color);
		panel.drawOval(x-6,y-6,12,12);
		if(bias > 0.0f)
			panel.setColor(new Color(0,0,(int)((Sigmoid(bias)-0.5f)*2.0f*255.0f)));
		else
			panel.setColor(new Color((int)((Sigmoid(-bias)-0.5f)*2.0f*255.0f),0,0));
		panel.fillOval(x-3,y-3,6,6);
	}
	public int fget_x(){
		return x;
	}
	public int fget_y(){
		return y;
	}
	private float DSigmoid(float x){
		if(x*x > 16)
			return 0.0f;
		float V = Sigmoid(x);
		V *= V;
		V *= exp(-x);
		return V;
	}
	private float Sigmoid(float x){
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
	public void fset_position(int new_x, int new_y){
		x = new_x;
		y = new_y;
	}
}
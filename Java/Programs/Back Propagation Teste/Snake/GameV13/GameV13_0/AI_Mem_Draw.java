import javax.swing.*;
import java.awt.*;

public class AI_Mem_Draw extends AI_Mem{
	boolean gif = false;
	boolean update = false;
	int[] x = new int[Ns.length];
	int[] y = new int[Ns.length];
	int width;
	int height;
	JPanel panel;
	int source;
	int[] layers;
	
	public AI_Mem_Draw(int[] ids, int n_mem){
		super(ids, n_mem);
		layers = ids.clone();
	}
	
	@Override
	public int fstart(){
		if(gif){
			if(update){
				if(source == -1)
					forganize_0();
				else
					forganize_1();
				panel.getGraphics().setColor(Color.black);
				for(int i = 0; i < width; i++)
					panel.getGraphics().drawLine(i,0,i,height);
				update = false;
			}
			
			Graphics g = panel.getGraphics();
			if(source == -1){
				float[] output = new float[output_ids.length];
				for(int j = 0; j < Ns.length; j++){
					for(int k:Ns[j].fget_connection_ids()){
						g.setColor(Color.black);
						g.drawLine(x[j]+1,y[j],x[k]+1,y[k]);
						g.drawLine(x[j]-1,y[j],x[k]-1,y[k]);
						g.drawLine(x[j],y[j]+1,x[k],y[k]+1);
						g.drawLine(x[j],y[j]-1,x[k],y[k]-1);
						g.setColor(Color.white);
						g.drawLine(x[j],y[j],x[k],y[k]);
						g.setColor(Color.black);
						g.fillOval(x[j]-5,y[j]-5,10,10);
						g.fillOval(x[k]-5,y[k]-5,10,10);
						g.setColor(Color.white);
						g.drawOval(x[j]-5,y[j]-5,10,10);
						g.drawOval(x[k]-5,y[k]-5,10,10);
					}
				}
				for(int i = 0; i < output_ids.length; i++){
					Ns[output_ids[i]].fready_up(true);
					while(!Ns[output_ids[i]].fready_up(false)[0]){
						for(int j = 0; j < Ns.length; j++){
							for(int k:Ns[j].fget_connection_ids()){
								if(Ns[j].fis_flag() && Ns[k].fis_flag()){
									g.setColor(Color.red);
									g.drawLine(x[j],y[j],x[k],y[k]);
									g.setColor(Color.black);
									g.fillOval(x[j]-5,y[j]-5,10,10);
									g.fillOval(x[k]-5,y[k]-5,10,10);
									g.setColor(Color.white);
									g.drawOval(x[j]-5,y[j]-5,10,10);
									g.drawOval(x[k]-5,y[k]-5,10,10);
								} else{
									g.setColor(Color.white);
									g.drawLine(x[j],y[j],x[k],y[k]);
									g.setColor(Color.black);
									g.fillOval(x[j]-5,y[j]-5,10,10);
									g.fillOval(x[k]-5,y[k]-5,10,10);
									g.setColor(Color.white);
									g.drawOval(x[j]-5,y[j]-5,10,10);
									g.drawOval(x[k]-5,y[k]-5,10,10);
								}
							}
						}
						for(int j = 0; j < Ns.length; j++){
							for(int k:Ns[j].fget_connection_ids()){
								if(Ns[j].fis_dentrite_flag(k)){
									g.setColor(Color.blue);
									g.drawLine(x[j],y[j],x[k],y[k]);
									g.setColor(Color.black);
									g.fillOval(x[j]-5,y[j]-5,10,10);
									g.fillOval(x[k]-5,y[k]-5,10,10);
									g.setColor(Color.white);
									g.drawOval(x[j]-5,y[j]-5,10,10);
									g.drawOval(x[k]-5,y[k]-5,10,10);
								}
							}
						}
					}
					output[i] = Ns[output_ids[i]].fstart();
					for(int j = 0; j < Ns.length; j++){
						g.setColor(new Color((int)((1.0f - Ns[j].fget_value())*255.0f),0,(int)(Ns[j].fget_value()*255.0f)));
						g.fillOval(x[j]-5,y[j]-5,10,10);
						g.setColor(Color.white);
						g.drawOval(x[j]-5,y[j]-5,10,10);
					}
					for(int j = 0; j < Ns.length; j++)
						Ns[j].freset_ready();
					try{
						Thread.sleep(500);
					}catch(Exception e){
						System.out.println(e);
					}
					
				}
				
				fadd_memorie(output);
				try{
					Thread.sleep(1000);
				}catch(Exception e){
					System.out.println(e);
				}
			} else{
				for(int i:Ns[source].fget_connection_ids()){
					g.setColor(Color.white);
					g.fillOval(x[i]-5,y[i]-5,10,10);
					g.setColor(new Color((int)(Ns[source].Sigmoid(-Ns[source].fget_multiplier(i))*255.0f),
					(int)(Ns[source].Sigmoid(Ns[source].fget_multiplier(i))*255.0f),
					0));
					g.fillOval(x[i]-4,y[i]-4,8,8);
				}
				super.fstart();
			}
		} else super.fstart();
		
		return action[0];
	}
	public void fset_gif(JPanel new_panel, int new_source){
		panel = new_panel;
		gif = true;
		update = true;
		source = new_source;
		width = (int)panel.getPreferredSize().getWidth();
		height = (int)panel.getPreferredSize().getHeight();
	}
	public void fdisable_gif(){
		gif = false;
	}
	private void forganize_0(){
		int k = 0;
		int dx = width/layers.length;
		int dy;
		for(int i = 0; i < layers.length; i++){
			dy = height/layers[i];
			for(int j = 0; j < layers[i]; j++){
				x[k] = i*dx + dx/2;
				y[k++] = j*dy + dy/2;
			}
		}
	}
	private void forganize_1(){
		if(Ns[source].fget_connection_ids().length == 0) return;
		int dx = width/2;
		int dy = height/Ns[source].fget_connection_ids().length;
		if(dy > 30){
			int n = 0;
			for(int i:Ns[source].fget_connection_ids()){
				x[i] = dx;
				y[i] = (n++)*dy + dy/2;
			}
		} else{
			dy = 30;
			int n = height/30;
			int m = Ns[source].fget_connection_ids().length/n+1;
			dx = width/m;
			int l = 0;
			for(int i:Ns[source].fget_connection_ids()){
				x[i] = (l/n)*dx + dx/2;
				y[i] = ((l++) - n*(l/n))*dy + dy/2;
			}
		}
	}
	public String fget_data(){
		String data = new String();
		return data;
	}
	public int fget_number_of_neurons(){
		return Ns.length;
	}
}
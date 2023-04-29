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
				for(int i = 0; i < Ns.length; i++){
					for(int j:Ns[i].fget_connection_ids()){
						g.setColor(Color.black);
						g.drawLine(x[i]+1,y[i],x[j]+1,y[j]);
						g.drawLine(x[i]-1,y[i],x[j]-1,y[j]);
						g.drawLine(x[i],y[i]+1,x[j],y[j]+1);
						g.drawLine(x[i],y[i]-1,x[j],y[j]-1);
						g.setColor(Color.white);
						g.drawLine(x[i],y[i],x[j],y[j]);
						g.setColor(Color.black);
						g.fillOval(x[i]-5,y[i]-5,10,10);
						g.fillOval(x[j]-5,y[j]-5,10,10);
						g.setColor(Color.white);
						g.drawOval(x[i]-5,y[i]-5,10,10);
						g.drawOval(x[j]-5,y[j]-5,10,10);
					}
				}
			} else{
				for(int i:Ns[output_ids[source]].fget_connection_ids()){
					g.setColor(Color.white);
					g.fillOval(x[i]-5,y[i]-5,10,10);
					g.setColor(new Color((int)(Ns[output_ids[source]].Sigmoid(-Ns[output_ids[source]].fget_multiplier(i))*255.0f),
					(int)(Ns[output_ids[source]].Sigmoid(Ns[output_ids[source]].fget_multiplier(i))*255.0f),
					0));
					g.fillOval(x[i]-4,y[i]-4,8,8);
				}
			}
		}
		super.fstart();
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
		int k = (int)Math.sqrt((double)(Ns[output_ids[source]].fget_connection_ids().length/2));
		int dx = width/k;
		int dy = height/k;
		for(int i = 0; i < k; i++){
			for(int j = 0; j < k; j++){
				x[Ns[output_ids[source]].fget_connection_ids()[i*k + j]] = j*dx + dx/2;
				y[Ns[output_ids[source]].fget_connection_ids()[i*k + j]] = i*dy + dy/2;
				x[Ns[output_ids[source]].fget_connection_ids()[k*k + i*k + j]] = j*dx + dx/4;
				y[Ns[output_ids[source]].fget_connection_ids()[k*k + i*k + j]] = i*dy + dy/4;
			}
		}
	}
	public String fget_data(){
		String data = new String();
		return data;
	}
}
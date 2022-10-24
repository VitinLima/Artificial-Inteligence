import javax.swing.*;
import java.awt.*;

public class Component_Info_Display extends JPanel{
	private JScrollPane scroll;
	private JTextArea text;
	private JPanel gif;
	
	private Snake[] snakes;
	
	public Component_Info_Display(Snake[] new_snakes){
		snakes = new_snakes;
		text = new JTextArea(fget_general_info());
		scroll = new JScrollPane(text);
		scroll.setPreferredSize(new Dimension(500,500));
		scroll.setLocation(0,0);
		scroll.setVisible(true);
		add(scroll);
		gif = new JPanel();
		gif.setPreferredSize(new Dimension(500,500));
		gif.setLocation(0,0);
		gif.setVisible(false);
		add(gif);
	}
	
	public void frefresh(int id, boolean display, boolean display_switch){
		for(int i = 0; i < snakes.length; i++)
			snakes[i].fdisable_gif();
		if(!display_switch){
			gif.setVisible(false);
			scroll.setVisible(false);
			return;
		}
		if(display){
			gif.setVisible(false);
			if(id != -1)
				text.setText(snakes[id].fget_information());
			else
				text.setText(fget_general_info());
			scroll.setVisible(true);
		} else{
			scroll.setVisible(false);
			if(id == -1){
				gif.setVisible(false);
				return;
			}
			gif.setVisible(true);
			snakes[id].fset_gif(gif);
		}
	}
	private String fget_general_info(){
		String data = new String();
		int temp, k;
		float f_temp;
		
		data += "Displaying general information about the game:\n\nNumber of snakes: " + snakes.length + "\n\n";
		
		if(snakes.length == 0)
			return data;
		
		f_temp = 0;
		for(Snake snake:snakes) if(snake.fget_deaths() > 0) f_temp += (float)snake.fget_scored()/(float)snake.fget_deaths();
		f_temp /= snakes.length;
		data += "Medium score: " + f_temp + "\n";
		
		temp = 0;
		k = 0;
		for(Snake snake:snakes) if(snake.fget_max_score() > temp){
			temp = snake.fget_max_score();
			k = snake.fget_id();
		}
		data += "Highest score is " + temp + " from snake " + k +"\n";
		
		temp = 0;
		for(Snake snake:snakes) temp += snake.fget_deaths();
		temp /= snakes.length;
		data += "Medium deaths: " + temp + "\n";
		return data;
	}
}
import javax.swing.*;
import java.awt.*;

public class Component_Spy_Text extends JScrollPane{
	JTextArea text;
	
	private int id;
	
	private Snake[] snakes;
	
	public Component_Spy_Text(Snake[] new_snakes){
		snakes = new_snakes;
		
		text = new JTextArea(fget_general_info());
		setViewportView(text);
		setPreferredSize(new Dimension(500,500));
		
		id = -1;
	}
	
	public void fset_id(int new_id){
		id = new_id;
	}
	
	public void frefresh(){
		if(id != -1)
			text.setText(snakes[id].fget_information());
		else
			text.setText(fget_general_info());
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
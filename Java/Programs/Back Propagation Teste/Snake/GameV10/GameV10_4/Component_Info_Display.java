import javax.swing.*;
import java.awt.*;

public class Component_Info_Display extends JPanel{
	private JScrollPane scroll;
	private JTextPane text;
	private JPanel gif;
	
	private Snake[] snakes;
	private Map map;
	
	public Component_Info_Display(Snake[] new_snakes, Map new_map){
		snakes = new_snakes;
		map = new_map;
		text = new JTextPane();
		text.setEditable(false);
		text.setContentType("text/html");
		text.setText(("<html>" + fget_general_info() + "</html").replaceAll("\n", "<br/>"));
		scroll = new JScrollPane(text);
		scroll.setPreferredSize(new Dimension(500,500));
		scroll.setLocation(0,0);
		scroll.setVisible(true);
		setPreferredSize(new Dimension(500,500));
		add(scroll);
		gif = new JPanel();
		gif.setPreferredSize(new Dimension(500,500));
		gif.setLocation(0,0);
		gif.setVisible(false);
		gif.setName("display panel");
		add(gif);
	}
	
	public void frefresh(int id, int rel_to, boolean display, boolean display_switch){
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
				text.setText(("<html>" + snakes[id].fget_information() + "</html").replaceAll("\n", "<br/>"));
			else
				text.setText(("<html>" + fget_general_info() + "</html").replaceAll("\n", "<br/>"));
			scroll.setVisible(true);
		} else{
			scroll.setVisible(false);
			if(id == -1){
				gif.setVisible(false);
				return;
			}
			gif.setVisible(true);
			snakes[id].fset_gif(gif, rel_to);
		}
	}
	private String fget_general_info(){
		String data = new String();
		int temp;
		float f_temp;
		
		data += "Displaying general information about the game:\n\nNumber of snakes: " + snakes.length + "\n\n";
		
		if(snakes.length == 0)
			return data;
		
		f_temp = 0;
		for(Snake snake:snakes) if(snake.fget_deaths() > 0) f_temp += (float)snake.fget_scored()/(float)snake.fget_deaths();
		f_temp /= snakes.length;
		data += "Medium score: " + f_temp + "\n";
		
		temp = 0;
		long k = 0;
		for(Snake snake:snakes) if(snake.fget_max_score() > temp){
			temp = snake.fget_max_score();
			k = snake.getId();
		}
		data += "Highest score is " + temp + " from snake " + k +"\n";
		
		temp = 0;
		for(Snake snake:snakes) temp += snake.fget_deaths();
		temp /= snakes.length;
		data += "Medium deaths: " + temp + "\n\n";
		/*data += "map (long ids):\n\n";
		for(long[] l:map.fget_ids()){
			for(long c:l)
				data += c + " ";
			data += '\n';
		}*/
		return data;
	}
}
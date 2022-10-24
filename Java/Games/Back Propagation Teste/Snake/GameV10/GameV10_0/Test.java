import javax.swing.*;
import java.awt.*;

public class Test extends Thread{
	public Test(){
		Color color = Color.gray;
		String red = Integer.toHexString(color.getRed());
		String green = Integer.toHexString(color.getGreen());
		String blue = Integer.toHexString(color.getBlue());
		String s = new String("#" + 
			(red.length() == 1? "0" + red : red) +
			(green.length() == 1? "0" + green : green) +
			(blue.length() == 1? "0" + blue : blue));
		s = "<font size='5' color=" + s + ">" + color + " -> \u25A0" + "</font>";
		s += "oi\noi</html>";
		s = s.replaceAll("\n", "<br/>");
		JTextPane t = new JTextPane();
		t.setContentType("text/html");
		t.setText(s);
		t.setEditable(false);
		JFrame f = new JFrame();
		f.add(t);
		f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		/*AI ai = new AI(new int[]{2,2}, 4);
		int first;
		int second;
		float[] input = new float[2];
		for(int i = 0; i < 1; i++){
			input[0] = 0.0f;
			input[1] = 1.0f;
			ai.fset_input(input);
			first = ai.fstart();
			input[0] = 1.0f;
			input[1] = 2.0f;
			ai.fset_input(input);
			second = ai.fstart();
			input[0] = 2.0f;
			input[1] = 3.0f;
			ai.fset_input(input);
			second = ai.fstart();
			input[0] = 0.0f;
			input[1] = 4.0f;
			ai.fset_input(input);
			second = ai.fstart();*/
			/*if(first == 1)
				ai.fback_propagate(true);
			else
				ai.fback_propagate(false);*/
		//}
	}
}
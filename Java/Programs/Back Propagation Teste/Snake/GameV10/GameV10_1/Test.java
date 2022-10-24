import javax.swing.*;
import java.awt.*;

public class Test extends Thread{
	public Test(){
		AI ai;
		for(int l = 0; l < 10; l++){
		ai = new AI(new int[]{1,2}, 2);
		int first;
		int second;
		float[] input = new float[1];
		for(int i = 0; i < 1000; i++){
			input[0] = 1.0f;
			ai.fset_input(input);
			first = ai.fstart();
			input[0] = 0.0f;
			ai.fset_input(input);
			second = ai.fstart();
			if(first == 1)
				ai.fback_propagate(true);
			else
				ai.fback_propagate(false);
			
			input[0] = 0.0f;
			ai.fset_input(input);
			first = ai.fstart();
			input[0] = 0.0f;
			ai.fset_input(input);
			second = ai.fstart();
			if(first == 0)
				ai.fback_propagate(true);
			else
				ai.fback_propagate(false);
		}
		input[0] = 1.0f;
		ai.fset_input(input);
		System.out.println(ai.fstart());
		input[0] = 0.0f;
		ai.fset_input(input);
		System.out.println(ai.fstart() + "\n");
		}
	}
}
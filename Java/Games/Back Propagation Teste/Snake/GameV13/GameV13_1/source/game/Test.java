import ai.*;

import javax.swing.*;
import java.awt.*;

public class Test extends Thread{
	public Test(){
		AI ai;
		for(int l = 0; l < 1; l++){
			ai = new AI_Mem(new int[]{1,2},4);
			System.out.println(ai.fget_data() + "\n\n");
			ai.frecursive_layer(0,1);
			int first;
			int second;
			float[] input = new float[1];
			for(int i = 0; i < 100000; i++){
				//System.out.println("1");
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
				
				//System.out.println("1");
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
				
				//System.out.println("1");
				input[0] = 0.0f;
				ai.fset_input(input);
				first = ai.fstart();
				input[0] = 1.0f;
				ai.fset_input(input);
				second = ai.fstart();
				if(first == 0)
					ai.fback_propagate(true);
				else
					ai.fback_propagate(false);
				
				//System.out.println("1");
				input[0] = 1.0f;
				ai.fset_input(input);
				first = ai.fstart();
				input[0] = 1.0f;
				ai.fset_input(input);
				second = ai.fstart();
				if(first == 1)
					ai.fback_propagate(true);
				else
					ai.fback_propagate(false);
			}
			System.out.println(ai.fget_data());
			input[0] = 1.0f;
			ai.fset_input(input);
			input[0] = 1.0f;
			ai.fset_input(input);
			System.out.println(ai.fstart() + " 1");
			input[0] = 0.0f;
			ai.fset_input(input);
			System.out.println(ai.fstart() + " 1");
			input[0] = 0.0f;
			ai.fset_input(input);
			System.out.println(ai.fstart() + " 0");
			input[0] = 1.0f;
			ai.fset_input(input);
			System.out.println(ai.fstart() + " 0" + "\n");
		}
	}
}
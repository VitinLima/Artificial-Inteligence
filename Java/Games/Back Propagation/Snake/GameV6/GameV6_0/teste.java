import java.lang.Thread;

public class teste{
	public static void main(String[] args){
		AI Ai = new AI(3,0,4);
		System.out.println(Ai.fget_data());
		int action;
		for(int i = 0; i < 20; i++){
			Ai.fset_input(new float[]{(float)Math.random(), (float)Math.random(), (float)Math.random()});
			action = Ai.fstart();
			if(action != 3)
				Ai.fback_propagate(false);
			else
				Ai.fback_propagate(true);
		}
		System.out.println(Ai.fget_data());
	}
}
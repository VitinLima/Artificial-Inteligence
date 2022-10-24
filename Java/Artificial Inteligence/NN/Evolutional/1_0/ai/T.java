public class T{
	public T(){
		EvolutionalAI A = new EvolutionalAI();
		AI ai = new AI();
		for(int i = 0; i < 3; i++)
			A.add(ai.copy());
	}
}
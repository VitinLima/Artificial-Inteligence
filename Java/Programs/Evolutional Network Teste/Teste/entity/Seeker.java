package entity;

import ai.EvolutionalAI;
import ai.AIConstructor;

public class Seeker extends Entity{
	private EvolutionalAI ai;
	private int id;
	private boolean seeing;
	
	public Seeker(EvolutionalAI ai){
		super.setSize(20.0f);
		
		AIConstructor thisAI = new AIConstructor(new int[]{8,2});
		this.ai = ai;
		this.id = this.ai.add(thisAI);
	}
	
	@Override
	public void step(){
		ai.setInput(super.getRelativeView(8), this.id);
		float[] output = ai.getOutput(this.id);
		
		super.setVelocity(output[0], 2.0f*(float)Math.PI*output[1]);
	}
	
	@Override
	public void beeingSeen(Entity e){
	
	}
	
	@Override
	public void seeing(Entity e){
		if(e instanceof Hider)
			seeing = true;
	}
	
	@Override
	public void firePreStep(){
		seeing = false;
	}
	
	@Override
	public void firePosStep(){
		if(seeing) ai.addEff(true, this.id);
		else ai.addEff(false, this.id);
	}
}
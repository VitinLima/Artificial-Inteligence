package main;

import frame.Frame;
import entity.Hider;
import entity.Seeker;
import entity.Entity;
import ai.EvolutionalAI;

public class main{
	public static void main(String[] args){
		int qttHiders = 1;
		int qttSeekers = 1;
		Entity[] e = new Entity[qttHiders+qttSeekers];
		
		EvolutionalAI hiders = new EvolutionalAI();
		for(int i = 0; i < qttHiders; i++)
			e[i] = new Hider(hiders);
		
		EvolutionalAI seekers = new EvolutionalAI();
		for(int i = qttHiders; i < e.length; i++)
			e[i] = new Seeker(seekers);
		
		for(int i = 0; i < 3000; i++){
			if(((double)i%100.0) == 0){
				Sync sync = new Sync(-1);
				new Frame(sync);
				for(int j = 0; j < e.length; j++)
					e[j].connect(sync);
				System.out.println("connected");
				sync.run();
				System.out.println("started");
			}
			else{
				Sync sync = new Sync(-1);
				new Frame(sync);
				for(int j = 0; j < e.length; j++)
					e[j].connect(sync);
				System.out.println("connected");
				sync.run();
				System.out.println("started");
			}
			hiders.select();
			seekers.select();
			//if(((double)i%100.0) == 0){
				System.out.println("done " + i);
			//}
		}
	}
}
package main;

import frame.Frame;

public class main{
	public static void main(String[] args){
		Sync sync = new Sync();
		new Frame(sync);
		sync.run();
	}
}